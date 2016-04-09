package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleoBase;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

/**
 *
 * @Nombre: Rodriguez Haro Ricardo
 * @seccion: D04
 * @No: Practica 2
 * Modificado para Practica 2
 */

public final class MicroNucleo extends MicroNucleoBase {

    //atributos
    private static MicroNucleo nucleo = new MicroNucleo();
    private Hashtable<Integer, TransmisionProceso> tablaEmision = new Hashtable<>();
    private Hashtable<Integer, byte[]> tablaRecepcion = new Hashtable<>();
    private final byte AU = -99;

    /**
     *
     */
    private MicroNucleo() {
    }

    /**
     *
     */
    public final static MicroNucleo obtenerMicroNucleo() {
        return nucleo;
    }

    /*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
    byte[] mensaje;

    public void sendFalso(int dest, byte[] message) {
        System.arraycopy(message, 0, mensaje, 0, message.length);
        notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
    }

    public void receiveFalso(int addr, byte[] message) {
        mensaje = message;
        suspenderProceso();
    }
    /*---------------------------------------------------------*/

    /**
     *
     */
    protected boolean iniciarModulos() {
        return true;
    }

    /**
     *
     */
    protected void sendVerdadero(int dest, byte[] message) {

        //variables locales
        TransmisionProceso proceso = tablaEmision.get(dest);
        ParMaquinaProceso pmp = dameDestinatarioDesdeInterfaz();

        int idOrigen;
        int idDestino;
        String ipDestino;

        imprimeln("El proceso invocante es el " + super.dameIdProceso());

        if (proceso == null) {
            imprimeln("Datos tomados desde Interfaz");

            idOrigen = super.dameIdProceso();
            idDestino = pmp.dameID();
            ipDestino = pmp.dameIP();

            imprimeln("Enviando mensaje a IP=" + idDestino + " ID=" + ipDestino);

        }//fin de if
        else {
            imprimeln("Datos tomados desde TablaEmision");

            idOrigen = super.dameIdProceso();
            idDestino = proceso.getIdProceso();
            ipDestino = proceso.getIpProceso();

            imprimeln("Enviando mensaje a IP=" + idDestino + " ID=" + ipDestino);

        }//fin de else

        //empacarMensaje
        message[0] = (byte) idOrigen; //0-3 id origen
        message[4] = (byte) idDestino; //4-7 id destino

        enviarMensaje(message, ipDestino);
        imprimeln("Mensaje Enviado");
    }//fin del metodo sendVerdadero

    private void enviarMensaje(byte[] message, String ip) {
        try {
            DatagramPacket paqueteDatagram = new DatagramPacket(message, message.length, InetAddress.getByName(ip), damePuertoRecepcion());

            DatagramSocket socketEmision = dameSocketEmision();
            socketEmision.send(paqueteDatagram);

        }//fin de try
        catch (IOException e) {
            e.printStackTrace();
        }//fin de catch
    }//fin del metodo enviarMensaje


    /**
     *
     */
    protected void receiveVerdadero(int addr, byte[] message) {
        tablaRecepcion.put(addr, message); //registrar
        suspenderProceso();
    }//fin del metodo receiveVerdadero

    /**
     * Para el(la) encargad@ de direccionamiento por servidor de nombres en practica 5
     */
    protected void sendVerdadero(String dest, byte[] message) {
    }

    /**
     * Para el(la) encargad@ de primitivas sin bloqueo en practica 5
     */
    protected void sendNBVerdadero(int dest, byte[] message) {
    }

    /**
     * Para el(la) encargad@ de primitivas sin bloqueo en practica 5
     */
    protected void receiveNBVerdadero(int addr, byte[] message) {
    }

    /**
     *
     */
    public void run() {

        imprimeln("Preparando Datos");

        //variables locales
        byte[] mensaje = new byte[1024];

        int origen;
        int destino;
        String ipOrigen;

        //mensajes de la red
        DatagramPacket paqueteDatagram = new DatagramPacket(mensaje, mensaje.length);
        DatagramSocket recepcion = dameSocketRecepcion();

        Proceso proceso;

        while (seguirEsperandoDatagramas()) {
            try {
                //recibir paquete
                recepcion.receive(paqueteDatagram);
                sleep(1000);
                imprimeln("Paquete recibido");

                //averiguar datos
                origen = mensaje[0];
                destino = mensaje[4];
                ipOrigen = paqueteDatagram.getAddress().getHostAddress();

                System.out.println("Destino: " + destino);
                System.out.println("Origen: " + origen);
                System.out.println("IP: " + ipOrigen);
                System.out.println();

                //registrar
                tablaEmision.put(new Integer(origen), new TransmisionProceso(origen, ipOrigen));

                proceso = dameProcesoLocal(destino);

                if (proceso == null) {
                    mensaje[0] = (byte) origen;
                    mensaje[4] = (byte) origen;
                    mensaje[10] = AU; //mensaje de error

                    String mensajeError = "Direccion Desconocida";

                    //empacar mensaje de error
                    for (int i = 0; i < mensajeError.length(); i++) {
                        mensaje[i + 10] = (byte) mensajeError.charAt(i);
                    }//fin de for

                    enviarMensaje(mensaje, ipOrigen);

                    imprimeln("Direccion Desconocida: AU");

                }//fin de if
                else //enviar direccion desconocida
                {
                    byte error = mensaje[10];
                    if (error == this.AU) {
                        imprimeln("Proceso Destino No Localizado");
                        reanudarProceso(proceso);
                    }//fin de if

                    byte[] datosDestino = tablaRecepcion.get(destino);

                    if (datosDestino.length != 0) {
                        System.arraycopy(mensaje, 0, datosDestino, 0, mensaje.length);

                        tablaEmision.put(new Integer(destino), new TransmisionProceso(destino, ipOrigen));
                        tablaRecepcion.remove(destino);

                        reanudarProceso(proceso);
                        imprimeln("Correcto");
                    }//fin de if
                    else {
                        imprimeln("Intenta de Nuevo: TA");
                    }//fin de else

                }//fin de else

            } catch (InterruptedException | IOException e) {
                System.out.println(e.toString());
            }
        }//fin de while
    }//fin del metodo run
}//fin de la clase MicroNucleo
