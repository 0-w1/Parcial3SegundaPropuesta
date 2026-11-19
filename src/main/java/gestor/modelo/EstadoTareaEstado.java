package gestor.modelo;

/** Comportamiento asociado al estado actual de una tarea (patrón State). */
public interface EstadoTareaEstado {
    EstadoTarea obtenerEstado();
    boolean permiteCambiarA(EstadoTarea nuevoEstado);
}
