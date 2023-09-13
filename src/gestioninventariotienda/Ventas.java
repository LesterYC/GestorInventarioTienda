package gestioninventariotienda;

import java.text.DecimalFormat;
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
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Ventas extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public Ventas() {
        initComponents();
        Connect();
        cargarCamposNoEditar();
    }
    
    public void Connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/db_smart_shop_inventory_manager", "root", "rootpass");
        } catch (ClassNotFoundException | SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarCamposNoEditar() {
        try {
            pst = con.prepareStatement("SELECT numero_movimiento FROM movimiento_inventario WHERE tipo_movimiento = ? ORDER BY id DESC LIMIT 1");
            pst.setString(1, "Venta");
            rs = pst.executeQuery();

            if (rs.next()) {
                int numeroMovimiento = rs.getInt("numero_movimiento");
                float totalTemporal = 0;
                
                PreparedStatement pst2 = con.prepareStatement("SELECT cantidad, precio FROM movimiento_inventario WHERE numero_movimiento = ? AND tipo_movimiento = ?");
                pst2.setInt(1, numeroMovimiento);
                pst2.setString(2, "Venta");
                ResultSet rs2 = pst2.executeQuery();

                while (rs2.next()) {
                    int cantidad = rs2.getInt("cantidad");
                    float precio = rs2.getFloat("precio");
                    float subtotal = cantidad * precio;
                    totalTemporal += subtotal;
                }

                rs2.close();
                pst2.close();

                txtTotalCarrito.setText(Float.toString(totalTemporal));
                txtNumeroVenta.setText(Integer.toString(numeroMovimiento));
                MostrarInventarioCompra(numeroMovimiento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void MostrarInventarioCompra(int numeroMovimiento){
        try{
            int q;
            pst = con.prepareStatement("SELECT * FROM movimiento_inventario WHERE numero_movimiento = ? AND tipo_movimiento = ?");
            pst.setInt(1, numeroMovimiento);
            pst.setString(1, "Venta");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();
            
            DefaultTableModel df = (DefaultTableModel)jTable1.getModel();
            df.setRowCount(0);
            
            while (rs.next()){
                Vector v2 = new Vector();
                for (int a = 0; a < 10; a++){
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add(rs.getString("cantidad"));
                    
                    float precio = Float.parseFloat(rs.getString("precio"));
                    DecimalFormat dc = new DecimalFormat("#.00");
                    String precioFormateado = dc.format(precio);

                    v2.add(precioFormateado);
                    
                    float cantidad = Float.parseFloat(rs.getString("cantidad"));
                    float total = cantidad * precio;
                    String totalFormateado = dc.format(total);

                    v2.add(totalFormateado);
                }
                df.addRow(v2);
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private int crearCliente(){
        int idCliente =0;
        try {
            String nombre = txtNombreCliente.getText();
            int telefono = Integer.parseInt(txtTelefonoCliente.getText());
            String direccion = txtDireccionCliente.getText();
            int nit = Integer.parseInt(txtNITCliente.getText());
            String tipoCliente = cbTipoCliente.getSelectedItem().toString();
            
            pst = con.prepareStatement("INSERT INTO clientes (nombre_cliente, telefono, direccion, nit, tipo_cliente) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombre);
            pst.setInt(2, telefono);
            pst.setString(3, direccion);
            pst.setInt(4, nit);
            pst.setString(5, tipoCliente);

            int result = pst.executeUpdate();

            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Cliente Registrado Exitosamente");
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idCliente = generatedKeys.getInt(1);
                } else {
                    System.err.println("No se pudo obtener el ID del cliente.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al Registrar al Cliente");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idCliente;
    }
    
    private void reciboVenta(int numeroVenta){
        Date date = new Date();
        try{
            FileOutputStream archivo;
            File file = new File("Factura de Venta No."+numeroVenta+" "+new SimpleDateFormat("yyyy-MM-dd").format(date)+".pdf");
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
            fecha.add("Factura Venta: "+numeroVenta+"\nFecha: "+ new SimpleDateFormat("yyyy-MM-dd").format(date)+"\n\n");

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
            PdfPCell cl1 = new PdfPCell(new Phrase("NIT", magenta));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", magenta));
            PdfPCell cl3 = new PdfPCell(new Phrase("Tipo Cliente", magenta));
            PdfPCell cl4 = new PdfPCell(new Phrase("Direccion", magenta));
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            tablaCli.addCell(cl1);
            tablaCli.addCell(cl2);
            tablaCli.addCell(cl3);
            tablaCli.addCell(cl4);
            tablaCli.addCell(txtNITCliente.getText());
            tablaCli.addCell(txtNombreCliente.getText());
            tablaCli.addCell(cbTipoCliente.getSelectedItem().toString());
            tablaCli.addCell(txtDireccionCliente.getText());
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDireccionCliente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNITCliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbTipoCliente = new javax.swing.JComboBox<>();
        txtTelefonoCliente = new javax.swing.JTextField();
        btnVender = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        txtNumeroVenta = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbMetodoPago = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtTotalCarrito = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("REGISTRAR VENTA");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo Producto", "Nombre", "Cantidad", "Precio", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setText("Nombre");

        jLabel5.setText("Telefono");

        jLabel7.setText("Direccion");

        jLabel8.setText("NIT");

        jLabel9.setText("Tipo Cliente");

        cbTipoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mayorista", "Minorista" }));

        btnVender.setText("Guardar");
        btnVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenderActionPerformed(evt);
            }
        });

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        txtNumeroVenta.setEditable(false);

        jLabel2.setText("Numero Venta");

        jLabel6.setText("Total Carrito");

        cbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tarjeta credito/debito", "Efectivo" }));

        jLabel3.setText("Metodo Pago");

        txtTotalCarrito.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVender, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(143, 143, 143))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNumeroVenta)
                                .addGap(35, 35, 35)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 121, Short.MAX_VALUE))
                            .addComponent(txtTotalCarrito)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(txtNITCliente))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTipoCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTelefonoCliente)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(cbMetodoPago, 0, 202, Short.MAX_VALUE)))
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(1, 1, 1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)
                        .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbTipoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNITCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVender, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(282, 282, 282)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenderActionPerformed
        try {
            int numeroVenta = Integer.parseInt(txtNumeroVenta.getText());
            String metodoPago = cbMetodoPago.getSelectedItem().toString();
            float totalCarrito = Float.parseFloat(txtTotalCarrito.getText());
            int idCliente = crearCliente();
            
            pst = con.prepareStatement("INSERT INTO ventas (numero_venta, id_cliente, metodo_pago, total, fecha_venta) VALUES (?,?,?,?,NOW())");

            pst.setInt(1, numeroVenta);
            pst.setInt(2, idCliente);
            pst.setString(3, metodoPago);
            pst.setFloat(4, totalCarrito);

            if (pst.executeUpdate() == 1) {
                reciboVenta(numeroVenta);
                JOptionPane.showMessageDialog(this, "Venta Registrada Exitosamente");
                txtNombreCliente.setText("");
                txtTelefonoCliente.setText("");
                txtDireccionCliente.setText("");
                txtNITCliente.setText("");
                txtNumeroVenta.setText("");
                txtTotalCarrito.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al Registrar la Venta");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnVenderActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        JFrame frame = new JFrame("Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu menuPrincipal = new Menu();
        frame.getContentPane().add(menuPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Cerrar la ventana actual
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).dispose();
        }
    }//GEN-LAST:event_btnVolverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVender;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cbMetodoPago;
    private javax.swing.JComboBox<String> cbTipoCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtNITCliente;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNumeroVenta;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTotalCarrito;
    // End of variables declaration//GEN-END:variables
}
