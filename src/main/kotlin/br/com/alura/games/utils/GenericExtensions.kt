package br.com.alura.games.utils

import com.google.gson.Gson
import org.springframework.util.StringUtils
import java.math.BigDecimal
import java.time.LocalDate
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaMethod

/**
 * Converte um objeto para uma representação textual, exibindo todos os campos com seus valores e tipos.
 *
 * @param vararg Um parâmetro que aceita um número indefinido de parâmetros de determinado tipo
 * @param exceptParams Lista de String (`vararg`) de nomes dos campos a serem excluídos da representação base.
 * @param acceptParams Lista de String (`vararg`) de nomes dos campos a serem incluídos da representação base.
 * @param capitalize Indica se os nomes dos campos devem ser capitalizados.
 * @param showType Indica se os tipos dos campos devem ser exibidos.
 * @return Uma representação textual do objeto com seus campos, valores e tipos.
 */
fun <T : Any> T.toStr(
    vararg acceptParams: String,
    capitalize: Boolean = false,
    showType: Boolean = true,
): String{
    return this.toStr(capitalize = capitalize, showType = showType, acceptParams = acceptParams.asList(), depth = 1)
}

fun <T : Any> T.toStr(
    vararg exceptParams: String,
    capitalize: Boolean = false,
    showType: Boolean = true,
    acceptParams: List<String> = emptyList(),
    depth: Int = 1
): String {
    if (depth > 2) return this.toString()

    val properties = this::class.memberProperties
    fun capitalizeString(text: String): String {
        return if (capitalize) text.capitalize() else text
    }

    fun getPropertyString(prop: KProperty1<T, *>): String {
        val value: Any = runCatching { prop.get(this) }.getOrNull() ?: return "${capitalizeString(prop.name)}: private"

        val type = (prop.returnType.classifier as? KClass<*>)?.simpleName ?: "Unknown"
        val typeString = if (depth == 1 && showType) "($type) " else ""

        val propertyValue = if (value.toString().matches("-?\\d+(\\.\\d+)?".toRegex())) {
            value.toString()
        } else {
            value.toStr(depth = depth + 1)
        }

        return "$typeString${capitalizeString(prop.name)}: $propertyValue"
    }

    val method = this::class.functions.find { it.name == "toStr" }

    if (this is List<*> || this is Array<*>) {
        val thisList = this as List<*>
        val size = thisList.size
        if (size == 0) return "Size: 0 []"
        val elementsStr = thisList.joinToString(",\n\n") { it?.toStr(depth = depth + 1) ?: "null" }
        return "Size: $size\n[$elementsStr]\n"
    }

    if (method != null) {
        return method.javaMethod?.invoke(this) as String
    }

    if (this.itComeFrom("java", "javax", "kotlin")) {
        return this.toString()
    }

    if (properties.isNotEmpty() && depth == 1) {
        return properties
            .filterNot { it.name in exceptParams }
            .filter { acceptParams.isEmpty() || it.name in acceptParams }
            .joinToString("\n") { getPropertyString(it as KProperty1<T, *>) }
    }

    if (properties.size > 1 && depth == 2) {
        return "("+ properties.joinToString(", ") { getPropertyString(it as KProperty1<T, *>) } + ")"
    }

    return this.toString()
}

fun Any.itComeFrom(vararg names:String): Boolean {
    return names.any { this::class.qualifiedName.toString().startsWith(it) }
}

/**
 * Lê uma linha digitada e a converte para um tipo específico.
 *
 * @param T O tipo para o qual a entrada deve ser convertida.
 * @param text O texto de entrada a ser lido.
 * @param lowercase Indica se a entrada deve ser convertida para minúsculas.
 * @return O valor convertido do tipo [T].
 * @throws Exception Se o tipo de conversão não é suportado ou ocorre uma falha na conversão.
 */
inline fun <reified T> input(text: String, lowercase: Boolean = false): T {
    print(if (!text.endsWith("\n") && !text.endsWith(" ")) "$text " else text)
    val result = readlnOrNull() ?: ""

    try {
        return when (T::class) {
            BigDecimal::class -> result.toBigDecimal() as T
            Double::class -> result.toDouble() as T
            Float::class -> result.toFloat() as T
            Byte::class -> result.toByte() as T
            Short::class -> result.toShort() as T
            Int::class -> result.toInt() as T
            Long::class -> result.toLong() as T
            Boolean::class -> result.toBoolean() as T
            String::class -> (if (lowercase) result.lowercase() else result) as T
            Any::class -> result as T
            else -> {
                throw Exception("Tipo ${T::class} não suportado.")
            }
        }
    } catch (ex: Exception) {
        throw TypeMismatchException("Não é possível converter \"$result\" para o tipo ${T::class}")
    }
}

