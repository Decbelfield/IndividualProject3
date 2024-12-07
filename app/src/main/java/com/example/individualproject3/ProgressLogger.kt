package com.example.individualproject3

import android.content.Context
import java.io.File
import java.io.FileWriter

object ProgressLogger {
    fun logProgress(context: Context, progress: String) {
        val file = File(context.filesDir, "progress_log.txt")
        val writer = FileWriter(file, true)
        writer.append(progress)
        writer.close()
    }
}
