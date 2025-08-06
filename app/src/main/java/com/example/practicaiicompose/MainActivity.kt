package com.example.practicaiicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.practicaiicompose.ui.theme.PracticaIIComposeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "form",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("form") { FormScreen(navController) }
                    composable("summary?name={name}&age={age}") { backStackEntry ->
                        SummaryScreen(
                            name = (backStackEntry.arguments?.getString("name") ?: "") as String,
                            age = (backStackEntry.arguments?.getString("age") ?: "") as String
                        )
                    }
                }
            }
        }
    }
}


private fun MainActivity.composable(string: String, function:  Any) {}

//BottomBar.kt
@Composable
fun BottomBar(navController: NavController) {
    val items = listOf("form" to "Formulario", "summary" to "Resumen")
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { (route, label) ->
            NavigationBarItem(
                label = { Text(label) },
                icon = {},
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
//FormScreen.kt
@Composable
fun FormScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Ingrese sus datos", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") }
        )
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = {
                navController.navigate("summary?name=$name&age=$age")
            }
        ) {
            Text("Enviar")
        }
    }
}

//SumaryScreen.kt
@Composable
fun SummaryScreen(name: String, age: Any) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Resumen", style = MaterialTheme.typography.titleMedium)
        Text("Nombre: $name")
        Text("Edad: $age")
    }
}





// ------- Previews --------

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    PracticaIIComposeTheme {
        FormScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    PracticaIIComposeTheme {
        SummaryScreen(name = "David", age = "25")
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    PracticaIIComposeTheme {
        BottomBar(navController = rememberNavController())
    }
}