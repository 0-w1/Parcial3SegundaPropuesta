package gestor.gui;

import gestor.modelo.*;
import gestor.modelo.comando.CambiarEstadoCommand;
import gestor.modelo.comando.GestorComandos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Ventana principal del gestor de tareas. */
public class VentanaPrincipal extends JFrame {
    private final Proyecto proyecto;
    private final GestorComandos historial = new GestorComandos();
    private final ModeloTablaTareas modeloTabla = new ModeloTablaTareas();
    private final JTable tabla = new JTable(modeloTabla);
    private final JTextArea registro = new JTextArea(5, 30);
    private final JComboBox<String> filtroEstado = new JComboBox<>(new String[]{
            "Todos", "PENDIENTE", "EN_CURSO", "EN_REVISION", "TERMINADA"
    });
    private final JLabel resumen = new JLabel();
    private final ObservadorInterfaz observador = new ObservadorInterfaz(this::registrarEvento);

    public VentanaPrincipal() {
        super("Gestor de tareas y proyectos");
        proyecto = new Proyecto("Mi proyecto", "Proyecto administrado desde la interfaz gráfica");
        configurarVentana();
        construirInterfaz();
        actualizarVista();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 650);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(18, 18, 18, 18));
        principal.setBackground(new Color(245, 247, 250));
        setContentPane(principal);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JLabel titulo = new JLabel("Gestor de tareas y proyectos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        JLabel subtitulo = new JLabel("Organiza actividades, controla estados y revisa fechas límite");
        subtitulo.setForeground(new Color(90, 100, 115));
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        encabezado.add(textos, BorderLayout.WEST);
        encabezado.add(resumen, BorderLayout.EAST);
        principal.add(encabezado, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setOpaque(false);
        JPanel herramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        herramientas.setOpaque(false);
        JButton nuevaTarea = new JButton("+ Nueva tarea");
        JButton cambiarEstado = new JButton("Cambiar estado");
        JButton deshacer = new JButton("Deshacer");
        JButton rehacer = new JButton("Rehacer");
        JButton recordatorios = new JButton("Ver recordatorios");
        herramientas.add(nuevaTarea);
        herramientas.add(cambiarEstado);
        herramientas.add(deshacer);
        herramientas.add(rehacer);
        herramientas.add(recordatorios);
        herramientas.add(new JLabel("Filtrar:") );
        herramientas.add(filtroEstado);
        centro.add(herramientas, BorderLayout.NORTH);

        tabla.setRowHeight(29);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.setAutoCreateRowSorter(true);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Tareas del proyecto: " + proyecto.getNombre()));
        centro.add(scrollTabla, BorderLayout.CENTER);

        registro.setEditable(false);
        registro.setLineWrap(true);
        registro.setWrapStyleWord(true);
        registro.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollRegistro = new JScrollPane(registro);
        scrollRegistro.setBorder(BorderFactory.createTitledBorder("Actividad reciente"));
        centro.add(scrollRegistro, BorderLayout.SOUTH);
        principal.add(centro, BorderLayout.CENTER);

        nuevaTarea.addActionListener(e -> crearTarea());
        cambiarEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        deshacer.addActionListener(e -> {
            if (historial.deshacer()) registrarEvento("Se deshizo el último cambio de estado.");
            else mostrarMensaje("No hay acciones para deshacer.");
            actualizarVista();
        });
        rehacer.addActionListener(e -> {
            if (historial.rehacer()) registrarEvento("Se rehízo el último cambio de estado.");
            else mostrarMensaje("No hay acciones para rehacer.");
            actualizarVista();
        });
        recordatorios.addActionListener(e -> mostrarRecordatorios());
        filtroEstado.addActionListener(e -> actualizarVista());
    }

    private void crearTarea() {
        JTextField nombre = new JTextField(22);
        JTextField descripcion = new JTextField(22);
        JComboBox<Prioridad> prioridad = new JComboBox<>(Prioridad.values());
        JTextField fecha = new JTextField(LocalDate.now().plusDays(7).toString(), 22);
        JTextField responsable = new JTextField(22);

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nombre:*")); formulario.add(nombre);
        formulario.add(new JLabel("Descripción:")); formulario.add(descripcion);
        formulario.add(new JLabel("Prioridad:")); formulario.add(prioridad);
        formulario.add(new JLabel("Fecha límite (AAAA-MM-DD):")); formulario.add(fecha);
        formulario.add(new JLabel("Responsable:")); formulario.add(responsable);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar nueva tarea",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        try {
            if (nombre.getText().isBlank()) throw new IllegalArgumentException("Escribe el nombre de la tarea.");
            LocalDate fechaLimite = fecha.getText().isBlank() ? null : LocalDate.parse(fecha.getText().trim());
            Prioridad nivel = (Prioridad) prioridad.getSelectedItem();
            PlantillaTareaFactory fabrica = nivel == Prioridad.ALTA
                    ? new TareaUrgenteFactory() : new TareaBasicaFactory();
            Tarea tarea = fabrica.crearTarea(nombre.getText(), fechaLimite);
            tarea.setPrioridad(nivel);
            tarea.setDescripcion(descripcion.getText());
            if (!responsable.getText().isBlank()) tarea.agregarResponsable(new Responsable(responsable.getText()));
            tarea.agregarObservador(observador);
            proyecto.agregarTarea(tarea);
            registrarEvento("Se creó la tarea: " + tarea.getNombre());
            actualizarVista();
        } catch (DateTimeParseException ex) {
            mostrarMensaje("La fecha no tiene el formato correcto. Usa AAAA-MM-DD.");
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }

    private void cambiarEstadoSeleccionado() {
        Tarea tarea = obtenerTareaSeleccionada();
        if (tarea == null) {
            mostrarMensaje("Selecciona una tarea de la tabla.");
            return;
        }
        List<EstadoTarea> permitidos = estadosPermitidos(tarea.getEstado());
        if (permitidos.isEmpty()) {
            mostrarMensaje("La tarea ya está terminada y no admite más cambios.");
            return;
        }
        EstadoTarea elegido = (EstadoTarea) JOptionPane.showInputDialog(this,
                "Selecciona el nuevo estado:", "Cambiar estado", JOptionPane.PLAIN_MESSAGE,
                null, permitidos.toArray(), permitidos.get(0));
        if (elegido == null) return;
        try {
            historial.ejecutar(new CambiarEstadoCommand(tarea, elegido));
            actualizarVista();
        } catch (IllegalStateException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }

    private List<EstadoTarea> estadosPermitidos(EstadoTarea actual) {
        switch (actual) {
            case PENDIENTE: return Arrays.asList(EstadoTarea.EN_CURSO);
            case EN_CURSO: return Arrays.asList(EstadoTarea.PENDIENTE, EstadoTarea.EN_REVISION);
            case EN_REVISION: return Arrays.asList(EstadoTarea.EN_CURSO, EstadoTarea.TERMINADA);
            default: return new ArrayList<>();
        }
    }

    private void mostrarRecordatorios() {
        List<String> avisos = new ServicioRecordatorios().revisar(proyecto.getTareas(), LocalDate.now(), 3);
        String contenido = avisos.isEmpty() ? "No hay tareas vencidas o próximas a vencer." :
                avisos.stream().collect(Collectors.joining("\n"));
        JOptionPane.showMessageDialog(this, contenido, "Recordatorios", JOptionPane.INFORMATION_MESSAGE);
    }

    private Tarea obtenerTareaSeleccionada() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return null;
        int filaModelo = tabla.convertRowIndexToModel(filaVista);
        return modeloTabla.obtenerTarea(filaModelo);
    }

    private void actualizarVista() {
        String estado = (String) filtroEstado.getSelectedItem();
        List<Tarea> tareas = proyecto.getTareas();
        if (estado != null && !estado.equals("Todos")) {
            EstadoTarea seleccionado = EstadoTarea.valueOf(estado);
            tareas = tareas.stream().filter(t -> t.getEstado() == seleccionado).collect(Collectors.toList());
        }
        modeloTabla.setTareas(tareas);
        resumen.setText("Total de tareas: " + proyecto.getTareas().size());
    }

    private void registrarEvento(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            registro.append("• " + mensaje + "\n");
            registro.setCaretPosition(registro.getDocument().getLength());
        });
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class ModeloTablaTareas extends AbstractTableModel {
        private final String[] columnas = {"Tarea", "Prioridad", "Estado", "Fecha límite", "Responsables"};
        private List<Tarea> tareas = new ArrayList<>();

        void setTareas(List<Tarea> tareas) {
            this.tareas = new ArrayList<>(tareas);
            fireTableDataChanged();
        }

        Tarea obtenerTarea(int fila) { return tareas.get(fila); }
        @Override public int getRowCount() { return tareas.size(); }
        @Override public int getColumnCount() { return columnas.length; }
        @Override public String getColumnName(int columna) { return columnas[columna]; }
        @Override public Object getValueAt(int fila, int columna) {
            Tarea tarea = tareas.get(fila);
            switch (columna) {
                case 0: return tarea.getNombre();
                case 1: return tarea.getPrioridad();
                case 2: return tarea.getEstado();
                case 3: return tarea.getFechaLimite() == null ? "Sin fecha" : tarea.getFechaLimite();
                case 4: return tarea.getResponsables().isEmpty() ? "Sin asignar" :
                        tarea.getResponsables().stream().map(Responsable::getNombre).collect(Collectors.joining(", "));
                default: return "";
            }
        }
    }
}
