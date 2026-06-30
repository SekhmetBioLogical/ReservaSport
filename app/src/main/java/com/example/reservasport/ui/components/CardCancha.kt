package com.example.reservasport.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservasport.data.Cancha

@Composable
fun CardCancha(cancha: Cancha, onCardClick: () -> Unit) {
    val esReservada = cancha.disponibilidad == "RESERVADA por ti"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (esReservada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // tutilo de la cancha
                Text(
                    text = cancha.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                // nombre del deporte
                Text(
                    text = "Disciplina: ${cancha.deporte}",
                    fontSize = 14.sp,
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                // estado de disponibilidad
                Text(
                    text = cancha.disponibilidad,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                )
            }

            // indicador del estado
            SuggestionChip(
                onClick = { },
                label = {
                    Text(
                        text = if (esReservada) "✓ LISTO" else "LIBRE",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = if (esReservada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    labelColor = if (esReservada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}