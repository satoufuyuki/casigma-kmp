package dev.pbt.casigma.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun OrderCard(
    tableNo: Int,
    time: LocalDateTime,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    Button (
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = white,
            contentColor = Color.Gray
        ),
        border = if (enabled) BorderStroke(2.dp, primaryLight) else null,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Table $tableNo",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            )
            Text(
                TextUtils.formatTime(time),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            )
        }
    }
}