package com.example.educasttv

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.educast.core.model.RecursoEducativo
import com.example.educast.core.model.RepositorioEducativo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contexto = LocalContext.current

            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaRecursosTV(
                        recursos = RepositorioEducativo.listaRecursos,
                        onRecursoClick = { recurso ->
                            Toast.makeText(
                                contexto,
                                "Reproduciendo: ${recurso.titulo}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaRecursosTV(
    recursos: List<RecursoEducativo>,
    onRecursoClick: (RecursoEducativo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 58.dp, top = 48.dp, end = 58.dp, bottom = 48.dp)
    ) {
        Text(
            text = "EduCast TV",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
        Text(
            text = "Catálogo de Recursos Educativos",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(end = 32.dp)
        ) {
            items(recursos) { recurso ->
                TarjetaRecursoTV(
                    recurso = recurso,
                    onClick = { onRecursoClick(recurso) }
                )
            }
        }
    }
}

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