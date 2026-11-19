package gestor.modelo.comando;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import gestor.modelo.EstadoTarea;
import gestor.modelo.Prioridad;
import gestor.modelo.Tarea;

class GestorComandosTest {
    @Test
    void permiteDeshacerYRehacerCambioDeNombre() {
        Tarea tarea = new Tarea("Nombre inicial", "", Prioridad.MEDIA, LocalDate.now());
        GestorComandos gestor = new GestorComandos();
        gestor.ejecutar(new CambiarNombreTareaCommand(tarea, "Nombre nuevo"));
        assertEquals("Nombre nuevo", tarea.getNombre());
        assertTrue(gestor.deshacer());
        assertEquals("Nombre inicial", tarea.getNombre());
        assertTrue(gestor.rehacer());
        assertEquals("Nombre nuevo", tarea.getNombre());
    }

    @Test
    void permiteDeshacerCambioDeEstadoInclusoDesdeTerminada() {
        Tarea tarea = new Tarea("Tarea", "", Prioridad.MEDIA, LocalDate.now());
        GestorComandos gestor = new GestorComandos();
        gestor.ejecutar(new CambiarEstadoCommand(tarea, EstadoTarea.EN_CURSO));
        gestor.ejecutar(new CambiarEstadoCommand(tarea, EstadoTarea.EN_REVISION));
        gestor.ejecutar(new CambiarEstadoCommand(tarea, EstadoTarea.TERMINADA));
        assertTrue(gestor.deshacer());
        assertEquals(EstadoTarea.EN_REVISION, tarea.getEstado());
    }
}
