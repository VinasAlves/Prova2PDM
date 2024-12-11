package com.example.prova2pdm_amalvada

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prova2pdm_amalvada.R.string.cliente
import com.example.prova2pdm_amalvada.R.string.edit
import com.example.prova2pdm_amalvada.R.string.home
import com.example.prova2pdm_amalvada.R.string.info
import com.example.prova2pdm_amalvada.R.string.itens
import com.example.prova2pdm_amalvada.models.Cafe
import com.example.prova2pdm_amalvada.ui.theme.Brown
import com.example.prova2pdm_amalvada.ui.theme.Prova2PDMAMalvadaTheme
import com.example.prova2pdm_amalvada.viewmodels.CafeViewModel
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prova2PDMAMalvadaTheme {
               MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHostContainer(navController, Modifier.padding(innerPadding))
    }
}

//bottom bar de navegação
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Cliente,
        BottomNavItem.Itens,
        BottomNavItem.Home,
        BottomNavItem.Edit,
        BottomNavItem.Info
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(id = item.title)) },
                label = { Text(stringResource(id = item.title)) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { HomeScreen() }
        composable(BottomNavItem.Cliente.route) {
            val cafeViewModel: CafeViewModel = viewModel()
            ClienteScreen(navController = navController,viewModel = cafeViewModel)
        }
        composable(BottomNavItem.Itens.route) {
            val cafeViewModel: CafeViewModel = viewModel()
            ItensScreen(navController = navController, viewModel = cafeViewModel)
        }
        composable(BottomNavItem.Info.route) {
            val cafeViewModel: CafeViewModel = viewModel()
            InfoScreen(viewModel = cafeViewModel)
        }
        composable(BottomNavItem.Edit.route) { backStackEntry ->
            val cafeId = backStackEntry.arguments?.getString("cafeId")
            val cafeViewModel: CafeViewModel = viewModel()
            EditScreen(navController = navController, cafeId = cafeId, viewModel = cafeViewModel)
        }
    }
}

