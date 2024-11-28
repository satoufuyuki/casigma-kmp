package dev.pbt.casigma

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import casigma.composeapp.generated.resources.Res
import casigma.composeapp.generated.resources.routes_login
import casigma.composeapp.generated.resources.routes_waiters_order_list_screen
import casigma.composeapp.generated.resources.routes_waiters_record_order_screen
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.providers.Argon2
import dev.pbt.casigma.modules.providers.Auth
import org.jetbrains.compose.ui.tooling.preview.Preview

import dev.pbt.casigma.ui.components.TopBar
import dev.pbt.casigma.ui.screen.LoginScreen
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.theme.AppTheme
import dev.pbt.casigma.ui.screen.waiters.OrderListScreen
import dev.pbt.casigma.ui.screen.waiters.RecordOrderScreen
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

enum class AppScreen(val title: StringResource) {
    WaitersOrderList(Res.string.routes_waiters_order_list_screen),
    WaitersRecordOrder(Res.string.routes_waiters_record_order_screen),

    Login(Res.string.routes_login)
}


@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()

    KoinApplication(application = {
        val mainModules = module {
            single { navController }
            single { Argon2() }
            single { Database() }
            factory { Auth(get(), get()) }
        }

        val screens = module {
            factory<ScreenBase>(named(AppScreen.Login)) { LoginScreen(AppScreen.Login.name) }
            factory<ScreenBase>(named(AppScreen.WaitersOrderList)) { OrderListScreen(AppScreen.WaitersOrderList.name) }
            factory<ScreenBase>(named(AppScreen.WaitersRecordOrder)) { RecordOrderScreen(AppScreen.WaitersRecordOrder.name) }
        }

        modules(mainModules, screens)
    }) {
        val koin = getKoin()
        val authProvider = koinInject<Auth>()
        AppTheme {
            Scaffold(
                topBar = {
                    val authenticatedUser by authProvider::authenticatedUser
                    if (authenticatedUser.value !== null) {
                        TopBar()
                    }
                }
            ) {
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