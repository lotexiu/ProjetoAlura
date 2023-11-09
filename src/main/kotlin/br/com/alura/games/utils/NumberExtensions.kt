package br.com.alura.games.utils

/**
 * Retorna o valor em porcentagem.
 *
 * @param multiply Um parâmetro que ao realizar a porcentagem será * em vez de /.
 * @return Retorna o valor realizando multiplicação ou divisão por 100,0
 */
fun Number.percent(multiply: Boolean = false): Double {
    return when(multiply) {
        true -> this * 100.0
        else -> this / 100.0
    }
}

operator fun Number.compareTo(number: Number): Int {
    return when{
        this.toDouble() > number.toDouble() -> 1
        this.toDouble() < number.toDouble() -> -1
        else -> 0
    }
}

operator fun Number.div(d: Number): Double {
    return this.toDouble() / d.toDouble()
}

operator fun Number.times(d: Number): Double {
    return this.toDouble() * d.toDouble()
}
