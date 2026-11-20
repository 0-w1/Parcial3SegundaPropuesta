package gestor.modelo;

import java.time.LocalDate;

/** Factory Method: define el método para crear tareas a partir de plantillas. */
public abstract class PlantillaTareaFactory {
    public abstract Tarea crearTarea(String nombre, LocalDate fechaLimite);
}
