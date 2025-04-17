package fri.uniza.sk.sharpgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fri.uniza.sk.sharpgames.ui.MenuScreen
import fri.uniza.sk.sharpgames.ui.theme.SharpGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharpGamesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(
                        onPhotographicMemoryClick = {},
                        onLogicalThinkingClick = {},
                        onReactionsClick = {},
                        onAbstractThinkingClick = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    SharpGamesTheme {
        MenuScreen(
            onPhotographicMemoryClick = {},
            onLogicalThinkingClick = {},
            onReactionsClick = {},
            onAbstractThinkingClick = {}
        )
    }
}
