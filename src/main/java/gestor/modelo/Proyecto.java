package gestor.modelo;

import java.time.LocalDate;

/** Proyecto que agrupará tareas (RF-01). */
public class Proyecto {

    private final String nombre;
    private final String descripcion;
    private final LocalDate fechaCreacion;

    public Proyecto(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.fechaCreacion = LocalDate.now();
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

    @Override
    public String toString() {
        return nombre + " (creado el " + fechaCreacion + ")";
    }
}
