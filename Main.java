import javax.swing.SwingUtilities;

/** Crea el catálogo una sola vez y se lo entrega a la ventana. */
public class Main {
    public static void main(String[] args) {
        OperacionesCatalogo catalogo = new CatalogoPeliculas();
        SwingUtilities.invokeLater(() -> new Ventana(catalogo).setVisible(true));
    }
}