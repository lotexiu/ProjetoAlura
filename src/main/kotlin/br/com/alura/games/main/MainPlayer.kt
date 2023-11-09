package br.com.alura.games.main

import br.com.alura.games.models.player.Player
import br.com.alura.games.utils.toDate
import br.com.alura.games.utils.toStr

fun main() {
    val player = Player(
        "Player",
        "scarlet",
        "player@gmail.com",
        "11/11/1111".toDate()
    )

    val player2 = Player(
        "Player",
        "User1",
        "player2@gmail.com",
    )

    player2.let {
        it.user = "scarlet2"
        it.birthDate = "11/11/1112".toDate()
    }

    println("${player}\n")
    println("${player2.toStr()}\n")
    println(Player.ids)
}