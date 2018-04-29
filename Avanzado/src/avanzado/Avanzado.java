/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avanzado;

/**
 *
 * @author andrea
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Avanzado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //-----------------------------------------CONECTANDONOS A LA BASE
        Connection con;
        cargar();
        
        con=conectar("jdbc:mysql://localhost:3306/proyectoavanzado","Laastar","123");
        TirarTodo();
        //----------------------------------------------------------------
        leerXMLF("C:\\Users\\TensinUriel\\Downloads\\ProyectoJavaAvanzado\\Facturas.xml","factura",con);
        leerXMLV("C:\\Users\\TensinUriel\\Downloads\\ProyectoJavaAvanzado\\Vehiculos.xml","vehiculo",con);
        leerXMLC("C:\\Users\\TensinUriel\\Downloads\\ProyectoJavaAvanzado\\Clientes.xml","cliente",con);
        CrearTablaPoliza();
        CrearTablaFechasPoliza();
        CargarTablaFechasPoliza();
        //crearTabla("");
        IniciarVentana();
    }
    
    public static void IniciarVentana(){
        Ventana abrir = new Ventana();
        abrir.setVisible(true);
    }
    
    //----------------------------------------------CREAR TABLA POLIZA
    public void crearTabla(Connection con){
        String query="CREATE TABLE Poliza(Cliente_id INT PRIMARY_KEY,Factura_id INT AS(Cliente_id),Total INT AS((SELECT Monto FROM Factura NATURAL JOIN Factura_id))*(6.67/12)";
        
    }
    
    //---------------------------------------------------------
    public static void TirarTodo() {
        Connection conn;
        Statement stmt;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "TRUNCATE TABLE poliza_fechas";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            System.out.println("Truncado Poliza Fechas Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        query = "TRUNCATE TABLE vehiculo";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            System.out.println("Truncado Vehiculo Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        query = "TRUNCATE TABLE cliente";
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            System.out.println("Truncado Cliente Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        query = "ALTER TABLE vehiculo "
                + "DROP FOREIGN KEY fk_factura_id";
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            System.out.println("Truncado Factura Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        query = "TRUNCATE TABLE factura";
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            System.out.println("Truncado Factura Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        query = "ALTER TABLE vehiculo "
                + "ADD CONSTRAINT fk_factura_id "
                + "FOREIGN KEY (factura_id) "
                + "REFERENCES factura (factura_id)";
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query 
            stmt.close();
            conn.close();
            System.out.println("Truncado Factura Correcto");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//-----------------------------------------------------------------------------------------------PARA INSERTAR LOS VALORES
    public static void leerXMLV(String nom_arch,String etiqueta,Connection con){
            PreparedStatement ps;
            String query="INSERT INTO Vehiculo (Vehiculo_id,Placas,Modelo,Marca,Factura_id) VALUES(?,?,?,?,?)";      
            try{
                File archivo= new File(nom_arch);
                DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
                DocumentBuilder db= dbf.newDocumentBuilder();
                Document document=db.parse(archivo);
                NodeList lista= document.getElementsByTagName(etiqueta);
                for(int i=0; i<lista.getLength();i++){
                    Node nodo=lista.item(i);
                    System.out.println("Elemento: "+nodo.getNodeName());
                    if(nodo.getNodeType()== Node.ELEMENT_NODE){
                            Element element=(Element)nodo;
                            try{
      
                                    ps= con.prepareStatement(query);
                                    ps.setInt(1, i+1);
                                    ps.setString(2, element.getElementsByTagName("placas").item(0).getTextContent());
                                    ps.setString(3, element.getElementsByTagName("marca").item(0).getTextContent());
                                    ps.setString(4, element.getElementsByTagName("modelo").item(0).getTextContent());
                                    ps.setInt(5, Integer.parseInt(element.getElementsByTagName("id_factura").item(0).getTextContent()));
                                    System.out.println(ps.executeUpdate());
                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                    }
                   
                }
                
            }catch(Exception e){
                System.out.println("Ocurrio un error :(");
            }
    }
 
    public static void leerXMLF(String nom_arch,String etiqueta,Connection con){
            PreparedStatement ps;
            String query="INSERT INTO Factura (Factura_id,Monto) VALUES(?,?)";      
            try{
                File archivo= new File(nom_arch);
                DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
                DocumentBuilder db= dbf.newDocumentBuilder();
                Document document=db.parse(archivo);
                NodeList lista= document.getElementsByTagName(etiqueta);
                for(int i=0; i<lista.getLength();i++){
                    Node nodo=lista.item(i);
                    System.out.println("Elemento: "+nodo.getNodeName());
                    if(nodo.getNodeType()== Node.ELEMENT_NODE){
                            Element element=(Element)nodo;
                            try{
                                    ps= con.prepareStatement(query);
                                    ps.setInt(1, i+1);
                                    ps.setInt(2, Integer.parseInt(element.getElementsByTagName("costo_total").item(0).getTextContent()));
                                    System.out.println(ps.executeUpdate());
                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                    }
                   
                }
                
            }catch(Exception e){
                System.out.println("Ocurrio un error :(");
            }
    }
    
    public static void leerXMLC(String nom_arch,String etiqueta,Connection con){
            PreparedStatement ps;
            String query="INSERT INTO Cliente (Cliente_id,Nombre,Direccion) VALUES(?,?,?)";      
            try{
                File archivo= new File(nom_arch);
                DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
                DocumentBuilder db= dbf.newDocumentBuilder();
                Document document=db.parse(archivo);
                NodeList lista= document.getElementsByTagName(etiqueta);
                for(int i=0; i<lista.getLength();i++){
                    Node nodo=lista.item(i);
                    System.out.println("Elemento: "+nodo.getNodeName());
                    if(nodo.getNodeType()== Node.ELEMENT_NODE){
                            Element element=(Element)nodo;
                            try{
                                    ps= con.prepareStatement(query);
                                    ps.setInt(1, i+1);
                                    ps.setString(2, element.getElementsByTagName("nombre").item(0).getTextContent());
                                    ps.setString(3, element.getElementsByTagName("direccion").item(0).getTextContent());
                                    System.out.println(ps.executeUpdate());
           
                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                    }
                   
                }
                
            }catch(Exception e){
                System.out.println("Ocurrio un error :(");
            }
    }
    
//--------------------------------------------------------------------------------------------------------------------    
    
    //------------------------------------------------------------------------PARA LA CONEXION A LA BASE
    public static void cargar(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver cargado");
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException e){
            System.out.println(e.getMessage());
        }
    
    }
    
    public static Connection conectar(String url, String username, String passwd){
        Connection con= null;
        try{
            con= DriverManager.getConnection(url,username,passwd);
            System.out.println("Conexion a base realizada");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return con;
        
    }
    //--------------------------------------------------------------------------------------------------
    
    public static void CrearTablaPoliza() {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "CREATE OR REPLACE VIEW POLIZA AS "
                     + "SELECT c.cliente_id poliza_id, c.nombre, f.monto, (f.monto*(6.67/12)/100) monto_total, "
                     + "(f.monto*0.85) prima_asegurada, fecha_inicio, fecha_vencimiento "
                     + "FROM cliente c INNER JOIN factura f ON c.cliente_id = f.factura_id";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query      
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void CrearTablaFechasPoliza() {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "CREATE TABLE poliza_fechas( "
                     + "poliza_id INT(10), "
                     + "fecha_inicio DATE, "
                     + "fecha_vencimiento DATE"
                     + ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query      
            System.out.println("Creación de tabla de fechas_poliza exitosa");
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void CargarTablaFechasPoliza() {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "INSERT INTO poliza_fechas (poliza_id,fecha_inicio,fecha_vencimiento) VALUES "
                     + "(1, '2018-07-04', '2018-07-04'), "
                     + "(2, '2017-11-12', '2018-11-12'), "
                     + "(3, '2018-01-22', '2019-01-22'), "
                     + "(4, '2018-03-16', '2019-03-16')";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            stmt.executeUpdate(query);     //Guardamos el resultado de nuestra query      
            System.out.println("Carga de tabla de fechas_poliza exitosa");
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void Actualizar() {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        int edad = 30;
        String nombre = "Sofia";
        int id = 4;
        String sexo = "F";
        
        String query = "UPDATE persona SET edad = ?, nombre = ?, sexo = ? WHERE id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, edad);
            ps.setString(2, nombre);
            ps.setString(3, sexo);
            ps.setInt(4, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            
        }

    }
    
    public static void ActualizarDireccion(String NuevaDireccion, int id) {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "UPDATE cliente SET Direccion = ? WHERE Cliente_id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, NuevaDireccion);
            ps.setInt(2, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    
    public static void ActualizarString(String tabla, String atributo, String NuevoValor, int id) {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "UPDATE " + tabla + " SET " + atributo + " = ? WHERE " + tabla + "_id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, NuevoValor);
            ps.setInt(2, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    
    public static void ActualizarInt(String tabla, String atributo, int NuevoValor, int id) {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "UPDATE " + tabla + " SET " + atributo + " = ? WHERE " + tabla + "_id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, NuevoValor);
            ps.setInt(2, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    
    public static void ActualizarMarca() {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        
        String query = "UPDATE vehiculo SET Marca = ? WHERE Vehiculo_id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            
        }

    }
    
    public static void ActualizarModelo() {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        int edad = 30;
        String nombre = "Sofia";
        int id = 4;
        String sexo = "F";
        
        String query = "UPDATE persona SET edad = ?, nombre = ?, sexo = ? WHERE id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, edad);
            ps.setString(2, nombre);
            ps.setString(3, sexo);
            ps.setInt(4, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            
        }

    }
    
    public static void ActualizarCosto() {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        int edad = 30;
        String nombre = "Sofia";
        int id = 4;
        String sexo = "F";
        
        String query = "UPDATE persona SET edad = ?, nombre = ?, sexo = ? WHERE id = ?";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, edad);
            ps.setString(2, nombre);
            ps.setString(3, sexo);
            ps.setInt(4, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Número de renglones actualizados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {
            
        }

    }
    
    public static void Insertar() {
        Connection conn;
        PreparedStatement ps;
        int renglones_afectados;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "INSERT INTO persona (nombre, edad, sexo) VALUES(?, ?, ?)";
        String nombre = "Fernanda";
        int edad = 22;
        String sexo = "F";
        
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setInt(2, edad);
            ps.setString(3, sexo);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Update exitoso...");
            System.out.println("Renglones afectados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (Exception e) {
            
        }
    }
    
    public static void Borrar() {
        Connection conn;
        PreparedStatement ps;
        int id = 6;
        int renglones_afectados;

        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/miercoles", "Laastar", "123");
        
        String query = "DELETE FROM persona WHERE id = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            renglones_afectados = ps.executeUpdate();
            System.out.println("Delete exitoso...");
            System.out.println("Renglones afectados: " + renglones_afectados);
            ps.close();
            conn.close();
        } catch (SQLException e) {

        }
    }
    
    public static String ConsultaGeneral(String tabla) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT * FROM " + tabla;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
            switch (tabla){
                case "cliente":
                    while (rs.next()) {
                         //Imprime de las columnas de la base de datos con el getInt
                        msj += "Id: " + rs.getInt("Cliente_id") + " ";
                        msj += "Nombre: " + rs.getString("Nombre") + " ";
                        msj += "\n";
                    }
                    break;
                case "factura":
                    while (rs.next()) {
                         //Imprime de las columnas de la base de datos con el getInt
                        msj += "Id: " + rs.getInt("Factura_id") + " ";
                        msj += "Monto: $" + rs.getInt("Monto") + " ";
                        msj += "\n";
                    }
                    break;
                }
            
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultaGeneralCliente(int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT * FROM cliente WHERE cliente_id = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");

                    while (rs.next()) {
                         //Imprime de las columnas de la base de datos con el getInt
                        msj += rs.getString("Nombre") + "\t";
                    }
            
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultaGeneralVehiculo(int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT Placas, Modelo, Monto "
                     + "FROM vehiculo INNER JOIN factura ON factura.factura_id = vehiculo.factura_id "
                     + "WHERE vehiculo.factura_id = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
            
            while (rs.next()) {
                //Imprime de las columnas de la base de datos con el getInt
                msj += rs.getString("placas") + "\t";
                msj += rs.getString("modelo") + "\t";
                msj += "$" + rs.getString("monto") + "\t";
            }
            
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String Consulta1(String tabla, String atributo, int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT " + atributo + " FROM " + tabla + " WHERE " + tabla +"_id" + " = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
                    while (rs.next()) {
                        msj += atributo + ": " + rs.getString(atributo) + "\t";
                        msj += "\n";
                    }          
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultaCostoPoliza(String tabla, String atributo, int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT " + atributo + " FROM " + tabla + " WHERE " + tabla +"_id" + " = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
                    while (rs.next()) {
                        msj += "Costo Póliza: $" + rs.getString(atributo) + "\t";
                        msj += "\n";
                    }          
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultaPrimaAsegurada(String tabla, String atributo, int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT " + atributo + " FROM " + tabla + " WHERE " + tabla +"_id" + " = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
                    while (rs.next()) {
                        msj += "Costo Póliza: $" + rs.getString(atributo) + "\t";
                        msj += "\n";
                    }          
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultarDeVehiculo(String atributo, int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT v." + atributo + " "
                     + "FROM vehiculo v INNER JOIN factura f ON f.factura_id = v.factura_id "
                     + "WHERE v.factura_id = " + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");
                    while (rs.next()) {
                        msj += atributo + ": " + rs.getString(atributo) + "\t";
                        msj += "\n";
                    }          
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
    public static String ConsultaFechaPolizas(int id) {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        
        cargar();
        conn = conectar("jdbc:mysql://localhost:3306/proyectoavanzado", "Laastar", "123");
        
        String query = "SELECT * FROM poliza_fechas WHERE poliza_id =" + id;
        String msj = "";
        
        try {
            stmt = conn.createStatement();     //Creamos el statement
            rs = stmt.executeQuery(query);     //Guardamos el resultado de nuestra query
            rs = stmt.getResultSet();
            System.out.println("Consulta exitosa: ");

                    while (rs.next()) {
                         //Imprime de las columnas de la base de datos con el getInt
                        msj += rs.getInt("poliza_id") + "\t";
                        msj += rs.getDate("fecha_inicio") + "\t";
                        msj += rs.getDate("fecha_vencimiento") + "\t";
                    }
            
            stmt.close();
            rs.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return msj;
    }
    
}
