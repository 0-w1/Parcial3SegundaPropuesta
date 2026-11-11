package gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProyectoTest {

    @Test
    void creaProyectoConDatosValidos() {
        Proyecto p = new Proyecto("Web", "Sitio de la clase");
        assertEquals("Web", p.getNombre());
        assertNotNull(p.getFechaCreacion());
    }

    @Test
    void rechazaNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Proyecto("  ", "x"));
    }
}
