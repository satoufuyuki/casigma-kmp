package dev.pbt.casigma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.ui.theme.grayWhite
import dev.pbt.casigma.ui.theme.primaryLight

data class OrderItem(val name: String, val price: Int, val quantity: Int)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderDetail() {
    val scrollState = rememberScrollState()
    val notesScrollState = rememberScrollState()
    val orderList = listOf(
        OrderItem("Chicken Steak", 15000, 1),
        OrderItem("Ice Tea", 5000, 2),
        OrderItem("Apple Pie", 10000, 1),
        OrderItem("Fried Rice", 12000, 1),
        OrderItem("Mineral Water", 3000, 2),
    )

    return Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        // Upper Details
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp).padding(horizontal = 24.dp)
        ) {
            Text(
                "Order Detail",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            // Details
            Column (
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Date:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "2021-10-10",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Time:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "13:00 WIB",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Customer:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Muhammad Raymond",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Medium
                    )
                }
                Divider()
            }
            // Items List
            FlowColumn (
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                overflow = FlowColumnOverflow.Clip,
                modifier = Modifier.fillMaxWidth().height(120.dp).verticalScroll(scrollState),
            ) {
                orderList.forEach { item ->
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${item.quantity}x ${item.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Rp${item.price * item.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Column (
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    "Additional Notes:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Card (
                    colors = CardColors(
                        containerColor = grayWhite,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                ) {
                    FlowRow (
                        modifier = Modifier.fillMaxWidth().height(120.dp).verticalScroll(notesScrollState)
                            .padding(16.dp),
                        overflow = FlowRowOverflow.Clip
                    ) {
                        Text(
                            "No additional notes",
                            style =  MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color =  Color(0xff949494)
                        )
                    }
                }
            }
        }
        // Bottom Details
        Column (
            modifier = Modifier.fillMaxWidth()
                .border(
                    width = 3.dp,
                    color = primaryLight,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Rp 50.000",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Update Order",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}