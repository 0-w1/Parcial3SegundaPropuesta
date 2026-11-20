package gestor.modelo;

import java.time.LocalDate;

public class TareaUrgenteFactory extends PlantillaTareaFactory {
    @Override
    public Tarea crearTarea(String nombre, LocalDate fechaLimite) {
        return new Tarea(nombre, "Tarea creada desde la plantilla urgente",
                Prioridad.ALTA, fechaLimite);
    }
}
