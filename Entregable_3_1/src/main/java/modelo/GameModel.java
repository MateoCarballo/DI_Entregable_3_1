package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;

public class GameModel {
    private List<String> imagenesParaJuego;
    private List<JButton> cartasSeleccionadas;
    private int paresEncontrados;
    private long tiempoInicio;

    public GameModel() {
        cartasSeleccionadas = new ArrayList<>();
    }

    public void iniciarJuego(String[] imagenesDisponibles) {
        imagenesParaJuego = new ArrayList<>();
        for (String img : imagenesDisponibles) {
            imagenesParaJuego.add(img);
            imagenesParaJuego.add(img); // Duplicamos para tener pares
        }
        Collections.shuffle(imagenesParaJuego);
        paresEncontrados = 0;
        tiempoInicio = System.currentTimeMillis();
    }

    public String getImagenEnPosicion(int posicion) {
        return imagenesParaJuego.get(posicion);
    }

    public void agregarCartaSeleccionada(JButton carta) {
        cartasSeleccionadas.add(carta);
    }

    public boolean sonPareja() {
        String img1 = imagenesParaJuego.get(imagenesParaJuego.indexOf(cartasSeleccionadas.get(0).getText()));
        String img2 = imagenesParaJuego.get(imagenesParaJuego.indexOf(cartasSeleccionadas.get(1).getText()));
        return img1.equals(img2);
    }

    public void limpiarSeleccion() {
        cartasSeleccionadas.clear();
    }

    public boolean juegoCompletado() {
        return paresEncontrados == imagenesParaJuego.size() / 2;
    }

    public long getTiempoTranscurrido() {
        return (System.currentTimeMillis() - tiempoInicio) / 1000;
    }
}