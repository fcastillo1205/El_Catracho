package com.example.elcatracho;

import java.sql.Blob;

public class Usuarios {
    private String id;
    private String nombre;
    private String username;
    private String password;
    private String telefono;
    private String correo;
    private String imagen;

    public Usuarios(String id, String username, String password, String telefono, String correo ) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.telefono = telefono;
        this.correo = correo;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
