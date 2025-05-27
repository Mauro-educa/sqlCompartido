package Modelo;

import java.sql.*;
import java.util.*;
import javax.swing.Timer;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
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
    public Timer generadorPedidos;

    //Listas de objetos
    static ArrayList<Cliente> listaClientes = obtenerListaClientes();
    static ArrayList<Receta> listaRecetas = obtenerListaRecetas();
    public static ArrayList<Pedido> listaPedidos = new ArrayList<>();
    public Receta[] bandeja = new Receta[3];

    public int dinero = 0;
    public int fallosDisponibles = 3;
    int tiempo = 8000;

    public Modelo() {
        iniciarGeneradorPedidos(); // Se inicia automáticamente al crear el modelo
        generarPedido(); // Se genera el primer pedido inmediatamente
    }

    /**
     * Cambia el cliente actual por otro aleatorio
     *
     * @return Devuelve el Cliente actual
     */
    public static Cliente nuevoCliente() {
        if (listaClientes.isEmpty()) {
            return null; // No hay clientes
        }

        // Filtrar clientes que NO tengan pedidos en estado 1 o 0
        ArrayList<Cliente> clientesPosibles = new ArrayList<>();

        for (Cliente c : listaClientes) {
            boolean tienePedidoEstado1 = false;

            // Recorrer listaPedidos para verificar si el cliente tiene pedido en estado 1 o 0
            for (Pedido p : listaPedidos) {
                if (p.getCliente().equals(c) && p.getEstado() != 2) {
                    tienePedidoEstado1 = true;
                    break;
                }
            }

            if (!tienePedidoEstado1) {
                clientesPosibles.add(c);
            }
        }

        if (clientesPosibles.isEmpty()) {
            return null; // Ningun cliente cumple la condicion
        }

        int indice = (int) (Math.random() * clientesPosibles.size());
        System.out.println("Cliente: " + indice);
        return clientesPosibles.get(indice);
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

    public void iniciarGeneradorPedidos() {
        generadorPedidos = new Timer(tiempo, e -> generarPedido());
        generadorPedidos.start();
    }

    /**
     * Genera un nuevo pedido para un cliente con 1-3 recetas
     */
    public void generarPedido() {
        Cliente cliente = nuevoCliente();

        //Si no hay clientes disponibles, vuelve
        if (cliente == null) {
            return;
        }

        //Genera de 1 a 3 recetas para el pedido
        ArrayList<Receta> recetas = new ArrayList<>();
        int cantidad = (int) (Math.random() * 3) + 1;
        for (int i = 0; i < cantidad; i++) {
            recetas.add(nuevaReceta());
        }

        //Añade el pedido a la lista de pedidos
        Pedido pedido = new Pedido(cliente, recetas);
        listaPedidos.add(pedido);
    }

    /**
     * Devuelve el pedido sin tomar más antiguo
     *
     * @return pedido sin tomar más antiguo
     */
    public Pedido getPedidoPendiente() {
        for (Pedido p : listaPedidos) {
            if (p.getEstado() == 0) {
                return p;
            }
        }
        return null;
    }

    /**
     * Toma un pedido, cambiando su estado
     *
     * @param p pedido a tomar
     */
    public void tomarPedido(Pedido p) {
        p.setEstado(1);
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
            System.out.println("Error de la base de datos");
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

    /**
     * Añade una receta a la bandeja
     *
     * @param foto nombre de la foto de la receta a añadir
     */
    public void addBandeja(String foto) {
        System.out.println(foto);
        for (Receta r : listaRecetas) {
            if (r.getFoto().equals(foto)) {
                //No hace falta comprobar que hay un espacio libre, el código no se ejecuta si no lo hay
                for (int i = 0; i < bandeja.length; i++) {
                    if (bandeja[i] == null) {
                        bandeja[i] = r;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Quitar una receta de la bandeja
     *
     * @param foto nombre de la foto de la receta a quitar
     */
    public void quitarBandeja(String foto) {
        for (int i = 0; i < bandeja.length; i++) {
            if (bandeja[i] != null && bandeja[i].getFoto().equals(foto)) {
                bandeja[i] = null;
                return; // Sale tras eliminar la primera coincidencia
            }
        }
    }

    /**
     * Vacía la bandeja entera
     */
    public void vaciarBandeja() {
        for (int i = 0; i < bandeja.length; i++) {
            bandeja[i] = null;
        }
    }

    /**
     * Servir un pedido
     *
     * @param nombreCliente nombre del cliente al que servimos un pedido para
     * buscarlo
     * @return true o false según se haya entregado correctamente o no
     */
    public Boolean servirPedido(String nombreCliente) {
        //Busca el pedido hecho por el cliente mediante el nombre
        Pedido pedido = buscarPedido(nombreCliente);
        if (pedido == null) {
            System.out.println("No hay pedidos pendientes de ese cliente");
            return false;
        } else {
            System.out.println(comprobarPedido(pedido));
            return true;
        }
    }

    /**
     * Busca los pedidos pendientes que haya hecho un cliente por su nombre
     *
     * @param nombreCliente nombre del cliente cuyo pedido se busca
     * @return pedido del cliente
     */
    public Pedido buscarPedido(String nombreCliente) {
        for (Pedido p : listaPedidos) {
            if (p.getCliente().getNombre().equals(nombreCliente) && p.getEstado() == 1) {
                return p;
            }
        }
        return null; // No se encontró ningún pedido con ese cliente
    }

    /**
     * Comprueba si las recetas entregadas corresponden con lo que ha pedido el
     * cliente
     *
     * @param pedido pedido hecho por el cliente con las recetas que debería
     * tener la bandeja
     * @return true o false según se haya entregado correctamente o no
     */
    public boolean comprobarPedido(Pedido pedido) {
        // Obtener códigos de recetas del pedido
        ArrayList<Integer> codigosPedido = new ArrayList<>();
        System.out.println("Recetas pedidas: ");
        for (Receta r : pedido.getPedido()) { // 
            codigosPedido.add(r.getCodigo());
            System.out.println(r.getNombre());
        }

        // Obtener códigos de recetas en la bandeja
        ArrayList<Integer> codigosBandeja = new ArrayList<>();
        System.out.println("Recetas en bandeja: ");
        for (Receta r : bandeja) {
            if (r != null) {
                codigosBandeja.add(r.getCodigo());
                System.out.println(r.getNombre());
            }
        }

        // Limpiar la bandeja
        vaciarBandeja();

        // Eliminar el pedido de la lista
        listaPedidos.remove(pedido);

        // Comparar tamaños
        if (codigosPedido.size() != codigosBandeja.size()) {
            fallosDisponibles--;
            return false;
        }

        // Ordenar y comparar listas
        Collections.sort(codigosPedido);
        Collections.sort(codigosBandeja);

        // Si el pedido es correcto, sumamos el dinero de los productos
        if (codigosPedido.equals(codigosBandeja)) {
            for (Receta r : pedido.getPedido()) {
                dinero += r.getPrecio();
            }
            return true;
        }
        // Si no es correcto, resta un fallo disponible
        fallosDisponibles--;
        return false;
    }
}
