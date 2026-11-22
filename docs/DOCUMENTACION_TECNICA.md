# Documentación técnica

## 1. Tecnologías y ejecución

- Lenguaje: Java (compilación configurada para Java 11).
- Interfaz gráfica: Swing.
- Pruebas unitarias existentes: JUnit Jupiter 5.
- Sin base de datos: los objetos viven en memoria durante la sesión.

Puntos de entrada:
- `gestor.Main`: ejemplo de ejecución por consola.
- `gestor.MainGUI`: inicia Swing mediante `SwingUtilities.invokeLater`.

## 2. Paquetes y responsabilidades

| Paquete | Responsabilidad |
|---|---|
| `gestor` | Puntos de entrada de consola y GUI. |
| `gestor.gui` | Ventana Swing y observador que actualiza el registro visual. |
| `gestor.modelo` | Entidades, estados, fábricas, consultas, recordatorios y notificaciones. |
| `gestor.modelo.comando` | Contrato de comando, comandos concretos e historial undo/redo. |

## 3. Modelo principal

- `Proyecto` agrupa una lista de `Tarea`.
- `Tarea` conserva sus datos, responsables, subtareas, estado y observadores.
- `Responsable` representa a una persona asignada.
- `Prioridad` y `EstadoTarea` son enumeraciones.
- `BuscadorTareas` filtra por estado/prioridad y ordena por fecha/prioridad.
- `ServicioRecordatorios` devuelve avisos para tareas vencidas o dentro de los días de anticipación indicados.

## 4. Patrones y clases reales

### Composite
`ElementoTarea` define la interfaz común; `Tarea` la implementa y mantiene `List<ElementoTarea>`. `contarTareas()` recorre recursivamente la jerarquía. Se valida que no se agreguen ciclos.

### State
`Tarea` delega las reglas a `EstadoTareaEstado`. Los estados concretos son `PendienteState`, `EnCursoState`, `EnRevisionState` y `TerminadaState`. Transiciones codificadas:
- Pendiente → En curso.
- En curso → Pendiente o En revisión.
- En revisión → En curso o Terminada.
- Terminada → ninguna.

`restaurarEstado()` permite que el historial recupere un estado anterior sin aplicar la transición normal; es un mecanismo interno de recuperación para Command.

### Command
`Comando` define `ejecutar()` y `deshacer()`. `CambiarEstadoCommand` y `CambiarNombreTareaCommand` encapsulan las acciones. `GestorComandos` conserva dos pilas para deshacer y rehacer.

### Factory Method
`PlantillaTareaFactory` declara `crearTarea()`. `TareaBasicaFactory` construye tareas con prioridad media y `TareaUrgenteFactory` con prioridad alta.

### Observer
`ObservadorTarea` declara `actualizar()`. `Tarea` mantiene observadores y les notifica al cambiar estado o fecha límite. `NotificadorConsola` y `gestor.gui.ObservadorInterfaz` son receptores concretos.

## 5. Compilación y pruebas

Compilar todas las clases productivas:

```bash
mkdir -p out
find src/main/java -name '*.java' > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

Ejecutar:
```bash
java -cp out gestor.Main
java -cp out gestor.MainGUI
```

Ejecutar las verificaciones funcionales independientes:
```bash
sh scripts/verificar.sh
```

Ejecutar los 17 casos JUnit 5:
```bash
mvn test
```

La configuración `pom.xml` agrega JUnit Jupiter como dependencia de prueba. La suite JUnit requiere Maven y acceso a repositorios Maven para descargar dependencias si no están en caché.

## 6. Limitaciones conocidas

- No existe almacenamiento persistente; cerrar el programa elimina los datos de la sesión.
- `Main` es una demostración de consola, no un menú interactivo completo.
- La interfaz Swing no expone necesariamente todas las operaciones que existen en el modelo.
- Los recordatorios se consultan bajo demanda; no hay un servicio en segundo plano ni envío externo.
- Las plantillas implementadas son fábricas de tareas básica y urgente; no se observa un catálogo persistente de plantillas de proyectos.
- No se verificó la ejecución visual de Swing en un entorno gráfico durante esta revisión.

## 7. Fuentes UML

Los diagramas de `docs/uml/` están escritos en PlantUML y se corresponden con las clases encontradas en `src/main/java`. Si se modifica una clase, atributo o relación, se debe actualizar el diagrama correspondiente.

En Windows (CMD o PowerShell), se puede ejecutar el script equivalente `scripts/verificar.bat`.
