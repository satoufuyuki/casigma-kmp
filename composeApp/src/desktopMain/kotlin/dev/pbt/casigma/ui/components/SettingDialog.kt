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
import dev.pbt.casigma.modules.datastore.SettingRepository
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white
import org.koin.compose.koinInject

@Composable
fun SettingDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    val settingRepository: SettingRepository = koinInject()
    val dbDrivers = remember { listOf("com.mysql.cj.jdbc.Driver", "org.postgresql.Driver", "org.mariadb.jdbc.Driver") }
    val dbDriver = remember { mutableStateOf(settingRepository.dbDriver) }
    val dbUrl = remember { mutableStateOf(settingRepository.dbUrl) }
    val dbUsername = remember { mutableStateOf(settingRepository.dbUsername) }
    val dbPassword = remember { mutableStateOf(settingRepository.dbPassword) }

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
                    text = "Configure App Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    DropdownDbDriver(
                        drivers = dbDrivers,
                        currentDriverIndex = dbDrivers.indexOf(dbDriver.value),
                        onDriverSelected = {
                            dbDriver.value = dbDrivers[it]
                        }
                    )
                    OutlinedTextField(
                        value = dbUrl.value ?: "",
                        onValueChange = {
                            dbUrl.value = it
                        },
                        label = {
                            Text("Database URL")
                        }
                    )
                    OutlinedTextField(
                        value = dbUsername.value ?: "",
                        onValueChange = {
                            dbUsername.value = it
                        },
                        label = {
                            Text("Database Username")
                        }
                    )
                    OutlinedTextField(
                        value = dbPassword.value ?: "",
                        onValueChange = {
                            dbPassword.value = it
                        },
                        label = {
                            Text("Database Password")
                        }
                    )
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
                            onConfirm(dbDriver.value, dbUrl.value, dbUsername.value, dbPassword.value)
                        },
                    ) {
                        Text("Save",
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