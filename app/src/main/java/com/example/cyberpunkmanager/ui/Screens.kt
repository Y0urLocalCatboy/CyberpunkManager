package com.example.cyberpunkmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.*
import com.example.cyberpunkmanager.data.models.enums.*
import com.example.cyberpunkmanager.ui.theme.*
import com.example.cyberpunkmanager.viewmodel.AppViewModel

@Composable
fun NeonGlow(
    color: Color,
    radius: Float = 20f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.drawBehind {
            val paint = Paint().asFrameworkPaint().apply {
                setShadowLayer(radius, 0f, 0f, color.toArgb())
            }
            drawContext.canvas.nativeCanvas.drawRect(
                0f, 0f, size.width, size.height, paint
            )
        }
    ) {
        content()
    }
}

@Composable
fun CyberHeader(title: String, subtitle: String? = null, onBack: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(CyberCyan.copy(alpha = 0.15f), Color.Transparent)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(CyberCyan, Color.Transparent)),
                shape = CutCornerShape(bottomEnd = 24.dp)
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                Text(
                    "< BACK",
                    modifier = Modifier.clickable { onBack() },
                    color = CyberPink,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = "> $title",
                style = MaterialTheme.typography.headlineLarge,
                color = CyberCyan
            )
        }
        if (subtitle != null) {
            Text(
                text = subtitle.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = CyberMuted,
                modifier = Modifier.padding(start = if (onBack != null) 70.dp else 24.dp)
            )
        }
    }
}

