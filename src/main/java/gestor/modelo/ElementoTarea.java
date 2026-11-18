package gestor.modelo;

import java.util.List;

/** Contrato común para los elementos que forman la estructura de tareas. */
public interface ElementoTarea {

    String getNombre();

    List<ElementoTarea> obtenerElementos();

    int contarTareas();
}
