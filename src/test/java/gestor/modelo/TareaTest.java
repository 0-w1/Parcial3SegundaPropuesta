package gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TareaTest {

    @Test
    void creaTareaConEstadoPendiente() {
        Tarea tarea = new Tarea("Diseñar pantalla", "Crear el diseño inicial",
                Prioridad.ALTA, LocalDate.of(2026, 11, 20));

        assertEquals("Diseñar pantalla", tarea.getNombre());
        assertEquals(EstadoTarea.PENDIENTE, tarea.getEstado());
        assertEquals(Prioridad.ALTA, tarea.getPrioridad());
    }

    @Test
    void rechazaNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Tarea(" ", "Descripción", Prioridad.MEDIA, LocalDate.now()));
    }

    @Test
    void permiteAgregarTareaAProyecto() {
        Proyecto proyecto = new Proyecto("Proyecto académico", "Prueba");
        Tarea tarea = new Tarea("Crear modelo", "Primera tarea",
                Prioridad.MEDIA, LocalDate.now());

        proyecto.agregarTarea(tarea);

        assertEquals(1, proyecto.getTareas().size());
        assertSame(tarea, proyecto.getTareas().get(0));
    }
}
