*ReservaSport 🏟️*
---------------
Gestión de Reservas de Canchas Deportivas
Aplicación móvil nativa desarrollada en Android con Jetpack Compose, Material Design 3 y arquitectura MVVM. Permite visualizar, filtrar y gestionar reservas de canchas deportivas, integrando conectividad REST, procesos asincrónicos y diagnóstico de memoria.

🛠️ Stack Tecnológico
---------------------

Lenguaje: Kotlin
UI: Jetpack Compose + Material Design 3
Arquitectura: MVVM (Model-View-ViewModel)
Asincronía: Kotlin Coroutines + StateFlow
Persistencia local: SharedPreferences
Conectividad: Retrofit 2 + Gson
Diagnóstico de memoria: LeakCanary 2.14
API de prueba: JSONPlaceholder


📁 Estructura del Proyecto
com.example.reservasport/
├── data/
│   ├── Cancha.kt
│   ├── CanchaRemota.kt
│   ├── CanchaApiService.kt
│   ├── RetrofitClient.kt
│   ├── PreferenciasReserva.kt
│   └── ReservaRepository.kt
├── ui/
│   ├── components/
│   │   └── CardCancha.kt
│   └── screens/
│       └── ReservaSportScreen.kt
├── viewmodel/
│   ├── ReservaViewModel.kt
│   └── AppViewModelFactory.kt
└── MainActivity.kt

⚙️ Mejoras Implementadas — Semana 6
------------------------------------
1. Integración Retrofit (Librería Externa)
Se integró Retrofit 2 con Gson para consumir la API REST pública JSONPlaceholder, simulando la carga de canchas desde un servidor real. La solicitud se realiza en segundo plano mediante Coroutines sin bloquear el hilo principal.
kotlinimplementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
Justificación técnica: Retrofit es la librería estándar de la industria para consumo de APIs REST en Android. Permite definir endpoints como interfaces Kotlin, gestiona automáticamente la serialización/deserialización JSON y se integra nativamente con Coroutines mediante funciones suspend.
2. Procesos Asincrónicos (Kotlin Coroutines)
Todas las operaciones de red y persistencia se ejecutan fuera del hilo principal usando viewModelScope.launch y withContext(Dispatchers.IO), garantizando una UI fluida sin ANR.
kotlinprivate fun cargarCanchasDesdeApi() {
    viewModelScope.launch {
        val respuesta = RetrofitClient.api.obtenerCanchas()
        _listaCanchas.value = respuesta.take(5).map { ... }
    }
}
3. Debugging y Manejo de Errores
Se implementaron bloques try-catch en todas las operaciones críticas con registro en Logcat mediante Log.d y Log.e. Se incluyó un fallback a datos locales si la API no responde, y una función simularErrorDebugging() que provoca y captura un IndexOutOfBoundsException de forma controlada para evidenciar el manejo de errores.
D/ReservaViewModel: Iniciando carga de canchas desde API...
D/ReservaViewModel: Canchas cargadas exitosamente desde API: 5
4. Diagnóstico y Corrección de Memory Leak (LeakCanary)
Leak intencional creado:
kotlin// ANTES — referencia estática que retiene la Activity
companion object {
    var fugaDeMemoria: Any? = null
}
override fun onCreate(...) {
    fugaDeMemoria = this // leak: Activity nunca es liberada por el GC
}
LeakCanary detectó el leak al destruirse la Activity durante rotación de pantalla:
D/LeakCanary: Watching instance of com.example.reservasport.MainActivity
(MainActivity received Activity#onDestroy() callback)
Corrección aplicada:
kotlin// DESPUÉS — sin companion object, sin referencia estática
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ... }
    }
}
Al eliminar la referencia estática, el Garbage Collector puede liberar la Activity correctamente al destruirse, eliminando la fuga de memoria.

📸 Capturas de Evidencia
EvidenciaDescripciónApp con canchas desde APIPantalla mostrando canchas cargadas desde JSONPlaceholderLogcat RetrofitConfirmación de carga exitosa desde API RESTLogcat LeakCanaryDetección del leak en MainActivityApp "Leaks" instaladaLeakCanary corriendo en el emulador

📦 APK
El instalador compilado está disponible en:
app/build/outputs/apk/debug/app-debug.apk
