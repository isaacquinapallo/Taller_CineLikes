import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;

public class Ventana {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txtCodigoReg;
    private JTextField txtTituloReg;
    private JTextField txtDirectorReg;
    private JComboBox cmbGeneroReg;
    private JSpinner spnAnioReg;
    private JButton btnRegistrar;
    private JLabel lblTotalPeliculas;
    private JTextField txtFiltroCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtFiltroTexto;
    private JButton btnBuscarTexto;
    private JLabel lblCantidadResultados;
    private JList lstResultados;
    private JPanel panelRanking;
    private JLabel lblTituloRanking;
    private JList<Pelicula> listaRanking;
    private JLabel lblCantidadRanking;
    private JButton btnActualizarRanking;
    private JEditorPane editorPane1;


    private final DefaultListModel<Pelicula> modeloRanking = new DefaultListModel<>();
    private CatalogoPeliculas catalogoRanking;


    private void configurarRanking(CatalogoPeliculas catalogo) {
        this.catalogoRanking = catalogo;

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


        btnActualizarRanking.addActionListener(e -> actualizarRanking());


        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedComponent() == panelRanking) {
                actualizarRanking();
            }
        });

        actualizarRanking();
    }


    private void actualizarRanking() {
        ArrayList<Pelicula> ranking = catalogoRanking.obtenerRanking();
        modeloRanking.clear();
        for (int i = 0; i < ranking.size(); i++) {
            modeloRanking.addElement(ranking.get(i));
        }
        lblCantidadRanking.setText("Películas en el ranking: " + ranking.size());
    }
}
