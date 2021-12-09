package com.example.elcatracho;

public class Cotizacion {
    private String id;
    private String vehiculo;
    private String tipoServicio;
    private String tipoUbicacion;
    private String fecha;
    private String hora;
    private String costo;
    private String usuario;
    private String latitud;
    private String longitud;

    public Cotizacion(String id, String vehiculo, String tipoServicio, String tipoUbicacion, String fecha, String hora, String costo, String usuario, String latitud, String longitud) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.tipoServicio = tipoServicio;
        this.tipoUbicacion = tipoUbicacion;
        this.fecha = fecha;
        this.hora = hora;
        this.costo = costo;
        this.usuario = usuario;
        this.latitud = latitud;
        this.longitud = longitud;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getTipoUbicacion() {
        return tipoUbicacion;
    }

    public void setTipoUbicacion(String tipoUbicacion) {
        this.tipoUbicacion = tipoUbicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
