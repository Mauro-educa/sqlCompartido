/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.*;
import Modelo.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author mnieves.domnav
 */
public class Controlador implements ActionListener {

    private static Vista vista;
    private static Modelo modelo;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        vista.setControlador(this);
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

    public void cambiarCliente() {
        Cliente cliente = modelo.nuevoCliente();
        URL iconURL = getClass().getResource("/Vista/img/" + cliente.getFoto());
        ImageIcon nuevoIcono = new ImageIcon(iconURL);
        vista.mCliente.setIcon(nuevoIcono);
        vista.mNombreCliente.setText(cliente.getNombre());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println("Boton pulsado: " + cmd);
        if (cmd.equals("cambio")) {
            cambiarCliente();
        }
    }
}
