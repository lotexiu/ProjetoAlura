package br.com.alura.games.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

private val patternList = listOf(
    "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
    "yyyy-MM-dd'T'HH:mm:ssZ",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy-MM-dd HH:mm",
    "yyyy-MM-dd",
    "dd-MM-yyyy'T'HH:mm:ss.SSSZ",
    "dd-MM-yyyy'T'HH:mm:ssZ",
    "dd-MM-yyyy HH:mm:ss",
    "dd-MM-yyyy HH:mm",
    "dd-MM-yyyy",)

fun String.getAge(): Int {
    val pattern = "dd/MM/yyyy"
    try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val dataNascimento = LocalDate.parse(this, formatter)
        return Period.between(dataNascimento, LocalDate.now()).years
    } catch (ex: Exception) {
        throw Exception(
            "Não foi possivel converter \"$this\" em idade.\n" +
                    "Formato obrigatorio: $pattern"
        )
    }
}

fun String.toDate(pattern: String? = null): LocalDateTime {
    if (pattern != null) {
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
    }
    val value = this.replace("/", "-")
    val finalPosition = value.indexOf("-")
    val withZone = value.contains("T")
    val totalColon = value.count { it == ':' }
    val withMS = value.contains(".")

    val newPatternList = patternList.filter { it ->
        (it.indexOf("-") == finalPosition) &&
        (it.contains("T") == withZone) &&
        (it.contains("SSS") == withMS) &&
        (it.count { it == ':'} == totalColon)
    }

    val patternResult = DateTimeFormatter.ofPattern(newPatternList[0])
    return when(totalColon){
        0 -> LocalDate.parse(value, patternResult).atStartOfDay()
        else -> LocalDateTime.parse(value, patternResult)
    }
}


/**
 * @return Verdadeiro ou Falso com base na String sendo:
 *
 * f, false, t, true ou um campo vazio.
 * @exception IllegalArgumentException
 * Ocorre caso a string não seja igual ao que foi citado acima.
 *
 */
fun String.toBoolean(): Boolean {
    val expr = this.trim().lowercase()
    return when (expr) {
        "true", "t" -> true
        "false", "f", "" -> false
        else -> throw IllegalArgumentException("Não é possível converter para Boolean")
    }
}

/**
 * @return Verdadeiro ou Falso com base na validação do regex abaixo:
 *
 * ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}
 * */
fun String.validateAsEmail(): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    return regex.matches(this)
}

/**
 * Converte uma string JSON para um objeto do tipo desejado.
 *
 * @param Target O tipo do objeto a ser convertido pela String.
 * @return Um objeto do tipo [Target] convertido.
 */
inline fun <reified Target: Any> String.toObject(): Target {
    return when {
        Target::class == String::class -> this as Target
        Target::class == List::class -> Gson().fromJson(this, object : TypeToken<Target>() {}.type)
        else -> Gson().fromJson(this, Target::class.java)
    }
}