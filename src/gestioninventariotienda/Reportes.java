package gestioninventariotienda;

import Reportes.Excel;
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
import java.io.FileInputStream;
import java.sql.DriverManager;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.IOUtil;

public class Reportes extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public Reportes() {
        initComponents();
        Connect();
    }
    
    public void Connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/db_smart_shop_inventory_manager", "root", "rootpass");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ReporteExcel(String sql, String tipoReporte){
        try{
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Productos");
            
            InputStream is = new FileInputStream("src\\Img\\logo.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            
            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
            
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);
            
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            org.apache.poi.ss.usermodel.Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short)14);
            tituloEstilo.setFont(fuenteTitulo);
            
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue(tipoReporte);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
            
            String[] cabecera = new String[]{"Tipo de Pedido","Numero de Pedido","Codigo de Producto","Nombre de Producto","Cantidad","Precio","Fecha de Pedido"};
            
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            
            org.apache.poi.ss.usermodel.Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short)12);
            headerStyle.setFont(font);
            
            Row filaEncabezados = sheet.createRow(4);
            for (int i = 0; i < cabecera.length; i++){
                Cell celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }
            
            int numFilaDatos = 5;
            
            CellStyle datosEstilos = book.createCellStyle();
            datosEstilos.setBorderBottom(BorderStyle.THIN);
            datosEstilos.setBorderLeft(BorderStyle.THIN);
            datosEstilos.setBorderRight(BorderStyle.THIN);
            datosEstilos.setBorderBottom(BorderStyle.THIN);
            
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            int numCol = rs.getMetaData().getColumnCount();
            
            while (rs.next()){                
                Row filaDatos = sheet.createRow(numFilaDatos);
                
                for (int a = 0; a < numCol; a++){
                    Cell CellDatos = filaDatos.createCell(a);
                    CellDatos.setCellStyle(datosEstilos);
                    
                    switch (a){
                        case 1:
                        case 2:
                        case 4:
                            CellDatos.setCellValue(rs.getInt(a+1));
                            break;
                        case 5:
                            CellDatos.setCellValue(rs.getDouble(a+1));
                            break;
                        default:
                            CellDatos.setCellValue(rs.getString(a+1));
                            break;
                    }
                }
                
//                Cell celdaImporte = filaDatos.createCell(4);
//                celdaImporte.setCellStyle(datosEstilos);
//                celdaImporte.setCellFormula(String.format("C%d+D%d", numFilaDatos+1, numFilaDatos+1));
                
                numFilaDatos++;
            }
            
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            
            sheet.setZoom(150);
            
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = formatoFecha.format(fechaActual);
            String nombreArchivo = tipoReporte + "_" + fechaFormateada + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
            
            book.write(fileOut);
            fileOut.close();
            JOptionPane.showMessageDialog(this, tipoReporte+" Generado Correctamente");
        } catch (IOException ex){
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex){
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ReportePdf(String tipoReporte){
        Date date = new Date();
        try{
            FileOutputStream archivo;
            File file = new File(tipoReporte+" "+new SimpleDateFormat("yyyy-MM-dd").format(date)+".pdf");
            archivo = new FileOutputStream(file);
            Document documento = new Document();
            PdfWriter.getInstance(documento, archivo);
            documento.open();
            Paragraph titulo = new Paragraph(tipoReporte, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK));
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            Image img = Image.getInstance("src\\Img\\logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor. WHITE);
            Font magenta = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor. MAGENTA);
            fecha.add(Chunk.NEWLINE);
            fecha.add("Fecha: "+ new SimpleDateFormat("yyyy-MM-dd").format(date)+"\n\n");

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float[]{20f, 10f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            Encabezado.addCell(img);
            Encabezado.addCell("");
            Encabezado.addCell("Gestion de Inventarios para Tiendas\nEncargado: Lester Yat\nDirección: Centro Chimaltenango Zona 1 12-1\nSucursal: Central\nDepartamento: Bodegas\nTipo Reporte: Gestion de Inventario");
            Encabezado.addCell(fecha);
            documento.add(Encabezado);
            documento.add(new Paragraph("\n\n"));

            PdfPTable tablaProd = new PdfPTable(7);
            tablaProd.setWidthPercentage(100);
            tablaProd.getDefaultCell().setBorder(0);
            float[] ColumnaProd = new float[]{15f, 15f, 10f, 10f, 20f, 10f, 10f};
            tablaProd.setWidths(ColumnaProd);
            tablaProd.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell prod1 = new PdfPCell(new Phrase("Fecha Pedido", negrita));
            PdfPCell prod2 = new PdfPCell(new Phrase("Tipo Pedido", negrita));
            PdfPCell prod3 = new PdfPCell(new Phrase("Numero Pedido", negrita));
            PdfPCell prod4 = new PdfPCell(new Phrase("Codigo", negrita));
            PdfPCell prod5 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell prod6 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell prod7 = new PdfPCell(new Phrase("Precio", negrita));
            prod1.setBorder(0);
            prod2.setBorder(0);
            prod3.setBorder(0);
            prod4.setBorder(0);
            prod5.setBorder(0);
            prod6.setBorder(0);
            prod7.setBorder(0);
            prod1.setBackgroundColor(BaseColor.DARK_GRAY);
            prod2.setBackgroundColor(BaseColor.DARK_GRAY);
            prod3.setBackgroundColor(BaseColor.DARK_GRAY);
            prod4.setBackgroundColor(BaseColor.DARK_GRAY);
            prod5.setBackgroundColor(BaseColor.DARK_GRAY);
            prod6.setBackgroundColor(BaseColor.DARK_GRAY);
            prod7.setBackgroundColor(BaseColor.DARK_GRAY);
            tablaProd.addCell(prod1);
            tablaProd.addCell(prod2);
            tablaProd.addCell(prod3);
            tablaProd.addCell(prod4);
            tablaProd.addCell(prod5);
            tablaProd.addCell(prod6);
            tablaProd.addCell(prod7);

//            float totalVentas = 0.0f;

            DecimalFormat dc = new DecimalFormat("#,###,##0.00");

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                String fechaPedido = jTable1.getValueAt(i, 0).toString();
                String tipoPedido = jTable1.getValueAt(i, 1).toString();
                String numeroPedido = jTable1.getValueAt(i, 2).toString();
                String codigoProducto = jTable1.getValueAt(i, 3).toString();
                String nombreProducto = jTable1.getValueAt(i, 4).toString();
                String cantidadProducto = jTable1.getValueAt(i, 5).toString();
                float precio = Float.parseFloat(jTable1.getValueAt(i, 6).toString());
                String precioFormateado = dc.format(precio);

//                float total = Float.parseFloat(jTable1.getValueAt(i, 4).toString());
//                String totalFormateado = dc.format(total);
//
//                totalVentas += total;

                tablaProd.addCell(fechaPedido);
                tablaProd.addCell(tipoPedido);
                tablaProd.addCell(numeroPedido);
                tablaProd.addCell(codigoProducto);
                tablaProd.addCell(nombreProducto);
                tablaProd.addCell(cantidadProducto);
                tablaProd.addCell("Q " + precioFormateado);
            }

            documento.add(tablaProd);
            
//            Paragraph info = new Paragraph();
//            info.add(Chunk.NEWLINE);
//            info.add("Total a Pagar: Q " + dc.format(totalVentas));
//            info.setAlignment(Element.ALIGN_RIGHT);
//            documento.add(info);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("________________________\n\n");
            mensaje.add("Firma Encargado");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            documento.add(mensaje);
            documento.close();
            archivo.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(this, tipoReporte+" Generado Correctamente");
        } catch (DocumentException | IOException e){
            System.out.println(e.toString());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtFechaFinal = new javax.swing.JTextField();
        txtFechaInicial = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbTipoArchivo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jCalendar2 = new com.toedter.calendar.JCalendar();
        cbTipoReporte = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnGenerarReporte = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("REPORTES DEL SISTEMA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Filtros");

        jLabel2.setText("Fecha de Inicial");

        jLabel3.setText("Fecha de Limite");

        jLabel5.setText("Tipos de Reportes");

        cbTipoArchivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EXCEL", "PDF" }));

        jLabel6.setText("Tipo de Archivo");

        jCalendar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendar1PropertyChange(evt);
            }
        });

        jCalendar2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendar2PropertyChange(evt);
            }
        });

        cbTipoReporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reporte Ventas", "Reporte Compras" }));
        cbTipoReporte.setToolTipText("");
        cbTipoReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCalendar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cbTipoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTipoArchivo, 0, 153, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(50, 50, 50))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCalendar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbTipoArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbTipoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
        );

        btnVolver.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnGenerarReporte.setText("Genarar Reporte");
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGenerarReporte)
                .addGap(63, 63, 63)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha Pedido", "Tipo Pedido", "Numero Pedido", "Codigo", "Nombre", "Cantidad", "Precio"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(312, 312, 312))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        String tipoArchivo = cbTipoArchivo.getSelectedItem().toString();
        if (tipoArchivo.equals("EXCEL")){
            String tipoReporte = cbTipoReporte.getSelectedItem().toString();
            String tipoReporteProcesado = "";
            String fechaInicio = txtFechaInicial.getText().trim();
            String fechaFinal = txtFechaFinal.getText().trim();
            
            if (tipoReporte.equals("Reporte Compras")){
                tipoReporteProcesado = "Compra";
            } else if(tipoReporte.equals("Reporte Ventas")){
                tipoReporteProcesado = "Venta";
            }
            
            String sql = "SELECT tipo_movimiento,numero_movimiento,codigo_producto,nombre_producto,cantidad,precio,fecha_movimiento FROM movimiento_inventario WHERE tipo_movimiento = '"+tipoReporteProcesado+"'";

            if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
                sql += " AND fecha_movimiento BETWEEN '"+fechaInicio+"' AND '"+fechaFinal+"'";
            } else if (!fechaInicio.isEmpty()) {
                sql += " AND fecha_movimiento >= '"+fechaInicio+"'";
            } else if (!fechaFinal.isEmpty()) {
                sql += " AND fecha_movimiento <= '"+fechaFinal+"'";
            }
            ReporteExcel(sql, tipoReporte);
        } else if(tipoArchivo.equals("PDF")){
            String tipoReporte = cbTipoReporte.getSelectedItem().toString();
            ReportePdf(tipoReporte);
        }
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

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

    private void jCalendar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendar1PropertyChange
        if (evt.getOldValue() != null) {
            SimpleDateFormat ff1 = new SimpleDateFormat("yyyy-MM-dd");
            txtFechaInicial.setText(ff1.format(jCalendar1.getCalendar().getTime()));
        }
    }//GEN-LAST:event_jCalendar1PropertyChange

    private void jCalendar2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendar2PropertyChange
        if (evt.getOldValue() != null) {
            SimpleDateFormat ff2 = new SimpleDateFormat("yyyy-MM-dd");
            txtFechaFinal.setText(ff2.format(jCalendar2.getCalendar().getTime())); // Cambia jCalendar1 a jCalendar2
        }
    }//GEN-LAST:event_jCalendar2PropertyChange

    private void cbTipoReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoReporteActionPerformed
        String tipoReporte = cbTipoReporte.getSelectedItem().toString();
        String fechaInicio = txtFechaInicial.getText().trim();
        String fechaFinal = txtFechaFinal.getText().trim();

        try {
            String sql = "SELECT * FROM movimiento_inventario WHERE tipo_movimiento = ?";

            if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
                // Ambas fechas seleccionadas
                sql += " AND fecha_movimiento BETWEEN ? AND ?";
                pst = con.prepareStatement(sql);
                if (tipoReporte.equals("Reporte Compras")){
                    pst.setString(1, "Compra");
                } else if(tipoReporte.equals("Reporte Ventas")){
                    pst.setString(1, "Venta");
                }
                pst.setDate(2, java.sql.Date.valueOf(fechaInicio));
                pst.setDate(3, java.sql.Date.valueOf(fechaFinal));
            } else if (!fechaInicio.isEmpty()) {
                // Solo fecha de inicio seleccionada
                sql += " AND fecha_movimiento >= ?";
                pst = con.prepareStatement(sql);
                if (tipoReporte.equals("Reporte Compras")){
                    pst.setString(1, "Compra");
                } else if(tipoReporte.equals("Reporte Ventas")){
                    pst.setString(1, "Venta");
                }
                pst.setDate(2, java.sql.Date.valueOf(fechaInicio));
            } else if (!fechaFinal.isEmpty()) {
                // Solo fecha final seleccionada
                sql += " AND fecha_movimiento <= ?";
                pst = con.prepareStatement(sql);
                if (tipoReporte.equals("Reporte Compras")){
                    pst.setString(1, "Compra");
                } else if(tipoReporte.equals("Reporte Ventas")){
                    pst.setString(1, "Venta");
                }
                pst.setDate(2, java.sql.Date.valueOf(fechaFinal));
            } else {
                // Ninguna fecha seleccionada
                pst = con.prepareStatement(sql);
                if (tipoReporte.equals("Reporte Compras")){
                    pst.setString(1, "Compra");
                } else if(tipoReporte.equals("Reporte Ventas")){
                    pst.setString(1, "Venta");
                }
            }

            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            rss.getColumnCount();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (rs.next()) {
                Vector v2 = new Vector();
                for (int a = 0; a < 10; a++) {
                    v2.add(rs.getString("fecha_movimiento"));
                    v2.add(rs.getString("tipo_movimiento"));
                    v2.add(rs.getString("numero_movimiento"));
                    v2.add(rs.getString("codigo_producto"));
                    v2.add(rs.getString("nombre_producto"));
                    v2.add(rs.getString("cantidad"));
                    v2.add(rs.getString("precio"));
                }
                df.addRow(v2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbTipoReporteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cbTipoArchivo;
    private javax.swing.JComboBox<String> cbTipoReporte;
    private com.toedter.calendar.JCalendar jCalendar1;
    private com.toedter.calendar.JCalendar jCalendar2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFechaFinal;
    private javax.swing.JTextField txtFechaInicial;
    // End of variables declaration//GEN-END:variables
}
