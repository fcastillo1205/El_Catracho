package com.example.elcatracho;

public class restApiMethods {
    private static final String ipaddres = "https://api-carwash.alzir.hn/";
    private static final String GetEndpointUsuario = "crearUsuario.php";
    private static final String GetEndpointGetUsuarios = "listaUsuarios.php";
    private static final String GetEndpointCrearCotizacion = "crearCotizacion.php";
    private static final String GetEndpointGetCotizacion = "listaCotizacion.php";
    private static final String GetEndpointUpdateUsuario = "updateCliente.php";

    public static final String endPointCrearUsuario =  ipaddres  + GetEndpointUsuario;
    public static final String endPointGetUsuarios = ipaddres + GetEndpointGetUsuarios;
    public static final String endPointCrearCotizacion = ipaddres  + GetEndpointCrearCotizacion;
    public static final String endPointGetCotizacion = ipaddres + GetEndpointGetCotizacion;
    public static final String endPointUpdateUsuario = ipaddres + GetEndpointUpdateUsuario;

}
