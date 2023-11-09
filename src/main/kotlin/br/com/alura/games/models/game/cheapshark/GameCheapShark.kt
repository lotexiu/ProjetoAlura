package br.com.alura.games.models.game.cheapshark

class GameCheapShark(val info: InfoGameCheapShark) {
    var id: Int? = null
    override fun toString(): String {
        return info.toString()
    }
}