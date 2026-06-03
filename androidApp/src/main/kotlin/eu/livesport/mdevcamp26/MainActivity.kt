package eu.livesport.mdevcamp26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import eu.livesport.mdevcamp26.ui.detail.DetailScreen
import eu.livesport.mdevcamp26.ui.home.HomeScreen
import eu.livesport.mdevcamp26.ui.navigation.Route.CountryDetail
import eu.livesport.mdevcamp26.ui.navigation.Route.Home
import eu.livesport.mdevcamp26.ui.theme.WcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WcTheme {
                val backStack = rememberNavBackStack(Home)

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    },
                    entryProvider = entryProvider {
                        entry<Home> {
                            HomeScreen(
                                onCountryClick = { code -> backStack.add(CountryDetail(code)) }
                            )
                        }
                        entry<CountryDetail> { route ->
                            DetailScreen(
                                countryCode = route.code,
                                onBackClick = { backStack.removeLastOrNull() })
                        }
                    }
                )
            }
        }
    }
}
