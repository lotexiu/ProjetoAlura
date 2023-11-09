package br.com.alura.games.models.avaliation

import java.time.LocalDate

open class Avaliation(
    val fromObject: Any,
    val toObject: Any,
    var note: Int
) {
    val date: LocalDate = LocalDate.now()
    val fromType = fromObject::class.simpleName
    val toType = toObject::class.simpleName

    init {
        if (fromObject == toObject){
            throw IllegalArgumentException("Um objeto não pode avaliar a si mesmo.")
        }
    }
}