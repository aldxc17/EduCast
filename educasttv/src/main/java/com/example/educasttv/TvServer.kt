package com.example.educasttv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket

object TvServer {
    private var serverSocket: ServerSocket? = null
    private var serverJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _recursoCasteado = MutableStateFlow<String?>(null)
    val recursoCasteado: StateFlow<String?> = _recursoCasteado.asStateFlow()

    fun iniciar(puerto: Int = 8080) {
        if (serverJob != null) return

        serverJob = scope.launch {
            try {
                serverSocket = ServerSocket(puerto)
                while (isActive) {
                    val clientSocket = serverSocket?.accept() ?: break
                    manejarPeticion(clientSocket)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun manejarPeticion(socket: Socket) {
        scope.launch {
            try {
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val writer = OutputStreamWriter(socket.getOutputStream())

                var contentLength = 0
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    if (line!!.isEmpty()) break
                    if (line!!.startsWith("Content-Length:", ignoreCase = true)) {
                        contentLength = line!!.substringAfter(":").trim().toIntOrNull() ?: 0
                    }
                }

                val body = if (contentLength > 0) {
                    val charArray = CharArray(contentLength)
                    reader.read(charArray, 0, contentLength)
                    String(charArray)
                } else {
                    ""
                }

                if (body.isNotEmpty()) {
                    _recursoCasteado.value = body
                }

                val responseBody = "OK"
                val response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: ${responseBody.toByteArray().size}\r\n" +
                        "Connection: close\r\n\r\n" +
                        responseBody

                writer.write(response)
                writer.flush()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun detener() {
        try {
            serverSocket?.close()
            serverSocket = null
            serverJob?.cancel()
            serverJob = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}