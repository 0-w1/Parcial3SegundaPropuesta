package gestor.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Busca tareas cuya fecha límite está próxima o ya venció. */
public class ServicioRecordatorios {
    public List<String> revisar(List<Tarea> tareas, LocalDate fechaActual, int diasAnticipacion) {
        if (tareas == null || fechaActual == null || diasAnticipacion < 0) {
            throw new IllegalArgumentException("Datos de recordatorio inválidos");
        }
        List<String> avisos = new ArrayList<>();
        LocalDate limite = fechaActual.plusDays(diasAnticipacion);
        for (Tarea tarea : tareas) {
            if (tarea.getFechaLimite() != null
                    && tarea.getEstado() != EstadoTarea.TERMINADA
                    && !tarea.getFechaLimite().isAfter(limite)) {
                String estado = tarea.getFechaLimite().isBefore(fechaActual)
                        ? "está vencida" : "está próxima a vencer";
                avisos.add("La tarea '" + tarea.getNombre() + "' " + estado
                        + " (fecha límite: " + tarea.getFechaLimite() + ").");
            }
        }
        return avisos;
    }
}
