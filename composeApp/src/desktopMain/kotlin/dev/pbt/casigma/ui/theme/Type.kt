package dev.pbt.casigma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import casigma.composeapp.generated.resources.*
import casigma.composeapp.generated.resources.Poppins_Black
import casigma.composeapp.generated.resources.Poppins_LightItalic
import casigma.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun AppFont(): FontFamily {
    return FontFamily(
        Font(Res.font.Poppins_Black, FontWeight.Black, FontStyle.Normal),
        Font(Res.font.Poppins_BlackItalic, FontWeight.Black, FontStyle.Italic),
        Font(Res.font.Poppins_Bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.Poppins_BoldItalic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.Poppins_ExtraBold, FontWeight.ExtraBold, FontStyle.Normal),
        Font(Res.font.Poppins_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(Res.font.Poppins_ExtraLight, FontWeight.ExtraLight, FontStyle.Normal),
        Font(Res.font.Poppins_ExtraLightItalic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(Res.font.Poppins_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.Poppins_LightItalic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.Poppins_Medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.Poppins_MediumItalic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.Poppins_Regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.Poppins_SemiBold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.Poppins_SemiBoldItalic, FontWeight.SemiBold, FontStyle.Italic),
        Font(Res.font.Poppins_Thin, FontWeight.Thin, FontStyle.Normal),
        Font(Res.font.Poppins_ThinItalic, FontWeight.Thin, FontStyle.Italic)
    )
}