package gestor.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** Operaciones de consulta, filtrado y ordenamiento de tareas. */
public class BuscadorTareas {
    public List<Tarea> filtrarPorEstado(List<Tarea> tareas, EstadoTarea estado) {
        validar(tareas);
        if (estado == null) throw new IllegalArgumentException("El estado es obligatorio");
        return tareas.stream().filter(t -> t.getEstado() == estado).collect(Collectors.toList());
    }

    public List<Tarea> filtrarPorPrioridad(List<Tarea> tareas, Prioridad prioridad) {
        validar(tareas);
        if (prioridad == null) throw new IllegalArgumentException("La prioridad es obligatoria");
        return tareas.stream().filter(t -> t.getPrioridad() == prioridad).collect(Collectors.toList());
    }

    public List<Tarea> ordenarPorFecha(List<Tarea> tareas) {
        validar(tareas);
        List<Tarea> resultado = new ArrayList<>(tareas);
        resultado.sort(Comparator.comparing(Tarea::getFechaLimite,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return resultado;
    }

    public List<Tarea> ordenarPorPrioridad(List<Tarea> tareas) {
        validar(tareas);
        List<Tarea> resultado = new ArrayList<>(tareas);
        resultado.sort(Comparator.comparing(Tarea::getPrioridad).reversed());
        return resultado;
    }

    private void validar(List<Tarea> tareas) {
        if (tareas == null) throw new IllegalArgumentException("La lista de tareas no puede ser nula");
    }
}
