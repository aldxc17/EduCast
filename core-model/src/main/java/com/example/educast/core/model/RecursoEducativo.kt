package com.example.educast.core.model

data class RecursoEducativo(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val duracion: String,
    val urlVideo: String
)

object RepositorioEducativo {
    val listaRecursos: List<RecursoEducativo> = listOf(
        RecursoEducativo(
            id = 1,
            titulo = "Fundamentos de Jetpack Compose",
            descripcion = "Manejo de estado declarativo, layouts y recomposición reactiva.",
            categoria = "Móvil",
            duracion = "15 min",
            urlVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        RecursoEducativo(
            id = 2,
            titulo = "Arquitectura de Software y Clean Code",
            descripcion = "Estructuración multi-módulo y separación de responsabilidades.",
            categoria = "Arquitectura",
            duracion = "20 min",
            urlVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ),
        RecursoEducativo(
            id = 3,
            titulo = "Interfaces y Navegación en Android TV",
            descripcion = "Manejo de foco D-Pad, diseño 10-foot UI y Compose for TV.",
            categoria = "Smart TV",
            duracion = "12 min",
            urlVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        )
    )
}