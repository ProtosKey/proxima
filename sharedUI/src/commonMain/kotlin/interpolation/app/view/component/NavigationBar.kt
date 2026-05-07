package interpolation.app.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import interpolation.app.view.screen.InputScreen
import interpolation.app.view.screen.MainScreen
import interpolation.app.view.screen.ResultsScreen
import interpolation.app.view.screen.SettingsScreen

@Composable
fun NavigationBar(navigator: Navigator) {
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = true,
                onClick = { navigator.push(MainScreen()) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
                label = { Text("Главная") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navigator.push(InputScreen()) },
                icon = { Icon(Icons.Default.Edit, contentDescription = "Ввод") },
                label = { Text("Ввод") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navigator.push(ResultsScreen()) },
                icon = { Icon(Icons.Default.BarChart, contentDescription = "Результаты") },
                label = { Text("Результаты") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navigator.push(SettingsScreen()) },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Настройки") },
                label = { Text("Настройки") }
            )
        }
    }
}
