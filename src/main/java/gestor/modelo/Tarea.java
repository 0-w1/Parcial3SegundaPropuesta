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
    private EstadoTareaEstado estado;
    private final List<Responsable> responsables;
    private final List<ElementoTarea> subtareas;
    private final List<ObservadorTarea> observadores;

    public Tarea(String nombre, String descripcion, Prioridad prioridad, LocalDate fechaLimite) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrioridad(prioridad);
        this.fechaLimite = fechaLimite;
        this.estado = new PendienteState();
        this.responsables = new ArrayList<>();
        this.subtareas = new ArrayList<>();
        this.observadores = new ArrayList<>();
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
        notificarObservadores("La fecha límite fue actualizada");
    }

    public EstadoTarea getEstado() {
        return estado.obtenerEstado();
    }

    /** Cambia el estado respetando las reglas del patrón State. */
    public void setEstado(EstadoTarea nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (!estado.permiteCambiarA(nuevoEstado)) {
            throw new IllegalStateException("No se permite pasar de " + getEstado() + " a " + nuevoEstado);
        }
        EstadoTarea anterior = getEstado();
        this.estado = crearEstado(nuevoEstado);
        notificarObservadores("El estado cambió de " + anterior + " a " + nuevoEstado);
    }

    /** Restaura un estado previo al deshacer una operación del historial. */
    public void restaurarEstado(EstadoTarea estadoAnterior) {
        if (estadoAnterior == null) {
            throw new IllegalArgumentException("El estado a restaurar es obligatorio");
        }
        this.estado = crearEstado(estadoAnterior);
    }

    private EstadoTareaEstado crearEstado(EstadoTarea estado) {
        switch (estado) {
            case PENDIENTE: return new PendienteState();
            case EN_CURSO: return new EnCursoState();
            case EN_REVISION: return new EnRevisionState();
            case TERMINADA: return new TerminadaState();
            default: throw new IllegalArgumentException("Estado no reconocido");
        }
    }


    /** Agrega una subtarea. Una tarea puede contener otras tareas. */
    public void agregarSubtarea(ElementoTarea subtarea) {
        if (subtarea == null) {
            throw new IllegalArgumentException("La subtarea no puede ser nula");
        }
        if (subtarea == this || contieneElemento(this, subtarea)) {
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

    public boolean eliminarResponsable(Responsable responsable) {
        return responsables.remove(responsable);
    }

    public void agregarObservador(ObservadorTarea observador) {
        if (observador == null) throw new IllegalArgumentException("El observador no puede ser nulo");
        if (!observadores.contains(observador)) observadores.add(observador);
    }

    public void eliminarObservador(ObservadorTarea observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(String mensaje) {
        for (ObservadorTarea observador : new ArrayList<>(observadores)) {
            observador.actualizar(this, mensaje);
        }
    }

    public List<Responsable> getResponsables() {
        return Collections.unmodifiableList(responsables);
    }

    @Override
    public String toString() {
        return nombre + " - " + getEstado() + " - Prioridad: " + prioridad;
    }
}