@Composable
fun CyberSearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, CyberLine, CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)),
        placeholder = { Text("SEARCH_DATABASE...", color = CyberMuted, style = MaterialTheme.typography.labelSmall) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CyberPanel2,
            unfocusedContainerColor = CyberPanel,
            focusedTextColor = CyberCyan,
            unfocusedTextColor = CyberText,
            cursorColor = CyberCyan
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun AdminScreen(viewModel: AppViewModel, onDetailClick: () -> Unit, onBack: () -> Unit) {
    var mode by remember { mutableStateOf("ADD") }
    var selectedCategory by remember { mutableStateOf("Cyberware") }

    Column(modifier = Modifier.fillMaxSize().background(CyberBg)) {
        CyberHeader(title = "ADMIN_CONSTRUCT", subtitle = mode, onBack = onBack)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.border(1.dp, CyberLine, CutCornerShape(4.dp))) {
                Text(
                    "ADD", 
                    modifier = Modifier
                        .clickable { mode = "ADD" }
                        .background(if (mode == "ADD") CyberCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    color = if (mode == "ADD") CyberCyan else CyberMuted,
                    style = MaterialTheme.typography.labelMedium
                )
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(CyberLine).align(Alignment.CenterVertically))
                Text(
                    "VIEW", 
                    modifier = Modifier
                        .clickable { mode = "VIEW" }
                        .background(if (mode == "VIEW") CyberCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    color = if (mode == "VIEW") CyberCyan else CyberMuted,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        if (mode == "ADD") {
            AdminAddView(viewModel, selectedCategory) { selectedCategory = it }
        } else {
            AdminListView(viewModel, selectedCategory, { selectedCategory = it }, onDetailClick)
        }
    }
}

@Composable
fun AdminAddView(viewModel: AppViewModel, category: String, onCategoryChange: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var uniqueName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(_WARE_TYPE.DEF) }
    var addictionRisk by remember { mutableStateOf(DICE.DEF) }
    var mechanicsStr by remember { mutableStateOf("") }
    
    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("SELECT_CATEGORY", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            categories.take(3).forEach { cat ->
                Text(
                    cat.uppercase(), 
                    color = if (category == cat) CyberCyan else CyberMuted, 
                    modifier = Modifier.clickable { onCategoryChange(cat) },
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            categories.drop(3).forEach { cat ->
                Text(
                    cat.uppercase(), 
                    color = if (category == cat) CyberCyan else CyberMuted, 
                    modifier = Modifier.clickable { onCategoryChange(cat) },
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberTextField(value = name, onValueChange = { name = it }, label = "NAME")
        CyberTextField(value = cost, onValueChange = { cost = it }, label = "COST")
        CyberTextField(value = description, onValueChange = { description = it }, label = "DESCRIPTION")

        if (category == "Cyberware") {
            CyberTextField(value = uniqueName, onValueChange = { uniqueName = it }, label = "UNIQUE_NAME (optional)")
            Text("TYPE", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            _WARE_TYPE.entries.forEach { t ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == t, onClick = { type = t })
                    Text(t.type, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (category == "Drugs") {
            Text("ADDICTION_RISK", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            DICE.entries.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = addictionRisk == d, onClick = { addictionRisk = d })
                    Text(d.type, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        CyberTextField(value = mechanicsStr, onValueChange = { mechanicsStr = it }, label = "MECHANICS (comma separated)")

        Spacer(modifier = Modifier.height(24.dp))

        val isValid = name.isNotBlank() && cost.toIntOrNull() != null && description.isNotBlank() && mechanicsStr.isNotBlank() &&
                (category != "Cyberware" || (type != _WARE_TYPE.DEF)) &&
                (category != "Drugs" || (addictionRisk != DICE.DEF))

        CyberButton(
            text = "UPLOAD_DATA",
            enabled = isValid
        ) {
            val cCost = cost.toIntOrNull() ?: 0
            val mechanicsList = mechanicsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            
            when(category) {
                "Cyberware" -> viewModel.addCyberware(cyberware().apply { 
                    this.name = name; this.cost = cCost; this.description = description
                    this.uniqueName = if (uniqueName.isNotBlank()) uniqueName else null
                    this.type = type.name; this.mechanics = mechanicsList
                })
                "Drugs" -> viewModel.addDrug(drug().apply { 
                    this.name = name; this.cost = cCost; this.description = description
                    this.addiction_risk = addictionRisk.name; this.mechanics = mechanicsList
                })
                "Gadgets" -> viewModel.addGadget(gadget().apply { this.name = name; this.cost = cCost; this.description = description; this.mechanics = mechanicsList })
                "Shards" -> viewModel.addShard(shard().apply { this.name = name; this.cost = cCost; this.description = description; this.mechanics = mechanicsList })
                "Quickhacks" -> viewModel.addQuickhack(quickhack().apply { this.name = name; this.cost = cCost; this.description = description; this.mechanics = mechanicsList })
                "Daemons" -> viewModel.addDaemon(daemon().apply { this.name = name; this.cost = cCost; this.description = description; this.mechanics = mechanicsList })
            }
            name = ""; cost = ""; description = ""; uniqueName = ""; mechanicsStr = ""
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AdminListView(viewModel: AppViewModel, category: String, onCategoryChange: (String) -> Unit, onItemClick: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards")

    LaunchedEffect(category) {
        viewModel.loadCategory(category)
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            categories.forEach { cat ->
                Text(
                    cat.uppercase(), 
                    color = if (category == cat) CyberCyan else CyberMuted, 
                    modifier = Modifier.clickable { onCategoryChange(cat) }.padding(vertical = 12.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        CyberSearchBar(query = searchQuery, onQueryChange = { viewModel.setSearchQuery(it) })

        Spacer(modifier = Modifier.height(8.dp))

        when (val state = uiState) {
            is AppViewModel.UiState.Loading -> CircularProgressIndicator(color = CyberCyan)
            is AppViewModel.UiState.Error -> Text("ERROR: ${state.message}", color = CyberDanger)
            is AppViewModel.UiState.Success -> {
                val filteredItems = state.items.filter { item ->
                    val name = when(item) {
                        is cyberware -> item.name
                        is drug -> item.name
                        is gadget -> item.name
                        is shard -> item.name
                        is quickhack -> item.name
                        is daemon -> item.name
                        else -> ""
                    }
                    name.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredItems) { item ->
                        AssetCard(item) {
                            viewModel.selectItem(item)
                            onItemClick()
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun DetailScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val item by viewModel.selectedItem.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().background(CyberBg)) {
        val name = when(item) {
            is cyberware -> (item as cyberware).name
            is drug -> (item as drug).name
            is gadget -> (item as gadget).name
            is shard -> (item as shard).name
            is quickhack -> (item as quickhack).name
            is daemon -> (item as daemon).name
            else -> "UNKNOWN"
        }
        
        CyberHeader(title = name.uppercase(), subtitle = "ASSET_DETAILS", onBack = onBack)
        
        Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            item?.let { asset ->
                DetailRow("COST", "${getProperty(asset, "cost") ?: 0} EB")
                
                val desc = getProperty(asset, "description") as? String
                if (!desc.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("DESCRIPTION", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
                    Text(desc, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }

                val mechanics = getProperty(asset, "mechanics") as? List<*>
                if (!mechanics.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MECHANICS", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
                    mechanics.forEach { m ->
                        Text("> $m", color = CyberCyan, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                if (asset is cyberware) {
                    DetailRow("TYPE", asset.type)
                    asset.uniqueName?.let { un -> DetailRow("UNIQUE_NAME", un) }
                }
                
                if (asset is drug) {
                    DetailRow("ADDICTION_RISK", asset.addiction_risk)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: Any) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth().border(1.dp, CyberLine).padding(12.dp)) {
        Text(label, color = CyberPink, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
        Text(value.toString().uppercase(), color = CyberYellow, style = MaterialTheme.typography.labelMedium)
    }
}

private fun getProperty(obj: Any, name: String): Any? {
    return try {
        val field = obj.javaClass.getDeclaredField(name)
        field.isAccessible = true
        field.get(obj)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun CyberTextField(value: String, onValueChange: (String) -> Unit, label: String, visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, color = CyberMuted, style = MaterialTheme.typography.labelSmall)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().border(1.dp, CyberLine),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CyberBg,
                unfocusedContainerColor = CyberBg,
                focusedTextColor = CyberText,
                unfocusedTextColor = CyberText
            ),
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AdminButton(onAuthSuccess: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(32.dp)
                .border(1.dp, CyberCyan.copy(alpha = 0.3f), CutCornerShape(4.dp))
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "C",
                style = MaterialTheme.typography.labelSmall,
                color = CyberCyan.copy(alpha = 0.4f)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("ENCRYPTION_KEY_REQUIRED", color = CyberYellow, style = MaterialTheme.typography.headlineMedium) },
            text = {
                CyberTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "KEY",
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                Text(
                    "DECRYPT", 
                    modifier = Modifier.clickable {
                        if (password == Constants.ADMIN_PASSWORD) {
                            showDialog = false
                            onAuthSuccess()
                        }
                    }.padding(16.dp),
                    color = CyberCyan,
                    style = MaterialTheme.typography.labelLarge
                )
            },
            containerColor = CyberBg,
            textContentColor = CyberText,
            shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
            modifier = Modifier.border(1.dp, CyberCyan, CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
        )
    }
}

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "CYBERPUNK\nMANAGER",
                style = MaterialTheme.typography.displayLarge,
                color = CyberCyan
            )
            Spacer(modifier = Modifier.height(48.dp))
            CyberButton(text = "INITIALIZE", onClick = onStartClick)
        }
    }
}

@Composable
fun DashboardScreen(onCategoryClick: (String) -> Unit) {
    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "> SELECT_MODULE",
                style = MaterialTheme.typography.headlineMedium,
                color = CyberCyan,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(categories) { category ->
            CategoryItem(category) { onCategoryClick(category) }
        }
    }
}

@Composable
fun CategoryScreen(category: String, viewModel: AppViewModel, onItemClick: () -> Unit, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CyberHeader(title = category.uppercase(), subtitle = "ACTIVE_MODULE", onBack = onBack)
        
        CyberSearchBar(query = searchQuery, onQueryChange = { viewModel.setSearchQuery(it) })

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            when (val state = uiState) {
                is AppViewModel.UiState.Loading -> CircularProgressIndicator(color = CyberCyan)
                is AppViewModel.UiState.Error -> Text("ERROR: ${state.message}", color = MaterialTheme.colorScheme.error)
                is AppViewModel.UiState.Success -> {
                    val filteredItems = state.items.filter { item ->
                        val name = when(item) {
                            is cyberware -> item.name
                            is drug -> item.name
                            is gadget -> item.name
                            is shard -> item.name
                            is quickhack -> item.name
                            is daemon -> item.name
                            else -> ""
                        }
                        name.contains(searchQuery, ignoreCase = true)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredItems) { item ->
                            AssetCard(item) {
                                viewModel.selectItem(item)
                                onItemClick()
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: () -> Unit) {
    NeonGlow(color = CyberCyan.copy(alpha = 0.3f), radius = 30f) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberCyan, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .background(CyberBg.copy(alpha = 0.5f), CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .clickable { onClick() }
                .padding(20.dp)
        ) {
            Text(text = name.uppercase(), style = MaterialTheme.typography.headlineMedium, color = CyberCyan)
        }
    }
}

@Composable
fun AssetCard(item: Any, onClick: () -> Unit) {
    val name = when(item) {
        is cyberware -> item.name
        is drug -> item.name
        is gadget -> item.name
        is shard -> item.name
        is quickhack -> item.name
        is daemon -> item.name
        else -> "Unknown"
    }
    
    NeonGlow(color = CyberYellow.copy(alpha = 0.2f), radius = 20f) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CutCornerShape(topStart = 8.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = name.uppercase(), color = CyberYellow, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CyberButton(
    text: String, 
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    NeonGlow(color = if (enabled) CyberCyan.copy(alpha = 0.4f) else Color.Transparent, radius = 40f) {
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = CyberCyan,
                disabledContainerColor = CyberMuted
            ),
            shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text, color = if (enabled) Color.Black else Color.DarkGray, style = MaterialTheme.typography.labelLarge)
        }
    }
}
