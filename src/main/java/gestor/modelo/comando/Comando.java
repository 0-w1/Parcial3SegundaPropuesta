package gestor.modelo.comando;

/** Operación que se puede ejecutar y revertir (patrón Command). */
public interface Comando {
    void ejecutar();
    void deshacer();
}
