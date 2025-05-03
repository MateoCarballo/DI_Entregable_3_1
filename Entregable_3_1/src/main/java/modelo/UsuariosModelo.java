package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class UsuariosModelo {

    private static final String ARCHIVO_USUARIOS = "src//main//persistencia//Usuarios.dat";
    private final List<Usuario> usuarios;
    String[] cabeceraTablaGeneral = {"Nombre", "Puntuación", "Fecha"};
    String[] cabeceraTablaPersonal = {"Puntuación", "Fecha"};
    private DefaultTableModel modeloTablaGeneral;
    private DefaultTableModel modeloTablaPersonal;

    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public UsuariosModelo() {
        usuarios = cargarUsuarios();
        obtenerModeloTablaGeneral();

        //Metodos usados para probar, sin tener que jugar partidas a lo loco! 
        mostrarPartidasConsola();
        getFakeData();
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
            //Si no tenemos datos pues los metemos por codigo para poder simular algunos internal frame que sin datos tendriamos que jugar bastante para ver
            return getFakeData();
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
                Partida p = new Partida(u.getNombre(), tiempoPartida);
                u.agregarPartida(p);
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

    private ArrayList<Usuario> getFakeData() {
        Random rand = new Random();
        ArrayList<Usuario> usuariosAuxiliares = new ArrayList<>();
        String[] nombres = {"Belén Esteban", "Paquirrín", "Jesulín", "El Fary", "Torrente"};
        for (String nombre : nombres) {
            Usuario usuario = new Usuario(nombre, "abc123");

            for (int i = 0; i < 3; i++) {
                long tiempoPartida = rand.nextInt(31) + 60;
                Partida partida = new Partida(nombre, tiempoPartida);
                usuario.agregarPartida(partida);
            }
            usuariosAuxiliares.add(usuario);
        }
        return usuariosAuxiliares;
    }

    public DefaultTableModel obtenerModeloTablaGeneral() {
        //Cargamos datos en la cabecera
        modeloTablaGeneral = new DefaultTableModel(cabeceraTablaGeneral, 0);
        //Instanciamos un arrayList auxiliar para guardar los datos traidos del archivo
        List<String[]> filas = new ArrayList<>();
        //Guardamos los datoss en el ArrayList auxiliar
        for (Usuario u : usuarios) {
            List<Partida> partidas = u.getPartidas();
            if (partidas != null) {
                for (Partida p : partidas) {
                    String[] row = {p.getJugador(),
                        String.valueOf(p.getTiempoEnSegundos()),
                        p.getFecha().format(formatoFecha)};
                    filas.add(row);
                }
            }
        }
        //Ordenamos de menor a mayor tiempo
        filas.sort((f1, f2) -> {
            long tiempo1 = Long.parseLong(f1[1]);
            long tiempo2 = Long.parseLong(f2[1]);
            return Long.compare(tiempo1, tiempo2);
        });
        //Añadimos al modelo que usaremos en la tabla los datos ya ordenados
        for (String[] row : filas) {
            modeloTablaGeneral.addRow(row);
        }

        return modeloTablaGeneral;
    }

    public DefaultTableModel obtenerModeloTablaJugador(String player) {

        //Sigue la misma logica que el metodo de arriba pero cambiando el indice que en esta sera el cero para la columna del tiempo
        modeloTablaPersonal = new DefaultTableModel(cabeceraTablaPersonal, 0);

        List<String[]> filas = new ArrayList<>();

        for (Usuario u : usuarios) {
            if (u.getNombre().equals(player)) {
                List<Partida> partidas = u.getPartidas();
                if (partidas != null) {
                    for (Partida p : partidas) {
                        String[] row = {
                            String.valueOf(p.getTiempoEnSegundos()),
                            p.getFecha().format(formatoFecha)};
                        filas.add(row);
                    }
                }
            }
        }

        filas.sort((f1, f2) -> {
            long tiempo1 = Long.parseLong(f1[0]);
            long tiempo2 = Long.parseLong(f2[0]);
            return Long.compare(tiempo1, tiempo2);
        });

        for (String[] row : filas) {
            modeloTablaPersonal.addRow(row);
        }

        return modeloTablaPersonal;
    }
}
