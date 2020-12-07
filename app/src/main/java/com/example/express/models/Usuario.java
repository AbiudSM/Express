package com.example.express.models;

public class Usuario {
    private String uid;
    private String Nombre;
    private String Profesion;
    private String Servicios;
    private String Cotizacion;
    private String Telefono;
    private String Descripcion;
    private String Imagen;
    private String Correo;
    private int Votantes;
    private float Suma;
    private float Ratio;
    private String Zona;

    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) {
        Zona = zona;
    }

    public int getVotantes() {
        return Votantes;
    }

    public void setVotantes(int votantes) {
        Votantes = votantes;
    }

    public float getSuma() {
        return Suma;
    }

    public void setSuma(float suma) {
        Suma = suma;
    }

    public float getRatio() {
        return Ratio;
    }

    public void setRatio(float ratio) {
        Ratio = ratio;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getProfesion() {
        return Profesion;
    }

    public void setProfesion(String profesion) {
        Profesion = profesion;
    }

    public String getServicios() {
        return Servicios;
    }

    public void setServicios(String servicios) {
        Servicios = servicios;
    }

    public String getCotizacion() {
        return Cotizacion;
    }

    public void setCotizacion(String cotizacion) {
        Cotizacion = cotizacion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
