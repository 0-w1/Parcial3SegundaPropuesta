package gestor.modelo;

/** Observador que muestra los cambios de una tarea en consola. */
public class NotificadorConsola implements ObservadorTarea {
    @Override
    public void actualizar(Tarea tarea, String mensaje) {
        System.out.println("[Notificación] " + tarea.getNombre() + ": " + mensaje);
    }
}
