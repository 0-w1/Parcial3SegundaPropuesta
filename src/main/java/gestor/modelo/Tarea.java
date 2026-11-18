package gestor.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Representa una actividad que pertenece a un proyecto. */
public class Tarea implements ElementoTarea {

    private String nombre;
    private String descripcion;
    private Prioridad prioridad;
    private LocalDate fechaLimite;
    private EstadoTarea estado;
    private final List<Responsable> responsables;
    private final List<ElementoTarea> subtareas;

    public Tarea(String nombre, String descripcion, Prioridad prioridad, LocalDate fechaLimite) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrioridad(prioridad);
        this.fechaLimite = fechaLimite;
        this.estado = EstadoTarea.PENDIENTE;
        this.responsables = new ArrayList<>();
        this.subtareas = new ArrayList<>();
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


    /** Agrega una subtarea. Una tarea puede contener otras tareas. */
    public void agregarSubtarea(ElementoTarea subtarea) {
        if (subtarea == null) {
            throw new IllegalArgumentException("La subtarea no puede ser nula");
        }
        if (subtarea == this || contieneElemento(subtarea, this)) {
            throw new IllegalArgumentException("No se puede crear una relación circular de tareas");
        }
        if (!subtareas.contains(subtarea)) {
            subtareas.add(subtarea);
        }
    }

    /** Elimina una subtarea directa. */
    public boolean eliminarSubtarea(ElementoTarea subtarea) {
        return subtareas.remove(subtarea);
    }

    /** Devuelve las subtareas directas sin permitir modificaciones externas. */
    public List<ElementoTarea> getSubtareas() {
        return Collections.unmodifiableList(subtareas);
    }

    @Override
    public List<ElementoTarea> obtenerElementos() {
        return getSubtareas();
    }

    /** Cuenta esta tarea y todas sus subtareas, incluyendo niveles internos. */
    @Override
    public int contarTareas() {
        int total = 1;
        for (ElementoTarea elemento : subtareas) {
            total += elemento.contarTareas();
        }
        return total;
    }

    private boolean contieneElemento(ElementoTarea buscado, ElementoTarea actual) {
        if (buscado == actual) {
            return true;
        }
        for (ElementoTarea hijo : actual.obtenerElementos()) {
            if (contieneElemento(buscado, hijo)) {
                return true;
            }
        }
        return false;
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
