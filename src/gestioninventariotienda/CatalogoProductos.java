 package gestioninventariotienda;

import Modelo.Conexion;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

public class CatalogoProductos extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public float totalCarritoTemporal;
    public List<Producto> carritoTemporal = new ArrayList<>();
    
    public CatalogoProductos(List<Producto> carritoInicial) {
        initComponents();
        this.carritoTemporal = carritoInicial;
        Inventario();
    }
    
    public List<Producto> getCarritoTemporal() {
        return carritoTemporal;
    }
    
    public void LlenadoCampos(String tipoOperacion, int numeroPedido){
        txtTipoOperacion.setText(tipoOperacion);
        txtNumeroOperacion.setText(String.valueOf(numeroPedido));
    }
    
    private void Inventario(){
        try{
            Conexion conexion1 = new Conexion();
            con = conexion1.Connect();
            pst = con.prepareStatement("SELECT * FROM inventario");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            rss.getColumnCount();
            
            DefaultTableModel df = (DefaultTableModel)jTable1.getModel();
            df.setRowCount(0);
            
            while (rs.next()){        
                Vector v2 = new Vector();
                for (int a = 0; a < 10; a++){
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add(rs.getString("cantidad_existente"));
                    v2.add(rs.getString("precio_unitario"));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void CarritoComprasVentas(String tipoMovimiento, List<Producto> carrito) {
        DefaultTableModel df = (DefaultTableModel) jTable2.getModel();
        df.setRowCount(0);
        
        for (Producto producto : carritoTemporal) {
            Vector v2 = new Vector();
            v2.add(producto.getCodigo());
            v2.add(producto.getNombre());
            v2.add(producto.getCantidad());
            v2.add(producto.getPrecio());
            df.addRow(v2);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        txtTotalCarrito = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnAgregarCarrito = new javax.swing.JButton();
        txtNumeroOperacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrecioProducto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTipoOperacion = new javax.swing.JTextField();
        btnEliminarCarrito = new javax.swing.JButton();
        btnActualizarCarrito = new javax.swing.JButton();
        btnProcesarOrden = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("CATALOGO DE PRODUCTOS");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Producto", "Nombre Producto", "Cantidad", "Precio"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);

        jLabel2.setText("Codigo Producto");

        txtCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProductoActionPerformed(evt);
            }
        });

        txtTotalCarrito.setEditable(false);

        jLabel3.setText("Total Carrito");

        jLabel5.setText("Tipo Operacion");

        jLabel6.setText("Cantidad");

        txtNombreProducto.setEditable(false);

        jLabel7.setText("Nombre Producto");

        btnAgregarCarrito.setText("Agregar al Carrito");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });

        txtNumeroOperacion.setEditable(false);

        jLabel8.setText("Numero Orden");

        txtPrecioProducto.setEditable(false);

        jLabel9.setText("Precio");

        txtTipoOperacion.setEditable(false);

        btnEliminarCarrito.setText("Eliminar del Carrito");
        btnEliminarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCarritoActionPerformed(evt);
            }
        });

        btnActualizarCarrito.setText("Actualizar Carrito");
        btnActualizarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarCarritoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(txtCodigoProducto)
                            .addComponent(txtTotalCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel8)
                                .addComponent(txtNumeroOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtTipoOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(btnAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarCarrito)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTotalCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel5)
                            .addGap(44, 44, 44)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTipoOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        btnProcesarOrden.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnProcesarOrden.setText("Procesar Orden");
        btnProcesarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarOrdenActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo Producto", "Nombre Producto", "Cantidad", "Precio"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Carrito de compras");

        btnVolver.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 911, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(28, 28, 28))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(288, 288, 288)
                        .addComponent(btnProcesarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProcesarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcesarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarOrdenActionPerformed
        String tipoMovimiento = txtTipoOperacion.getText();
        int numeroOrden = Integer.parseInt(txtNumeroOperacion.getText());
        float totalCarrito = Float.parseFloat(txtTotalCarrito.getText());
        List<Producto> carritoActualizado = getCarritoTemporal();
        
        if (tipoMovimiento.equals("Compra")){
            JFrame frame = new JFrame("Procesar Compras");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Compras compra = new Compras();
            frame.getContentPane().add(compra);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            compra.LlenarCampos(numeroOrden, totalCarrito, carritoActualizado);
            
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).dispose();
            }
        } else{
            JFrame frame = new JFrame("Procesar Ventas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Ventas venta = new Ventas();
            frame.getContentPane().add(venta);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            venta.LlenarCampos(numeroOrden, totalCarrito, carritoActualizado);
            
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).dispose();
            }
        }
    }//GEN-LAST:event_btnProcesarOrdenActionPerformed

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        try {
            String tipoMovimiento = txtTipoOperacion.getText();
            int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
            String nombreProducto = txtNombreProducto.getText();
            int cantidadProducto = Integer.parseInt(txtCantidadProducto.getText());
            Float precioProducto = Float.valueOf(txtPrecioProducto.getText());

            float totalCarritoTemporal = (float) carritoTemporal.stream()
                    .mapToDouble(p -> p.getCantidad() * p.getPrecio())
                    .sum();

            float totalNuevoProducto = cantidadProducto * precioProducto;

            totalCarritoTemporal += totalNuevoProducto;

            Producto producto = new Producto(codigoProducto, nombreProducto, cantidadProducto, precioProducto);
            carritoTemporal.add(producto);

            txtCodigoProducto.setText("");
            txtNombreProducto.setText("");
            txtCantidadProducto.setText("");
            txtPrecioProducto.setText("");

            DecimalFormat df = new DecimalFormat("#.00");
            String totalFormateado = df.format(totalCarritoTemporal);
            txtTotalCarrito.setText(totalFormateado);

            Inventario();
            CarritoComprasVentas(tipoMovimiento, carritoTemporal);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al Agregar al Carrito");
        }
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

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

    private void txtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProductoActionPerformed
        try{
            Conexion conexion1 = new Conexion();
            con = conexion1.Connect();
            int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
            pst = con.prepareStatement("SELECT * FROM inventario WHERE codigo_producto=?");
            pst.setInt(1, codigoProducto);
            rs=pst.executeQuery();

            if (rs.next()==true){
                txtCodigoProducto.setText("");
                txtNombreProducto.setText("");
                txtCantidadProducto.setText("");
                txtPrecioProducto.setText("");
                txtCodigoProducto.setText(rs.getString(2));
                txtNombreProducto.setText(rs.getString(3));
                txtPrecioProducto.setText(rs.getString(6));
            } else{
                JOptionPane.showMessageDialog(this, "No se encontro el producto");
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtCodigoProductoActionPerformed

    private void btnEliminarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCarritoActionPerformed
        String tipoMovimiento = txtTipoOperacion.getText();
        int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
        String nombreProducto = txtNombreProducto.getText();
        List<Producto> carritoTemporalNuevo = new ArrayList<>();

        for (Producto producto : carritoTemporal) {
            if (!(producto.getCodigo() == codigoProducto && producto.getNombre().equals(nombreProducto))) {
                carritoTemporalNuevo.add(producto);
            }
        }

        carritoTemporal = carritoTemporalNuevo;
        
        totalCarritoTemporal = 0.0f;
        for (Producto producto : carritoTemporal) {
            totalCarritoTemporal += (producto.getCantidad() * producto.getPrecio());
        }

        DecimalFormat df = new DecimalFormat("#.00");
        txtTotalCarrito.setText(df.format(totalCarritoTemporal));

        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");

        CarritoComprasVentas(tipoMovimiento, carritoTemporal);
    }//GEN-LAST:event_btnEliminarCarritoActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int filaSeleccinada = jTable1.rowAtPoint(evt.getPoint());
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        txtCodigoProducto.setText(jTable1.getValueAt(filaSeleccinada, 0).toString());
        txtNombreProducto.setText(jTable1.getValueAt(filaSeleccinada, 1).toString());
        txtPrecioProducto.setText(jTable1.getValueAt(filaSeleccinada, 3).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int filaSeleccinada = jTable2.rowAtPoint(evt.getPoint());
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        txtCodigoProducto.setText(jTable2.getValueAt(filaSeleccinada, 0).toString());
        txtNombreProducto.setText(jTable2.getValueAt(filaSeleccinada, 1).toString());
        txtPrecioProducto.setText(jTable2.getValueAt(filaSeleccinada, 3).toString());
    }//GEN-LAST:event_jTable2MouseClicked

    private void btnActualizarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarCarritoActionPerformed
        String tipoMovimiento = txtTipoOperacion.getText();
        int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
        String nombreProducto = txtNombreProducto.getText();
        int nuevaCantidad = Integer.parseInt(txtCantidadProducto.getText());
        float precioProducto = Float.parseFloat(txtPrecioProducto.getText());
        
        for (Producto producto : carritoTemporal) {
            if (producto.getCodigo() == codigoProducto && producto.getNombre().equals(nombreProducto)) {
                producto.setCantidad(nuevaCantidad);
                break;
            }
        }
        
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        
        totalCarritoTemporal = 0.0f;
        for (Producto producto : carritoTemporal) {
            totalCarritoTemporal += (producto.getCantidad() * producto.getPrecio());
        }
        DecimalFormat df = new DecimalFormat("#.00");
        txtTotalCarrito.setText(df.format(totalCarritoTemporal));
        
        CarritoComprasVentas(tipoMovimiento, carritoTemporal);
    }//GEN-LAST:event_btnActualizarCarritoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarCarrito;
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnEliminarCarrito;
    private javax.swing.JButton btnProcesarOrden;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNumeroOperacion;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtTipoOperacion;
    private javax.swing.JTextField txtTotalCarrito;
    // End of variables declaration//GEN-END:variables

    public class Producto {
        private int codigo;
        private String nombre;
        private int cantidad;
        private float precio;

        public Producto(int codigo, String nombre, int cantidad, float precio) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public float getPrecio() {
            return precio;
        }

        public void setCantidad(int nuevaCantidad) {
            this.cantidad = nuevaCantidad;
        }
    }
}
