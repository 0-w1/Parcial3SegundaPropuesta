package gestor;

import gestor.modelo.*;
import gestor.modelo.comando.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Verificación funcional ejecutable sin dependencias externas. */
public class VerificacionDia6 {
    private static int comprobaciones = 0;

    private static void comprobar(boolean condicion, String mensaje) {
        comprobaciones++;
        if (!condicion) throw new AssertionError("FALLÓ: " + mensaje);
    }

    public static void main(String[] args) {
        LocalDate hoy = LocalDate.now();

        // Composite: jerarquía, conteo y prevención de ciclos.
        Tarea raiz = new Tarea("Proyecto", "", Prioridad.MEDIA, hoy);
        Tarea hija = new Tarea("Módulo", "", Prioridad.BAJA, hoy);
        Tarea nieta = new Tarea("Actividad", "", Prioridad.ALTA, hoy);
        raiz.agregarSubtarea(hija);
        hija.agregarSubtarea(nieta);
        comprobar(raiz.contarTareas() == 3, "Composite cuenta niveles");
        boolean cicloRechazado = false;
        try { nieta.agregarSubtarea(raiz); } catch (IllegalArgumentException ex) { cicloRechazado = true; }
        comprobar(cicloRechazado, "Composite rechaza ciclos");

        // State: flujo permitido y transición no permitida.
        hija.setEstado(EstadoTarea.EN_CURSO);
        hija.setEstado(EstadoTarea.EN_REVISION);
        hija.setEstado(EstadoTarea.TERMINADA);
        comprobar(hija.getEstado() == EstadoTarea.TERMINADA, "State permite flujo válido");
        boolean saltoRechazado = false;
        try { raiz.setEstado(EstadoTarea.TERMINADA); } catch (IllegalStateException ex) { saltoRechazado = true; }
        comprobar(saltoRechazado, "State rechaza salto de pendiente a terminada");

        // Command: deshacer y rehacer nombre y estado.
        Tarea cmdTarea = new Tarea("Antes", "", Prioridad.MEDIA, hoy);
        GestorComandos historial = new GestorComandos();
        historial.ejecutar(new CambiarNombreTareaCommand(cmdTarea, "Después"));
        comprobar("Después".equals(cmdTarea.getNombre()), "Command ejecuta cambio de nombre");
        comprobar(historial.deshacer() && "Antes".equals(cmdTarea.getNombre()), "Command deshace nombre");
        comprobar(historial.rehacer() && "Después".equals(cmdTarea.getNombre()), "Command rehace nombre");
        historial.ejecutar(new CambiarEstadoCommand(cmdTarea, EstadoTarea.EN_CURSO));
        comprobar(historial.deshacer() && cmdTarea.getEstado() == EstadoTarea.PENDIENTE, "Command deshace estado");
        comprobar(historial.rehacer() && cmdTarea.getEstado() == EstadoTarea.EN_CURSO, "Command rehace estado");

        // Factory Method.
        Tarea basica = new TareaBasicaFactory().crearTarea("Básica", hoy);
        Tarea urgente = new TareaUrgenteFactory().crearTarea("Urgente", hoy);
        comprobar(basica.getPrioridad() == Prioridad.MEDIA, "Factory crea tarea básica");
        comprobar(urgente.getPrioridad() == Prioridad.ALTA, "Factory crea tarea urgente");

        // Observer.
        Tarea observada = new Tarea("Observada", "", Prioridad.MEDIA, hoy);
        AtomicInteger avisos = new AtomicInteger();
        observada.agregarObservador((t, mensaje) -> avisos.incrementAndGet());
        observada.setEstado(EstadoTarea.EN_CURSO);
        observada.setFechaLimite(hoy.plusDays(1));
        comprobar(avisos.get() == 2, "Observer informa cambio de estado y fecha");

        // Proyecto, recordatorios y buscador.
        Proyecto proyecto = new Proyecto("Proyecto de prueba", "Verificación");
        proyecto.agregarTarea(observada);
        proyecto.agregarTarea(new Tarea("Vencida", "", Prioridad.ALTA, hoy.minusDays(1)));
        ServicioRecordatorios recordatorios = new ServicioRecordatorios();
        comprobar(recordatorios.revisar(proyecto.getTareas(), hoy, 2).size() == 2,
                "Recordatorios detecta vencida y próxima");
        BuscadorTareas buscador = new BuscadorTareas();
        comprobar(buscador.filtrarPorPrioridad(proyecto.getTareas(), Prioridad.ALTA).size() == 1,
                "Buscador filtra prioridad");
        List<Tarea> ordenadas = buscador.ordenarPorFecha(proyecto.getTareas());
        comprobar(ordenadas.get(0).getFechaLimite().isBefore(hoy), "Buscador ordena por fecha");

        System.out.println("VERIFICACIÓN FUNCIONAL: " + comprobaciones + " comprobaciones superadas.");
    }
}
