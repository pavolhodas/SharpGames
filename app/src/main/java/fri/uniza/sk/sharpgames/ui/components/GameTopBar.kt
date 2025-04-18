package fri.uniza.sk.sharpgames.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    title: String,
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Menu.route) }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Back to menu"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFF4b638b),
            titleContentColor = androidx.compose.ui.graphics.Color.White
        )
    )
} 