package me.kofesst.android.motivatorapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Парольное текстовое поле приложения. Является модификацией
 * [AppTextField].
 *
 * [modifier] - модификации поля.
 *
 * [value] - значение поля.
 *
 * [onValueChange] - функция, вызываемая при изменении значения поля.
 *
 * [isReadOnly] - доступно ли поле для записи только для чтения.
 *
 * [errorMessage] - сообщение об ошибке.
 *
 * [label] - название поля.
 *
 * [leadingIcon] - иконка в левой части поля.
 *
 * [singleLine] - если равно true, то в поле нельзя будет делать переносы текста.
 *
 * [textStyle] - стиль текста в поле.
 */
@Composable
fun AppPasswordField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isReadOnly: Boolean = false,
    errorMessage: String? = null,
    label: String = "",
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    AppTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        isReadOnly = isReadOnly,
        errorMessage = errorMessage,
        label = label,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        textStyle = textStyle,
        modifier = modifier,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = if (passwordVisible) {
            Icons.Outlined.Visibility
        } else {
            Icons.Outlined.VisibilityOff
        },
        onTrailingIconClick = {
            passwordVisible = !passwordVisible
        }
    )
}

/**
 * Стандартное текстовое поле приложения.
 *
 * [modifier] - модификации поля.
 *
 * [value] - значение поля.
 *
 * [onValueChange] - функция, вызываемая при изменении значения поля.
 *
 * [isReadOnly] - доступно ли поле для записи только для чтения.
 *
 * [errorMessage] - сообщение об ошибке.
 *
 * [label] - название поля.
 *
 * [leadingIcon] - иконка в левой части поля.
 *
 * [trailingIcon] - иконка в правой части поля.
 *
 * [onTrailingIconClick] - функция, вызываемая при нажатии на [trailingIcon].
 *
 * [singleLine] - если равно true, то в поле нельзя будет делать переносы текста.
 *
 * [textStyle] - стиль текста в поле.
 *
 * [keyboardOptions] - настройки клавиатуры при открытии поля.
 *
 * [visualTransformation] - визуальные изменения текста в поле.
 *
 * [maxCounter] - максимальное количество символов, которое можно
 * вписать в поле. Если равно -1, то количество символов будет
 * неограниченным.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isReadOnly: Boolean = false,
    errorMessage: String? = null,
    label: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxCounter: Int = -1,
) {
    val counter = value.length
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (maxCounter < 0 || it.length <= maxCounter) {
                    onValueChange(it)
                }
            },
            readOnly = isReadOnly,
            isError = errorMessage != null,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            } else {
                null
            },
            singleLine = singleLine,
            trailingIcon = if (trailingIcon != null) {
                {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null
                        )
                    }
                }
            } else {
                null
            },
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextFieldError(
                message = errorMessage
            )
            if (maxCounter >= 0) {
                Spacer(
                    modifier = Modifier.weight(1.0f)
                )
                TextFieldCounter(
                    counter = counter,
                    maxCounter = maxCounter
                )
            }
        }
    }
}

/**
 * Счетчик символов для текстового поля.
 *
 * [counter] - текущее значение счетчика.
 *
 * [maxCounter] - максимальное значение счетчика.
 *
 * [modifier] - модификации счетчика.
 *
 * [textStyle] - стиль текста счетчика.
 */
@Composable
private fun TextFieldCounter(
    counter: Int,
    maxCounter: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    Text(
        text = "$counter / $maxCounter",
        textAlign = TextAlign.End,
        style = textStyle,
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    )
}

/**
 * Текст с ошибкой для текстового поля.
 *
 * [modifier] - модификации для текста.
 *
 * [message] - сообщение об ошибке.
 */
@Composable
private fun TextFieldError(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    AnimatedVisibility(visible = message != null) {
        Text(
            text = message ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }
}