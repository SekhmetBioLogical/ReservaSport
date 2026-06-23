package com.example.reservasport.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferenciasReserva(context: Context) {
    private val TAG = "PreferenciasReserva"
    private val prefs: SharedPreferences = context.getSharedPreferences("ReservaSportPrefs", Context.MODE_PRIVATE)

    fun guardarEstadoCancha(canchaId: Int, estado: String) {
        try {
            prefs.edit().putString("cancha_$canchaId", estado).apply()
            Log.d(TAG, "Estado guardado exitosamente para cancha: $canchaId")
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar estado de cancha $canchaId: ${e.message}")
        }
    }

    fun obtenerEstadoCancha(canchaId: Int, valorPorDefecto: String): String {
        return try {
            prefs.getString("cancha_$canchaId", valorPorDefecto) ?: valorPorDefecto
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer estado de cancha $canchaId, retornando defecto: ${e.message}")
            valorPorDefecto
        }
    }
}