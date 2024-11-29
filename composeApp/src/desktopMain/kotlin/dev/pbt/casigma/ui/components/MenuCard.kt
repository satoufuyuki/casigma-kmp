package dev.pbt.casigma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import casigma.composeapp.generated.resources.Res
import casigma.composeapp.generated.resources.chicken_steak
import casigma.composeapp.generated.resources.remove
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.ui.theme.white
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MenuCard(
    image: String,
    name: String,
    price: Float,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    return OutlinedCard(
        colors = CardColors(
            containerColor = white,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier.width(260.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = Modifier.height(160.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
            Text(
                name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
            Text(
                TextUtils.formatRupiah(price),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontSize = 20.sp
            )
            // Quantity Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)
            ) {
                IconButton(
                    onClick = onRemove,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.remove),
                        contentDescription = "Remove"
                    )
                }
                Text(
                    quantity.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                IconButton(
                    onClick = onAdd,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}