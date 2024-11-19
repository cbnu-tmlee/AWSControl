package kr.ac.cbnu.software.aws_control.presentation.screen.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.InputStream

fun getFileName(context: Context, uri: Uri): String {
    var fileName = "unknown"
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = it.getString(displayNameIndex)
            }
        }
    }

    return fileName
}

fun getFileBinary(context: Context, uri: Uri): ByteArray {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    return inputStream?.readBytes() ?: byteArrayOf()
}