package br.com.alura.games.main

import br.com.alura.games.utils.BetweenDates
import br.com.alura.games.models.game.Game
import br.com.alura.games.models.game.InfoGameJson
import br.com.alura.games.models.plan.enums.PlanEnum
import br.com.alura.games.models.player.InfoPlayerJson
import br.com.alura.games.models.player.Player
import br.com.alura.games.utils.convert
import br.com.alura.games.utils.httpSearch
import br.com.alura.games.utils.toStr
import java.time.LocalDate

fun main() {
    val infoPlayers: List<InfoPlayerJson> = httpSearch("https://raw.githubusercontent.com/jeniblodev/arquivosJson/main/gamers.json")
    val infoGames: List<InfoGameJson> = httpSearch("https://raw.githubusercontent.com/jeniblodev/arquivosJson/main/jogos.json")
    val playerList: List<Player> = infoPlayers.convert()
    val gameList: List<Game> = infoGames.convert()

    var player1 = playerList[0]
    player1.plan = PlanEnum.PRATA
    player1.addAvaliationNote(playerList[3],5)
    player1.addAvaliationNote(playerList[1],10)
    player1.addAvaliationNote(playerList[5],7)
    player1.addAvaliationNote(playerList[4],8)

    val period = BetweenDates(LocalDate.now(), LocalDate.now().plusDays(10))
    player1.rentGame(period, gameList[0], gameList[1], gameList[2], gameList[3])
    println(player1.gamesRented.toStr())
    println(player1.avaliationNote)

//    val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
//
//    var file = File("playerJson.json")
//    file.writeText(gson.toJson(player1))
}