package com.example.reservasport.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasport.data.*
import com.example.reservasport.ui.components.CardCancha
import com.example.reservasport.viewmodel.AppViewModelFactory
import com.example.reservasport.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaSportScreen() {
    val context = LocalContext.current
    // inicializacion manual necesaria para el MVVM
    val repository = remember { ReservaRepository(PreferenciasReserva(context)) }
    val viewModel: ReservaViewModel = viewModel(factory = AppViewModelFactory(repository))

    val listaCanchas by viewModel.listaCanchas.collectAsState()
    var canchaSeleccionada by remember { mutableStateOf<Cancha?>(null) }
    var deporteSeleccionado by remember { mutableStateOf("Todos") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("ReservaSport") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Filtros
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Fútbol 7", "Tenis", "Básquetbol").forEach { deporte ->
                    FilterChip(selected = deporteSeleccionado == deporte, onClick = { deporteSeleccionado = deporte }, label = { Text(deporte) })
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val listaFiltrada = if (deporteSeleccionado == "Todos") listaCanchas
                else listaCanchas.filter { it.deporte == deporteSeleccionado }

                items(listaFiltrada, key = { it.id }) { cancha ->
                    CardCancha(cancha = cancha, onCardClick = { canchaSeleccionada = cancha })
                }
            }
        }

        canchaSeleccionada?.let { cancha ->
            val yaEstaReservada = cancha.disponibilidad.contains("RESERVADA")
            AlertDialog(
                onDismissRequest = { canchaSeleccionada = null },
                confirmButton = {
                    Button(onClick = {
                        val nuevoEstado = if (yaEstaReservada) "Disponible" else "RESERVADA por ti"
                        viewModel.reservarCancha(cancha, nuevoEstado)
                        canchaSeleccionada = null
                    }) { Text(if (yaEstaReservada) "Liberar" else "Confirmar") }
                },
                title = { Text(if (yaEstaReservada) "Gestionar Reserva" else "Confirmar") },
                text = { Text("¿Estás seguro de realizar esta acción?") }
            )
        }
    }
}