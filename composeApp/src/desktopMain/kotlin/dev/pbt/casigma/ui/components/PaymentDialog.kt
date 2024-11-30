package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.pbt.casigma.modules.database.models.OrderObject
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white


@Composable
fun PaymentDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit,
    orderObject: OrderObject
) {
    val paymentMethod = remember { mutableListOf("Cash", "QRIS") }
    val cashAmount = remember { mutableStateOf(0) }
    val selectedPaymentMethod = remember { mutableStateOf<Int>(0) }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = "Table ${orderObject.tableNo} - Billing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        )
                        Text(
                            TextUtils.formatRupiah(orderObject.items.map { it.menu.price * (it.quantity ?: 0) }.sum()),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        )
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Change",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        )
                        Text(
                            if (selectedPaymentMethod.value == 1) "0" else TextUtils.formatRupiah(cashAmount.value - orderObject.items.map { it.menu.price * (it.quantity ?: 0) }.sum()),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        paymentMethod.forEachIndexed { index, it ->
                            Button(
                                onClick = { selectedPaymentMethod.value = index },
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = if (selectedPaymentMethod.value == index) primaryLight else Color.Transparent,
                                    contentColor = if (selectedPaymentMethod.value == index) white else Color.Black
                                ),
                            ) {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = if (selectedPaymentMethod.value == index) white else Color.Black,
                                )
                            }
                        }
                    }

                    // Cash Payment
                    when (selectedPaymentMethod.value) {
                        0 -> {
                            OutlinedTextField(
                                value = cashAmount.value.toString(),
                                onValueChange = {
                                    cashAmount.value = it.toIntOrNull() ?: 0
                                },
                                label = {
                                    Text("Cash Payment Amount")
                                }
                            )
                        }
                        // QRIS Payment
                        1 -> {
                            Text(
                                "Tell the customer to scan the QRIS then confirm the payment",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = primaryLight,
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = primaryLight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            onConfirm(orderObject.id)
                        },
                    ) {
                        Text("Submit",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = white,
                        )
                    }
                }
            }
        }
    }
}