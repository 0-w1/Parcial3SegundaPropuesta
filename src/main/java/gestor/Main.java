package gestor;

import gestor.modelo.Proyecto;

/** Punto de entrada temporal. Luego se reemplazará por la consola real. */
public class Main {

    public static void main(String[] args) {
        Proyecto demo = new Proyecto("Proyecto de prueba", "Verificación de que todo compila");
        System.out.println("Gestor de tareas iniciado: " + demo);
    }
}
