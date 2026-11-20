package gestor;

import gestor.modelo.*;
import java.time.LocalDate;
import java.util.List;

/** Punto de entrada con una demostración breve de las funciones del gestor. */
public class Main {
    public static void main(String[] args) {
        Proyecto proyecto = new Proyecto("Proyecto académico", "Demostración del gestor");

        Tarea tarea = new TareaUrgenteFactory().crearTarea(
                "Entregar informe", LocalDate.now().plusDays(2));
        tarea.agregarResponsable(new Responsable("Juan"));
        tarea.agregarObservador(new NotificadorConsola());
        proyecto.agregarTarea(tarea);

        tarea.setEstado(EstadoTarea.EN_CURSO);

        ServicioRecordatorios recordatorios = new ServicioRecordatorios();
        List<String> avisos = recordatorios.revisar(proyecto.getTareas(), LocalDate.now(), 3);
        avisos.forEach(System.out::println);

        BuscadorTareas buscador = new BuscadorTareas();
        System.out.println("Tareas de prioridad alta: "
                + buscador.filtrarPorPrioridad(proyecto.getTareas(), Prioridad.ALTA).size());
    }
}
