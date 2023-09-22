package gestioninventariotienda;

import Modelo.Conexion;
import java.awt.Window;
import java.sql.*;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MostrarInventario extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public MostrarInventario() {
        initComponents();
        Inventario();
    }
    
    public void Inventario(){
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
                for (int a = 0; a < 10; a++)
                {
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("fecha_vencimiento"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add((rs.getString("cantidad_existente").equals("0")) ? "Agotado" : rs.getString("cantidad_existente"));
                    v2.add(rs.getString("tipo_tarifa"));
                    v2.add(rs.getString("precio_unitario"));
                    v2.add(Float.valueOf(rs.getString("cantidad_existente")) * Float.valueOf(rs.getString("precio_unitario")));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cbOrden = new javax.swing.JComboBox<>();
        cbTipoTarifa = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnAplicarFiltros = new javax.swing.JButton();
        txtCalendario = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnActualizarProducto = new javax.swing.JButton();
        jCalendar1 = new com.toedter.calendar.JCalendar();

        setBackground(new java.awt.Color(0, 51, 102));

        jLabel1.setBackground(new java.awt.Color(204, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("INVENTARIO");

        jTable1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo Producto", "Fecha Vencimiento", "Nombre Producto", "Cantidad", "Tipo Tarifa", "Precio", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnRegresar.setBackground(new java.awt.Color(204, 0, 0));
        btnRegresar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("VOLVER");
        btnRegresar.setBorder(null);
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        cbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DESC" }));
        cbOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrdenActionPerformed(evt);
            }
        });

        cbTipoTarifa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Mayorista", "Minorista" }));
        cbTipoTarifa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoTarifaActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Filtros");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Fecha");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Precios");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tipo Tarifa");

        btnAplicarFiltros.setBackground(new java.awt.Color(204, 0, 0));
        btnAplicarFiltros.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnAplicarFiltros.setForeground(new java.awt.Color(255, 255, 255));
        btnAplicarFiltros.setText("Filtrar");
        btnAplicarFiltros.setBorder(null);
        btnAplicarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarFiltrosActionPerformed(evt);
            }
        });

        btnAgregarProducto.setBackground(new java.awt.Color(204, 0, 0));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminarProducto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.setBorder(null);
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnActualizarProducto.setBackground(new java.awt.Color(204, 0, 0));
        btnActualizarProducto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnActualizarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarProducto.setText("Actualizar");
        btnActualizarProducto.setBorder(null);
        btnActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProductoActionPerformed(evt);
            }
        });

        jCalendar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendar1PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCalendario))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbTipoTarifa, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAplicarFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(395, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(304, 304, 304))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbOrden, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(btnAplicarFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbTipoTarifa)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(401, 401, 401)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
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
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void cbOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdenActionPerformed
        
    }//GEN-LAST:event_cbOrdenActionPerformed

    private void cbTipoTarifaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoTarifaActionPerformed
        
    }//GEN-LAST:event_cbTipoTarifaActionPerformed

    private void jCalendar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendar1PropertyChange
        if (evt.getOldValue() != null){
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
            txtCalendario.setText(ff.format(jCalendar1.getCalendar().getTime()));
        }
    }//GEN-LAST:event_jCalendar1PropertyChange

    private void btnAplicarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarFiltrosActionPerformed
        try {
            int q;
            String sqlFechaVencimiento = "";
            Date fechaVencimiento = null;

            String fechaVencimientoStr = txtCalendario.getText();

            if (!fechaVencimientoStr.isEmpty()) {
                fechaVencimiento = Date.valueOf(fechaVencimientoStr);
                sqlFechaVencimiento = "fecha_vencimiento = ?";
            }

            String tipoTarifa = cbTipoTarifa.getSelectedItem().toString();
            String sqlTipoTarifa = (tipoTarifa.equals("Todos")) ? "" : "tipo_tarifa = ?";
            String orden = cbOrden.getSelectedItem().toString();
            Conexion conexion2 = new Conexion();
            con = conexion2.Connect();
            String sql = "SELECT * FROM inventario";

            if (!sqlFechaVencimiento.isEmpty() || !sqlTipoTarifa.isEmpty()) {
                sql += " WHERE";
            }

            if (!sqlFechaVencimiento.isEmpty()) {
                sql += " " + sqlFechaVencimiento;
            }

            if (!sqlFechaVencimiento.isEmpty() && !sqlTipoTarifa.isEmpty()) {
                sql += " AND";
            }

            if (!sqlTipoTarifa.isEmpty()) {
                sql += " " + sqlTipoTarifa;
            }

            sql += " ORDER BY precio_unitario " + orden;

            pst = con.prepareStatement(sql);

            if (!sqlFechaVencimiento.isEmpty()) {
                pst.setDate(1, fechaVencimiento);
            }

            if (!tipoTarifa.equals("Todos")) {
                int parametroFecha = (!sqlFechaVencimiento.isEmpty()) ? 2 : 1;
                pst.setString(parametroFecha, tipoTarifa);
            }

            rs = pst.executeQuery();

            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (rs.next()) {
                Vector v2 = new Vector();
                for (int a = 0; a < 10; a++) {
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("fecha_vencimiento"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add(rs.getString("cantidad_existente"));
                    v2.add(rs.getString("tipo_tarifa"));
                    v2.add(rs.getString("precio_unitario"));
                    v2.add(Float.valueOf(rs.getString("cantidad_existente")) * Float.valueOf(rs.getString("precio_unitario")));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAplicarFiltrosActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        JFrame frame = new JFrame("Registro de Productos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RegistrarProducto registrar = new RegistrarProducto();
        frame.getContentPane().add(registrar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).dispose();
        }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        JFrame frame = new JFrame("Registro de Productos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EliminarProducto eliminar = new EliminarProducto();
        frame.getContentPane().add(eliminar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).dispose();
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProductoActionPerformed
        JFrame frame = new JFrame("Registro de Productos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ActualizarProducto actualizar = new ActualizarProducto();
        frame.getContentPane().add(actualizar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).dispose();
        }
    }//GEN-LAST:event_btnActualizarProductoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAplicarFiltros;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cbOrden;
    private javax.swing.JComboBox<String> cbTipoTarifa;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtCalendario;
    // End of variables declaration//GEN-END:variables
}
