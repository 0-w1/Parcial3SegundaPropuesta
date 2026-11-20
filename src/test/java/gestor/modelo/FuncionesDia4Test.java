package gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class FuncionesDia4Test {
    @Test
    void observadorRecibeCambioDeEstado() {
        Tarea tarea = new Tarea("Diseño", "", Prioridad.MEDIA, LocalDate.now());
        AtomicInteger avisos = new AtomicInteger();
        tarea.agregarObservador((t, mensaje) -> avisos.incrementAndGet());
        tarea.setEstado(EstadoTarea.EN_CURSO);
        assertEquals(1, avisos.get());
    }

    @Test
    void factoryCreaTareaUrgente() {
        Tarea tarea = new TareaUrgenteFactory().crearTarea("Entrega", LocalDate.now());
        assertEquals(Prioridad.ALTA, tarea.getPrioridad());
    }

    @Test
    void filtraPorEstadoYOrdenaPorFecha() {
        Tarea hoy = new Tarea("Hoy", "", Prioridad.BAJA, LocalDate.now());
        Tarea manana = new Tarea("Mañana", "", Prioridad.ALTA, LocalDate.now().plusDays(1));
        manana.setEstado(EstadoTarea.EN_CURSO);
        BuscadorTareas buscador = new BuscadorTareas();
        assertEquals(1, buscador.filtrarPorEstado(List.of(hoy, manana), EstadoTarea.EN_CURSO).size());
        assertEquals("Hoy", buscador.ordenarPorFecha(List.of(manana, hoy)).get(0).getNombre());
    }

    @Test
    void encuentraRecordatorioProximo() {
        Tarea tarea = new Tarea("Informe", "", Prioridad.ALTA, LocalDate.now().plusDays(1));
        List<String> avisos = new ServicioRecordatorios().revisar(List.of(tarea), LocalDate.now(), 2);
        assertEquals(1, avisos.size());
    }
}
