package Reportes;

import Modelo.Conexion;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.collection.IndexedCollection;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    
    public static void main(String[] args) throws SQLException {
        Excel();
    }

    public static void Excel() throws SQLException {
        try{
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Productos");
            
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
            celdaTitulo.setCellValue("Reporte de Productos");
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
            
            String[] cabecera = new String[]{"Codigo","Nombre","Existencia","Precio","Importe"};
            
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
            
            Conexion con = new Conexion();
            PreparedStatement pst;
            ResultSet rs;
            Connection conn = con.Connect();
            
            int numFilaDatos = 5;
            
            CellStyle datosEstilos = book.createCellStyle();
            datosEstilos.setBorderBottom(BorderStyle.THIN);
            datosEstilos.setBorderLeft(BorderStyle.THIN);
            datosEstilos.setBorderRight(BorderStyle.THIN);
            datosEstilos.setBorderBottom(BorderStyle.THIN);
            
            pst = conn.prepareStatement("SELECT codigo_producto, nombre_producto, cantidad_existente, precio_unitario FROM inventario");
            
            rs = pst.executeQuery();
            
            int numCol = rs.getMetaData().getColumnCount();
            
            while (rs.next()){                
                Row filaDatos = sheet.createRow(numFilaDatos);
                
                for (int a = 0; a < numCol; a++){
                    Cell CellDatos = filaDatos.createCell(a);
                    CellDatos.setCellStyle(datosEstilos);
                    
                    if (a == 2 || a==3){
                        CellDatos.setCellValue(rs.getDouble(a+1));
                    } else{
                        CellDatos.setCellValue(rs.getString(a+1));
                    }
                }
                
                Cell celdaImporte = filaDatos.createCell(4);
                celdaImporte.setCellStyle(datosEstilos);
                celdaImporte.setCellFormula(String.format("C%d+D%d", numFilaDatos+1, numFilaDatos+1));
                
                numFilaDatos++;
            }
            
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            
            sheet.setZoom(150);
            
            FileOutputStream fileOut = new FileOutputStream("ReporteProductos.xlsx");
            book.write(fileOut);
            fileOut.close();
        } catch (IOException ex){
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
