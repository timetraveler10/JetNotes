package com.hussein.jetnotes.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val JetShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
)

object ListComponentsShapes {
    val TopItem =
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 2.dp, bottomEnd = 2.dp)

    val MidItem = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp, bottomStart = 2.dp, bottomEnd = 2.dp)
    val BottomItem = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
}