package com.example.express.models;

public class Contactos {
    private String ContactoId;
    private String Nombre;
    private String Profesion;
    private String Telefono;

    public String getProfesion() {
        return Profesion;
    }

    public void setProfesion(String profesion) {
        Profesion = profesion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Contactos() {
    }

    public String getContactoId() {
        return ContactoId;
    }

    public void setContactoId(String contactoId) {
        ContactoId = contactoId;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
