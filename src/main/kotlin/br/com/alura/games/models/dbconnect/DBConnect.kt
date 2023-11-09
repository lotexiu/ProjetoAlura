package br.com.alura.games.models.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager
import java.sql.SQLException

class DBConnect {
    private val user = "root"
    private val password = "123456789"
    private val url = "jdbc:mysql://localhost:3306/alugames"

    fun connect(): Connection? {
        return try {
            DriverManager.getConnection(url, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }}
