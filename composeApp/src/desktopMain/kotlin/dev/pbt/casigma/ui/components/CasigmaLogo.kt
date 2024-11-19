package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import casigma.composeapp.generated.resources.Res
import casigma.composeapp.generated.resources.casigma_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun CasigmaLogo(includeText: Boolean = true, size: Int = 48, theme: Color) {
    return Row(
        modifier = Modifier.fillMaxHeight().padding(24.dp, 0.dp, 24.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                Res.drawable.casigma_logo
            ),
            tint = theme,
            contentDescription = "Casigma logo",
            modifier = Modifier.size(size.dp)
        )
        if (includeText) {
            Text(
                text = "casigma",
                color = theme,
                fontWeight = FontWeight.SemiBold,
                fontSize = 40.sp,
            )
        }
    }
}