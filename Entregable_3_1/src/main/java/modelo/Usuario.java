
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private final String nombre;
    private final String contrasena;
    private final List<Partida> partidas;

    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.partidas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public void agregarPartida(Partida partida) {
        partidas.add(partida);
    }

    public List<Partida> getPartidas() {
        return partidas;
    }
}

