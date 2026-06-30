package com.example.reservasport.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasport.data.Cancha
import com.example.reservasport.data.ReservaRepository
import com.example.reservasport.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(private val repository: ReservaRepository) : ViewModel() {

    private val TAG = "ReservaViewModel"

    private val _listaCanchas = MutableStateFlow<List<Cancha>>(emptyList())
    val listaCanchas: StateFlow<List<Cancha>> = _listaCanchas

    private val deportes = listOf("Fútbol 7", "Tenis", "Básquetbol", "Fútbol 7", "Tenis")

    init {
        cargarCanchasDesdeApi()
    }

    private fun cargarCanchasDesdeApi() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Iniciando carga de canchas desde API...")
                val respuesta = RetrofitClient.api.obtenerCanchas()

                val canchas = respuesta.take(5).mapIndexed { index, remota ->
                    val estado = repository.obtener(remota.id, "Disponible")
                    Cancha(
                        id = remota.id,
                        nombre = "Cancha ${remota.title.take(15)}",
                        deporte = deportes[index % deportes.size],
                        disponibilidad = estado
                    )
                }

                _listaCanchas.value = canchas
                Log.d(TAG, "Canchas cargadas exitosamente desde API: ${canchas.size}")

            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar canchas desde API: ${e.message}")
                // Fallback con datos locales si la API falla
                _listaCanchas.value = listOf(
                    Cancha(1, "Cancha Norte", "Fútbol 7", "Disponible"),
                    Cancha(2, "Cancha Sur", "Tenis", "Disponible"),
                    Cancha(3, "Cancha Central", "Básquetbol", "Disponible")
                )
                Log.d(TAG, "Usando datos locales como fallback")
            }
        }
    }

    fun reservarCancha(cancha: Cancha, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Reservando cancha ${cancha.id} con estado: $nuevoEstado")
                repository.guardar(cancha.id, nuevoEstado)
                cargarCanchasDesdeApi()
                Log.d(TAG, "Reserva completada exitosamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error al reservar cancha ${cancha.id}: ${e.message}")
            }
        }
    }

    // funcion para simular error deliberado - solo para evidencia de debugging
    fun simularErrorDebugging() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== INICIO SIMULACIÓN DE ERROR ===")
                // simulacion: intentar acceder a un indice invalido
                val listaVacia = emptyList<Cancha>()
                val resultado = listaVacia[0]
                Log.d(TAG, "Este log nunca se ejecuta: $resultado")
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "ERROR SIMULADO capturado: ${e.message}")
                Log.d(TAG, "App sigue funcionando correctamente después del error")
                Log.d(TAG, "=== FIN SIMULACIÓN DE ERROR ===")
            }
        }
    }
}