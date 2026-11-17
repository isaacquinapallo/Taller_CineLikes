import java.util.ArrayList;

/**
 * Contrato del catálogo de películas.
 * La interfaz gráfica trabaja contra esta interfaz, de modo que
 * la lógica queda separada de los eventos de Swing.
 */
public interface OperacionesCatalogo {

    /** Géneros permitidos; se usan para llenar el JComboBox. */
    String[] GENEROS = {
            "Ciencia ficción", "Drama", "Aventura", "Comedia",
            "Animación", "Suspenso", "Documental"
    };

    /** Año mínimo permitido para una película. */
    int ANIO_MINIMO = 1;

    // ---- Integrante A: carga, orden y registro (R1, R2) ----
    void ordenarPorCodigoBurbuja();
    void registrar(Pelicula pelicula);
    void registrar(int codigo, String titulo, String director, String genero, int anio);
    int getCantidad();
    ArrayList<Pelicula> getPeliculas();

    // ---- Integrante B: búsquedas (R3, R4) ----
    ArrayList<Pelicula> buscarCoincidencias(String texto);
    int buscarIndicePorCodigo(int codigo);

    // ---- Integrante C: edición, me gusta y ranking (R4, R5, R6) ----
    void editarPelicula(int codigo, String titulo, String director, String genero, int anio);
    void darMeGusta(int codigo);
    ArrayList<Pelicula> obtenerRanking();
}