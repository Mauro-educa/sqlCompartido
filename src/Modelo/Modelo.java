package Modelo;

import Controlador.*;
import Vista.*;
import java.sql.*;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mnieves.domnav
 */
public class Modelo {

    //Datos de conexión a MySQL
    static String url = "jdbc:mysql://localhost:3306/juegoCocina";
    static String user = "root";
    static String password = "1234";

    static Connection con = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    static ArrayList<Cliente> listaClientes = obtenerListaClientes();

    public Modelo() {

    }

    public static Cliente nuevoCliente() {
        //Primero elegimos un cliente aleatorio
        if (listaClientes.isEmpty()) {
            return null; //No hay clientes
        }
        int indice = (int) (Math.random() * listaClientes.size());
        System.out.println("Cliente: " + indice);
        return listaClientes.get(indice);
    }

    public static ArrayList<Cliente> obtenerListaClientes() {
        System.out.println("Obteniendo lista de clientes");
        ArrayList<Cliente> lista = new ArrayList<>();
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/juegoCocina", "root", "1234");
            String sql = "select * from Cliente";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                String foto = rs.getString("foto");
                Cliente c = new Cliente(codigo, nombre, foto);
                lista.add(c);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public String obtenerImagen(int id, String tabla) {
        String rutaImagen = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            ps = con.prepareStatement("select foto from " + tabla + " where codigo=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                rutaImagen = rs.getString("foto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rutaImagen;
    }

    /**
     * Cuenta el número total de elementos en una tabla de la base de datos. Lo
     * usamos para contar clientes o recetas, por ejemplo.
     *
     * @param tabla tabla de la que consultar.
     * @return número de elementos en la tabla.
     */
    public static int contarElementos(String tabla) {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            ps = con.prepareStatement("select count(*) from " + tabla);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public static String conectar(String query) {
        String resultado = "";
        try {
            //Cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Establecer la conexión
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    resultado += (meta.getColumnLabel(i) + ": " + rs.getString(i) + " ");
                }
                resultado += "\n";
            }

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC.");
        } catch (SQLException e) {
            System.out.println("Error al conectar o consultar: " + e.getMessage());
        }
        return resultado;
    }
}
