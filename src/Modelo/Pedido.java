package Modelo;


import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mnieves.domnav
 */
public class Pedido {

    protected String cliente; //ID del cliente
    protected List<String> pedido; //Lista de IDs de los diferentes platos en el pedido

    /**
     * Contructor de un pedido, crea uno nuevo a partir del ID del cliente
     *
     * @param cliente ID del cliente que hace el pedido
     */
    public Pedido(String cliente) {
        this.cliente = cliente;
        this.pedido = new ArrayList<>();
    }

    /**
     * Contructor de un pedido, crea uno nuevo a partir del ID del cliente y la
     * lista de IDs de los platos que componen el pedido
     *
     * @param cliente ID del cliente que hace el pedido
     * @param pedido Lista de IDs de los diferentes platos en el pedido
     */
    public Pedido(String cliente, List<String> pedido) {
        this.cliente = cliente;
        this.pedido = pedido;
    }

    /**
     * Getter del cliente
     *
     * @return ID del cliente que hace el pedido
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Getter del pedido
     *
     * @return Arraylist con todos los pedidos
     */
    public List<String> getPedido() {
        return pedido;
    }

    /**
     * Getter de un plato en concreto
     *
     * @param indice Índice del plato que buscamos, debe existir
     * @return ID del plato en la posición dada
     */
    public String getPlato(int indice) {
        return pedido.get(indice);
    }

    /**
     * Añadir un nuevo plato al arraylist del pedido
     *
     * @param plato Plato a añadir
     */
    public void addPlato(String plato) {
        this.pedido.add(plato);
    }

    /**
     * Setter del cliente
     *
     * @param cliente ID del cliente
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    
}
