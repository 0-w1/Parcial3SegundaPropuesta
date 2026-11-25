# Gestor de Tareas y Proyectos

Proyecto académico en Java para organizar proyectos y tareas. La aplicación conserva dos puntos de entrada: consola (`gestor.Main`) e interfaz gráfica Swing (`gestor.MainGUI`), que reutilizan las clases del paquete `gestor.modelo`.

## Requisitos

- JDK 11 o superior.
- Para ejecutar las pruebas JUnit: Maven 3.8 o superior (Maven descarga JUnit 5 durante la primera ejecución).
- No requiere base de datos. Los datos se mantienen en memoria durante la ejecución.

## Estructura

```text
.
├── src/
│   ├── main/java/gestor/
│   │   ├── Main.java
│   │   ├── MainGUI.java
│   │   ├── gui/
│   │   └── modelo/
│   │       └── comando/
│   └── test/java/gestor/
├── docs/
│   ├── DOCUMENTACION_TECNICA.md
│   └── uml/
├── scripts/
│   ├── VerificacionDia6.java
│   └── verificar.sh
├── pom.xml
└── README.md
```

## Funcionalidades presentes en el código

- Crear proyectos y asociarles tareas.
- Crear tareas básicas y urgentes mediante fábricas.
- Registrar nombre, descripción, prioridad, fecha límite y responsables.
- Organizar tareas y subtareas mediante una estructura jerárquica.
- Controlar los estados pendiente, en curso, en revisión y terminada.
- Deshacer y rehacer cambios de nombre y de estado.
- Recibir notificaciones de cambios de estado y fecha límite.
- Consultar recordatorios de tareas vencidas o próximas a vencer.
- Filtrar por estado o prioridad y ordenar por fecha o prioridad.
- Usar la interfaz gráfica Swing o la demostración por consola.

**Alcance que debe tenerse presente:** la información se conserva en memoria y se pierde al cerrar la aplicación. No hay persistencia en archivos o base de datos ni integración con calendarios, correo o SMS. La GUI ofrece un subconjunto de las operaciones disponibles en el modelo; no debe interpretarse que todos los RF del documento inicial están implementados en pantalla.

## Compilar y ejecutar

### Consola (Windows PowerShell)

Desde la carpeta raíz:

```powershell
New-Item -ItemType Directory -Force out
$files = Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
java -cp out gestor.Main
```

### Interfaz gráfica (Windows PowerShell)

Con las clases compiladas en `out`:

```powershell
java -cp out gestor.MainGUI
```

También se puede abrir el proyecto desde un IDE Java y ejecutar `gestor.MainGUI` o `gestor.Main`.

### Maven

```bash
mvn clean package
mvn test
```

Para ejecutar la interfaz gráfica con Maven, después de compilar:

```bash
java -cp target/classes gestor.MainGUI
```

## Pruebas

- Hay 17 casos JUnit 5 en `src/test/java`.
- Ejecutar la suite con `mvn test`.
- La verificación independiente sin dependencias externas se ejecuta con `sh scripts/verificar.sh` (Linux/macOS/Git Bash). En Windows también se puede ejecutar `scripts\\verificar.bat` desde CMD o PowerShell.

El informe de la jornada 6 está en `docs/CIERRE_DIA6.md`. Los diagramas UML editables en PlantUML están en `docs/uml/`.

## Patrones documentados en el código

- **Composite:** `ElementoTarea` y `Tarea`.
- **State:** `EstadoTareaEstado`, `PendienteState`, `EnCursoState`, `EnRevisionState`, `TerminadaState` y `Tarea`.
- **Command:** `Comando`, `CambiarEstadoCommand`, `CambiarNombreTareaCommand` y `GestorComandos`.
- **Factory Method:** `PlantillaTareaFactory`, `TareaBasicaFactory` y `TareaUrgenteFactory`.
- **Observer:** `ObservadorTarea`, `NotificadorConsola`, `ObservadorInterfaz` y `Tarea`.

## Diagramas UML

Los ocho archivos `.puml` de `docs/uml/` corresponden a casos de uso, clases generales, los cinco patrones y una secuencia de cambio de estado. Se pueden abrir o renderizar con PlantUML. Se mantienen como fuentes editables para facilitar ajustes si cambia el código.
