package controlador;

import vista.VentanaJuego;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class JuegoController {

    private final VentanaJuego vista;
    private Timer mostrarCartasTimer;
    private Timer voltearCartasTimer;
    private final GameState estadoJuego;
    private final Map<JButton, Integer> posicionesCartas;
    private ImageIcon interrogacionIcon;
    private JButton[] cartasSeleccionadas = new JButton[2];

    // Clase interna para manejar el estado del juego
    private class GameState {

        private final List<String> imagenesParaJuego;
        private int paresEncontrados;
        private long tiempoInicio;
        private final String[] imagenesDisponibles = {
            "001.png", "002.png", "003.png", "004.png", "005.png", "006.png",
            "007.png", "008.png", "009.png", "010.png", "011.png", "012.png"
        };

        public GameState() {
            this.imagenesParaJuego = new ArrayList<>();
            this.paresEncontrados = 0;
        }

        public void iniciarNuevoJuego() {
            imagenesParaJuego.clear();
            for (String img : imagenesDisponibles) {
                imagenesParaJuego.add(img);
                imagenesParaJuego.add(img);
            }
            Collections.shuffle(imagenesParaJuego);
            paresEncontrados = 0;
            tiempoInicio = System.currentTimeMillis();
        }

        public boolean verificarPareja(int index1, int index2) {
            if (index1 >= imagenesParaJuego.size() || index2 >= imagenesParaJuego.size()) {
                return false;
            }
            return imagenesParaJuego.get(index1).equals(imagenesParaJuego.get(index2));
        }

        public void incrementarPares() {
            paresEncontrados++;
        }

        public boolean juegoCompletado() {
            return paresEncontrados == (imagenesParaJuego.size() / 2);
        }

        public long getTiempoTranscurrido() {
            return (System.currentTimeMillis() - tiempoInicio) / 1000;
        }

        public String getImagenEnPosicion(int posicion) {
            if (posicion < 0 || posicion >= imagenesParaJuego.size()) {
                throw new IllegalArgumentException("Posición inválida: " + posicion);
            }
            return imagenesParaJuego.get(posicion);
        }
    }

    public JuegoController(VentanaJuego vista) {
        this.vista = vista;
        this.estadoJuego = new GameState();
        this.posicionesCartas = new HashMap<>();
        configurarJuego();
    }

    private void configurarJuego() {
        // Configurar mapeo de posiciones
        JButton[] botones = vista.getTodosLosBotones();
        for (int i = 0; i < botones.length; i++) {
            posicionesCartas.put(botones[i], i);
        }

        // Configurar interrogación icon con verificación
        try {
            interrogacionIcon = escalarImagen("/img/interrogacion.png", 100, 100);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar imágenes: " + e.getMessage());
            return;
        }

        // Configurar botón comenzar
        vista.getBotonComenzar().addActionListener(e -> iniciarPartida());

        // Configurar listeners para cartas
        for (JButton carta : vista.getTodosLosBotones()) {
            carta.addActionListener(new CartaClickListener());
            carta.setEnabled(false);
            carta.setIcon(interrogacionIcon);
        }
    }

    private void iniciarPartida() {
        resetGame();
        estadoJuego.iniciarNuevoJuego();

        // Validar que hay suficientes botones
        if (vista.getTodosLosBotones().length < estadoJuego.imagenesParaJuego.size()) {
            JOptionPane.showMessageDialog(vista,
                    "No hay suficientes botones para todas las imágenes");
            return;
        }

        // Mostrar cartas al inicio
        for (int i = 0; i < vista.getTodosLosBotones().length; i++) {
            try {
                String nombreImagen = estadoJuego.getImagenEnPosicion(i);
                vista.getTodosLosBotones()[i].setIcon(escalarImagen("/img/" + nombreImagen, 100, 100));
                vista.getTodosLosBotones()[i].setEnabled(false);
            } catch (IllegalArgumentException e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
            }
        }

        // Temporizador para ocultar cartas
        mostrarCartasTimer = new Timer(2000, e -> {
            for (JButton boton : vista.getTodosLosBotones()) {
                boton.setIcon(interrogacionIcon);
                boton.setEnabled(true);
            }
        });
        mostrarCartasTimer.setRepeats(false);
        mostrarCartasTimer.start();
    }

    private class CartaClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton cartaClickeada = (JButton) e.getSource();
            
            if (cartasSeleccionadas[0] == null) {
                cartasSeleccionadas[0] = cartaClickeada;
                mostrarImagenCarta(cartaClickeada);
            } else if (cartasSeleccionadas[1] == null && cartaClickeada != cartasSeleccionadas[0]) {
                cartasSeleccionadas[1] = cartaClickeada;
                mostrarImagenCarta(cartaClickeada);
                verificarPareja();
            }
        }
        
        private void mostrarImagenCarta(JButton carta) {
            int posicion = posicionesCartas.get(carta);
            String nombreImagen = estadoJuego.getImagenEnPosicion(posicion);
            ImageIcon icono = escalarImagen("/img/" + nombreImagen, 100, 100);
            vista.mostrarCarta(carta, icono);
        }
    }

    private void verificarPareja() {
        JButton carta1 = cartasSeleccionadas[0];
        JButton carta2 = cartasSeleccionadas[1];

        voltearCartasTimer = new Timer(1000, e -> {
            int index1 = posicionesCartas.get(carta1);
            int index2 = posicionesCartas.get(carta2);

            boolean sonPareja = estadoJuego.verificarPareja(index1, index2);

            if (sonPareja) {
                vista.habilitarCarta(carta1, false);
                vista.habilitarCarta(carta2, false);
                estadoJuego.incrementarPares();
            } else {
                vista.mostrarCarta(carta1, interrogacionIcon);
                vista.mostrarCarta(carta2, interrogacionIcon);
                vista.habilitarCarta(carta1, true);
                vista.habilitarCarta(carta2, true);
            }

            // Resetear selección
            cartasSeleccionadas[0] = null;
            cartasSeleccionadas[1] = null;

            if (estadoJuego.juegoCompletado()) {
                vista.mostrarMensajeFin("¡Ganaste! Tiempo: "
                        + estadoJuego.getTiempoTranscurrido() + " segundos");
            }
        });
        voltearCartasTimer.setRepeats(false);
        voltearCartasTimer.start();
    }

    public ImageIcon escalarImagen(String ruta, int ancho, int alto) {
        if (getClass().getResource(ruta) == null) {
            throw new IllegalArgumentException("Imagen no encontrada: " + ruta);
        }
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));
        java.awt.Image imagen = iconoOriginal.getImage().getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    public void resetGame() {
        if (mostrarCartasTimer != null) {
            mostrarCartasTimer.stop();
        }
        if (voltearCartasTimer != null) {
            voltearCartasTimer.stop();
        }

        for (JButton boton : vista.getTodosLosBotones()) {
            boton.setIcon(interrogacionIcon);
            boton.setEnabled(false);
        }

        vista.getBotonComenzar().setEnabled(true);
    }
}
