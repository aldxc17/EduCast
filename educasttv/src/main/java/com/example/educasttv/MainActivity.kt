package com.example.educasttv

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.educast.core.model.RecursoEducativo
import com.example.educast.core.model.RepositorioEducativo
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TvServer.iniciar(8080)

        setContent {
            val contexto = LocalContext.current
            val recursoCasteado by TvServer.recursoCasteado.collectAsState()

            // Estado local para controlar qué pantalla mostrar
            var recursoActual by remember { mutableStateOf<String?>(null) }

            // Escuchamos el estado del servidor
            LaunchedEffect(recursoCasteado) {
                recursoCasteado?.let {
                    recursoActual = it // Cambia la vista a la pantalla de transmisión
                    Toast.makeText(contexto, "Recibido de Móvil: $it", Toast.LENGTH_LONG).show()
                }
            }

            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Condicional principal para alternar entre el catálogo y el video
                    if (recursoActual != null) {
                        // Se muestra la simulación a pantalla completa
                        VistaTransmisionSimulada(
                            recurso = recursoActual!!,
                            onFinalizar = {
                                recursoActual = null // Al limpiar el estado, regresamos al catálogo
                                // Opcional: TvServer.limpiarEstado() si tienes esa función implementada
                            }
                        )
                    } else {
                        // Pantalla original del catálogo
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 58.dp, top = 40.dp, end = 58.dp, bottom = 40.dp)
                        ) {
                            Text(
                                text = "MecxiHub",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Catálogo de Recursos Educativos",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.LightGray
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                contentPadding = PaddingValues(end = 32.dp)
                            ) {
                                items(RepositorioEducativo.listaRecursos) { recurso ->
                                    TarjetaRecursoTV(
                                        recurso = recurso,
                                        onClick = {
                                            Toast.makeText(
                                                contexto,
                                                "Reproduciendo localmente: ${recurso.titulo}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TvServer.detener()
    }
}

// Nueva vista a pantalla completa que simula la transmisión con controles
@Composable
fun VistaTransmisionSimulada(recurso: String, onFinalizar: () -> Unit) {
    // Estado para controlar si el video está en reproducción o pausado
    var reproduciendo by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Transmisión en curso",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recurso,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF64B5F6)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Fila de controles de reproducción
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de Play/Pause
                BotonControlAnimado(
                    icono = if (reproduciendo) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    texto = if (reproduciendo) "Pausar" else "Reproducir",
                    colorBase = Color(0xFF1976D2), // Azul
                    onClick = { reproduciendo = !reproduciendo }
                )

                Spacer(modifier = Modifier.width(32.dp))

                // Botón de Finalizar Transmisión
                BotonControlAnimado(
                    icono = Icons.Filled.Stop,
                    texto = "Finalizar",
                    colorBase = Color(0xFFD32F2F), // Rojo
                    onClick = onFinalizar
                )
            }
        }
    }
}

// Componente personalizado que maneja el foco, el aumento de tamaño y la saturación
@Composable
fun BotonControlAnimado(
    icono: ImageVector,
    texto: String,
    colorBase: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    // Detecta si el control remoto tiene seleccionado este botón
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Animación de escala: crece a 1.2x si está enfocado, regresa a 1.0x si no
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.2f else 1.0f,
        label = "animacion_escala"
    )

    // Animación de color: usa el color puro si está enfocado, y reduce la opacidad (desaturado) si no
    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused) colorBase else colorBase.copy(alpha = 0.4f),
        label = "animacion_color"
    )

    Button(
        onClick = onClick,
        interactionSource = interactionSource, // Vincula el estado de foco al botón
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .padding(16.dp)
            .scale(scale) // Aplica la animación de tamaño
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            color = Color.White
        )
    }
}

// El componente original de la tarjeta se mantiene igual
@Composable
fun TarjetaRecursoTV(
    recurso: RecursoEducativo,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) Color(0xFF2E3856) else Color(0xFF1E1E2C)
        ),
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .then(
                if (isFocused) {
                    Modifier.border(3.dp, Color.White, RoundedCornerShape(12.dp))
                } else {
                    Modifier.border(1.dp, Color.DarkGray, RoundedCornerShape(12.dp))
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = recurso.categoria,
                style = MaterialTheme.typography.labelMedium,
                color = if (isFocused) Color(0xFF64B5F6) else Color.Gray
            )
            Text(
                text = recurso.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = recurso.duracion,
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )
        }
    }
}