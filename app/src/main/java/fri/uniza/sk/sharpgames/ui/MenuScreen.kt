package fri.uniza.sk.sharpgames.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onPhotographicMemoryClick: () -> Unit,
    onLogicalThinkingClick: () -> Unit,
    onReactionsClick: () -> Unit,
    onAbstractThinkingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sharp Games",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        MenuButton(
            text = "Photographic Memory",
            onClick = onPhotographicMemoryClick,
            modifier = Modifier.padding(vertical = 8.dp),
            buttonColor = Color(0xFFec8484)
        )

        MenuButton(
            text = "Logical Thinking",
            onClick = onLogicalThinkingClick,
            modifier = Modifier.padding(vertical = 8.dp),
            buttonColor = Color(0xFFd4d36a)
        )

        MenuButton(
            text = "Reactions",
            onClick = onReactionsClick,
            modifier = Modifier.padding(vertical = 8.dp),
            buttonColor = Color(0xFF5c9464)
        )

        MenuButton(
            text = "Abstract Thinking",
            onClick = onAbstractThinkingClick,
            modifier = Modifier.padding(vertical = 8.dp),
            buttonColor = Color(0xFF4b638b)
        )
    }
}

@Composable
private fun MenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
} 
