package br.com.alura.games.models.game

import br.com.alura.games.models.game.cheapshark.GameCheapShark
import br.com.alura.games.interfaces.AddAvalation
import br.com.alura.games.utils.ExceededValue
import br.com.alura.games.utils.input
import jakarta.persistence.Id
import jakarta.persistence.Table

//@Table(name = "game")
class Game(
    var titulo: String,
    var capa: String
) {

//    @Id
    var id: Int = 0
    var descricao: String? = null
    var preco: Double = 0.0

    operator fun Game.compareTo(game: Game):Int{
        return when{
            this.id > game.id -> 1
            this.id < game.id -> -1
            else -> 0
        }
    }

    constructor(game: GameCheapShark) :
            this(
                game.info.title,
                game.info.thumb,
            ) {
        addGame()
    }

    constructor(game: InfoGameJson) :
            this(
                game.titulo,
                game.capa
            ) {
        descricao = game.descricao
        preco = game.preco.toDouble()
    }

    init {
        id = games.size
        addGame()
    }

    private fun addGame() {
        if (!games.any { it.titulo == titulo }) {
            games.add(this)
        }
    }

    fun addDescription() {
        descricao = input("Insira a descrição personalizado para o jogo:")
    }

    companion object {
        val games: ArrayList<Game> = ArrayList()
    }



}