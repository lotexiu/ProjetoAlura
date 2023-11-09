package br.com.alura.games.utils

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.full.memberProperties

/**
 * Realiza uma busca HTTP em uma URL especificada com parâmetros opcionais e converte a resposta para o tipo desejado.
 *
 * @param url O endereço da requisição HTTP sem os query parameters (?).
 * @param params Os parâmetros da requisição, podendo ser uma String ou um objeto com campos.
 * @return Um objeto do tipo [Target] representando a resposta da requisição HTTP.
 * @throws Exception Se a resposta da URL não puder ser convertida para o tipo [Target].
 * @see buildAddress
 */
inline fun <reified Target : Any> httpSearch(url: String, params: Any? = null): Target {

    val client = HttpClient.newHttpClient()
    val url = buildAddress(url, params)
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build()

    try {
        val response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        )
        val json = response.body()
        return json.toObject()
    } catch (ex: Exception) {
        throw Exception("Resultado da url é inválido: $url")
    }
}

/**
 * Constrói um endereço URL incluindo os parâmetros de query.
 *
 * @param url O endereço base sem os query parameters (?).
 * @param params Os parâmetros da requisição, podendo ser uma String ou um objeto com campos.
 * @return Uma String representando o endereço URL completo com os parâmetros de query.
 */
fun buildAddress(url: String, params: Any? = null): String {
    val address = StringBuilder(url)

    if (!url.endsWith("?")) {
        address.append("?")
    }
    when (params) {
        is String -> address.append(params)
        else -> {
            val objParams = params?.convertFieldsToUrlParams() ?: ""
            address.append(objParams)
        }
    }
    return address.toString()
}

/**
 * Converte os campos de um objeto em uma representação de parâmetros de URL.
 *
 * @return Uma String contendo os parâmetros de URL no formato "campo1=valor1&campo2=valor2...".
 */
inline fun <reified Source : Any> Source.convertFieldsToUrlParams(): String {
    val properties = Source::class.memberProperties
    val keyValuePairs = mutableListOf<String>()
    for (prop in properties) {
        val propName = prop.name
        val propValue = prop.get(this)
        if (propValue != null) {
            keyValuePairs.add("$propName=${propValue}")
        }
    }
    return keyValuePairs.joinToString("&")
}