/**
 * Lê a entrada do usuário e a interpreta como uma confirmação (verdadeiro ou falso).
 *
 * @param text O texto a ser exibido para a confirmação.
 * @return Verdadeiro se a entrada for "s" ou "sim", falso caso contrário.
 */
fun inputConfirm(text: String): Boolean {
    val answer: String = input(text, true)
    return (answer == "s" || answer == "sim")
}

/**
 * Gera uma sequência de números formatada com zeros à esquerda.
 *
 * @param max O valor máximo do número gerado (exclusivo).
 * @return Uma sequência numérica formatada com zeros à esquerda.
 */
fun numTag(max: Int): String {
    val num = Random.nextInt(max)
    return String.format("%04d", num)
}

/**
 * Gera um número aleatório num intervalo especificado, excluindo números específicos.
 *
 * @param T O tipo do número a ser gerado (ex: Double, Int, Long, etc.).
 * @param min O valor mínimo do intervalo.
 * @param max O valor máximo do intervalo.
 * @param exceptNumbers Uma lista de números a serem excluídos da geração.
 * @return Um número aleatório do tipo [T] dentro do intervalo especificado, excluindo os números excluídos.
 * @throws IllegalArgumentException Se o valor mínimo for maior ou igual ao valor máximo.
 */
inline fun <reified T : Number> generateNum(min: T, max: T, vararg exceptNumbers: T?): T {
    val minDouble = min.toDouble()
    val maxDouble = max.toDouble()

    if (minDouble >= maxDouble) {
        throw IllegalArgumentException("O valor mínimo deve ser menor que o valor máximo.")
    }
    var randomNum: T = Random.nextDouble(minDouble, maxDouble).convertToType()
    while (exceptNumbers.isNotEmpty() && exceptNumbers.contains(randomNum)) {
        randomNum = Random.nextDouble(minDouble, maxDouble).convertToType()
    }
    return randomNum
}

/**
 * Gera um número aleatório num intervalo especificado, excluindo números específicos.
 *
 * @param T O tipo do número a ser gerado (ex: Double, Int, Long, etc.).
 * @param max O valor máximo do intervalo.
 * @param exceptNumbers Uma lista de números a serem excluídos da geração.
 * @return Um número aleatório do tipo [T] dentro do intervalo especificado, excluindo os números excluídos.
 * @throws IllegalArgumentException Se o valor mínimo for maior ou igual ao valor máximo.
 */
inline fun <reified T : Number> generateNum(max: T, vararg exceptNumbers: T?): T {
    return generateNum(min = 0, max = max, *exceptNumbers) as T
}

/**
 * Avalia um script Kotlin e retorna o resultado da avaliação.
 *
 * @param script O script Kotlin a ser avaliado.
 * @return O resultado da avaliação do script.
 * @throws IllegalStateException Se o motor de script Kotlin não for encontrado.
 * @throws Exception Se ocorrer um erro durante a avaliação do script.
 */
fun eval(script: String): Any? {
    val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
        ?: throw IllegalStateException("Kotlin scripting engine not found")
    return engine.eval(script)
}

/**
 * Converte um objeto para uma representação JSON.
 *
 * @param T O tipo do objeto a ser convertido.
 * @return Uma representação JSON do objeto no formato de string.
 * @throws RuntimeException Se a biblioteca GSON não estiver presente no projeto.
 */
inline fun <reified T> T.toJson(): String {
    try {
        val gson = Gson()
        return gson.toJson(this)
    } catch (e: NoClassDefFoundError) {
        throw RuntimeException("GSON library not found. Please add the GSON dependency to your project.", e)
    }
}


/**
 * Converte um objeto do tipo [Source] para o tipo [Target] usando seus construtores correspondentes.
 *
 * @param Source O tipo do objeto de origem a ser convertido.
 * @param Target O tipo do objeto de destino após a conversão.
 * @return Um objeto do tipo [Target] convertido a partir do objeto de origem [Source].
 * @throws Exception Se ocorrer um erro durante a conversão.
 */
inline fun <reified Source : Any, reified Target : Any> Source.convert(): Target {
    try {
        val constructor = Target::class.java.constructors.firstOrNull {
            it.parameterTypes.size == 1 && it.parameterTypes[0].isAssignableFrom(this::class.java)
        }
        return constructor?.newInstance(this) as Target
    } catch (e: Exception) {
        throw Exception(e)
    }
}

/**
 * Converte uma lista de objetos do tipo [Source] para uma lista de objetos do tipo [Target] usando seus construtores correspondentes.
 *
 * @param Source O tipo do objeto de origem a ser convertido.
 * @param Target O tipo do objeto de destino após a conversão.
 * @return Uma lista de objetos do tipo [Target] convertidos a partir da lista de objetos de origem [Source].
 */
inline fun <reified Source : Any, reified Target : Any> List<Source>.convert(): List<Target> {
    return this.map { it.convert<Source, Target>() }
}