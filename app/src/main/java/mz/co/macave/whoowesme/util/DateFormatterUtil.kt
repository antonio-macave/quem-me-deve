package mz.co.macave.whoowesme.util

import android.os.Build
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(date: String): String {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        val parsedDate = LocalDate.parse(date, inputFormat)
        parsedDate.format(outputFormat)

    } else {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate!!)

    }
}

fun Double.toMzn(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt")).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }
    return "${numberFormat.format(this)} MZN"
}