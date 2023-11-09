package br.com.alura.games.models.player

import br.com.alura.games.interfaces.AddAvalation
import br.com.alura.games.utils.BetweenDates
import br.com.alura.games.models.aluguel.Rent
import br.com.alura.games.models.avaliation.Avaliation
import br.com.alura.games.models.game.Game
import br.com.alura.games.models.plan.enums.PlanEnum
import br.com.alura.games.utils.*
import java.time.LocalDateTime
import kotlin.random.Random

class Player(
    var name: String,
    var user: String,
    var email: String,
): AddAvalation {

    var birthDate: LocalDateTime? = null
    var id: String = ""
        private set

    var plan: PlanEnum = PlanEnum.BRONZE

    private var rating: ArrayList<Avaliation> = ArrayList()
    override val avaliationNote: Double
        get() = rating.map { it.note }.average()

    override fun addAvaliationNote(to: Any, note: Int) {
        rating.add(Avaliation(this, to, note))
    }

    val gamesRented = ArrayList<Rent>()
    var recommendedGames: ArrayList<Game> = ArrayList()

    init {
        if (name.isBlank()) throw InvalidValueField("Nome está em branco.")
        if (user.isBlank()) throw InvalidValueField("Nome de Usuário está em branco.")
        if (!email.validateAsEmail()) throw InvalidValueField("Email inválido.")
        id = makeId()
    }

    constructor(
        name: String,
        user: String,
        email: String,
        birthDate: LocalDateTime
    ) :
            this(
                name,
                user,
                email
            ) {
        this.birthDate = birthDate
    }

    constructor(it: InfoPlayerJson) :
            this(
                name = it.nome,
                user = it.usuario,
                email = it.email,
            ) {
        this.birthDate = it.dataNascimento.toDate()
    }

    private fun makeId(): String {
        if(id.isBlank()){
            val tag = numTag(9999)
            val idResult = "$user#$tag"
            ids.add(idResult)
            return idResult
        }
        return ""
    }

    fun rentGame(periodo: BetweenDates, vararg games: Game): List<Rent> {
        return games.map { it -> Rent(this, it, periodo) }
    }

    fun gamesMonth(mes:Int): List<Game> {
        return gamesRented
            .filter { rent ->  rent.period.initialDate.monthValue == mes}
            .map { aluguel ->  aluguel.game}
    }

    companion object {
        fun build(autoBuild: Boolean = true): Player {
            if (autoBuild) {
                val tag: String = numTag(9999)
                val name = "Player - $tag"
                val email = "something_about$tag@Gmail.com"
                val data = "10/10/${Random.nextInt(1000, 9999)}"
                val user = "Scarlet"
                return Player(name,user, email, data.toDate())
            } else {
                println("Olá, Bem vindo ao AluGames!\n")
                println("Digite seu nome, usuário e email para o cadastro.")
                val name: String = input("Nome:")
                val user: String = input("Usuário:")
                val email: String = input("Email:")
                val answer: Boolean = inputConfirm("\nDeseja completar o casdastro com data de nascimento? (S/N):")
                if (answer) {
                    val data: String = input("Data (DD/MM/AAAA):")
                    return Player(name, email, user, data.toDate())
                }
                return Player(name, user, email)
            }
        }

        var ids: ArrayList<String> = ArrayList()
    }
}
