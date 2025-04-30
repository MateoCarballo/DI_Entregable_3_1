/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UsuariosModelo {

    private static final String ARCHIVO_USUARIOS = "Usuarios.csv";
    private final List<Usuario> usuarios;

    public UsuariosModelo() {
        usuarios = cargarUsuarios();
    }

    public void registrarUsuario(String nombre, String contrasena) {
        usuarios.add(new Usuario(nombre, contrasena));
        guardarUsuarios();
    }

    public Usuario iniciarSesion(String nombre, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equals(nombre) && u.verificarContrasena(contrasena)) {
                return u;
            }
        }
        return null;
    }
    
    public boolean existeNombreParaRegistrar(String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void guardarUsuarios() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            out.writeObject(usuarios);
        } catch (IOException e) {
        }
    }

    private List<Usuario> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Usuario>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
