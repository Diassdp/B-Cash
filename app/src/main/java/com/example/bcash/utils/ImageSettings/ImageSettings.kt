package com.example.bcash.utils.ImageSettings

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.bcash.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


private const val FILENAME_FORMAT = "dd-MMM-yyyy"

fun getCurrentTimeStamp(): String =
    SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(getCurrentTimeStamp(), ".jpg", storageDirectory).apply {
        deleteOnExit()
    }
}

fun createFile(application: Application): File {
    val mediaDirectory = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDirectory != null && mediaDirectory.exists()
    ) mediaDirectory else application.filesDir

    return File(outputDirectory, "${getCurrentTimeStamp()}.jpg")
}

fun convertUriToFile(image: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream =
        contentResolver.openInputStream(image) ?: throw NullPointerException("InputStream is null")
    return try {
        val file = createTempFile(context)
        val output = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) output.write(buffer, 0, length)
        file
    } finally {
        inputStream.close()
    }
}

fun compressFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
        val byteArray = outputStream.toByteArray()
        streamLength = byteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000 && compressQuality > 0)

    val fileOutputStream = FileOutputStream(file)
    return try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fileOutputStream)
        file
    } finally {
        fileOutputStream.close()
    }
}
