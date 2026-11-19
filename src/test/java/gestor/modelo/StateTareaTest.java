package gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class StateTareaTest {
    @Test
    void permiteFlujoValidoDeEstados() {
        Tarea tarea = new Tarea("Diseñar", "", Prioridad.MEDIA, LocalDate.now());
        tarea.setEstado(EstadoTarea.EN_CURSO);
        tarea.setEstado(EstadoTarea.EN_REVISION);
        tarea.setEstado(EstadoTarea.TERMINADA);
        assertEquals(EstadoTarea.TERMINADA, tarea.getEstado());
    }

    @Test
    void rechazaSaltarEstados() {
        Tarea tarea = new Tarea("Diseñar", "", Prioridad.MEDIA, LocalDate.now());
        assertThrows(IllegalStateException.class, () -> tarea.setEstado(EstadoTarea.TERMINADA));
    }
}
