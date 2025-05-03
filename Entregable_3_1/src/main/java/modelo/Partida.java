
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Partida implements Serializable {
    private final LocalDateTime fecha;
    private final long tiempoEnSegundos;
    private final String jugador;

    public Partida(String jugador, long tiempoEnSegundos) {
        this.fecha = LocalDateTime.now();
        this.tiempoEnSegundos = tiempoEnSegundos;
        this.jugador = jugador;
    }

    public String getJugador() {
        return jugador;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public long getTiempoEnSegundos() {
        return tiempoEnSegundos;
    }
}

