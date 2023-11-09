package br.com.alura.games.models.aluguel

import br.com.alura.games.utils.BetweenDates
import br.com.alura.games.models.game.Game
import br.com.alura.games.models.player.Player
import br.com.alura.games.utils.toStr

class Rent(
    val player: Player,
    val game: Game,
    val period: BetweenDates,
){
    val value:Double = player.plan.getRentValue(this)
    val originalValue = player.plan.getOriginalRentValue(this)

    init {
        player.gamesRented.add(this)
    }

    fun toStr(): String {
        return "${game.toStr("titulo","preco", showType = false)}\n" +
                "disconto: ${if(value != originalValue) player.plan.discount else 0}%\n" +
                "valor total: $originalValue \n" +
                "valor final: $value"
    }

}
