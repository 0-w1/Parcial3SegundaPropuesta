package gestor.modelo;

/** Estado concreto de una tarea: en revision. */
public class EnRevisionState implements EstadoTareaEstado {
    @Override
    public EstadoTarea obtenerEstado() {
        return EstadoTarea.EN_REVISION;
    }

    @Override
    public boolean permiteCambiarA(EstadoTarea nuevoEstado) {
        return nuevoEstado == EstadoTarea.EN_CURSO || nuevoEstado == EstadoTarea.TERMINADA;
    }
}
