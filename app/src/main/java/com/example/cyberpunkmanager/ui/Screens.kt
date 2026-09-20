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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.*
import com.example.cyberpunkmanager.data.models.enums.*
import com.example.cyberpunkmanager.ui.theme.*
import com.example.cyberpunkmanager.viewmodel.AppViewModel

@Composable
fun NeonGlow(
    color: Color,
    radius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.drawBehind {
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                this.color = android.graphics.Color.TRANSPARENT

                setShadowLayer(
                    radius.toPx(),
                    0f,
                    0f,
                    color.toArgb()
                )
            }

            drawContext.canvas.nativeCanvas.drawRect(
                0f,
                0f,
                size.width,
                size.height,
                paint
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
            .statusBarsPadding()
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
        if (onBack != null) {
            Text(
                "< WSTECZ",
                modifier = Modifier.clickable { onBack() }.padding(bottom = 8.dp),
                color = CyberPink,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = "> $title",
            style = MaterialTheme.typography.headlineLarge,
            color = CyberCyan
        )
        if (subtitle != null) {
            Text(
                text = translateSubtitle(subtitle).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = CyberMuted,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp)
            )
        }
    }
}

private fun translateSubtitle(subtitle: String): String {
    return when(subtitle.uppercase()) {
        "ACTIVE_MODULE" -> "AKTYWNY MODUŁ"
        "ASSET_DETAILS" -> "SZCZEGÓŁY ZASOBU"
        "ADMIN_CONSTRUCT" -> "KONSTRUKT ADMINA"
        "ADD" -> "DODAJ"
        "VIEW" -> "WIDOK"
        "EDIT" -> "EDYTUJ"
        "LOCAL STORAGE" -> "PAMIĘĆ LOKALNA"
        else -> subtitle
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
        placeholder = { Text("PRZESZUKAJ BAZĘ DANYCH...", color = CyberMuted, style = MaterialTheme.typography.labelSmall) },
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
    var editingItem by remember { mutableStateOf<Any?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(CyberBg)) {
        CyberHeader(title = "KONSTRUKT ADMINA", subtitle = if (editingItem != null) "EDIT" else mode, onBack = onBack)

        if (editingItem == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.border(1.dp, CyberLine, CutCornerShape(4.dp))) {
                    Text(
                        "DODAJ",
                        modifier = Modifier
                            .clickable { mode = "ADD" }
                            .background(if (mode == "ADD") CyberCyan.copy(alpha = 0.2f) else Color.Transparent)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        color = if (mode == "ADD") CyberCyan else CyberMuted,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(CyberLine).align(Alignment.CenterVertically))
                    Text(
                        "LISTA",
                        modifier = Modifier
                            .clickable { mode = "VIEW" }
                            .background(if (mode == "VIEW") CyberCyan.copy(alpha = 0.2f) else Color.Transparent)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        color = if (mode == "VIEW") CyberCyan else CyberMuted,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        if (editingItem != null) {
            AdminFormView(
                viewModel = viewModel,
                category = selectedCategory,
                initialItem = editingItem,
                onFinished = { editingItem = null }
            )
        } else if (mode == "ADD") {
            AdminFormView(viewModel, selectedCategory, onCategoryChange = { selectedCategory = it })
        } else {
            AdminListView(
                viewModel = viewModel,
                category = selectedCategory,
                onCategoryChange = { selectedCategory = it },
                onEditClick = { item ->
                    editingItem = item
                },
                onItemClick = onDetailClick
            )
        }
    }
}

@Composable
fun AdminFormView(
    viewModel: AppViewModel,
    category: String,
    initialItem: Any? = null,
    onCategoryChange: ((String) -> Unit)? = null,
    onFinished: (() -> Unit)? = null
) {
    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var pcCost by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var uniqueName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(_WARE_TYPE.DEF) }
    var addictionRisk by remember { mutableStateOf(DICE.DEF) }
    var mechanicsStr by remember { mutableStateOf("") }
    var weaponAttack by remember { mutableStateOf("") }
    var weaponIsRanged by remember { mutableStateOf(true) }
    
    var agentHp by remember { mutableStateOf("") }
    var agentInt by remember { mutableStateOf("") }
    var agentCha by remember { mutableStateOf("") }
    var agentStr by remember { mutableStateOf("") }
    var agentSpd by remember { mutableStateOf("") }
    var agentAcc by remember { mutableStateOf("") }
    var agentMonthlyCost by remember { mutableStateOf("") }
    var agentType by remember { mutableStateOf(AGENT_TYPE.DEF) }
    var agentActionsStr by remember { mutableStateOf("") }
    var agentPassiveActionsStr by remember { mutableStateOf("") }

    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards", "Weapons", "Agents")

    LaunchedEffect(initialItem) {
        if (initialItem != null) {
            name = getProperty(initialItem, "name") as? String ?: ""
            cost = (getProperty(initialItem, "cost") ?: "").toString()
            description = getProperty(initialItem, "description") as? String ?: ""
            mechanicsStr = (getProperty(initialItem, "mechanics") as? List<*>)?.joinToString("; ") ?: ""

            when (initialItem) {
                is Cyberware -> {
                    pcCost = initialItem.pcCost
                    uniqueName = initialItem.uniqueName ?: ""
                    type = _WARE_TYPE.entries.find { it.name == initialItem.type } ?: _WARE_TYPE.DEF
                }
                is Drug -> {
                    addictionRisk = DICE.entries.find { it.name == initialItem.addiction_risk } ?: DICE.DEF
                }
                is Weapon -> {
                    weaponAttack = initialItem.attack
                    weaponIsRanged = initialItem.isRanged
                    uniqueName = initialItem.uniqueName ?: ""
                }
                is Agent -> {
                    agentHp = initialItem.hitPoints.toString()
                    agentInt = initialItem.intBonus.toString()
                    agentCha = initialItem.chaBonus.toString()
                    agentStr = initialItem.strBonus.toString()
                    agentSpd = initialItem.spdBonus.toString()
                    agentAcc = initialItem.accBonus.toString()
                    agentMonthlyCost = initialItem.monthlyCost.toString()
                    agentType = AGENT_TYPE.entries.find { it.name == initialItem.type } ?: AGENT_TYPE.DEF
                    uniqueName = initialItem.uniqueName ?: ""
                    agentActionsStr = initialItem.actions?.joinToString("; ") ?: ""
                    agentPassiveActionsStr = initialItem.passiveActions?.joinToString("; ") ?: ""
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (initialItem != null) {
            Text(
                "< POWRÓT DO LISTY",
                modifier = Modifier.clickable { onFinished?.invoke() }.padding(vertical = 8.dp),
                color = CyberPink,
                style = MaterialTheme.typography.labelSmall
            )
        }

        if (onCategoryChange != null) {
            Text("WYBIERZ KATEGORIĘ", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                categories.take(3).forEach { cat ->
                    Text(
                        translateCategory(cat).uppercase(),
                        color = if (category == cat) CyberCyan else CyberMuted,
                        modifier = Modifier.clickable { onCategoryChange(cat) },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                categories.drop(3).forEach { cat ->
                    Text(
                        translateCategory(cat).uppercase(),
                        color = if (category == cat) CyberCyan else CyberMuted,
                        modifier = Modifier.clickable { onCategoryChange(cat) },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        } else {
            Text("KATEGORIA: ${translateCategory(category).uppercase()}", color = CyberCyan, style = MaterialTheme.typography.labelMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberTextField(value = name, onValueChange = { name = it }, label = "NAZWA")
        CyberTextField(value = cost, onValueChange = { cost = it }, label = "KOSZT")
        if (category != "Daemons" && category != "Quickhacks" && category != "Shards") {
            CyberTextField(value = description, onValueChange = { description = it }, label = "OPIS")
        }

        if (category == "Cyberware") {
            CyberTextField(value = pcCost, onValueChange = { pcCost = it }, label = "KOSZT PC")
            CyberTextField(value = uniqueName, onValueChange = { uniqueName = it }, label = "UNIKALNA NAZWA (opcjonalnie)")
            Text("TYP", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            _WARE_TYPE.entries.forEach { t ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == t, onClick = { type = t })
                    Text(t.type, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (category == "Drugs") {
            Text("RYZYKO UZALEŻNIENIA", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            DICE.entries.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = addictionRisk == d, onClick = { addictionRisk = d })
                    Text(d.type, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (category == "Weapons") {
            CyberTextField(value = weaponAttack, onValueChange = { weaponAttack = it }, label = "ATAK (np. 2d6)")
            CyberTextField(value = uniqueName, onValueChange = { uniqueName = it }, label = "UNIKALNA NAZWA (opcjonalnie)")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = weaponIsRanged, onCheckedChange = { weaponIsRanged = it })
                Text("CZY ZASIĘGOWA?", color = CyberText, style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (category == "Agents") {
            CyberTextField(value = agentHp, onValueChange = { agentHp = it }, label = "PŻ (HITPOINTS)")
            CyberTextField(value = agentMonthlyCost, onValueChange = { agentMonthlyCost = it }, label = "KOSZT MIESIĘCZNY")
            CyberTextField(value = uniqueName, onValueChange = { uniqueName = it }, label = "UNIKALNA NAZWA (opcjonalnie)")

            Text("TYP AGENTA", color = CyberYellow, style = MaterialTheme.typography.labelSmall)
            AGENT_TYPE.entries.filter { it != AGENT_TYPE.DEF }.forEach { t ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = agentType == t, onClick = { agentType = t })
                    Text(t.typeName, color = CyberText, style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            Text("BONUSY DO RZUTÓW", color = CyberYellow, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) { CyberTextField(value = agentInt, onValueChange = { agentInt = it }, label = "INT") }
                Box(modifier = Modifier.weight(1f)) { CyberTextField(value = agentCha, onValueChange = { agentCha = it }, label = "CHA") }
                Box(modifier = Modifier.weight(1f)) { CyberTextField(value = agentStr, onValueChange = { agentStr = it }, label = "STR") }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) { CyberTextField(value = agentSpd, onValueChange = { agentSpd = it }, label = "SPD") }
                Box(modifier = Modifier.weight(1f)) { CyberTextField(value = agentAcc, onValueChange = { agentAcc = it }, label = "ACC") }
            }
            
            CyberTextField(value = agentActionsStr, onValueChange = { agentActionsStr = it }, label = "AKCJE (oddzielone średnikami)")
            CyberTextField(value = agentPassiveActionsStr, onValueChange = { agentPassiveActionsStr = it }, label = "AKCJE PASYWNE (oddzielone średnikami)")
        }

        if (category != "Agents") {
            CyberTextField(value = mechanicsStr, onValueChange = { mechanicsStr = it }, label = "MECHANIKA (oddzielona średnikami)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        val isDescriptionRequired = category != "Daemons" && category != "Quickhacks" && category != "Shards"
        val isMechanicsRequired = category != "Agents"
        val isValid = name.isNotBlank() && cost.toIntOrNull() != null && (description.isNotBlank() || !isDescriptionRequired) && 
                (mechanicsStr.isNotBlank() || !isMechanicsRequired) &&
                (category != "Cyberware" || (type != _WARE_TYPE.DEF && pcCost.isNotBlank())) &&
                (category != "Drugs" || (addictionRisk != DICE.DEF)) &&
                (category != "Weapons" || weaponAttack.isNotBlank()) &&
                (category != "Agents" || (agentHp.isNotBlank() && agentMonthlyCost.isNotBlank() && agentType != AGENT_TYPE.DEF))

        CyberButton(
            text = if (initialItem != null) "AKTUALIZUJ DANE" else "PRZEŚLIJ DANE",
            enabled = isValid
        ) {
            val cCost = cost.toIntOrNull() ?: 0
            val mechanicsList = mechanicsStr.split(";").map { it.trim() }.filter { it.isNotEmpty() }
            val id = if (initialItem != null) (getProperty(initialItem, "id") as? String ?: "") else ""

            when(category) {
                "Cyberware" -> {
                    val item = Cyberware().apply {
                        this.id = id; this.name = name; this.cost = cCost; this.pcCost = pcCost; this.description = description
                        this.uniqueName = if (uniqueName.isNotBlank()) uniqueName else null
                        this.type = type.name; this.mechanics = mechanicsList
                    }
                    if (initialItem != null) viewModel.editCyberware(item) else viewModel.addCyberware(item)
                }
                "Drugs" -> {
                    val item = Drug().apply {
                        this.id = id; this.name = name; this.cost = cCost; this.description = description
                        this.addiction_risk = addictionRisk.name; this.mechanics = mechanicsList
                    }
                    if (initialItem != null) viewModel.editDrug(item) else viewModel.addDrug(item)
                }
                "Gadgets" -> {
                    val item = Gadget().apply { this.id = id; this.name = name; this.cost = cCost; this.description = description; this.mechanics = mechanicsList }
                    if (initialItem != null) viewModel.editGadget(item) else viewModel.addGadget(item)
                }
                "Shards" -> {
                    val item = Shard().apply { this.id = id; this.name = name; this.cost = cCost; this.mechanics = mechanicsList }
                    if (initialItem != null) viewModel.editShard(item) else viewModel.addShard(item)
                }
                "Quickhacks" -> {
                    val item = Quickhack().apply { this.id = id; this.name = name; this.cost = cCost; this.mechanics = mechanicsList }
                    if (initialItem != null) viewModel.editQuickhack(item) else viewModel.addQuickhack(item)
                }
                "Daemons" -> {
                    val item = Daemon().apply { this.id = id; this.name = name; this.cost = cCost; this.mechanics = mechanicsList }
                    if (initialItem != null) viewModel.editDaemon(item) else viewModel.addDaemon(item)
                }
                "Weapons" -> {
                    val item = Weapon().apply {
                        this.id = id; this.name = name; this.cost = cCost; this.description = description; this.attack = weaponAttack; this.isRanged = weaponIsRanged
                        this.uniqueName = if (uniqueName.isNotBlank()) uniqueName else null
                        this.mechanics = mechanicsList
                    }
                    if (initialItem != null) viewModel.editWeapon(item) else viewModel.addWeapon(item)
                }
                "Agents" -> {
                    val item = Agent().apply {
                        this.id = id; this.name = name; this.initialCost = cCost; this.description = description
                        this.hitPoints = agentHp.toIntOrNull() ?: 0
                        this.monthlyCost = agentMonthlyCost.toIntOrNull() ?: 0
                        this.type = agentType.name
                        this.intBonus = agentInt.toIntOrNull() ?: 0
                        this.chaBonus = agentCha.toIntOrNull() ?: 0
                        this.strBonus = agentStr.toIntOrNull() ?: 0
                        this.spdBonus = agentSpd.toIntOrNull() ?: 0
                        this.accBonus = agentAcc.toIntOrNull() ?: 0
                        this.uniqueName = if (uniqueName.isNotBlank()) uniqueName else null
                        this.actions = agentActionsStr.split(";").map { it.trim() }.filter { it.isNotEmpty() }
                        this.passiveActions = agentPassiveActionsStr.split(";").map { it.trim() }.filter { it.isNotEmpty() }
                    }
                    if (initialItem != null) viewModel.editAgent(item) else viewModel.addAgent(item)
                }
            }
            
            if (initialItem != null) {
                onFinished?.invoke()
            } else {
                name = ""; cost = ""; pcCost = ""; description = ""; uniqueName = ""; mechanicsStr = ""; weaponAttack = ""
                agentHp = ""; agentInt = ""; agentCha = ""; agentStr = ""; agentSpd = ""; agentAcc = ""
                agentMonthlyCost = ""; agentType = AGENT_TYPE.DEF; agentActionsStr = ""; agentPassiveActionsStr = ""
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AdminListView(
    viewModel: AppViewModel,
    category: String,
    onCategoryChange: (String) -> Unit,
    onEditClick: (Any) -> Unit,
    onItemClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards", "Weapons", "Agents")

    LaunchedEffect(category) {
        viewModel.loadCategory(category)
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            categories.forEach { cat ->
                Text(
                    translateCategory(cat).uppercase(),
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
            is AppViewModel.UiState.Error -> Text("BŁĄD: ${state.message}", color = CyberDanger)
            is AppViewModel.UiState.Success -> {
                val filteredItems = state.items.filter { item ->
                    val name = when(item) {
                        is Cyberware -> item.uniqueName ?: item.name
                        is Drug -> item.name
                        is Gadget -> item.name
                        is Shard -> item.name
                        is Quickhack -> item.name
                        is Daemon -> item.name
                        is Weapon -> item.uniqueName ?: item.name
                        is Agent -> item.uniqueName ?: item.name
                        else -> ""
                    }
                    name.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredItems) { item ->
                        AssetCard(
                            item = item,
                            onEditClick = { onEditClick(item) },
                            onClick = {
                                viewModel.selectItem(item, category)
                                onItemClick()
                            }
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun AssetStatsRow(
    cost: String,
    mainLabel: String? = null,
    mainValue: String? = null,
    sideLabel: String? = null,
    isMainValueComplex: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberLine)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Price (Left)
        Text(
            text = cost,
            color = CyberYellow,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1.5f),
            maxLines = 1,
            softWrap = false
        )

        // Center Content (Main Stat)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(2.2f)
        ) {
            if (mainLabel != null) {
                Text(
                    text = mainLabel.uppercase(),
                    color = CyberPink,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (mainValue != null) {
                if (isMainValueComplex) {
                    val text = mainValue.uppercase()
                    val delimiter = if (text.contains("+")) "+" else if (text.contains(" LUB ")) "LUB" else null
                    val parts = if (delimiter != null) text.split(if (delimiter == "LUB") " LUB " else delimiter) else emptyList()

                    if (delimiter != null && parts.size > 1) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(parts[0].trim(), color = CyberCyan, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text(delimiter, color = CyberCyan, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text(parts[1].trim(), color = CyberCyan, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                    } else {
                        Text(text, color = CyberCyan, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                } else {
                    Text(
                        text = mainValue.uppercase(),
                        color = CyberCyan,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Side Label (Right)
        Text(
            text = sideLabel?.uppercase() ?: "",
            color = CyberMuted,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun DetailScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val item by viewModel.selectedItem.collectAsState()
    val isSaved by viewModel.isCurrentItemSaved.collectAsState()
    val category by viewModel.selectedCategory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CyberBg)) {
        val name = when(item) {
            is Cyberware -> (item as Cyberware).name
            is Drug -> (item as Drug).name
            is Gadget -> (item as Gadget).name
            is Shard -> (item as Shard).name
            is Quickhack -> (item as Quickhack).name
            is Daemon -> (item as Daemon).name
            is Weapon -> (item as Weapon).name
            is Agent -> (item as Agent).name
            else -> "NIEZNANY"
        }

        CyberHeader(title = name.uppercase(), subtitle = "ASSET_DETAILS", onBack = onBack)

        Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            item?.let { asset ->
                CyberButton(
                    text = if (isSaved) "USUŃ Z KOLEKCJI" else "ZAPISZ W KOLEKCJI",
                    onClick = { viewModel.toggleSave(asset, category) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Common Description Block
                val description = when(asset) {
                    is Weapon -> asset.description
                    is Cyberware -> asset.description
                    is Drug -> asset.description
                    is Gadget -> asset.description
                    is Agent -> asset.description
                    else -> ""
                }

                if (description.isNotBlank()) {
                    NeonGlow(color = CyberYellow.copy(alpha = 0.15f), radius = 30.dp) {
                        Box(modifier = Modifier.fillMaxWidth().border(1.dp, CyberYellow, CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)).padding(16.dp)) {
                            Text(description, color = CyberText, style = MaterialTheme.typography.bodyLarge, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Unified Stats Row
                when(asset) {
                    is Weapon -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            mainLabel = "RZUT NA:",
                            mainValue = asset.attack,
                            sideLabel = if (asset.isRanged) "BROŃ ZASIĘGOWA" else "BLISKI KONTAKT",
                            isMainValueComplex = true
                        )
                    }
                    is Cyberware -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            mainLabel = "KOSZT PC:",
                            mainValue = asset.pcCost,
                            sideLabel = when (asset.type){
                                _WARE_TYPE.BORG.name -> _WARE_TYPE.BORG.name + " (+" + _WARE_TYPE.BORG.cost + " WW)"
                                _WARE_TYPE.CYBER.name -> _WARE_TYPE.CYBER.name + " (+" + _WARE_TYPE.CYBER.cost + " WW)"
                                _WARE_TYPE.BIO.name -> _WARE_TYPE.BIO.name + " (+" + _WARE_TYPE.BIO.cost + " WW)"
                                _WARE_TYPE.EGZO.name -> _WARE_TYPE.EGZO.name + " (+" + _WARE_TYPE.EGZO.cost + " WW)"
                                _WARE_TYPE.JUNK.name -> _WARE_TYPE.JUNK.name + " (+" + _WARE_TYPE.JUNK.cost + " WW)"
                                _WARE_TYPE.KORPO.name -> _WARE_TYPE.KORPO.name + " (+" + _WARE_TYPE.KORPO.cost + " WW)"
                                else -> "WW ? Nieznany typ"
                            }
                        )
                    }
                    is Drug -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            mainLabel = "UZALEŻNIENIE:",
                            mainValue = asset.addiction_risk,
                            sideLabel = "SUBSTANCJA"
                        )
                    }
                    is Gadget -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            sideLabel = "GADŻET"
                        )
                    }
                    is Quickhack -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            sideLabel = "QUICKHACK"
                        )
                    }
                    is Daemon -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            sideLabel = "DAEMON"
                        )
                    }
                    is Shard -> {
                        AssetStatsRow(
                            cost = "${asset.cost} ŻD",
                            sideLabel = "DRZAZGA"
                        )
                    }
                    is Agent -> {
                        Column {
                            AssetStatsRow(
                                cost = "${asset.initialCost} ŻD",
                                mainLabel = "PŻ:",
                                mainValue = asset.hitPoints.toString(),
                                sideLabel = "AGENT " + (AGENT_TYPE.entries.find { it.name == asset.type }?.typeName ?: "")
                            )
                            Text(
                                text = "KOSZT MIESIĘCZNY: ${asset.monthlyCost} ŻD",
                                color = CyberCyan,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("BONUSY DO RZUTÓW", color = CyberPink, style = MaterialTheme.typography.labelSmall)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("INT" to asset.intBonus, "CHA" to asset.chaBonus, "STR" to asset.strBonus, "SPD" to asset.spdBonus, "ACC" to asset.accBonus).forEach { (label, value) ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(label, color = CyberYellow, style = MaterialTheme.typography.labelSmall)
                                        Text(if (value >= 0) "+$value" else "$value", color = CyberText, style = MaterialTheme.typography.titleMedium)
                                    }
                                }
                            }
                        }
                    }
                }

                // Mechanics at bottom
                val mechanics = when(asset) {
                    is Weapon -> asset.mechanics
                    is Cyberware -> asset.mechanics
                    is Drug -> asset.mechanics
                    is Gadget -> asset.mechanics
                    is Shard -> asset.mechanics
                    is Quickhack -> asset.mechanics
                    is Daemon -> asset.mechanics
                    is Agent -> asset.actions
                    else -> null
                }

                if (!mechanics.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(if (asset is Agent) "AKCJE" else "SPECYFIKACJA TECHNICZNA", color = CyberPink, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        mechanics.forEach { m ->
                            FormattedCombatMechanic(m)
                        }
                    }
                }

                if (asset is Agent && !asset.passiveActions.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("AKCJE PASYWNE", color = CyberPink, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        asset.passiveActions?.forEach { m ->
                            FormattedCombatMechanic(m)
                        }
                    }
                }

                // Extra Unique ID info if present
                val uniqueName = when(asset) {
                    is Weapon -> asset.uniqueName
                    is Cyberware -> asset.uniqueName
                    is Agent -> asset.uniqueName
                    else -> null
                }

                uniqueName?.let { un ->
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailRow("UNIKALNA NAZWA", un)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: Any) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth().border(1.dp, CyberLine).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = CyberPink,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            value.toString().uppercase(),
            color = CyberYellow,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun FormattedCombatMechanic(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = CyberCyan,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, size.height),
                    strokeWidth = strokeWidth * 2
                )
            }
            .background(
                 Brush.horizontalGradient(
                    listOf(CyberCyan.copy(alpha = 0.15f), Color.Transparent)
                )
            )
            .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
    ) {
            Text(
                text.replaceFirstChar { it.uppercase() },
                color = CyberText,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp
            )
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .border(1.dp, CyberCyan.copy(alpha = 0.3f), CutCornerShape(4.dp))
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                style = MaterialTheme.typography.labelSmall,
                color = CyberCyan.copy(alpha = 0.4f)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("WYMAGANY KLUCZ DOSTĘPU", color = CyberYellow, style = MaterialTheme.typography.headlineMedium) },
            text = {
                CyberTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "KLUCZ",
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                Text(
                    "AUTORYZUJ",
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
            .background(CyberBg)
            .drawBehind {
                val lineSpacing = 40.dp.toPx()
                val color = CyberLine.copy(alpha = 0.2f)
                for (i in 0..(size.height / lineSpacing).toInt()) {
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, i * lineSpacing),
                        end = androidx.compose.ui.geometry.Offset(size.width, i * lineSpacing),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Text(
            "wersja_2.1.37_REL",
            modifier = Modifier.align(Alignment.TopStart),
            color = CyberMuted,
            style = MaterialTheme.typography.labelSmall
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = CyberCyan.copy(alpha = 0.08f),
                        shape = CutCornerShape(16.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = CyberCyan.copy(alpha = 0.8f),
                        shape = CutCornerShape(16.dp)
                    )
                    .padding(horizontal = 28.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "CYBERPUNK\nMANAGER",
                    color = CyberCyan,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 42.sp,
                        letterSpacing = 2.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .background(
                        color = CyberPink.copy(alpha = 0.08f),
                        shape = CutCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = CyberPink.copy(alpha = 0.6f),
                        shape = CutCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Podręczna Baza Danych",
                    color = CyberPink,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .clickable { onStartClick() }
                    .background(
                        color = CyberCyan.copy(alpha = 0.06f),
                        shape = CutCornerShape(12.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = CyberCyan,
                        shape = CutCornerShape(12.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 14.dp)
            ) {
                Text(
                    text = ">   INICJALIZUJ SYSTEM",
                    color = CyberCyan,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, CyberCyan, Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun DashboardScreen(onCategoryClick: (String) -> Unit, onSavedClick: () -> Unit) {
    val categories = listOf("Cyberware", "Gadgets", "Drugs", "Daemons", "Quickhacks", "Shards", "Weapons", "Agents")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "> WYBIERZ MODUŁ",
                style = MaterialTheme.typography.headlineMedium,
                color = CyberCyan,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            SavedCategoryItem(onClick = onSavedClick)
        }
        items(categories) { category ->
            CategoryItem(translateCategory(category)) { onCategoryClick(category) }
        }
    }
}

@Composable
fun SavedCategoryItem(onClick: () -> Unit) {
    NeonGlow(color = CyberPink.copy(alpha = 0.3f), radius = 30.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberPink, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .background(CyberBg.copy(alpha = 0.5f), CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .clickable { onClick() }
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "ZAPISANE", style = MaterialTheme.typography.headlineSmall, color = CyberPink)
            }
        }
    }
}

@Composable
fun SavedScreen(viewModel: AppViewModel, onItemClick: () -> Unit, onBack: () -> Unit) {
    val savedItems by viewModel.savedItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        CyberHeader(title = "ZAPISANE", subtitle = "LOCAL_STORAGE", onBack = onBack)

        CyberSearchBar(query = searchQuery, onQueryChange = { viewModel.setSearchQuery(it) })

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            val filteredItems = savedItems.filter { item ->
                val name = when(item) {
                    is Cyberware -> item.uniqueName ?: item.name
                    is Drug -> item.name
                    is Gadget -> item.name
                    is Shard -> item.name
                    is Quickhack -> item.name
                    is Daemon -> item.name
                    is Weapon -> item.uniqueName ?: item.name
                    is Agent -> item.uniqueName ?: item.name
                    else -> ""
                }
                name.contains(searchQuery, ignoreCase = true)
            }

            if (filteredItems.isEmpty()) {
                Text(
                    "BRAK ZAPISANYCH ELEMENTÓW",
                    color = CyberMuted,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center).padding(top = 40.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredItems) { item ->
                        AssetCard(item) {
                            val category = when(item) {
                                is Cyberware -> "Cyberware"
                                is Drug -> "Drugs"
                                is Gadget -> "Gadgets"
                                is Shard -> "Shards"
                                is Quickhack -> "Quickhacks"
                                is Daemon -> "Daemons"
                                is Weapon -> "Weapons"
                                is Agent -> "Agents"
                                else -> ""
                            }
                            viewModel.selectItem(item, category)
                            onItemClick()
                        }
                    }
                }
            }
        }
    }
}

fun translateCategory(category: String): String {
    return when(category) {
        "Cyberware" -> "Wszczepy"
        "Gadgets" -> "Gadżety"
        "Drugs" -> "Substancje"
        "Daemons" -> "Daemony"
        "Quickhacks" -> "Quickhacki"
        "Shards" -> "Drzazgi"
        "Weapons" -> "Broń"
        "Agents" -> "Agenci"
        else -> category
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
        CyberHeader(title = translateCategory(category).uppercase(), subtitle = "AKTYWNY MODUŁ", onBack = onBack)

        CyberSearchBar(query = searchQuery, onQueryChange = { viewModel.setSearchQuery(it) })

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            when (val state = uiState) {
                is AppViewModel.UiState.Loading -> CircularProgressIndicator(color = CyberCyan)
                is AppViewModel.UiState.Error -> Text("BŁĄD: ${state.message}", color = MaterialTheme.colorScheme.error)
                is AppViewModel.UiState.Success -> {
                    val filteredItems = state.items.filter { item ->
                        val name = when(item) {
                            is Cyberware -> item.uniqueName ?: item.name
                            is Drug -> item.name
                            is Gadget -> item.name
                            is Shard -> item.name
                            is Quickhack -> item.name
                            is Daemon -> item.name
                            is Weapon -> item.uniqueName ?: item.name
                            is Agent -> item.uniqueName ?: item.name
                            else -> ""
                        }
                        name.contains(searchQuery, ignoreCase = true)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredItems) { item ->
                            AssetCard(item) {
                                viewModel.selectItem(item, category)
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
    NeonGlow(color = CyberCyan.copy(alpha = 0.3f), radius = 30.dp) {
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
fun AssetCard(item: Any, onEditClick: (() -> Unit)? = null, onClick: () -> Unit) {
    val name = when(item) {
        is Cyberware -> item.uniqueName ?: item.name
        is Drug -> item.name
        is Gadget -> item.name
        is Shard -> item.name
        is Quickhack -> item.name
        is Daemon -> item.name
        is Weapon -> item.uniqueName ?: item.name
        is Agent -> item.uniqueName ?: item.name
        else -> "Unknown"
    }

    NeonGlow(color = CyberYellow.copy(alpha = 0.2f), radius = 20.dp) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CutCornerShape(topStart = 8.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name.uppercase(),
                    color = CyberYellow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (onEditClick != null) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = CyberCyan
                        )
                    }
                }
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
    NeonGlow(color = if (enabled) CyberCyan.copy(alpha = 0.4f) else Color.Transparent, radius = 40.dp) {
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
