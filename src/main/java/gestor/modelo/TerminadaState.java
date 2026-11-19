package gestor.modelo;

/** Estado concreto de una tarea: terminada. */
public class TerminadaState implements EstadoTareaEstado {
    @Override
    public EstadoTarea obtenerEstado() {
        return EstadoTarea.TERMINADA;
    }

    @Override
    public boolean permiteCambiarA(EstadoTarea nuevoEstado) {
        return false;
    }
}
