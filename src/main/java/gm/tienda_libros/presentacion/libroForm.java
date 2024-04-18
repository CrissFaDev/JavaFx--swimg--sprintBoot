package gm.tienda_libros.presentacion;

import gm.tienda_libros.modelo.Libro;
import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class libroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public libroForm(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> {
                agregarLibro();
        });
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
         modificarButton.addActionListener(e-> modificarLibro());
        eliminarButton.addActionListener(e -> eliminarLibro());
    }

    private void cargarLibroSeleccionado(){
        //los indices de las columna de nuestra tabla inician en 0
        var renglon = tablaLibros.getSelectedRow();
        if (renglon != -1){
            //Regresa -1 sino se selecciono ningun registro
            String idlibro =
                    tablaLibros.getModel().getValueAt(renglon, 0).toString();
            //campo no visible idtexto
            idTexto.setText(idlibro);

            String nombreLibro =
                    tablaLibros.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(nombreLibro);

            String autor =
                    tablaLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);

            String precio =
                    tablaLibros.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(precio);

            String existencias =
                    tablaLibros.getModel().getValueAt(renglon, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void modificarLibro(){
        if (this.idTexto.getText().equals("")) {
            mostrarMensaje("debe seleccionar un registro");
        }else  {
            //verificamos que el nombre del libro no sea nulo
            if (libroTexto.getText().equals("")){
                mostrarMensaje("proporciona el nombre del libro....");
                libroTexto.requestFocusInWindow();
                return;
            }

            //lleneamos el objeto libro a actualizar
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var existencia = Integer.parseInt(existenciasTexto.getText());
            var libro = new Libro(idLibro, nombreLibro, autor, precio, existencia);

            libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro...");
            limpiarFormulario();
            listarLibros();
        }
    }

    private void eliminarLibro(){
    var renglon = tablaLibros.getSelectedRow();
    if (renglon != -1){
        String idLibro =
                tablaLibros.getModel().getValueAt(renglon , 0).toString();
        var libro = new Libro();
        libro.setIdLibro(Integer.parseInt(idLibro));
        libroServicio.eliminarLibro(libro);
        mostrarMensaje("libro " + idLibro + " eliminado");
        limpiarFormulario();
        listarLibros();
        }
    else  {
        mostrarMensaje("no se ha seleccionado ningun libro a eliminar");
    }
    }
    private void iniciarForma() {
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width = getWidth() / 2);
        int y = (tamanioPantalla.height = getHeight() / 2);
        setLocation(x, y);
    }

    private void agregarLibro(){
        //leer los valores de formulario
        if (libroTexto.getText().equals("")) {

            mostrarMensaje("proporcionar el nombre del libro");
            libroTexto.requestFocusInWindow();
            return;
        }

        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var existencia = Integer.parseInt(existenciasTexto.getText());
        //Crear objeto libro
        var libro = new Libro(null, nombreLibro , autor, precio, existencia);
        // libro.setNombreLibro(nombreLibro);
        //libro.setAutor(autor);
        //libro.setPrecio(precio);
        //libro.setExistencias(existencia);

        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("se agrego el libro");
        limpiarFormulario();
        listarLibros();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void createUIComponents() {
        //creamos el elemento idTextoOCULTO
        idTexto = new JTextField("");
        idTexto.setVisible(false);


        // TODO: place custom component creation code here
        this.tablaModeloLibros = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Existencia"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);

        //instanciar el objeto JTABLE
        this.tablaLibros = new JTable(tablaModeloLibros);
        //evitar que se seleccionen varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();

    }

    private void listarLibros() {
        //limpiar la tabla
        tablaModeloLibros.setRowCount(0);
        //obtener los libros
        var libros = libroServicio.ListarLibros();
        libros.forEach((libro) -> {
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };

            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
