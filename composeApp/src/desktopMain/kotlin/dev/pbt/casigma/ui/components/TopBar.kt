package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = primaryLight,
            titleContentColor = white,
        ),
        title = {
            CasigmaLogo(true, 48, white)
        },
        modifier = Modifier.height(104.dp)
    )
}