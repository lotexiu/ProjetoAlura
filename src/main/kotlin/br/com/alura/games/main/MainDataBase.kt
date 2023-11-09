package br.com.alura.games.main

import br.com.alura.games.models.avaliation.Avaliation
import br.com.alura.games.models.player.Player
import br.com.alura.games.utils.toStr
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

fun main() {

    val player = Player( "Aleph","Lotexiu", "test@gmail.com")
    val player2 = Player.build()
    val test = Avaliation(player,player2,6)

    println(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).time)

}