/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.*;
import Modelo.*;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;
import java.util.HashSet;
import javax.swing.*;

/**
 * @author Ivan.siefer
 * @author mnieves.domnav
 */
public class Controlador implements ActionListener {

    private static Vista vista;
    private static Modelo modelo;
    private Timer actualizadorVista;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        vista.setControlador(this);
        vista.arranca();
        iniciarActualizadorVista1();

        //Datos de conexión a MySQL
        String url = "jdbc:mysql://localhost:3306/juegoCocina";
        String user = "root";
        String password = "1234";

        try {
            //Cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Establecer la conexión
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC.");
        } catch (SQLException e) {
            System.out.println("Error al conectar o consultar: " + e.getMessage());
        }

        cambiarPedido();
        imagenesRecetas();
    }

    public static void main(String[] args) {
        Modelo mod = new Modelo();
        Vista v = new Vista();
        new Controlador(v, mod);
    }

    public void cambiarIconoPedido(String nombre) {
        URL iconURL = getClass().getResource("/Vista/img/" + nombre);
        ImageIcon nuevoIcono = new ImageIcon(iconURL);
        vista.mPedido.setIcon(nuevoIcono);
    }

    public void cambiarPedido() {
        // Tomar el pedido más antiguo con estado 0
        Pedido pedido = modelo.getPedidoPendiente();

        if (pedido != null) {
            modelo.tomarPedido(pedido); // Cambiar su estado a 1

            // Mostrar los datos del pedido recién tomado
            Cliente cliente = pedido.getCliente();
            Receta receta = pedido.getPlato(0); // Mostramos el primer plato

            //Mostrar datos del cliente
            URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
            vista.mCliente.setIcon(new ImageIcon(iconURL));
            vista.mNombreCliente.setText(cliente.getNombre());

            iconURL = getClass().getResource("/Vista/img/" + receta.getFoto());
            vista.mPedido.setIcon(new ImageIcon(iconURL));
        } else {
            // No hay ningún pedido listo para tomar: limpiar vista
            vista.mCliente.setIcon(null);
            vista.mPedido.setIcon(null);
            vista.mNombreCliente.setText("");
        }
    }

    public void iniciarActualizadorVista() {
        actualizadorVista = new Timer(100, e -> {
            Pedido pedido = modelo.getPedidoPendiente();
            actualizarVistaConPedido(pedido);

        });
        actualizadorVista.start();
    }

    public void iniciarActualizadorVista1() {
        actualizadorVista = new Timer(100, e -> {
            Pedido pedido = modelo.getPedidoPendiente();
            if (pedido != null) {
                actualizarVistaConPedido(pedido);
            }
        });
        actualizadorVista.start();
    }

    private void actualizarVistaConPedido(Pedido pedido) {
        if (pedido == null) {
            vista.mCliente.setIcon(null);
            vista.mNombreCliente.setText("");
            vista.mPedido.setIcon(null);
            return;
        }

        Cliente cliente = pedido.getCliente();
        Receta receta = pedido.getPlato(0);

        URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
        ImageIcon nuevoIcono = new ImageIcon(iconURL);
        vista.mCliente.setIcon(nuevoIcono);
        vista.mNombreCliente.setText(cliente.getNombre());

        iconURL = getClass().getResource("/Vista/img/" + receta.getFoto());
        nuevoIcono = new ImageIcon(iconURL);
        vista.mPedido.setIcon(nuevoIcono);
    }

    /**
     * Cambia la imagen y el nombre del cliente en la vista Manda generar un
     * nuevo pedido y actualiza la vista en consecuencia
     */
    /*public void cambiarPedido() {
        modelo.modificarEstado();

        // Genera un nuevo pedido
        Pedido pedido = modelo.generarPedido();

        // Si no se generó pedido, limpiar imágenes y texto y salir
        if (pedido == null) {
            vista.mCliente.setIcon(null);
            vista.mNombreCliente.setText("");
            vista.mPedido.setIcon(null);
            return;
        }

        // Obtiene el cliente y el primer plato
        Cliente cliente = pedido.getCliente();
        Receta receta = pedido.getPlato(0);

        // Cambia las imágenes y texto de la vista
        URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
        ImageIcon nuevoIcono = new ImageIcon(iconURL);
        vista.mCliente.setIcon(nuevoIcono);
        vista.mNombreCliente.setText(cliente.getNombre());

        iconURL = getClass().getResource("/Vista/img/" + receta.getFoto());
        nuevoIcono = new ImageIcon(iconURL);
        vista.mPedido.setIcon(nuevoIcono);
    }*/
    /**
     * Imprime la lista de pedidos en el apartado de pedidos
     */
    public void imprimirPedidos() {
        StringBuilder sb = new StringBuilder();
        for (Pedido p : modelo.listaPedidos) {
            if (p.getEstado() == 1) {
                sb.append(p.getCliente().getNombre()).append(": ");
                for (int i = 0; i < p.getPedido().size(); i++) {
                    sb.append(p.getPedido().get(i).getNombre());
                    if (i < p.getPedido().size() - 1) {
                        sb.append(", ");
                    } else {
                        sb.append(".");
                    }
                }
                sb.append("\n");
            }
        }
        vista.pPedidos.setText(sb.toString());
    }

    /**
     * Añade las imágenes de las diferentes recetas a sus botones
     * correspondientes
     */
    public void imagenesRecetas() {
        setIconoEscalado(vista.cReceta0, "/Vista/img/receta0.png");
        setIconoEscalado(vista.cReceta1, "/Vista/img/receta1.png");
        setIconoEscalado(vista.cReceta2, "/Vista/img/receta2.png");
        setIconoEscalado(vista.cReceta3, "/Vista/img/receta3.png");
        setIconoEscalado(vista.cReceta4, "/Vista/img/receta4.png");
        setIconoEscalado(vista.cReceta5, "/Vista/img/receta5.png");
        setIconoEscalado(vista.cReceta6, "/Vista/img/receta6.png");
        setIconoEscalado(vista.cReceta7, "/Vista/img/receta7.png");
        setIconoEscalado(vista.cReceta8, "/Vista/img/receta8.png");
        setIconoEscalado(vista.cReceta9, "/Vista/img/receta9.png");
        setIconoEscalado(vista.cReceta10, "/Vista/img/receta10.png");
        setIconoEscalado(vista.cReceta11, "/Vista/img/receta11.png");
    }

    /**
     * Escala los iconos para algunos botones que son más pequeños
     *
     * @param boton botón a actualizar
     * @param ruta imagen con la que se le actualiza
     */
    private void setIconoEscalado(JButton boton, String ruta) {
        boton.setText(""); // elimina texto si lo hay

        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setVerticalAlignment(SwingConstants.CENTER);

        // Icono original
        URL url = getClass().getResource(ruta);
        ImageIcon iconoOriginal = new ImageIcon(url);

        int ancho = boton.getWidth();
        int alto = boton.getHeight();

        if (ancho == 0 || alto == 0) {
            ancho = 100;
            alto = 100;
        }

        // Escalar imagen
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        // Crear icono escalado con descripcion
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        // Extraer el nombre de archivo de la ruta (despues de la ultima /)
        String descripcion = ruta.substring(ruta.lastIndexOf("/") + 1);
        iconoEscalado.setDescription(descripcion);

        // Asignar icono al boton
        boton.setIcon(iconoEscalado);
    }

    /**
     * Añade los clientes que tienen un pedido pendiente al combo box de la
     * cocina para poder entregarles los pedidos
     */
    public void actualizarComboClientes() {
        HashSet<String> pedidos = new HashSet<>();

        // Recorrer listaPedidos para filtrar clientes con pedidos estado 1
        for (Pedido p : modelo.listaPedidos) {
            if (p.getEstado() == 1) {
                String nombreCliente = p.getCliente().getNombre();
                pedidos.add(nombreCliente);
            }
        }

        // Limpiar combo box antes de agregar
        vista.cCliente.removeAllItems();

        // Agregar los nombres sin duplicados al combo box
        for (String nombre : pedidos) {
            vista.cCliente.addItem(nombre);
        }
    }

    /**
     * Añadir una receta a la bandeja de pedidos para entregar
     *
     * @param boton el botón pulsado, correspondiente a la receta que se quiere
     * añadir
     */
    private void agregarEntrega(JButton boton) {
        Icon iconoReceta = boton.getIcon();

        if (iconoReceta == null) {
            // No hay imagen en el botón pulsado, no hacer nada
            return;
        }

        if (vista.cEntrega1.getIcon() == null) {
            vista.cEntrega1.setIcon(iconoReceta);
            modelo.addBandeja(iconoReceta.toString());
        } else if (vista.cEntrega2.getIcon() == null) {
            vista.cEntrega2.setIcon(iconoReceta);
            modelo.addBandeja(iconoReceta.toString());
        } else if (vista.cEntrega3.getIcon() == null) {
            vista.cEntrega3.setIcon(iconoReceta);
            modelo.addBandeja(iconoReceta.toString());
        }
        // Si están todos ocupados, no se añade nada
    }

    /**
     * Quitar una receta de la bandeja de pedidos
     *
     * @param boton el botón que se ha pulsado para llegar aquí y del que hay
     * que eliminar la foto
     */
    private void quitar(JButton boton) {
        Icon icono = boton.getIcon();
        if (icono == null) {
            // No hay imagen en el botón pulsado, no hacer nada
            return;
        } else {
            modelo.quitarBandeja(icono.toString());
            boton.setIcon(null);
        }
    }

    private void servir() {
        System.out.println(vista.cCliente.getSelectedItem().toString());
        boolean exito = modelo.servirPedido(vista.cCliente.getSelectedItem().toString());
        if (exito) {
            //Limpiamos la bandeja
            vista.cEntrega1.setIcon(null);
            vista.cEntrega2.setIcon(null);
            vista.cEntrega3.setIcon(null);

            //Actualizamos los pedidos y combos
            actualizarComboClientes();
            imprimirPedidos();
            //Actualizar puntuación
            vista.mDinero.setText("Dinero: " + modelo.dinero + "€");
            vista.cDinero.setText("Dinero: " + modelo.dinero + "€");
        }

        //Si los fallos disponibles llegan a 0, game over
        if (modelo.fallosDisponibles == 0) {
            modelo.generadorPedidos.stop();
            System.out.println("No hay fallos");
            JOptionPane.showMessageDialog(vista, "¡Game Over! Dinero conseguido: " + modelo.dinero + "€", "Fin del juego", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println("Boton pulsado: " + cmd);
        switch (cmd) {
            case "cambio":
                cambiarPedido();
                imprimirPedidos();
                actualizarComboClientes();
                iniciarActualizadorVista();
                break;
            case "agregar": {
                JButton boton = (JButton) e.getSource();
                agregarEntrega(boton);
                break;
            }
            case "quitar": {
                JButton boton = (JButton) e.getSource();
                quitar(boton);
                break;
            }
            case "servir":
                servir();
                break;
            default:
                break;
        }

    }
}
