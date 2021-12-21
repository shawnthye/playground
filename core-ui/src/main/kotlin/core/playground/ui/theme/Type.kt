package core.playground.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import core.playground.ui.R

private val Montserrat = FontFamily(
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
)

@Suppress("SpellCheckingInspection")
private val Lekton = FontFamily(
    Font(R.font.lekton_bold, FontWeight.Bold),
)

private val Default = Typography()

internal val PlaygroundTypography = Typography(
    h1 = Default.h1.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 96.sp,
    ),
    h2 = Default.h2.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 60.sp,
    ),
    h3 = Default.h3.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
    ),
    h4 = Default.h4.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
    ),
    h5 = Default.h5.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
    ),
    h6 = Default.h6.copy(
        fontFamily = Lekton,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
    ),
    subtitle1 = Default.subtitle1.copy(
        fontFamily = Lekton,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
    ),
    subtitle2 = Default.subtitle2.copy(
        fontFamily = Lekton,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
    ),
    body1 = Default.body1.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    body2 = Default.body2.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    button = Default.button.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    caption = Default.caption.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
    overline = Default.overline.copy(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    ),
)
