package br.com.alura.games.interfaces

import br.com.alura.games.models.player.Player

interface AddAvalation {
    val avaliationNote: Double

    fun addAvaliationNote(to:Any, note:Int)
}