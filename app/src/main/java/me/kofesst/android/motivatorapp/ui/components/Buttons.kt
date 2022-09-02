package me.kofesst.android.motivatorapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import me.kofesst.android.motivatorapp.presentation.utils.UiText

@Composable
fun AppTextButton(
    uiText: UiText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    AppTextButton(
        content = {
            Text(
                text = uiText(),
                style = textStyle,
                color = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@Composable
fun AppTextButton(
    content: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    TextButton(
        onClick = onClick,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        content(this)
    }
}

@Composable
fun AppButton(
    uiText: UiText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = uiText().uppercase(),
            style = textStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}