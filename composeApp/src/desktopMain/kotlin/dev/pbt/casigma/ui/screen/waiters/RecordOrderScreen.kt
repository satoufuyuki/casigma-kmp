package dev.pbt.casigma.ui.screen.waiters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.ui.components.DropdownTable
import dev.pbt.casigma.ui.components.MenuCard
import dev.pbt.casigma.ui.components.OrderDetail
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.theme.neutral
import dev.pbt.casigma.ui.theme.white

class RecordOrderScreen(override val route: String): ScreenBase(route) {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun render() {
        val state = rememberScrollState()
        val isDropDownExpanded = remember {
            mutableStateOf(false)
        }

        val currentActiveCategory = remember {
            mutableStateOf(0)
        }

        val mockData = listOf("Table 1", "Table 2", "Table 3")
        val menuCategory = listOf("Foods", "Drinks", "Desserts")

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
                        "Please select the menu!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 40.sp
                    )
                    DropdownTable(mockData)
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                ) {
                    menuCategory.forEachIndexed { index, category ->
                        TextButton(onClick = { currentActiveCategory.value = index }) {
                            Text(
                                category,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp,
                                color = if (currentActiveCategory.value == index) Color.Black else Color.Gray,
                            )
                        }
                    }
                }
                // Menu Card
                FlowRow (
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
                    modifier = Modifier.fillMaxWidth().verticalScroll(state),
                    overflow = FlowRowOverflow.Visible
                ) {
                    for (i in 0..19) {
                        MenuCard()
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(white)
            ) {
                OrderDetail()
            }
        }
    }
}