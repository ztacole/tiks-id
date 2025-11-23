package com.zetta.tiksid.utils

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    val result = format.format(amount)

    return result.replace(",00", ",-")
}