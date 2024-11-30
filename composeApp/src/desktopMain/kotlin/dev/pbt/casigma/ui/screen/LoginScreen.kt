package dev.pbt.casigma.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.pbt.casigma.AppScreen
import dev.pbt.casigma.GlobalContext
import dev.pbt.casigma.modules.database.models.UserObject
import dev.pbt.casigma.modules.database.models.UserRole
import dev.pbt.casigma.modules.providers.Auth
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.AlertUtils
import dev.pbt.casigma.ui.components.CasigmaLogo
import dev.pbt.casigma.ui.theme.neutral
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class LoginScreen(override val route: String): ScreenBase(route) {
    @Composable
    override fun render() {
        val globalDatabaseConnected = koinInject<MutableState<Boolean>>(qualifier = named(GlobalContext.DatabaseConnected))
        val authenticatedUser: MutableState<UserObject?> = koinInject()
        val authProvider = koinInject<Auth>()
        val navProvider = koinInject<NavHostController>()
        val dialogProvider: DialogProvider = koinInject()
        val isSubmitting = remember { mutableStateOf(false) }
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        fun handleLogin() {
            if (!globalDatabaseConnected.value) {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildError("Database is not connected! (Last error: ${koinInject<MutableState<String?>>(qualifier = named(GlobalContext.LastDatabaseError)).value})")
                }.show()
                return
            }

            isSubmitting.value = true
            if (!authProvider.authenticate(username.value, password.value)) {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildError("Invalid username or password!")
                }.show()
            } else {
                when (authenticatedUser.value?.role) {
                    UserRole.Admin -> navProvider.navigate(AppScreen.WaitersOrderList.name)
                    UserRole.Waiters -> navProvider.navigate(AppScreen.WaitersOrderList.name)
                    UserRole.Chef -> navProvider.navigate(AppScreen.ChefOrderList.name)
                    UserRole.Cashier -> navProvider.navigate(AppScreen.CashierOrderList.name)
                    null -> throw IllegalStateException("User role is unknown")
                }
            }

            isSubmitting.value = false
        }

        return Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(neutral)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(520.dp)
            ) {
                CasigmaLogo(true, 48, primaryLight, Modifier)
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSubmitting.value
                    )
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSubmitting.value
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = primaryLight,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { handleLogin() },
                    enabled = !isSubmitting.value
                ) {
                    Text("Login",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = white,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}