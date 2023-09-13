package gestioninventariotienda;

import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CatalogoProductos extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public float totalCarritoTemporal;
    
    public CatalogoProductos() {
        initComponents();
        Connect();
        Inventario();
    }

    public void Connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/db_smart_shop_inventory_manager", "root", "rootpass");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Inventario(){
        try{
            int q;
            pst = con.prepareStatement("SELECT * FROM inventario");
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
                    v2.add(rs.getString("cantidad_existente"));
                    v2.add(rs.getString("precio_unitario"));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CarritoComprasVentas(String tipoMovimiento,int numeroMovimiento){
        try{
            int q;
            pst = con.prepareStatement("SELECT * FROM movimiento_inventario WHERE tipo_movimiento = ? AND numero_movimiento=?");
            pst.setString(1, tipoMovimiento);
            pst.setInt(2,numeroMovimiento);
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();
            
            DefaultTableModel df = (DefaultTableModel)jTable2.getModel();
            df.setRowCount(0);
            
            while (rs.next()){        
                Vector v2 = new Vector();
                for (int a = 0; a < 10; a++){
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add(rs.getString("cantidad"));
                    v2.add(rs.getString("precio"));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarStock(String tipoMovimiento, int codigoProducto, int cantidadProducto){
        int cantidadProductoBD = 0;
        try{
            pst = con.prepareStatement("SELECT cantidad_existente FROM inventario WHERE codigo_producto=?");
            pst.setInt(1, codigoProducto);
            rs=pst.executeQuery();

            if (rs.next()==true){
                cantidadProductoBD = Integer.parseInt(rs.getString(1));
            } else{
                JOptionPane.showMessageDialog(this, "No se encontro el usuario");
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try{
            pst = con.prepareStatement("UPDATE inventario SET cantidad_existente=? WHERE codigo_producto=?");
            if (tipoMovimiento.equals("Venta")){
                cantidadProducto = cantidadProductoBD - cantidadProducto;
            } else if (tipoMovimiento.equals("Compra")){
                cantidadProducto = cantidadProductoBD + cantidadProducto;
            }
            
            pst.setInt(1, cantidadProducto);
            pst.setInt(2, codigoProducto);
            pst.executeUpdate();
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
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
        btnBuscarProducto = new javax.swing.JButton();
        txtTotalCarrito = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbTipoOperacion = new javax.swing.JComboBox<>();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnAgregarCarrito = new javax.swing.JButton();
        txtNumeroOperacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrecioProducto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnProcesarOrden = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("CATALOGO PRODUCTOS");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Producto", "Nombre Producto", "Cantidad", "Precio"
            }
        ));
        jScrollPane1.setViewportView(jTable2);

        jLabel2.setText("Codigo Producto");

        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        txtTotalCarrito.setEditable(false);

        jLabel3.setText("Total Carrito");

        jLabel5.setText("Tipo Operacion");

        cbTipoOperacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Compra", "Venta" }));

        jLabel6.setText("Cantidad");

        txtNombreProducto.setEditable(false);

        jLabel7.setText("Nombre Producto");

        btnAgregarCarrito.setText("Agregar a Carrito");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });

        jLabel8.setText("Numero Orden");

        txtPrecioProducto.setEditable(false);

        jLabel9.setText("Precio");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtCodigoProducto)
                    .addComponent(txtTotalCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
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
                            .addComponent(cbTipoOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
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
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbTipoOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
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
        jScrollPane2.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Carrito de Compras");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addGap(269, 269, 269)
                        .addComponent(btnProcesarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProcesarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
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

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        try{
            int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
            pst = con.prepareStatement("SELECT * FROM inventario WHERE codigo_producto=?");
            pst.setInt(1, codigoProducto);
            rs=pst.executeQuery();

            if (rs.next()==true){
                txtCodigoProducto.setText(rs.getString(2));
                txtNombreProducto.setText(rs.getString(3));
                txtCantidadProducto.setText(rs.getString(4));
                txtPrecioProducto.setText(rs.getString(6));
            } else{
                JOptionPane.showMessageDialog(this, "No se encontro el producto");
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnProcesarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarOrdenActionPerformed
        try {
            pst = con.prepareStatement("SELECT tipo_movimiento FROM movimiento_inventario ORDER BY id DESC LIMIT 1");
            rs = pst.executeQuery();
            
            if (rs.next()) {
                String tipoMovimiento = rs.getString("tipo_movimiento");
                if (tipoMovimiento.equals("Compra")){
                    JFrame frame = new JFrame("Procesar Compras");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Compras compra = new Compras();
                    frame.getContentPane().add(compra);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

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

                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron órdenes.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProcesarOrdenActionPerformed

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        try {
            String tipoMovimiento = cbTipoOperacion.getSelectedItem().toString();
            int numeroMovimiento = Integer.parseInt(txtNumeroOperacion.getText());
            int codigoProducto = Integer.parseInt(txtCodigoProducto.getText());
            String nombreProducto = txtNombreProducto.getText();
            int cantidadProducto = Integer.parseInt(txtCantidadProducto.getText());
            Float precioProducto = Float.valueOf(txtPrecioProducto.getText());
            
            pst = con.prepareStatement("INSERT INTO movimiento_inventario (tipo_movimiento, numero_movimiento, codigo_producto, nombre_producto, cantidad, precio, fecha_movimiento) VALUES (?,?,?,?,?,?,NOW())");

            pst.setString(1, tipoMovimiento);
            pst.setInt(2, numeroMovimiento);
            pst.setInt(3, codigoProducto);
            pst.setString(4, nombreProducto);
            pst.setInt(5, cantidadProducto);
            pst.setFloat(6, precioProducto);

            if (pst.executeUpdate() == 1) {
                actualizarStock(tipoMovimiento, codigoProducto, cantidadProducto);
                JOptionPane.showMessageDialog(this, "Agregado al Carrito Correctamente");
                
                txtCodigoProducto.setText("");
                txtNombreProducto.setText("");
                txtCantidadProducto.setText("");
                txtPrecioProducto.setText("");
                totalCarritoTemporal += (cantidadProducto * precioProducto);
                txtTotalCarrito.setText(Float.toString(totalCarritoTemporal));
                Inventario();
                CarritoComprasVentas(tipoMovimiento, numeroMovimiento);
            } else {
                JOptionPane.showMessageDialog(this, "Error al Agregar al Carrito");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnProcesarOrden;
    private javax.swing.JComboBox<String> cbTipoOperacion;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JTextField txtTotalCarrito;
    // End of variables declaration//GEN-END:variables
}
