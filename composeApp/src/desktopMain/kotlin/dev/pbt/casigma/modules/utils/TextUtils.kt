package dev.pbt.casigma.modules.utils

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TextUtils {
    fun formatRupiah(amount: Float): String {
        return "Rp${NumberFormat.getNumberInstance(Locale.GERMANY).format(amount)}"
    }

    fun formatDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return date.format(formatter)
    }

    fun formatTime(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm 'WIB'")
        return date.format(formatter)
    }

    fun toTitleCase(input: String): String {
        return input.split(" ").joinToString(" ") { s ->
            s.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
    }
}
