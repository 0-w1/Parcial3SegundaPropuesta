package gestor.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Proyecto que agrupará tareas (RF-01). */
public class Proyecto {

    private final String nombre;
    private final String descripcion;
    private final LocalDate fechaCreacion;
    private final List<Tarea> tareas;

    public Proyecto(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.fechaCreacion = LocalDate.now();
        this.tareas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void agregarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        tareas.add(tarea);
    }

    public List<Tarea> getTareas() {
        return Collections.unmodifiableList(tareas);
    }

    @Override
    public String toString() {
        return nombre + " (creado el " + fechaCreacion + ")";
    }
}
