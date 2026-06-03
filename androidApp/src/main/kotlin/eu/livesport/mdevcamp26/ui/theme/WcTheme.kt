package eu.livesport.mdevcamp26.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import eu.livesport.mdevcamp26.R

val ArchivoBlackFamily = FontFamily(
    Font(R.font.archivo_black, weight = FontWeight.Black),
)

val InterFamily = FontFamily(
    Font(R.font.inter_regular, weight = FontWeight.Normal),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
)

val MonoFamily = FontFamily(
    Font(R.font.jetbrains_mono_regular, weight = FontWeight.Normal),
    Font(R.font.jetbrains_mono_medium, weight = FontWeight.Medium),
)

val WcLightColors = lightColorScheme(
    background = LightBg,
    surface = LightCard,
    onBackground = LightInk,
    onSurface = LightInk,
    primary = Accent,
    onPrimary = AccentInk,
    error = LightHeat,
    outline = LightInk.copy(alpha = 0.12f),
    surfaceVariant = LightBg,
    onSurfaceVariant = LightInk.copy(alpha = 0.7f),
)

val WcDarkColors = darkColorScheme(
    background = DarkBg,
    surface = DarkCard,
    onBackground = DarkInk,
    onSurface = DarkInk,
    primary = AccentDark,
    onPrimary = AccentInk,
    error = DarkHeat,
    outline = DarkInk.copy(alpha = 0.12f),
    surfaceVariant = DarkBg,
    onSurfaceVariant = DarkInk.copy(alpha = 0.7f),
)

val WcTypography = Typography(
    displayLarge = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    displayMedium = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 36.sp, lineHeight = 44.sp),
    headlineLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    titleSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    bodySmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelLarge = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)

val WcHeroDisplay = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 54.sp, lineHeight = 48.sp, letterSpacing = (-2).sp)
val WcStatValueLarge = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 34.sp, lineHeight = 34.sp)
val WcStatValueMedium = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 30.sp, lineHeight = 30.sp, letterSpacing = (-1).sp)
val WcStatValueSmall = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 18.sp, lineHeight = 19.8.sp, letterSpacing = (-1).sp)
val WcMomentTitle = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Black, fontSize = 22.sp, lineHeight = 23.sp, letterSpacing = (-1).sp)
val WcKickoffLabel = TextStyle(fontFamily = ArchivoBlackFamily, fontWeight = FontWeight.Normal, fontSize = 18.sp)
val WcCountdownDigit = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Medium, fontSize = 30.sp, letterSpacing = 1.sp)
val WcCountdownUnits = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Normal, fontSize = 9.sp, letterSpacing = 2.sp)
val WcMicroLabel = TextStyle(fontFamily = MonoFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp, letterSpacing = 1.sp)
val WcChipLabel = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
val WcButtonLabel = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)

@Composable
fun WcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val controller = WindowCompat.getInsetsController(
                (view.context as Activity).window, view
            )
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = if (darkTheme) WcDarkColors else WcLightColors,
        typography = WcTypography,
        content = content,
    )
}
