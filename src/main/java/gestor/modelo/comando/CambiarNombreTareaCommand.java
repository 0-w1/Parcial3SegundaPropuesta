package gestor.modelo.comando;

import gestor.modelo.Tarea;

/** Comando para modificar y restaurar el nombre de una tarea. */
public class CambiarNombreTareaCommand implements Comando {
    private final Tarea tarea;
    private final String nuevoNombre;
    private String nombreAnterior;
    private boolean ejecutado;

    public CambiarNombreTareaCommand(Tarea tarea, String nuevoNombre) {
        if (tarea == null || nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("La tarea y el nuevo nombre son obligatorios");
        }
        this.tarea = tarea;
        this.nuevoNombre = nuevoNombre;
    }

    @Override
    public void ejecutar() {
        if (ejecutado) return;
        nombreAnterior = tarea.getNombre();
        tarea.setNombre(nuevoNombre);
        ejecutado = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutado) return;
        tarea.setNombre(nombreAnterior);
        ejecutado = false;
    }
}
