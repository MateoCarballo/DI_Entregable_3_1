/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import controlador.Controller;
import modelo.UsuariosModelo;

/**
 *
 * @author mateo
 */
public class Main {

    public static void main(String[] args) {
        /*
        Esta clase la uso para instanciar el controlador desde el instanciare la
        ventana principal, así como las internal frames dentro de ella.
         */
        
        UsuariosModelo usuariosModelo = new UsuariosModelo();
        Controller controlador = new Controller(usuariosModelo);
        
    }
}
