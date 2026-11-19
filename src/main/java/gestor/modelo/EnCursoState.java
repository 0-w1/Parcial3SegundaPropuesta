package gestor.modelo;

/** Estado concreto de una tarea: en curso. */
public class EnCursoState implements EstadoTareaEstado {
    @Override
    public EstadoTarea obtenerEstado() {
        return EstadoTarea.EN_CURSO;
    }

    @Override
    public boolean permiteCambiarA(EstadoTarea nuevoEstado) {
        return nuevoEstado == EstadoTarea.PENDIENTE || nuevoEstado == EstadoTarea.EN_REVISION;
    }
}
