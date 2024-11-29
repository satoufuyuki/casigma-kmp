package dev.pbt.casigma.modules.utils

import java.text.NumberFormat
import java.util.Locale

object TextUtils {
    fun formatRupiah(amount: Float): String {
        return "Rp${NumberFormat.getNumberInstance(Locale.GERMANY).format(amount)}"
    }
}