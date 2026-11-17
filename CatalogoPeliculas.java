import java.time.Year;
import java.util.ArrayList;

/**
 * Lógica del catálogo. El ArrayList principal siempre se mantiene
 * ordenado por código ascendente para permitir la búsqueda binaria.
 *
 * Integrante A: carga única, burbuja por código, validaciones y registro.
 * Integrantes B y C: reemplazan los métodos marcados como PENDIENTE.
 */
public class CatalogoPeliculas implements OperacionesCatalogo {

    private final ArrayList<Pelicula> peliculas;

    /** Carga las 20 películas una sola vez y las ordena por código. */
    public CatalogoPeliculas() {
        peliculas = DatosIniciales.cargar();
        ordenarPorCodigoBurbuja();
    }

    // ===================== Integrante A: R1 y R2 =====================

    /** Burbuja por código ascendente; termina antes si una pasada no intercambia. */
    @Override
    public void ordenarPorCodigoBurbuja() {
        int n = peliculas.size();
        for (int i = 0; i < n - 1; i++) {
            boolean huboIntercambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (peliculas.get(j).getCodigo() > peliculas.get(j + 1).getCodigo()) {
                    Pelicula temporal = peliculas.get(j);
                    peliculas.set(j, peliculas.get(j + 1));
                    peliculas.set(j + 1, temporal);
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) {
                break;
            }
        }
    }

    /** Valida todo antes de agregar; si algo falla, la colección no cambia. */
    @Override
    public void registrar(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("No se recibió ninguna película.");
        }
        validarCodigo(pelicula.getCodigo());
        validarDatos(pelicula.getTitulo(), pelicula.getDirector(),
                pelicula.getGenero(), pelicula.getAnio());
        if (pelicula.getMeGusta() != 0) {
            throw new IllegalArgumentException("Una película nueva debe comenzar con 0 me gusta.");
        }
        if (existeCodigo(pelicula.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una película con el código "
                    + pelicula.getCodigo() + ".");
        }
        peliculas.add(pelicula);
        // El nuevo código puede ser menor que los existentes (ej. 1999).
        ordenarPorCodigoBurbuja();
    }

    /** Versión para el formulario: limpia espacios y crea la película con 0 me gusta. */
    @Override
    public void registrar(int codigo, String titulo, String director,
                          String genero, int anio) {
        registrar(new Pelicula(codigo, limpiar(titulo), limpiar(director), genero, anio));
    }

    @Override
    public int getCantidad() {
        return peliculas.size();
    }

    /** Copia de la lista con los mismos objetos; no altera el orden principal. */
    @Override
    public ArrayList<Pelicula> getPeliculas() {
        return new ArrayList<>(peliculas);
    }

    // ============ Validaciones (C también las usa al editar) ============

    public static int anioMaximo() {
        return Year.now().getValue();
    }

    public void validarCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un número positivo.");
        }
    }

    public void validarDatos(String titulo, String director, String genero, int anio) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El director es obligatorio.");
        }
        if (!esGeneroValido(genero)) {
            throw new IllegalArgumentException("Seleccione un género válido.");
        }
        if (anio < ANIO_MINIMO || anio > anioMaximo()) {
            throw new IllegalArgumentException("El año debe estar entre "
                    + ANIO_MINIMO + " y " + anioMaximo() + ".");
        }
    }

    private boolean esGeneroValido(String genero) {
        if (genero == null) {
            return false;
        }
        for (String g : GENEROS) {
            if (g.equals(genero)) {
                return true;
            }
        }
        return false;
    }

    /** Cuando B termine, puede reemplazarse por: return buscarIndicePorCodigo(codigo) != -1; */
    private boolean existeCodigo(int codigo) {
        for (Pelicula p : peliculas) {
            if (p.getCodigo() == codigo) {
                return true;
            }
        }
        return false;
    }

    private static String limpiar(String texto) {
        return texto == null ? null : texto.trim();
    }

    /** Acceso a la lista principal para los métodos de B y C. */
    protected ArrayList<Pelicula> lista() {
        return peliculas;
    }

    // ============ Integrante B: R3 y R4. PENDIENTE ============

    @Override
    public ArrayList<Pelicula> buscarCoincidencias(String texto) {
        throw new UnsupportedOperationException("Pendiente: integrante B (búsqueda lineal).");
    }

    @Override
    public int buscarIndicePorCodigo(int codigo) {
        throw new UnsupportedOperationException("Pendiente: integrante B (búsqueda binaria).");
    }

    // ============ Integrante C: R4, R5 y R6. PENDIENTE ============

    @Override
    public void editarPelicula(int codigo, String titulo, String director,
                               String genero, int anio) {
        throw new UnsupportedOperationException("Pendiente: integrante C (edición).");
    }

    @Override
    public void darMeGusta(int codigo) {
        throw new UnsupportedOperationException("Pendiente: integrante C (me gusta).");
    }

    @Override
    public ArrayList<Pelicula> obtenerRanking() {
        throw new UnsupportedOperationException("Pendiente: integrante C (ranking burbuja).");
    }
}