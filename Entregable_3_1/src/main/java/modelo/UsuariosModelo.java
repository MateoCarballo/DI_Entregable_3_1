package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuariosModelo {

    private static final String ARCHIVO_USUARIOS = "src//main//persistencia//Usuarios.dat";
    private final List<Usuario> usuarios;

    public UsuariosModelo() {
        usuarios = cargarUsuarios();
        //Metodo para ver el contenido del archivo y saber si la lista tiene las partidas o no 
        mostrarPartidasConsola();
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
            try {
                archivo.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(UsuariosModelo.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Usuario>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void agregarNuevaPartida(String userName, long tiempoPartida) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equals(userName)) {
                u.agregarPartida(new Partida(u.getNombre(), tiempoPartida));
            }
        }
        guardarUsuarios();
    }

    private void mostrarPartidasConsola() {
        for (Usuario usuario : usuarios) {
            System.out.println("Usuario: " + usuario.getNombre());
            System.out.println("Partidas:");
            List<Partida> partidas = usuario.getPartidas();
            for (Partida partida : partidas) {
                System.out.println("  Fecha: " + partida.getFecha());
                System.out.println("  Tiempo: " + partida.getTiempoEnSegundos() + " segundos");
            }

            System.out.println();  // Salto de línea entre usuarios
        }
    }
}
