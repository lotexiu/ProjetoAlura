//package br.com.alura.games.models.plan.deprecated.signature
//
//import br.com.alura.games.models.aluguel.Rent
//import br.com.alura.games.models.plan.deprecated.Plan
//
//class signaturePlan(
//    tipo: String,
//    val mensalidade: Double,
//    val includedGames: Int
//): Plan(tipo) {
//
//    override fun getRentValue(rent: Rent): Double {
//        val totalGamesMonth = rent.player.gamesMonth(rent.period.initialDate.monthValue).size
//        return if (totalGamesMonth <= includedGames) super.getRentValue(rent) else 0.0
//    }
//}