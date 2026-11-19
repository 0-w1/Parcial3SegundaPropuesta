package gestor.modelo.comando;

import java.util.ArrayDeque;
import java.util.Deque;

/** Mantiene el historial de comandos para deshacer y rehacer acciones. */
public class GestorComandos {
    private final Deque<Comando> deshacer = new ArrayDeque<>();
    private final Deque<Comando> rehacer = new ArrayDeque<>();

    public void ejecutar(Comando comando) {
        if (comando == null) throw new IllegalArgumentException("El comando no puede ser nulo");
        comando.ejecutar();
        deshacer.push(comando);
        rehacer.clear();
    }

    public boolean deshacer() {
        if (deshacer.isEmpty()) return false;
        Comando comando = deshacer.pop();
        comando.deshacer();
        rehacer.push(comando);
        return true;
    }

    public boolean rehacer() {
        if (rehacer.isEmpty()) return false;
        Comando comando = rehacer.pop();
        comando.ejecutar();
        deshacer.push(comando);
        return true;
    }

    public boolean puedeDeshacer() { return !deshacer.isEmpty(); }
    public boolean puedeRehacer() { return !rehacer.isEmpty(); }
}
