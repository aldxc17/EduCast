# EduCast: Sistema de Apoyo Visual para Aulas Inteligentes

## Visión General
EduCast es un ecosistema tecnológico diseñado para facilitar la proyección de material didáctico multimedia en aulas inteligentes. Elimina la dependencia de cables y la duplicación de pantalla tradicional, permitiendo a los docentes controlar la presentación directamente desde su teléfono móvil hacia la pantalla del aula en tiempo real.

## Arquitectura Modular
El proyecto está desarrollado exclusivamente para plataformas Android utilizando **Jetpack Compose** y consta de tres módulos principales:
*   **:mobile (Panel de Control):** Aplicación móvil que permite al docente explorar el catálogo, enviar comandos de reproducción y controlar el flujo de la clase.
*   **:tv (Visor Receptor):** Aplicación para Android TV que escucha peticiones en tiempo real y renderiza el contenido multimedia a pantalla completa.
*   **:core-model:** Módulo central compartido que almacena las entidades de datos y el catálogo, garantizando consistencia en todo el ecosistema.

## Requisitos de Ejecución
Para compilar y probar la interacción de este sistema en tu computadora, necesitas:
*   Android Studio.
*   Un emulador de teléfono Android (`emulator-5554`).
*   Un emulador de Android TV (`emulator-5556`).
*   Ambos emuladores deben estar encendidos simultáneamente antes de ejecutar los comandos y la transmisión.

## Configuración de Red Local (ADB)
Por defecto, cada emulador de Android opera en una red virtual aislada, lo que impide que se comuniquen entre sí. Para que la aplicación móvil envíe comandos al puerto local `8080` de la Smart TV, es necesario crear un puente de comunicación utilizando tu computadora como intermediaria.

Abre la terminal de **PowerShell** en Android Studio con ambos emuladores ejecutándose y lanza los siguientes comandos:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -s emulator-5554 forward --remove-all
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -s emulator-5556 forward --remove-all
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -s emulator-5554 forward tcp:8080 tcp:8080
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -s emulator-5556 forward tcp:8080 tcp:8080