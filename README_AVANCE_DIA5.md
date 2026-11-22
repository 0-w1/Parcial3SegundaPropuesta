# Avance del día 5 - Interfaz gráfica

## ¿Qué se agregó?

Se incorporó una interfaz gráfica desarrollada con Java Swing, conectada con las clases del modelo que ya estaban implementadas.

La ventana permite:

- Crear tareas indicando nombre, descripción, prioridad, fecha límite y responsable.
- Consultar las tareas en una tabla con sus datos principales.
- Filtrar las tareas por estado.
- Cambiar el estado de una tarea respetando las transiciones permitidas.
- Deshacer y rehacer cambios de estado.
- Consultar recordatorios de tareas vencidas o próximas a vencer.
- Ver un registro de actividad que recibe las notificaciones de las tareas.

## Cómo ejecutar

Se mantienen dos puntos de entrada independientes:

- `gestor.Main`: ejecuta la demostración por consola que ya existía.
- `gestor.MainGUI`: abre la interfaz gráfica.

En Visual Studio Code, abre el proyecto y ejecuta el archivo `src/main/java/gestor/MainGUI.java` para iniciar la versión gráfica. Para la versión de consola, ejecuta `src/main/java/gestor/Main.java`.

Si se compila desde una terminal con JDK instalado, desde la carpeta raíz del proyecto se puede usar:

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out gestor.MainGUI
```

En Windows PowerShell, también se puede compilar desde VS Code ejecutando el archivo `MainGUI.java` con la extensión de Java.

## Nota

En este avance, los datos se mantienen en memoria mientras la aplicación está abierta. Todavía no se implementa almacenamiento permanente en archivos o base de datos.
