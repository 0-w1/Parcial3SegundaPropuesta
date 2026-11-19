package gestor.modelo.comando;

import gestor.modelo.EstadoTarea;
import gestor.modelo.Tarea;

/** Comando para cambiar y restaurar el estado de una tarea. */
public class CambiarEstadoCommand implements Comando {
    private final Tarea tarea;
    private final EstadoTarea nuevoEstado;
    private EstadoTarea estadoAnterior;
    private boolean ejecutado;

    public CambiarEstadoCommand(Tarea tarea, EstadoTarea nuevoEstado) {
        if (tarea == null || nuevoEstado == null) {
            throw new IllegalArgumentException("La tarea y el nuevo estado son obligatorios");
        }
        this.tarea = tarea;
        this.nuevoEstado = nuevoEstado;
    }

    @Override
    public void ejecutar() {
        if (ejecutado) return;
        estadoAnterior = tarea.getEstado();
        tarea.setEstado(nuevoEstado);
        ejecutado = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutado) return;
        tarea.restaurarEstado(estadoAnterior);
        ejecutado = false;
    }
}
