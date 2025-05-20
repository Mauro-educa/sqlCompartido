package Modelo;

import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * @author Ivan.siefer
 * @author mnieves.domnav
 */
public class Pedido {

    protected Cliente cliente; //ID del cliente
    protected ArrayList<Receta> pedido; //Lista con los diferentes platos en el pedido
    protected int estado; //Estado del pedido. 0: no recogido. 1: recogido. 2: completado.

    /**
     * Contructor de un pedido, crea uno nuevo a partir del ID del cliente
     *
     * @param cliente ID del cliente que hace el pedido
     */
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.pedido = new ArrayList<Receta>();
        this.estado = 0;
    }

    /**
     * Contructor de un pedido, crea uno nuevo a partir del ID del cliente y la
     * lista de IDs de los platos que componen el pedido
     *
     * @param cliente ID del cliente que hace el pedido
     * @param pedido Lista de IDs de los diferentes platos en el pedido
     */
    public Pedido(Cliente cliente, ArrayList<Receta> pedido) {
        this.cliente = cliente;
        this.pedido = pedido;
        this.estado = 0;
    }

    /**
     * Getter del cliente
     *
     * @return ID del cliente que hace el pedido
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Getter del pedido
     *
     * @return Arraylist con todos los pedidos
     */
    public List<Receta> getPedido() {
        return pedido;
    }

    /**
     * Getter de un plato en concreto
     *
     * @param indice Índice del plato que buscamos, debe existir
     * @return ID del plato en la posición dada
     */
    public Receta getPlato(int indice) {
        return pedido.get(indice);
    }

    /**
     * Getter del estado
     *
     * @return el estado actual del pedido
     */
    public int getEstado() {
        return estado;
    }

    /**
     * Añadir un nuevo plato al arraylist del pedido
     *
     * @param plato Plato a añadir
     */
    public void addPlato(Receta plato) {
        this.pedido.add(plato);
    }

    /**
     * Setter del cliente
     *
     * @param cliente ID del cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Setter del estado
     *
     * @param estado estado al que cambiar el pedido
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

}
