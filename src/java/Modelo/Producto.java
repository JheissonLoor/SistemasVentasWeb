/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class Producto {
    int id;
    String dni;
    String nom;
    String tel;
    String estado;
    String user;
    String Dir;
    String Es;
    double Pre;
    int Stock;

    public Producto() {
    }

    public Producto(int id, String dni, String nom, String tel, String estado, String user, String Dir, String Es, double Pre, int Stock) {
        this.id = id;
        this.dni = dni;
        this.nom = nom;
        this.tel = tel;
        this.estado = estado;
        this.user = user;
        this.Dir = Dir;
        this.Es = Es;
        this.Pre = Pre;
        this.Stock = Stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDir() {
        return Dir;
    }

    public void setDir(String Dir) {
        this.Dir = Dir;
    }

    public String getEs() {
        return Es;
    }

    public void setEs(String Es) {
        this.Es = Es;
    }

    public double getPre() {
        return Pre;
    }

    public void setPre(double Pre) {
        this.Pre = Pre;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    

 

}
