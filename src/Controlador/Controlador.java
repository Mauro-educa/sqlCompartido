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
 * @author mnieves.domnav
 */
public class Controlador implements ActionListener {

    private static Vista vista;
    private static Modelo modelo;
    private static Sound sound;
    private Timer actualizadorVista;
    private Pedido pedidoMostrado = null;
    public int tiempoRestante = 0;

    private boolean enPausa = false;

    /**
     * Constructor del controlador
     *
     * @param vista la vista
     * @param modelo el modelo
     */
    public Controlador(Vista vista, Modelo modelo, Sound sound) {
        this.vista = vista;
        this.modelo = modelo;
        this.sound = sound;
        vista.setControlador(this);
        modelo.setControlador(this);
        vista.arranca();

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
        iniciarActualizadorVista();
    }

    public static void main(String[] args) {
        Modelo mod = new Modelo();
        Vista v = new Vista();

        //Reproducir música de fondo
        Sound s = new Sound("/Vista/Img/musica.wav");
        new Controlador(v, mod, s);
        sound.play();
    }

    public void pausarContinuar() {
        if (!enPausa) {
            tiempoRestante = modelo.tiempoActual - modelo.tiempo;
            System.out.println(tiempoRestante);
            // Pausar los temporizadores
            actualizadorVista.stop();
            modelo.generadorPedidos.stop();
            enPausa = true;

            // Mostrar cuadro de diálogo con dos opciones
            Object[] opciones = {"Reanudar", "Salir"};
            int seleccion = JOptionPane.showOptionDialog(
                    vista, // Componente padre
                    "El juego está en pausa", // Mensaje
                    "Pausa", // Título
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, // Ícono
                    opciones, // Botones personalizados
                    opciones[0] // Botón por defecto
            );

            if (seleccion == 0) {
                // Reanudar
                actualizadorVista.start();
                modelo.generadorPedidos.start(); // Si pausaste también el generador
                enPausa = false;
            } else {
                // Salir del juego
                System.exit(0);
            }
        }
    }

    public void pedidoGenerado() {
        //Actualizar cola y pedidos pendientes
        vista.mPendientes.setText("Pedidos pendientes: " + modelo.pendientes());
        vista.mCola.setText("Cola: " + modelo.cola());
        vista.cPendientes.setText("Pedidos pendientes: " + modelo.pendientes());
        vista.cCola.setText("Cola: " + modelo.cola());
    }

    /**
     * Toma un pedido y muestra el siguiente
     */
    public void cambiarPedido() {
        if (pedidoMostrado != null) {
            modelo.tomarPedido(pedidoMostrado); // Solo tomamos el que se está mostrando

            Cliente cliente = pedidoMostrado.getCliente();
            Receta receta = pedidoMostrado.getPlato(0);
            Receta receta2 = pedidoMostrado.getPlato(1);
            Receta receta3 = pedidoMostrado.getPlato(2);

            //Información del cliente
            URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
            vista.mCliente.setIcon(new ImageIcon(iconURL));
            vista.mNombreCliente.setText(cliente.getNombre());

            //Imágenes de las recetas pedidas
            iconURL = getClass().getResource("/Vista/img/" + receta.getFoto());
            vista.mPedido1.setIcon(new ImageIcon(iconURL));
            if (receta2 != null) {
                iconURL = getClass().getResource("/Vista/img/" + receta2.getFoto());
                vista.mPedido.setIcon(new ImageIcon(iconURL));
            } else {
                vista.mPedido.setIcon(null);
            }
            if (receta3 != null) {
                iconURL = getClass().getResource("/Vista/img/" + receta3.getFoto());
                vista.mPedido2.setIcon(new ImageIcon(iconURL));
            } else {
                vista.mPedido2.setIcon(null);
            }

            pedidoMostrado = null; // Ya fue tomado

            // Mostrar el siguiente pedido si existe
            Pedido siguiente = modelo.getPedidoPendiente();
            if (siguiente != null) {
                actualizarVista(siguiente);
            } else {
                // Si no hay más pedidos pendientes, limpiar vista
                vista.mCliente.setIcon(null);
                vista.mPedido.setIcon(null);
                vista.mPedido1.setIcon(null);
                vista.mPedido2.setIcon(null);
                vista.mNombreCliente.setText("");
            }

        } else {
            // Si no se estaba mostrando ninguno, mostrar el primero pendiente
            Pedido pedido = modelo.getPedidoPendiente();
            if (pedido != null) {
                actualizarVista(pedido);
            } else {
                vista.mCliente.setIcon(null);
                vista.mPedido.setIcon(null);
                vista.mPedido1.setIcon(null);
                vista.mPedido2.setIcon(null);
                vista.mNombreCliente.setText("");
            }
        }
        pedidoGenerado();
    }

