package com.example.privatechatplats.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KeyDialog(
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var key by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Ingrese la clave para chatear") },
        text = {
            OutlinedTextField(
                value = key,
                onValueChange = { key = it },
                label = { Text("Clave") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(key) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

@Preview
@Composable
fun PreviewKeyDialog() {
    KeyDialog(onConfirm = {}, onCancel = {})
}
