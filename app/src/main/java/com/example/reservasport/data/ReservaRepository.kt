package com.example.reservasport.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReservaRepository(private val prefs: PreferenciasReserva) {

    suspend fun guardar(canchaId: Int, estado: String) {
        withContext(Dispatchers.IO) {
            prefs.guardarEstadoCancha(canchaId, estado)
        }
    }

    suspend fun obtener(canchaId: Int, defecto: String): String {
        return withContext(Dispatchers.IO) {
            prefs.obtenerEstadoCancha(canchaId, defecto)
        }
    }
}