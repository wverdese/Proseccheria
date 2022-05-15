package me.wverdese.proseccheria

fun Int.asTwoDigits(): String = if (this in 0..9) "0$this" else toString()
