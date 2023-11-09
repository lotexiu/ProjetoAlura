package br.com.alura.games.utils

import java.time.LocalDate
import java.time.Period

class BetweenDates(
    initial: LocalDate,
    final: LocalDate ,
){
    var initialDate = initial
        set(date){
            field = date
            updateBetween()
        }
    var finalDate = final
        set(date){
            field = date
            updateBetween()
        }
    var days = 0
    var month = 0
    var year = 0

    init {
        updateBetween()
    }

    private fun updateBetween(){
        val period = Period.between(initialDate, finalDate)
        days = period.days
        month = period.months
        year = period.years
    }
}
