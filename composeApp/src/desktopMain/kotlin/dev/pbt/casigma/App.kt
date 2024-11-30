package dev.pbt.casigma

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import casigma.composeapp.generated.resources.*
import casigma.composeapp.generated.resources.Res
import casigma.composeapp.generated.resources.routes_login
import casigma.composeapp.generated.resources.routes_waiters_order_list_screen
import casigma.composeapp.generated.resources.routes_waiters_record_order_screen
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.UserObject
import dev.pbt.casigma.modules.datastore.SettingRepository
import dev.pbt.casigma.modules.providers.Argon2
import dev.pbt.casigma.modules.providers.Auth
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.OrderUtils
import dev.pbt.casigma.modules.viewmodel.MenuViewModel
import dev.pbt.casigma.modules.viewmodel.TableViewModel
import dev.pbt.casigma.ui.components.MenuBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import dev.pbt.casigma.ui.components.TopBar
import dev.pbt.casigma.ui.screen.LoginScreen
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.screen.cashier.CashierOrderListScreen
import dev.pbt.casigma.ui.screen.chef.ChefOrderListScreen
import dev.pbt.casigma.ui.theme.AppTheme
import dev.pbt.casigma.ui.screen.waiters.OrderListScreen
import dev.pbt.casigma.ui.screen.waiters.RecordOrderScreen
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class GlobalContext(val variableName: String) {
    ManagedOrderId("managedOrderId"),
    DatabaseConnected("databaseConnected"),
    LastDatabaseError("lastDatabaseError")
}

enum class AppScreen(val title: StringResource) {
    WaitersOrderList(Res.string.routes_waiters_order_list_screen),
    WaitersRecordOrder(Res.string.routes_waiters_record_order_screen),

    ChefOrderList(Res.string.routes_chef_order_list_screen),

    CashierOrderList(Res.string.routes_cashier_order_list_screen),
    Login(Res.string.routes_login)
}

@Composable
@Preview
fun App(windowScope: FrameWindowScope) {
    val navController: NavHostController = rememberNavController()
    val authenticatedUser: MutableState<UserObject?> = remember { mutableStateOf(null) }

    // Dialog Provider
    val isShown: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertComponent: MutableState<(@Composable () -> Unit)?> = remember { mutableStateOf(null) }

    // Global context
    val globalManagedOrderId = remember { mutableStateOf<Int?>(null) }
    val globalDatabaseConnected = remember { mutableStateOf(false) }
    val globalLastDatabaseError = remember { mutableStateOf<String?>(null) }

    KoinApplication(application = {
        val mainModules = module {
            // Global context
            single(named(GlobalContext.ManagedOrderId)) { globalManagedOrderId }
            single(named(GlobalContext.DatabaseConnected)) { globalDatabaseConnected }
            single(named(GlobalContext.LastDatabaseError)) { globalLastDatabaseError }

            single { windowScope }
            single { SettingRepository() }
            single { navController }
            single { authenticatedUser }
            single { Argon2() }
            single { Database(get(), globalLastDatabaseError, globalDatabaseConnected) }
            single { DialogProvider(isShown, alertComponent) }
            factory { Auth(get(), get(), get()) }
            factory { OrderUtils(get()) }

            // View models
            viewModel { MenuViewModel(get()) }
            viewModel { TableViewModel(get(), get()) }
        }

        val screens = module {
            factory<ScreenBase>(named(AppScreen.Login)) { LoginScreen(AppScreen.Login.name) }
            factory<ScreenBase>(named(AppScreen.WaitersOrderList)) { OrderListScreen(AppScreen.WaitersOrderList.name) }
            factory<ScreenBase>(named(AppScreen.WaitersRecordOrder)) { RecordOrderScreen(AppScreen.WaitersRecordOrder.name) }
            factory<ScreenBase>(named(AppScreen.ChefOrderList)) { ChefOrderListScreen(AppScreen.ChefOrderList.name) }
            factory<ScreenBase>(named(AppScreen.CashierOrderList)) { CashierOrderListScreen(AppScreen.CashierOrderList.name) }
        }

        modules(mainModules, screens)
    }) {
        val koin = getKoin()
        val dialogProvider: DialogProvider = koinInject()
        val db: Database = koinInject()

        // First connect
        db.connect()

        AppTheme {
            Scaffold(
                topBar = {
                    if (authenticatedUser.value !== null) {
                        TopBar()
                    }
                }
            ) {
                if (dialogProvider.isShown.value) {
                    dialogProvider.render()
                }

                MenuBar()
                NavHost(navController = navController, startDestination = AppScreen.Login.name) {
                    AppScreen.entries.forEach {
                        val screen = koin.get<ScreenBase>(qualifier = named(it))
                        composable(screen.route) {
                            screen.render()
                        }
                    }
                }
            }
        }
    }
}