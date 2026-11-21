package gestor.gui;

import gestor.modelo.ObservadorTarea;
import gestor.modelo.Tarea;
import java.util.function.Consumer;

/** Recibe los cambios de las tareas y los muestra en el registro de la interfaz. */
public class ObservadorInterfaz implements ObservadorTarea {
    private final Consumer<String> receptor;

    public ObservadorInterfaz(Consumer<String> receptor) {
        this.receptor = receptor;
    }

    @Override
    public void actualizar(Tarea tarea, String mensaje) {
        receptor.accept(tarea.getNombre() + ": " + mensaje);
    }
}