    /**
     * Llama a actualizar la vista periódicamente
     */
    public void iniciarActualizadorVista() {
        actualizadorVista = new Timer(100, e -> {
            boolean vistaVacia = vista.mCliente.getIcon() == null && vista.mPedido.getIcon() == null;
            Pedido pedidoPendiente = modelo.getPedidoPendiente();

            if (vistaVacia && pedidoPendiente != null) {
                actualizarVista(pedidoPendiente);
            }
        });
        actualizadorVista.start();
    }

    /**
     * Actualiza la vista con las imágenes del cliente el pedido siguiente
     *
     * @param pedido pedido siguiente para tomar
     */
    private void actualizarVista(Pedido pedido) {
        if (pedido == null) {
            vista.mCliente.setIcon(null);
            vista.mNombreCliente.setText("");
            vista.mPedido.setIcon(null);
            vista.mPedido1.setIcon(null);
            vista.mPedido2.setIcon(null);
            pedidoMostrado = null;
            return;
        }

        // Evitamos mostrar otro si ya se está mostrando uno pendiente
        if (pedidoMostrado != null) {
            return;
        }

        Cliente cliente = pedido.getCliente();
        Receta receta = pedido.getPlato(0);
        Receta receta2 = pedido.getPlato(1);
        Receta receta3 = pedido.getPlato(2);

        //Cambia la información del cliente
        URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
        ImageIcon nuevoIcono = new ImageIcon(iconURL);
        vista.mCliente.setIcon(nuevoIcono);
        vista.mNombreCliente.setText(cliente.getNombre());

        //Cambia los iconos de las recetas
        iconURL = getClass().getResource("/Vista/img/" + receta.getFoto());
        nuevoIcono = new ImageIcon(iconURL);
        vista.mPedido1.setIcon(nuevoIcono);
        if (receta2 != null) {
            iconURL = getClass().getResource("/Vista/img/" + receta2.getFoto());
            vista.mPedido.setIcon(new ImageIcon(iconURL));
        } else {
            vista.mPedido.setIcon(null);
        }
        if (receta3 != null) {
            iconURL = getClass().getResource("/Vista/img/" + receta3.getFoto());
            vista.mPedido2.setIcon(new ImageIcon(iconURL));
        } else {
            vista.mPedido2.setIcon(null);
        }

        pedidoMostrado = pedido; // Guardamos el pedido mostrado

        pedidoGenerado();
    }

    /**
     * Imprime la lista de pedidos en la ventana de pedidos
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
     * Añade las imágenes de las diferentes recetas en cocina a sus botones
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

        // Recorrer listaPedidos para filtrar clientes con pedidos en estado 1
        for (Pedido p : modelo.listaPedidos) {
            if (p.getEstado() == 1) {
                String nombreCliente = p.getCliente().getNombre();
                pedidos.add(nombreCliente);
            }
        }

        // Limpiar combo box antes de agregar
        vista.cCliente.removeAllItems();

        for (Pedido p : modelo.listaPedidos) {
            if (p.getEstado() == 1) {
                vista.cCliente.addItem(p.getCliente().getNombre());
            }
        }

        pedidoGenerado();
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

    /**
     * Se sirven las recetas de la bandeja a un cliente
     */
    private void servir() {
        // Verificar si hay recetas en la bandeja
        boolean hayRecetas = false;
        for (Receta r : modelo.bandeja) {
            if (r != null) {
                hayRecetas = true;
                break;
            }
        }

        // Si no hay recetas, no hacer nada
        if (!hayRecetas) {
            return;
        }

        // Si no hay cliente seleccionado, solo vaciar la bandeja
        Object seleccionado = vista.cCliente.getSelectedItem();
        if (seleccionado == null || seleccionado.toString().isEmpty()) {
            modelo.vaciarBandeja();
            vista.cEntrega1.setIcon(null);
            vista.cEntrega2.setIcon(null);
            vista.cEntrega3.setIcon(null);
            return;
        }

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

        pedidoGenerado();

        //Si los fallos disponibles llegan a 0, game over
        if (modelo.fallosDisponibles == 0) {
            modelo.generadorPedidos.stop();
            System.out.println("Game over");
            modelo.guardarPartida();
            Modelo mod = new Modelo();
            Vista v = new Vista();
            sound.pause();
            vista.gameov.setSize(1024, 640);
            vista.gameov.setLocationRelativeTo(null);
            vista.gameov.setVisible(true);
            Sound s = new Sound("/Vista/Img/screamer.wav");
            new Controlador(v, mod, s);
            sound.play();
            Timer r = new Timer(7500, e -> {
                JOptionPane.showMessageDialog(vista.gameov, "¡Game Over! Dinero conseguido: " + modelo.dinero + "€", "Fin del juego", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            });
            r.start();
        }
    }

    /**
     * Ejecuta unos métodos u otros según el botón pulsado por el jugador
     *
     * @param e ActionEvent, escucha a la vista
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println("Boton pulsado: " + cmd);
        switch (cmd) {
            case "cambio":
                cambiarPedido();
                imprimirPedidos();
                actualizarComboClientes();
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
            case "pausar": {
                pausarContinuar();
                break;
            }
            default:
                break;
        }

    }
}
