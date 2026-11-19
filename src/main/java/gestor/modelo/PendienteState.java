package gestor.modelo;

/** Estado concreto de una tarea: pendiente. */
public class PendienteState implements EstadoTareaEstado {
    @Override
    public EstadoTarea obtenerEstado() {
        return EstadoTarea.PENDIENTE;
    }

    @Override
    public boolean permiteCambiarA(EstadoTarea nuevoEstado) {
        return nuevoEstado == EstadoTarea.EN_CURSO;
    }
}