@Composable
fun HomeScreen() {
    // Conteúdo da tela Home
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_empresa),
            contentDescription = stringResource(id = R.string.logo_description),
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.welcome_message, "Jeff Coffee"),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(navController: NavHostController, viewModel: CafeViewModel = viewModel()) {
    // Estados para os campos de entrada
    var nomeCafe by remember { mutableStateOf(TextFieldValue("")) }
    var notaCafe by remember { mutableStateOf(TextFieldValue("")) }
    var aroma by remember { mutableStateOf(TextFieldValue("")) }
    var acidez by remember { mutableStateOf(TextFieldValue("")) }
    var amargor by remember { mutableStateOf(TextFieldValue("")) }
    var sabor by remember { mutableStateOf(TextFieldValue("")) }
    var precoCafe by remember { mutableStateOf(TextFieldValue("")) }

    var expanded by remember { mutableStateOf(false) }
    var expandedAroma by remember { mutableStateOf(false) }
    var expandedAcidez by remember { mutableStateOf(false) }
    var expandedAmargor by remember { mutableStateOf(false) }
    var expandedSabor by remember { mutableStateOf(false) }
    val range = listOf("1","2","3","4","5")
    val notas = listOf("Doce","Floral","Frutado","Especiarias")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cadastro de Café", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = nomeCafe,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
            ),
            onValueChange = { nomeCafe = it },
            label = { Text("Nome do Café") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        //dropdown menu das Notas do Café
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = notaCafe,
                onValueChange = { }, label = { Text("Nota do Cafe") }, leadingIcon = { Icon(Icons.Filled.BarChart, contentDescription = null) }, readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                notas.forEach { notaOption ->
                    DropdownMenuItem(
                        text = { Text(text = notaOption) },
                        onClick = {
                            notaCafe = TextFieldValue(notaOption)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //dropdown menu da nota do aroma
        ExposedDropdownMenuBox(
            expanded = expandedAroma,
            onExpandedChange = { expandedAroma = !expandedAroma }
        ) {
            TextField(
                value = aroma, onValueChange = { }, label = { Text("Aroma") }, leadingIcon = { Icon(Icons.Filled.Category, contentDescription = null) }, readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAroma)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedAroma,
                onDismissRequest = { expandedAroma = false }
            ) {
                range.forEach { notaOption ->
                    DropdownMenuItem(
                        text = { Text(text = notaOption) },
                        onClick = {
                            aroma = TextFieldValue(notaOption)
                            expandedAroma = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //dropdown menu para acidez
        ExposedDropdownMenuBox(
            expanded = expandedAcidez,
            onExpandedChange = { expandedAcidez = !expandedAcidez }
        ) {
            TextField(value = acidez, onValueChange = { }, label = { Text("Acidez") }, leadingIcon = {
                    Icon(
                        Icons.Filled.ConnectWithoutContact,
                        contentDescription = null
                    )
                }, readOnly = true, trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAcidez)
                }, colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                ), shape = RoundedCornerShape(8.dp), modifier = Modifier
                .fillMaxWidth()
                .menuAnchor())
            ExposedDropdownMenu(
                expanded = expandedAcidez,
                onDismissRequest = { expandedAcidez = false }
            ) {
                range.forEach { notaOption ->
                    DropdownMenuItem(
                        text = { Text(text = notaOption) },
                        onClick = {
                            acidez = TextFieldValue(notaOption)
                            expandedAcidez = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //dropdown menu para amargor
        ExposedDropdownMenuBox(
            expanded = expandedAmargor,
            onExpandedChange = { expandedAmargor = !expandedAmargor }
        ) {
            TextField(value = amargor, onValueChange = { }, label = { Text("Amargor") }, leadingIcon = { Icon(Icons.Filled.Code, contentDescription = null) }, readOnly = true, trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAmargor)
                }, colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue
                ), shape = RoundedCornerShape(8.dp), modifier = Modifier
                .fillMaxWidth()
                .menuAnchor())
            ExposedDropdownMenu(
                expanded = expandedAmargor,
                onDismissRequest = { expandedAmargor = false }
            ) {
                range.forEach { notaOption ->
                    DropdownMenuItem(
                        text = { Text(text = notaOption) },
                        onClick = {
                            amargor = TextFieldValue(notaOption)
                            expandedAmargor = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //dropdown menu para sabor
        ExposedDropdownMenuBox(
            expanded = expandedSabor,
            onExpandedChange = { expandedSabor = !expandedSabor }
        ) {
            TextField(
                value = sabor, onValueChange = { }, label = { Text("Sabor") }, leadingIcon = { Icon(Icons.Filled.Cable, contentDescription = null) }, readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSabor)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedSabor,
                onDismissRequest = { expandedSabor = false }
            ) {
                range.forEach { notaOption ->
                    DropdownMenuItem(
                        text = { Text(text = notaOption) },
                        onClick = {
                            sabor = TextFieldValue(notaOption)
                            expandedSabor = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        //preço do café
        OutlinedTextField(
            value = precoCafe,
            leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
            ),
            onValueChange = { precoCafe = it },
            label = { Text("Preço do Café") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (validarCampos(
                        nomeCafe.text, notaCafe.text, aroma.text, acidez.text, amargor.text, sabor.text, precoCafe.text
                    )) {
                    val novoCafe = Cafe(
                        idCafe = UUID.randomUUID().toString(),
                        nomeCafe = nomeCafe.text, notaCafe = notaCafe.text, aroma = aroma.text.toIntOrNull() ?: 0, acidez = acidez.text.toIntOrNull() ?: 0, amargor = amargor.text.toIntOrNull() ?: 0, sabor = sabor.text, precoCafe = precoCafe.text.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.create(novoCafe)
                } else {
                    // Exibir mensagem de erro ou feedback para o usuário
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(Brown, contentColor = Color.White, disabledContainerColor = Color.Red, disabledContentColor = Color.Black)

        ) {
            Text("Cadastrar Café")
        }
    }
}

//TELA DE ITENS
@Composable
fun ItensScreen(navController: NavHostController, viewModel: CafeViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(viewModel.cafes.value ?: emptyList()) { cafe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Nome: ${cafe.nomeCafe}", color = Color.Black, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota: ${cafe.notaCafe}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota do aroma: ${cafe.aroma}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota da acidez: ${cafe.acidez}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota do amargor: ${cafe.amargor}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota do sabor: ${cafe.sabor}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Preço: ${cafe.precoCafe}", color = Color.Black, style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("editScreen/${cafe.idCafe}") }
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    //botão para deletar
                    IconButton(
                        onClick = { viewModel.run{ delete(cafe.idCafe) } }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Deletar", tint = Color.Red)
                    }
                }
            }
        }
    }
}


//TELA DE EDIÇÃO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavController, cafeId: String?, viewModel: CafeViewModel) {
    val cafe = viewModel.getCafeById(cafeId)

    if (cafe != null) {
        var nomeCafe by remember { mutableStateOf(cafe.nomeCafe) }
        var notaCafe by remember { mutableStateOf(cafe.notaCafe) }
        var aroma by remember { mutableStateOf(cafe.aroma.toString()) }
        var acidez by remember { mutableStateOf(cafe.acidez.toString()) }
        var amargor by remember { mutableStateOf(cafe.amargor.toString()) }
        var sabor by remember { mutableStateOf(cafe.sabor) }
        var precoCafe by remember { mutableStateOf(cafe.precoCafe.toString()) }

        var expanded by remember { mutableStateOf(false) }
        var expandedAroma by remember { mutableStateOf(false) }
        var expandedAcidez by remember { mutableStateOf(false) }
        var expandedAmargor by remember { mutableStateOf(false) }
        var expandedSabor by remember { mutableStateOf(false) }
        val range = listOf("1", "2", "3", "4", "5")
        val notas = listOf("Doce", "Floral", "Frutado", "Especiarias")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Editar Café", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = nomeCafe,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                ),
                onValueChange = { nomeCafe = it },
                label = { Text("Nome do Café") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu das Notas do Café
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = notaCafe,
                    onValueChange = { },
                    label = { Text("Nota do Cafe") },
                    leadingIcon = { Icon(Icons.Filled.BarChart, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    notas.forEach { notaOption ->
                        DropdownMenuItem(
                            text = { Text(text = notaOption) },
                            onClick = {
                                notaCafe = notaOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu da nota do aroma
            ExposedDropdownMenuBox(
                expanded = expandedAroma,
                onExpandedChange = { expandedAroma = !expandedAroma }
            ) {
                TextField(
                    value = aroma,
                    onValueChange = { },
                    label = { Text("Aroma") },
                    leadingIcon = { Icon(Icons.Filled.Category, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAroma)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAroma,
                    onDismissRequest = { expandedAroma = false }
                ) {
                    range.forEach { notaOption ->
                        DropdownMenuItem(
                            text = { Text(text = notaOption) },
                            onClick = {
                                aroma = notaOption
                                expandedAroma = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu para acidez
            ExposedDropdownMenuBox(
                expanded = expandedAcidez,
                onExpandedChange = { expandedAcidez = !expandedAcidez }
            ) {
                TextField(
                    value = acidez,
                    onValueChange = { },
                    label = { Text("Acidez") },
                    leadingIcon = { Icon(Icons.Filled.ConnectWithoutContact, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAcidez)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAcidez,
                    onDismissRequest = { expandedAcidez = false }
                ) {
                    range.forEach { notaOption ->
                        DropdownMenuItem(
                            text = { Text(text = notaOption) },
                            onClick = {
                                acidez = notaOption
                                expandedAcidez = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu para amargor
            ExposedDropdownMenuBox(
                expanded = expandedAmargor,
                onExpandedChange = { expandedAmargor = !expandedAmargor }
            ) {
                TextField(
                    value = amargor,
                    onValueChange = { },
                    label = { Text("Amargor") },
                    leadingIcon = { Icon(Icons.Filled.Code, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAmargor)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAmargor,
                    onDismissRequest = { expandedAmargor = false }
                ) {
                    range.forEach { notaOption ->
                        DropdownMenuItem(
                            text = { Text(text = notaOption) },
                            onClick = {
                                amargor = notaOption
                                expandedAmargor = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown menu para sabor
            ExposedDropdownMenuBox(
                expanded = expandedSabor,
                onExpandedChange = { expandedSabor = !expandedSabor }
            ) {
                TextField(
                    value = sabor,
                    onValueChange = { },
                    label = { Text("Sabor") },
                    leadingIcon = { Icon(Icons.Filled.Cable, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSabor)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedSabor,
                    onDismissRequest = { expandedSabor = false }
                ) {
                    notas.forEach { notaOption ->
                        DropdownMenuItem(
                            text = { Text(text = notaOption) },
                            onClick = {
                                sabor = notaOption
                                expandedSabor = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = precoCafe,
                leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Blue
                ),
                onValueChange = { precoCafe = it },
                label = { Text("Preço do Café") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedCafe = cafe.copy(
                        nomeCafe = nomeCafe,
                        notaCafe = notaCafe,
                        aroma = aroma.toIntOrNull() ?: 0,
                        acidez = acidez.toIntOrNull() ?: 0,
                        amargor = amargor.toIntOrNull() ?: 0,
                        sabor = sabor,
                        precoCafe = precoCafe.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.update(updatedCafe)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    } else {
        Text("Café não encontrado")
    }
}

//TELA DE INFORMAÇOES
@Composable
fun InfoScreen(viewModel: CafeViewModel) {
    val totalCafes = viewModel.getTotalCafes()
    val mostExpensiveCafe = viewModel.getMostExpensiveCafe()
    val cafesSortedBySabor = viewModel.getCafesSortedBySabor()
    val averagePrice = viewModel.getAveragePrice()
    val cafeWithMostAroma = viewModel.getCafeWithMostAroma()
    val cafeWithLeastAcidez = viewModel.getCafeWithLeastAcidez()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Informações dos Cafés", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Text(text = "Total de Cafés: $totalCafes", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        mostExpensiveCafe?.let {
            Text(text = "Café Mais Caro: ${it.nomeCafe} - R\$ ${it.precoCafe}", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Cafés Ordenados por Sabor:", style = MaterialTheme.typography.bodyMedium)
        cafesSortedBySabor.forEach { cafe ->
            Text(text = "${cafe.nomeCafe} - Sabor: ${cafe.sabor}", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Preço Médio dos Cafés: R\$ ${"%.2f".format(averagePrice)}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        cafeWithMostAroma?.let {
            Text(text = "Café com Mais Aroma: ${it.nomeCafe} - Aroma: ${it.aroma}", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        cafeWithLeastAcidez?.let {
            Text(text = "Café com Menos Acidez: ${it.nomeCafe} - Acidez: ${it.acidez}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: Int) {
    object Home : BottomNavItem("home", Icons.Filled.Home, home)
    object Cliente : BottomNavItem("cliente", Icons.Filled.Coffee, cliente)
    object Itens : BottomNavItem("itens", Icons.Filled.Menu, itens)
    object Info : BottomNavItem("info", Icons.Filled.Info, info)
    object Edit : BottomNavItem("editScreen/{cafeId}", Icons.Filled.Edit, edit)
}