package me.kofesst.android.motivatorapp.presentation.utils

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

var hasStoragePermission: Boolean = false

val imagePickerIntent = Intent().apply {
    type = "image/*"
    action = ACTION_GET_CONTENT
}

@Composable
fun rememberPermissionLauncher(
    onDismiss: () -> Unit = {},
    onAccess: () -> Unit = {},
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) {
    hasStoragePermission = it

    if (hasStoragePermission) {
        onAccess()
    } else {
        onDismiss()
    }
}

@Composable
fun rememberFilePicker(
    onFileSelected: (Intent) -> Unit = {},
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    if (it.resultCode == RESULT_OK && it.data != null) {
        onFileSelected(it.data!!)
    }
}
