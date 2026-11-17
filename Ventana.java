import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;

public class Ventana extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txtCodigoReg;
    private JTextField txtTituloReg;
    private JTextField txtDirectorReg;
    private JComboBox<String> cmbGeneroReg;
    private JSpinner spnAnioReg;
    private JButton btnRegistrar;
    private JLabel lblTotalPeliculas;
    private JTextField txtFiltroCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtFiltroTexto;
    private JButton btnBuscarTexto;
    private JLabel lblCantidadResultados;
    private JList<Pelicula> lstResultados;
    private JTextField txtTituloEdit;
    private JTextField txtDirectorEdit;
    private JComboBox<String> cmbGeneroEdit;
    private JSpinner spnAnioEdit;
    private JButton btnGuardarCambios;
    private JButton btnMeGusta;
    private JPanel panelRanking;
    private JLabel lblTituloRanking;
    private JList<Pelicula> listaRanking;
    private JLabel lblCantidadRanking;
    private JButton btnActualizarRanking;

    private final DefaultListModel<Pelicula> modeloRanking = new DefaultListModel<>();
    private final DefaultListModel<Pelicula> modeloResultados = new DefaultListModel<>();
    private final OperacionesCatalogo catalogo;
    private Pelicula seleccionActual;

    public Ventana(OperacionesCatalogo catalogo) {
        this.catalogo = catalogo;
        setContentPane(panel1);
        setTitle("CineLikes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        configurarControles();
    }

    private void configurarControles() {
        cmbGeneroReg.setModel(new DefaultComboBoxModel<>(OperacionesCatalogo.GENEROS));
        cmbGeneroEdit.setModel(new DefaultComboBoxModel<>(OperacionesCatalogo.GENEROS));
        spnAnioReg.setModel(new SpinnerNumberModel(CatalogoPeliculas.anioMaximo(),
                OperacionesCatalogo.ANIO_MINIMO, CatalogoPeliculas.anioMaximo(), 1));
        spnAnioEdit.setModel(new SpinnerNumberModel(CatalogoPeliculas.anioMaximo(),
                OperacionesCatalogo.ANIO_MINIMO, CatalogoPeliculas.anioMaximo(), 1));

        lstResultados.setModel(modeloResultados);
        lstResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaRanking.setModel(modeloRanking);
        listaRanking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaRanking.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Pelicula p = (Pelicula) value;
                String texto = (index + 1) + ".  " + p.getCodigo() + "  |  "
                        + p.getTitulo() + "  |  " + p.getMeGusta() + " me gusta";
                return super.getListCellRendererComponent(list, texto, index,
                        isSelected, cellHasFocus);
            }
        });

        lstResultados.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Pelicula) {
                    Pelicula p = (Pelicula) value;
                    return super.getListCellRendererComponent(list, p.toString(),
                            index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
            }
        });

        lblTotalPeliculas.setText("Total de películas: " + catalogo.getCantidad());
        btnRegistrar.addActionListener(e -> registrar());
        btnBuscarCodigo.addActionListener(e -> buscarPorCodigo());
        btnBuscarTexto.addActionListener(e -> buscarPorTexto());
        btnGuardarCambios.addActionListener(e -> guardarCambios());
        btnMeGusta.addActionListener(e -> darMeGusta());
        btnActualizarRanking.addActionListener(e -> actualizarRanking());

        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedComponent() == panelRanking) {
                actualizarRanking();
            }
        });

        lstResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion(lstResultados.getSelectedValue());
            }
        });

        actualizarRanking();
    }

    private void actualizarRanking() {
        ArrayList<Pelicula> ranking = catalogo.obtenerRanking();
        modeloRanking.clear();
        for (int i = 0; i < ranking.size(); i++) {
            modeloRanking.addElement(ranking.get(i));
        }
        lblCantidadRanking.setText("Películas en el ranking: " + ranking.size());
    }

    private void registrar() {
        try {
            int codigo = Integer.parseInt(txtCodigoReg.getText().trim());
            String titulo = txtTituloReg.getText();
            String director = txtDirectorReg.getText();
            String genero = (String) cmbGeneroReg.getSelectedItem();
            int anio = (Integer) spnAnioReg.getValue();

            catalogo.registrar(codigo, titulo, director, genero, anio);
            lblTotalPeliculas.setText("Total de películas: " + catalogo.getCantidad());
            limpiarRegistro();
            actualizarRanking();
            JOptionPane.showMessageDialog(this, "Película registrada correctamente.",
                    "Registro", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorCodigo() {
        try {
            int codigo = Integer.parseInt(txtFiltroCodigo.getText().trim());
            int indice = catalogo.buscarIndicePorCodigo(codigo);
            modeloResultados.clear();

            if (indice == -1) {
                limpiarSeleccion();
                lblCantidadResultados.setText("Resultados: 0");
                JOptionPane.showMessageDialog(this, "No existe una película con el código "
                                + codigo + ".", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Pelicula encontrada = catalogo.getPeliculas().get(indice);
            modeloResultados.addElement(encontrada);
            lstResultados.setSelectedIndex(0);
            lblCantidadResultados.setText("Resultados: 1");
        } catch (NumberFormatException ex) {
            limpiarSeleccion();
            lblCantidadResultados.setText("Resultados: 0");
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            limpiarSeleccion();
            lblCantidadResultados.setText("Resultados: 0");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorTexto() {
        try {
            ArrayList<Pelicula> resultados =
                    catalogo.buscarCoincidencias(txtFiltroTexto.getText());
            modeloResultados.clear();
            for (Pelicula pelicula : resultados) {
                modeloResultados.addElement(pelicula);
            }
            lblCantidadResultados.setText("Resultados: " + resultados.size());

            if (modeloResultados.isEmpty()) {
                limpiarSeleccion();
            } else {
                lstResultados.setSelectedIndex(0);
            }
        } catch (IllegalArgumentException ex) {
            modeloResultados.clear();
            limpiarSeleccion();
            lblCantidadResultados.setText("Resultados: 0");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccion(Pelicula pelicula) {
        seleccionActual = pelicula;
        if (pelicula == null) {
            limpiarCamposEdicion();
            return;
        }
        txtTituloEdit.setText(pelicula.getTitulo());
        txtDirectorEdit.setText(pelicula.getDirector());
        cmbGeneroEdit.setSelectedItem(pelicula.getGenero());
        spnAnioEdit.setValue(pelicula.getAnio());
    }

    private void guardarCambios() {
        if (seleccionActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una película antes de guardar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int codigo = seleccionActual.getCodigo();
            catalogo.editarPelicula(codigo, txtTituloEdit.getText(),
                    txtDirectorEdit.getText(), (String) cmbGeneroEdit.getSelectedItem(),
                    (Integer) spnAnioEdit.getValue());

            actualizarResultadosDespuesDeModificar();
            actualizarRanking();
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.",
                    "Edición", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darMeGusta() {
        if (seleccionActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una película primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            catalogo.darMeGusta(seleccionActual.getCodigo());
            Pelicula actualizada = catalogo.getPeliculas()
                    .get(catalogo.buscarIndicePorCodigo(seleccionActual.getCodigo()));
            seleccionActual = actualizada;
            actualizarResultadosDespuesDeModificar();
            actualizarRanking();
            lstResultados.repaint();
        } catch (IllegalArgumentException ex) {
            limpiarSeleccion();
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarResultadosDespuesDeModificar() {
        String texto = txtFiltroTexto.getText().trim();
        if (!texto.isEmpty()) {
            buscarPorTexto();
            return;
        }

        int indice = catalogo.buscarIndicePorCodigo(seleccionActual.getCodigo());
        if (indice == -1) {
            limpiarSeleccion();
            return;
        }
        Pelicula actualizada = catalogo.getPeliculas().get(indice);
        seleccionActual = actualizada;

        int posicion = modeloResultados.indexOf(actualizada);
        if (posicion >= 0) {
            modeloResultados.set(posicion, actualizada);
            lstResultados.setSelectedIndex(posicion);
        } else {
            modeloResultados.clear();
            modeloResultados.addElement(actualizada);
            lstResultados.setSelectedIndex(0);
            lblCantidadResultados.setText("Resultados: 1");
        }
        cargarSeleccion(actualizada);
    }

    private void limpiarRegistro() {
        txtCodigoReg.setText("");
        txtTituloReg.setText("");
        txtDirectorReg.setText("");
        cmbGeneroReg.setSelectedIndex(0);
        spnAnioReg.setValue(CatalogoPeliculas.anioMaximo());
    }

    private void limpiarSeleccion() {
        lstResultados.clearSelection();
        modeloResultados.clear();
        seleccionActual = null;
        limpiarCamposEdicion();
    }

    private void limpiarCamposEdicion() {
        txtTituloEdit.setText("");
        txtDirectorEdit.setText("");
        cmbGeneroEdit.setSelectedIndex(0);
        spnAnioEdit.setValue(CatalogoPeliculas.anioMaximo());
    }
}
