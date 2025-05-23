/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author mnieves.domnav
 */
public class Receta {

    private int codigo;
    private String nombre;
    private double precio;
    private String foto;

    // Constructor vacío
    public Receta() {
    }

    // Constructor con parámetros
    public Receta(int codigo, String nombre, double precio, String foto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.foto = foto;
    }

    // Getter y Setter para codigo
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    // Getter y Setter para nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter para precio
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Getter y Setter para foto
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
