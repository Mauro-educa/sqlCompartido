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
    static ArrayList<Receta> listaRecetas = obtenerListaRecetas();
    public static ArrayList<Pedido> listaPedidos = new ArrayList<>();

    public Modelo() {

    }

    /**
     * Cambia el cliente actual por otro aleatorio
     *
     * @return Devuelve el Cliente actual
     */
    public static Cliente nuevoCliente() {
        if (listaClientes.isEmpty()) {
            return null; //No hay clientes
        }
        int indice = (int) (Math.random() * listaClientes.size());
        System.out.println("Cliente: " + indice);
        return listaClientes.get(indice);
    }

    /**
     * Elige una nueva receta de forma aleatoria
     *
     * @return La receta elegida
     */
    public static Receta nuevaReceta() {
        if (listaRecetas.isEmpty()) {
            return null; //No hay recetas
        }
        int indice = (int) (Math.random() * listaRecetas.size());
        System.out.println("Receta: " + indice);
        return listaRecetas.get(indice);
    }

    /**
     * Genera un nuevo pedido, con su cliente y sus recetas
     *
     * @return El nuevo pedido
     */
    public static Pedido generarPedido() {
        //Si hay un pedido que se ha generado con anterioridad, establece su estado a en cola
        if (!listaPedidos.isEmpty()) {
            Pedido ultimo = listaPedidos.get(listaPedidos.size() - 1);
            ultimo.setEstado(1);
        }
        //Se elige un cliente
        Cliente cliente = nuevoCliente();
        //Se generará un número aleatorio de recetas entre 1 y 3 para el pedido
        ArrayList<Receta> recetas = new ArrayList<Receta>();
        for (int num = (int) (Math.random() * 3) + 1; num > 0; num--) {
            recetas.add(nuevaReceta());
        }
        //Se añade un nuevo pedido a la lista
        Pedido pedido = new Pedido(cliente, recetas);
        listaPedidos.add(pedido);
        return pedido;
    }

    /**
     * Obtiene la lista de clientes de la base de datos y los almacena en una
     * lista.
     *
     * @return Devuelve la lista de clientes
     */
    public static ArrayList<Cliente> obtenerListaClientes() {
        System.out.println("Obteniendo lista de clientes");
        ArrayList<Cliente> lista = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, user, password);
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

    /**
     * Obtiene la lista de recetas de la base de datos
     *
     * @return Una lista con todas las recetas
     */
    public static ArrayList<Receta> obtenerListaRecetas() {
        System.out.println("Obteniendo lista de recetas");
        ArrayList<Receta> lista = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, user, password);
            String sql = "select * from Receta";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                int precio = rs.getInt("precio");
                String foto = rs.getString("foto");
                Receta r = new Receta(codigo, nombre, precio, foto);
                lista.add(r);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
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
