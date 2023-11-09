package br.com.alura.games.models.plan.enums

import br.com.alura.games.models.aluguel.Rent
import br.com.alura.games.utils.percent
import br.com.alura.games.utils.round

enum class PlanEnum(val value: Double, val includedGames: Int, val discount: Int) {
    BRONZE(0.0, 0, 10),
    PRATA(9.90, 3, 15),
    OURO(19.90, 5, 20),
    PLATINA(29.90, 10, 30),
    DIAMANTE(29.90, 20, 50),
    ;

    fun getRentValue(rent: Rent): Double {
        val value = getOriginalRentValue(rent)
        return when{
            rent.player.avaliationNote >= 6 -> (value - value * discount.percent()).round(2)
            else -> value
        }
    }

    fun getOriginalRentValue(rent: Rent): Double{
        val totalGamesMonth = rent.player.gamesMonth(rent.period.initialDate.monthValue).size
        return if (includedGames > 0 && totalGamesMonth < includedGames){
            0.0
        } else {
            return rent.game.preco.times(rent.period.days)
        }
    }
}