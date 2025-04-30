package controlador;

import vista.VentanaPrimerosPasos;
import vista.VentanaRegistro;
import vista.VentanaTablaRecords;
import vista.VentanaJuego;
import vista.VentanaInicioSesion;
import vista.VentanaCreditos;
import vista.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class Controller implements ActionListener {
    
    /*
    Instancias de los internal frames y de la ventana contenedora. 
    Tambien tenemos una variable booleana para evitar que se abra una ventana si 
    existe una instancia de alguna o que se abra otra si existe una abierta.
    */
    boolean algunaVentanaAbierta = false;
    VentanaPrincipal ventanaPpal;
    VentanaRegistro ventanaRegistro;
    VentanaInicioSesion ventanaInicioSesion;
    VentanaJuego ventanaJ;
    VentanaTablaRecords ventanaRecords;
    VentanaCreditos ventanaCreditos;
    VentanaPrimerosPasos ventanaPrimerosPasos;
    
    public Controller() {
        ventanaPpal = new VentanaPrincipal(this);
        centrarVentana(ventanaPpal);
        ventanaPpal.setVisible(true);
    }
    
    public static void centrarVentana(JFrame ventana) {
        // Obtener la resolución de la pantalla
        java.awt.Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        // Calcular la posición para centrar la ventana
        int x = (pantalla.width - ventana.getWidth()) / 2;
        int y = (pantalla.height - ventana.getHeight()) / 2;

        // Establecer la posición de la ventana
        ventana.setLocation(x, y);
    }

    
    /*
    Aqui recogemos mediante comando que boton se pulsa dentro del menu y si existe alguna intancia ya no pasa por el switch
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        //SI tenemos alguna ventana abierta no abre otra hata que se cierre, gestionado mas abajo con un listener del internal frame
       
        if (algunaVentanaAbierta) {
            System.out.println("Ya hay una ventana abierta, cierra primero la actual.");
            return;
        }

        ventanaPpal.deleteFirstMessage(); 

        switch (comando) {
            case "Inicio sesion" -> abrirVentanaInicioSesion();
            case "Registro usuario" -> abrirVentanaRegistro();
            case "Abrir juego" -> abrirVentanaJuego();
            case "Records" -> abrirVentanaRecords();
            case "Creditos" -> abrirVentanaCreditos();
            case "Primeros pasos" -> abrirVentanaAyuda();
            default -> System.out.println("Acción desconocida: " + comando);
        }
    }
    
    /*
    Metodos para abrir cada uno de los internal frames
    */

    private void abrirVentanaInicioSesion() {
        ventanaInicioSesion = new VentanaInicioSesion();
        agregarVentana(ventanaInicioSesion);
    }

    private void abrirVentanaRegistro() {
        ventanaRegistro = new VentanaRegistro();
        agregarVentana(ventanaRegistro);
    }

    private void abrirVentanaJuego() {
        ventanaJ = new VentanaJuego();
        new JuegoController(ventanaJ); // Pasamos la ventana al controlador específico
        agregarVentana(ventanaJ);
    }

    private void abrirVentanaRecords() {
        ventanaRecords = new VentanaTablaRecords();
        agregarVentana(ventanaRecords);
    }

    private void abrirVentanaCreditos() {
        ventanaCreditos = new VentanaCreditos();
        agregarVentana(ventanaCreditos);
    }

    private void abrirVentanaAyuda() {
        ventanaPrimerosPasos = new VentanaPrimerosPasos();
        agregarVentana(ventanaPrimerosPasos);
    }
    
    /*
    Escuchador de los internal frame para gestionar el cierre y apertura si existe una ventana abierta
    */

    private void agregarVentana(javax.swing.JInternalFrame ventana) {
        //Aqui gestionamos el escuchador del internal frame de modo que si cerramos la ventana ejecute el metodo limpiarReferenciasVentanas para poder asi instanciar otro nuevo internal frame
        ventana.setVisible(true);
        ventanaPpal.getjDesktopPane1().add(ventana);
        algunaVentanaAbierta = true;

        ventana.addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) { }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                limpiarReferenciasVentanas();
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent e) { }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) { }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) { }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) { }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) { }
        });
    }

    private void limpiarReferenciasVentanas() {
        ventanaInicioSesion = null;
        ventanaRegistro = null;
        ventanaJ = null;
        ventanaRecords = null;
        ventanaCreditos = null;
        algunaVentanaAbierta = false;
    }
}
