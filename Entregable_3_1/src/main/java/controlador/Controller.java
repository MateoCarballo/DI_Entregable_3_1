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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import modelo.Usuario;
import modelo.UsuariosModelo;

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
    //Comentario para commit 
    UsuariosModelo usuariosModelo;

    Usuario player = null;

    public Controller(UsuariosModelo uModelo) {
        this.usuariosModelo = uModelo;
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

        //Para que ejecute el codigo de sacar el joptionpane si no introducimos credenciales validos tenemos que hacer esta ñapa
        if (comando.equals("Boton Inicio Sesion Pulsado")) {
            iniciarSesion();
            return;
        }

        if (comando.equals("Boton Registro Usuario Pulsado")) {
            registrarUsuario();
            return;
        }

        //SI tenemos alguna ventana abierta no abre otra hata que se cierre, gestionado mas abajo con un listener del internal frame
        if (algunaVentanaAbierta) {
            System.out.println("Ya hay una ventana abierta, cierra primero la actual.");
            return;
        }

        ventanaPpal.deleteFirstMessage();

        switch (comando) {
            case "Inicio sesion" ->
                abrirVentanaInicioSesion();
            case "Registro usuario" ->
                abrirVentanaRegistro();
            case "Abrir juego" ->
                abrirVentanaJuego();
            case "Records" ->
                abrirVentanaRecords();
            case "Creditos" ->
                abrirVentanaCreditos();
            case "Primeros pasos" ->
                abrirVentanaAyuda();
            default ->
                System.out.println("Acción desconocida: " + comando);
        }
    }

    /*
    Metodos para abrir cada uno de los internal frames
     */
    private void abrirVentanaInicioSesion() {
        ventanaInicioSesion = new VentanaInicioSesion(this);
        agregarVentana(ventanaInicioSesion);
    }

    private void abrirVentanaRegistro() {
        ventanaRegistro = new VentanaRegistro(this);
        agregarVentana(ventanaRegistro);
    }

    private void abrirVentanaJuego() {
        ventanaJ = new VentanaJuego();
        if (player != null){
            ventanaJ.setjLabelJugadorContenido(player.getNombre());
        } else {
            ventanaJ.setjLabelJugadorContenido("Sin registrar");
        }
        new JuegoController(ventanaJ, usuariosModelo); // Pasamos la ventana al controlador específico
        agregarVentana(ventanaJ);
    }

    private void abrirVentanaRecords() {
        ventanaRecords = new VentanaTablaRecords();
        ventanaRecords.getjTableRecordGeneral().setModel(usuariosModelo.obtenerModeloTablaGeneral());
        if(player != null){
            ventanaRecords.getjTableRecordPersonal().setModel(usuariosModelo.obtenerModeloTablaJugador(player.getNombre()));
        }
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
            public void internalFrameClosing(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                limpiarReferenciasVentanas();
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
            }
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

    private void iniciarSesion() {
        System.out.println("Entrado en metodo iniciar sesion");
        Usuario instanciaJugador = usuariosModelo.iniciarSesion(ventanaInicioSesion.getjTextFieldnombreUsuario().getText(), ventanaInicioSesion.getjTextFieldClaveUsuario().getText());

        if (instanciaJugador == null) {
            JOptionPane.showMessageDialog(null, "No exiten jugadores con estos credenciales.");
        } else {
            player = instanciaJugador;
            ventanaInicioSesion.dispose();
            limpiarReferenciasVentanas();
            mostraMensaje("Inicio de sesion completo para el usuario, " + player.getNombre());
            abrirVentanaJuego();
        }
    }

    private void atajoIniciarSesion(String nombreUsuario, String clave) {
        System.out.println("Entrado en metodo iniciar sesion");
        Usuario instanciaJugador = usuariosModelo.iniciarSesion(nombreUsuario, clave);

        if (instanciaJugador == null) {
            JOptionPane.showMessageDialog(null, "No exiten jugadores con estos credenciales.");
        } else {
            player = instanciaJugador;
            ventanaRegistro.dispose();
            limpiarReferenciasVentanas();
            mostraMensaje("Inicio de sesion completo para el usuario, " + player.getNombre());
            abrirVentanaJuego();
        }
    }

    private void registrarUsuario() {
        String posibleNombre = ventanaRegistro.getjTextFieldnombreUsuario().getText();
        String posibleClave = ventanaRegistro.getjTextFieldClaveUsuario().getText();
        if (usuariosModelo.existeNombreParaRegistrar(posibleNombre)) {
            JOptionPane.showMessageDialog(null, "Este nombre ya esta registrado intentalo con otro");
        } else {
            usuariosModelo.registrarUsuario(posibleNombre, posibleClave);
            atajoIniciarSesion(posibleNombre, posibleClave);
            mostraMensaje("El usuario " + posibleNombre + " se ha registrado correctamente.");
        }
    }

    private void mostraMensaje(String mensaje) {
        JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Aviso");
        dialog.setModal(false);
        dialog.setVisible(true);
    }
}
