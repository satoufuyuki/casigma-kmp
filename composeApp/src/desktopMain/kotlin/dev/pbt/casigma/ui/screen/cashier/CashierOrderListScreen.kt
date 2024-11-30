package dev.pbt.casigma.ui.screen.cashier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.modules.database.models.OrderStatus
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.AlertUtils
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.modules.viewmodel.TableViewModel
import dev.pbt.casigma.ui.components.OrderCard
import dev.pbt.casigma.ui.components.OrderDetail
import dev.pbt.casigma.ui.components.PaymentDialog
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.theme.neutral
import dev.pbt.casigma.ui.theme.white
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class CashierOrderListScreen(override val route: String): ScreenBase(route) {
    @Composable
    @OptIn(ExperimentalLayoutApi::class)
    override fun render() {
        val dialogProvider: DialogProvider = koinInject()
        val tableScrollState = rememberScrollState()
        val tableViewModel = koinInject<TableViewModel>()
        val tables by tableViewModel.tableState.collectAsState()
        val currentActiveOrderId = remember { mutableStateOf<Int?>(null) }
        val currentActiveCategory = remember { mutableStateOf(OrderStatus.Pending) }
        val orderCategory = listOf(OrderStatus.Pending, OrderStatus.Completed, OrderStatus.Cancelled)

        // Initial fetch
        if (tableViewModel.tableState.value.isEmpty()) {
            tableViewModel.fetchOrder(currentActiveCategory.value)
        }

        val makePayment = { orderId: Int? ->
            if (orderId != null) {
                dialogProvider.setAlertComponent {
                    PaymentDialog(
                        onDismiss = { dialogProvider.dismiss() },
                        onConfirm = { orderId ->
                            try {
                                tableViewModel.updateOrderStatus(orderId!!, OrderStatus.Paid)
                                dialogProvider.setAlertComponent { AlertUtils.buildSuccess("Payment has been made successfully. Transaction finished") }.show()
                            } catch (e: Exception) {
                                dialogProvider.setAlertComponent {
                                    AlertUtils.buildError("Failed to make payment: ${e.message}")
                                }.show()
                            }
                        },
                        orderObject = tables.find { it.id == orderId }!!
                    )
                }.show()
            }
        }

        val cancelOrder = { orderId: Int? ->
            if (orderId != null) {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildPromptDecision(
                        "Are you sure you want to cancel this order?",
                        onConfirm = {
                            try {
                                tableViewModel.updateOrderStatus(orderId, OrderStatus.Cancelled)
                            } catch (e: Exception) {
                                dialogProvider.setAlertComponent {
                                    AlertUtils.buildError("Failed to cancel order: ${e.message}")
                                }.show()
                            }
                        }
                    )
                }.show()
            }
        }
        // Container for the view
        Row(
            modifier = Modifier.fillMaxWidth().background(neutral),
            horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.Start),
            verticalAlignment = Alignment.Top
        ) {
            // Column for the left side of the view
            Column(
                modifier = Modifier.fillMaxWidth(0.75f).padding(48.dp, 48.dp, 0.dp, 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                // Title
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Orders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 40.sp
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                ) {
                    orderCategory.forEach { category ->
                        TextButton(onClick = { currentActiveCategory.value = category; currentActiveOrderId.value = null }) {
                            Text(
                                TextUtils.toTitleCase(category.status),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp,
                                color = if (currentActiveCategory.value == category) Color.Black else Color.Gray,
                            )
                        }
                    }
                }
                // Menu Card
                if (tables.none { it.status == currentActiveCategory.value }) {
                    Text(
                        "No orders found",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 25.sp,
                        color = Color.Gray,
                    )
                } else {
                    FlowColumn(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        modifier = Modifier.fillMaxWidth().verticalScroll(tableScrollState),
                        overflow = FlowColumnOverflow.Clip
                    ) {
                        tables.filter { it.status == currentActiveCategory.value }.forEach { it ->
                            OrderCard(
                                tableNo = it.tableNo,
                                time = it.createdAt,
                                enabled = currentActiveOrderId.value == it.id,
                                onClick = { currentActiveOrderId.value = it.id }
                            )
                        }
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(white)
            ) {
                OrderDetail(
                    currentActiveOrderId.value?.let { tables.find { table -> it == table.id } },
                    buttonText = "Make Payment",
                    buttonAction = { makePayment(currentActiveOrderId.value) },
                    buttonEnabled = currentActiveCategory.value == OrderStatus.Completed,
                    deleteAction = { cancelOrder(currentActiveOrderId.value) },
                    deleteText = "Cancel Order",
                    deleteEnabled = currentActiveCategory.value == OrderStatus.Pending
                )
            }
        }
    }
}
