package gestioninventariotienda;

import Modelo.Conexion;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Compras extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public List<CatalogoProductos.Producto> carrito = new ArrayList<>();
    
    public Compras() {
        initComponents();
    }
    
    public void LlenarCampos(int numeroCompra, float totalCarrito, List<CatalogoProductos.Producto> carrito) {
        txtNumeroCompra.setText(String.valueOf(numeroCompra));
        txtTotalCarrito.setText(String.valueOf(totalCarrito));
        
        this.carrito = carrito;
        
        DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
        df.setRowCount(0);

        DecimalFormat dc = new DecimalFormat("#.00");

        for (CatalogoProductos.Producto producto : carrito) {
            Vector v2 = new Vector();

            v2.add(producto.getCodigo());
            v2.add(producto.getNombre());
            v2.add(producto.getCantidad());
            v2.add(dc.format(producto.getPrecio()));

            float total = producto.getCantidad() * producto.getPrecio();
            v2.add(dc.format(total));

            df.addRow(v2);
        }
    }
    
    private void reciboCompra(int numeroCompra){
    Date date = new Date();
        try{
            FileOutputStream archivo;
            File file = new File("Factura de Compra No."+numeroCompra+" "+new SimpleDateFormat("yyyy-MM-dd").format(date)+".pdf");
            archivo = new FileOutputStream(file);
            Document documento = new Document();
            PdfWriter.getInstance(documento, archivo);
            documento.open();
            Paragraph titulo = new Paragraph("Factura", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK));
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            Image img = Image.getInstance("src\\Img\\logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor. WHITE);
            Font magenta = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor. MAGENTA);
            fecha.add(Chunk.NEWLINE);
            fecha.add("Factura Compra: "+numeroCompra+"\nFecha: "+ new SimpleDateFormat("yyyy-MM-dd").format(date)+"\n\n");

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float[]{20f, 10f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            Encabezado.addCell(img);
            Encabezado.addCell("");
            Encabezado.addCell("Gestion de Inventarios para Tiendas\nNit: 84054107\nDirección: Centro Chimaltenango Zona 1 12-1\nDepartamento: Chimaltenango\nTeléfono: 4132-4585\nCorreo Electrónico: yatc.lester@gmail.com");
            Encabezado.addCell(fecha);
            documento.add(Encabezado);
            documento.add(new Paragraph("\n\n"));

            PdfPTable tablaCli = new PdfPTable(4);
            tablaCli.setWidthPercentage(100);
            tablaCli.getDefaultCell().setBorder(0);
            float[] ColumnaCli = new float[]{40f, 40f, 40f, 40f};
            tablaCli.setWidths(ColumnaCli);
            tablaCli.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cl1 = new PdfPCell(new Phrase("Numero de Compra", magenta));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre Proveedor", magenta));
            PdfPCell cl3 = new PdfPCell(new Phrase("Metodo de Pago", magenta));
            PdfPCell cl4 = new PdfPCell(new Phrase("Total de Compra", magenta));
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            tablaCli.addCell(cl1);
            tablaCli.addCell(cl2);
            tablaCli.addCell(cl3);
            tablaCli.addCell(cl4);
            tablaCli.addCell(txtNumeroCompra.getText());
            tablaCli.addCell(txtNombreProveedor.getText());
            tablaCli.addCell(cbMetodoPago.getSelectedItem().toString());
            
            String totalCarritoStr = txtTotalCarrito.getText();
            float totalCarrito = Float.parseFloat(totalCarritoStr);
            DecimalFormat formato = new DecimalFormat("#,###,##0.00");
            String totalCarritoFormateado = formato.format(totalCarrito);
            tablaCli.addCell(totalCarritoFormateado);
            
            documento.add(tablaCli);
            documento.add(new Paragraph("\n\n"));

            PdfPTable tablaProd = new PdfPTable(5);
            tablaProd.setWidthPercentage(100);
            tablaProd.getDefaultCell().setBorder(0);
            float[] ColumnaProd = new float[]{10f, 30f, 20f, 20f, 20f};
            tablaProd.setWidths(ColumnaProd);
            tablaProd.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell prod1 = new PdfPCell(new Phrase("Codigo", negrita));
            PdfPCell prod2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell prod3 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell prod4 = new PdfPCell(new Phrase("Precio", negrita));
            PdfPCell prod5 = new PdfPCell(new Phrase("Sub Total", negrita));
            prod1.setBorder(0);
            prod2.setBorder(0);
            prod3.setBorder(0);
            prod4.setBorder(0);
            prod5.setBorder(0);
            prod1.setBackgroundColor(BaseColor.DARK_GRAY);
            prod2.setBackgroundColor(BaseColor.DARK_GRAY);
            prod3.setBackgroundColor(BaseColor.DARK_GRAY);
            prod4.setBackgroundColor(BaseColor.DARK_GRAY);
            prod5.setBackgroundColor(BaseColor.DARK_GRAY);
            tablaProd.addCell(prod1);
            tablaProd.addCell(prod2);
            tablaProd.addCell(prod3);
            tablaProd.addCell(prod4);
            tablaProd.addCell(prod5);

            float totalVentas = 0.0f;

            DecimalFormat dc = new DecimalFormat("#,###,##0.00");

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                String codigo = jTable1.getValueAt(i, 0).toString();
                String producto = jTable1.getValueAt(i, 1).toString();
                String cantidad = jTable1.getValueAt(i, 2).toString();

                float precio = Float.parseFloat(jTable1.getValueAt(i, 3).toString());
                String precioFormateado = dc.format(precio);

                float total = Float.parseFloat(jTable1.getValueAt(i, 4).toString());
                String totalFormateado = dc.format(total);

                totalVentas += total;

                tablaProd.addCell(codigo);
                tablaProd.addCell(producto);
                tablaProd.addCell(cantidad);
                tablaProd.addCell("Q " + precioFormateado);
                tablaProd.addCell("Q " + totalFormateado);
            }

            documento.add(tablaProd);
            
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a Pagar: Q " + dc.format(totalVentas));
            info.setAlignment(Element.ALIGN_RIGHT);
            documento.add(info);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("Gracias por su Compra");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            documento.add(mensaje);
            documento.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e){
            System.out.println(e.toString());
        }
    }
    
    public void guardarProductos(String tipoMovimiento, int numeroMovimiento, List<CatalogoProductos.Producto> carrito) {
        try (Connection con = new Conexion().Connect()) {
            String insertQuery = "INSERT INTO movimiento_inventario (tipo_movimiento, numero_movimiento, codigo_producto, nombre_producto, cantidad, precio, fecha_movimiento) VALUES (?,?,?,?,?,?,NOW())";
            try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
                for (CatalogoProductos.Producto producto : carrito) {
                    pst.setString(1, tipoMovimiento);
                    pst.setInt(2, numeroMovimiento);
                    pst.setInt(3, producto.getCodigo());
                    pst.setString(4, producto.getNombre());
                    pst.setInt(5, producto.getCantidad());
                    pst.setFloat(6, producto.getPrecio());
                    pst.executeUpdate();
                }
            }
            
            for (CatalogoProductos.Producto producto : carrito) {
                int codigoProducto = producto.getCodigo();
                int cantiadProducto = producto.getCantidad();
                actualizarStock(con, codigoProducto, cantiadProducto);
            }
        } catch (SQLException ex) { 
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarStock(Connection con, int codigoProducto, int cantidadProducto) {
        int cantidadProductoBD = 0;
        int cantidadTotal = 0;
        try {
            pst = con.prepareStatement("SELECT cantidad_existente FROM inventario WHERE codigo_producto=?");
            pst.setInt(1, codigoProducto);
            rs = pst.executeQuery();

            if (rs.next()) {
                cantidadProductoBD = rs.getInt(1);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro el producto");
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            pst = con.prepareStatement("UPDATE inventario SET cantidad_existente=? WHERE codigo_producto=?");
            cantidadTotal = cantidadProductoBD + cantidadProducto;
            pst.setInt(1, cantidadTotal);
            pst.setInt(2, codigoProducto);
            if (pst.executeUpdate() == 1) {
            }

        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNumeroCompra = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTotalCarrito = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbMetodoPago = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnComprar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        txtNombreProveedor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        txtNumeroCompra.setEditable(false);

        jLabel2.setText("Numero Compra");

        jLabel6.setText("Total Carrito");

        txtTotalCarrito.setEditable(false);

        jLabel4.setText("Nombre Proveedor");

        jLabel3.setText("Metodo Pago");

        cbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tarjeta credito/debito", "efectivo" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("REGISTRAR COMPRA");

        btnComprar.setText("Guardar");
        btnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarActionPerformed(evt);
            }
        });

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(btnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Producto", "Nombre", "Cantidad", "Precio", "Sub Total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(247, 247, 247))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNumeroCompra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalCarrito, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(cbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        JFrame frame = new JFrame("Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu menuPrincipal = new Menu();
        frame.getContentPane().add(menuPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).dispose();
        }
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarActionPerformed
        try {
            int numeroCompra = Integer.parseInt(txtNumeroCompra.getText());
            String nombreProveedor = txtNombreProveedor.getText();
            String metodoPago = cbMetodoPago.getSelectedItem().toString();
            Float totalCarrito = Float.valueOf(txtTotalCarrito.getText());
            
            if (!nombreProveedor.isEmpty()) {
                Conexion conexion1 = new Conexion();
                con = conexion1.Connect();
                guardarProductos("Compra", numeroCompra, carrito);
                pst = con.prepareStatement("INSERT INTO compras (numero_compra, nombre_proveedor, metodo_pago, total, fecha_compra) VALUES (?,?,?,?,NOW())");
                pst.setInt(1, numeroCompra);
                pst.setString(2, nombreProveedor);
                pst.setString(3, metodoPago);
                pst.setFloat(4, totalCarrito);

                if (pst.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(this, "Compra Agregada Correctamente");
                    reciboCompra(numeroCompra);
                    txtNumeroCompra.setText("");
                    txtNombreProveedor.setText("");
                    txtTotalCarrito.setText("");

                    JFrame frame = new JFrame("Catalogo de Productos");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Menu menuPrincipal = new Menu();
                    frame.getContentPane().add(menuPrincipal);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al Agregar la Compra");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del proveedor.", "Alerta", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnComprarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComprar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cbMetodoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNumeroCompra;
    private javax.swing.JTextField txtTotalCarrito;
    // End of variables declaration//GEN-END:variables
}
