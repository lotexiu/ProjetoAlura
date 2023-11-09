package br.com.alura.games.api

import br.com.alura.games.models.game.cheapshark.GameCheapShark
import com.google.gson.Gson
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class CheapSharkAPI {
    private val endereco = "https://www.cheapshark.com/api/1.0/games?id="

    fun buscaJogo(id: String): GameCheapShark {
        val client: HttpClient = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(endereco + id))
            .build()

        val response = client
            .send(request, HttpResponse.BodyHandlers.ofString())

        if(response.statusCode() == 200){
            val json = response.body()
            val meuGameCheapShark = Gson().fromJson(json, GameCheapShark::class.java)
            meuGameCheapShark.id = id.toInt()
            return meuGameCheapShark
        }else{
            throw Exception("Código $id invalido.")
        }
    }

}