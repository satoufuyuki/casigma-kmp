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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.modules.database.models.OrderObject
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.ui.theme.grayWhite
import dev.pbt.casigma.ui.theme.primaryLight
import org.koin.compose.koinInject

data class OrderItem(val name: String, val price: Int, val quantity: Int)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderDetail(
    orderData: OrderObject? = null,
    buttonAction: () -> Unit,
    deleteAction: (() -> Unit)? = null
) {
    val itemScrollState = rememberScrollState()
    val notesScrollState = rememberScrollState()

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
                        if (orderData != null) TextUtils.formatDate(orderData.createdAt) else "N/A",
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
                        if (orderData != null) TextUtils.formatTime(orderData.createdAt) else "N/A",
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
                        if (orderData != null) orderData.customerName else "N/A",
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
                modifier = Modifier.fillMaxWidth().height(120.dp).verticalScroll(itemScrollState),
            ) {
                orderData?.items?.filter { (it.quantity != 0) and (it.quantity != null) }?.forEach { item ->
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${item.quantity}x ${item.menu.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            TextUtils.formatRupiah(item.menu.price * (item.quantity ?: 0)),
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
                            orderData?.additionalNotes ?: "N/A",
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
                    if (orderData != null) TextUtils.formatRupiah(orderData.items.map { (it.quantity ?: 0) * it.menu.price }.sum()) else TextUtils.formatRupiah(0f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Button(
                onClick = buttonAction,
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
            if (deleteAction != null) {
                Button(
                    onClick = { deleteAction.invoke() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Delete Order",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}