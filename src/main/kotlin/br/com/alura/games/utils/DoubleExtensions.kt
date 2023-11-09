package br.com.alura.games.utils

inline fun <reified Target : Number> Double.convertToType(): Target {
    return when (Target::class) {
        Double::class -> this as Target
        Float::class -> this.toFloat() as Target
        Int::class -> this.toInt() as Target
        Long::class -> this.toLong() as Target
        Short::class -> this.toInt().toShort() as Target
        Byte::class -> this.toInt().toByte() as Target
        else -> throw UnsupportedOperationException("Conversão de tipo não suportada")
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return Math.round(this * multiplier) / multiplier
}