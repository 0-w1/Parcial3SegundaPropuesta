package gestor.modelo;

/** Observador que recibe avisos cuando una tarea cambia. */
public interface ObservadorTarea {
    void actualizar(Tarea tarea, String mensaje);
}
