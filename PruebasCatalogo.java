import java.util.ArrayList;
public class PruebasCatalogo {
    public static void main(String[] args) {
        CatalogoPeliculas catalogo = new CatalogoPeliculas();
        System.out.println("=== INICIO DE PRUEBAS DE CONSOLA ===\n");
        System.out.println("1. Carga inicial (total películas): " + catalogo.getCantidad());
        Pelicula nuevaValida = new Pelicula(1999, "Película de Prueba", "Director Prueba", "Drama", 2024, 0);
        try {
            catalogo.registrar(nuevaValida);
            System.out.println("2. Registro válido (código 1999): APROBADO");
        } catch (Exception e) {
            System.out.println("2. Registro válido (código 1999): FALLÓ (" + e.getMessage() + ")");
        }
        int totalAntes = catalogo.getCantidad();
        Pelicula duplicada = new Pelicula(1999, "Otra Película", "Otro Director", "Acción", 2020, 0);
        boolean regDuplicadoOk = false;
        try {
            catalogo.registrar(duplicada);
        } catch (Exception e) {
            regDuplicadoOk = true;
        }
        boolean intacto = (catalogo.getCantidad() == totalAntes);
        System.out.println("3. Rechazo de duplicado sin cambios: " + (regDuplicadoOk && intacto ? "APROBADO" : "FALLÓ"));
        boolean regInvalidoOk = false;
        try {
            catalogo.registrar(8888, "   ", "Director", "Comedia", 1800);
        } catch (Exception e) {
            regInvalidoOk = true;
        }
        System.out.println("4. Rechazo de datos inválidos al registrar: " + (regInvalidoOk ? "APROBADO" : "FALLÓ"));
        int idxExistente = catalogo.buscarIndicePorCodigo(1999);
        int idxInexistente = catalogo.buscarIndicePorCodigo(99999);
        System.out.println("5. Búsqueda binaria (existente=" + idxExistente + ", inexistente=" + idxInexistente + "): "
                + (idxExistente != -1 && idxInexistente == -1 ? "APROBADO" : "FALLÓ"));
        ArrayList<Pelicula> coincidencias = catalogo.buscarCoincidencias("  Prueba  ");
        System.out.println("6. Búsqueda con trim/case-insensitive (resultados=" + coincidencias.size() + "): "
                + (!coincidencias.isEmpty() ? "APROBADO" : "FALLÓ"));
        ArrayList<Pelicula> sinResultados = catalogo.buscarCoincidencias("XYZ_NO_EXISTE_999");
        System.out.println("7. Búsqueda sin coincidencias no nula y vacía: "
                + (sinResultados != null && sinResultados.isEmpty() ? "APROBADO" : "FALLÓ"));
        try {
            catalogo.editarPelicula(1999, "Película Editada", "Director Editado", "Ciencia ficción", 2025);
            Pelicula pEditada = catalogo.getPeliculas().get(catalogo.buscarIndicePorCodigo(1999));
            System.out.println("8. Edición válida aplicada: "
                    + (pEditada.getTitulo().equals("Película Editada") ? "APROBADO" : "FALLÓ"));
        } catch (Exception e) {
            System.out.println("8. Edición válida aplicada: FALLÓ (" + e.getMessage() + ")");
        }
        boolean editFailOk = false;
        try {
            catalogo.editarPelicula(1999, "", "Director", "Drama", 2025);
        } catch (Exception e) {
            editFailOk = true;
        }
        System.out.println("9. Validación previa a modificar en edición: " + (editFailOk ? "APROBADO" : "FALLÓ"));
        Pelicula pEditada2 = catalogo.getPeliculas().get(catalogo.buscarIndicePorCodigo(1999));
        int likesAntes = pEditada2.getMeGusta();
        catalogo.darMeGusta(1999);
        catalogo.darMeGusta(1999);
        System.out.println("10. Incremento de Me gusta (+2 total=" + pEditada2.getMeGusta() + "): "
                + (pEditada2.getMeGusta() == likesAntes + 2 ? "APROBADO" : "FALLÓ"));
        ArrayList<Pelicula> ranking = catalogo.obtenerRanking();
        boolean ordenDescendente = true;
        for (int i = 0; i < ranking.size() - 1; i++) {
            if (ranking.get(i).getMeGusta() < ranking.get(i + 1).getMeGusta()) {
                ordenDescendente = false;
                break;
            }
        }
        boolean originalPorCodigo = true;
        ArrayList<Pelicula> original = catalogo.getPeliculas();
        for (int i = 0; i < original.size() - 1; i++) {
            if (original.get(i).getCodigo() > original.get(i + 1).getCodigo()) {
                originalPorCodigo = false;
                break;
            }
        }
        System.out.println("11. Ranking estable descendente sin alterar catálogo: "
                + (ordenDescendente && originalPorCodigo ? "APROBADO" : "FALLÓ"));
    }
}