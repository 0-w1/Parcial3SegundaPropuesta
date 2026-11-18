package gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CompositeTareaTest {

    private Tarea crear(String nombre) {
        return new Tarea(nombre, "Descripción", Prioridad.MEDIA, LocalDate.now());
    }

    @Test
    void permiteAgregarSubtareas() {
        Tarea principal = crear("Preparar entrega");
        Tarea diseno = crear("Diseñar interfaz");
        Tarea codigo = crear("Programar interfaz");

        principal.agregarSubtarea(diseno);
        principal.agregarSubtarea(codigo);

        assertEquals(2, principal.getSubtareas().size());
        assertEquals(3, principal.contarTareas());
    }

    @Test
    void permiteCrearVariosNivelesDeSubtareas() {
        Tarea principal = crear("Proyecto");
        Tarea modulo = crear("Módulo");
        Tarea actividad = crear("Actividad");

        principal.agregarSubtarea(modulo);
        modulo.agregarSubtarea(actividad);

        assertEquals(3, principal.contarTareas());
    }

    @Test
    void evitaAgregarUnaTareaComoSubtareaDeSiMisma() {
        Tarea tarea = crear("Tarea");

        assertThrows(IllegalArgumentException.class,
                () -> tarea.agregarSubtarea(tarea));
    }

    @Test
    void permiteEliminarSubtarea() {
        Tarea principal = crear("Principal");
        Tarea secundaria = crear("Secundaria");
        principal.agregarSubtarea(secundaria);

        assertTrue(principal.eliminarSubtarea(secundaria));
        assertEquals(1, principal.contarTareas());
    }
}
