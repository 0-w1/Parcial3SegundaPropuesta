package gestor.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Representa una actividad que pertenece a un proyecto. */
public class Tarea {

    private String nombre;
    private String descripcion;
    private Prioridad prioridad;
    private LocalDate fechaLimite;
    private EstadoTarea estado;
    private final List<Responsable> responsables;

    public Tarea(String nombre, String descripcion, Prioridad prioridad, LocalDate fechaLimite) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrioridad(prioridad);
        this.fechaLimite = fechaLimite;
        this.estado = EstadoTarea.PENDIENTE;
        this.responsables = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria");
        }
        this.prioridad = prioridad;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    /** Por ahora permite asignar el estado; las reglas se incorporarán con State. */
    public void setEstado(EstadoTarea estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    public void agregarResponsable(Responsable responsable) {
        if (responsable == null) {
            throw new IllegalArgumentException("El responsable no puede ser nulo");
        }
        if (!responsables.contains(responsable)) {
            responsables.add(responsable);
        }
    }

    public List<Responsable> getResponsables() {
        return Collections.unmodifiableList(responsables);
    }

    @Override
    public String toString() {
        return nombre + " - " + estado + " - Prioridad: " + prioridad;
    }
}
