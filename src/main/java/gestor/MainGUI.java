package gestor;

import gestor.gui.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada para ejecutar el gestor con interfaz gráfica. */
public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("No se pudo aplicar el estilo visual predeterminado.");
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}
