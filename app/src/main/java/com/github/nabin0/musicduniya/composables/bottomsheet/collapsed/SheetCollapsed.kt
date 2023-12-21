package com.github.nabin0.musicduniya.composables.bottomsheet.collapsed

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.github.nabin0.musicduniya.extension.noRippleClickable

@Composable
fun SheetCollapsed(
    isCollapsed: Boolean,
    currentFraction: Float,
    onSheetClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(alpha = 1f - (currentFraction * 100))
            .noRippleClickable(
                onClick = onSheetClick,
                enabled = isCollapsed
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}