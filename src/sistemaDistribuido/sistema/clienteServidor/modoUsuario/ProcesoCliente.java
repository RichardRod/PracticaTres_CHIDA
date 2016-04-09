package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 *
 * @Nombre: Rodriguez Haro Ricardo
 * @seccion: D04
 * @No: Practica 1
 * Modificado para Practica 2
 */

public class ProcesoCliente extends Proceso {

    //atributos
    private byte codigoOperacion;
    private String datosOperacion;
    private final byte crearArchivo = 0;
    private final byte eliminarArchivo = 1;
    private final byte leerArchivo = 2;
    private final byte escribirArchivo = 3;


    public ProcesoCliente(Escribano esc) {
        super(esc);
        start();
    }

    public void run() {
        imprimeln("Inicio de proceso: Cliente.");
        imprimeln("Esperando datos para continuar.");
        Nucleo.suspenderProceso();

        byte[] solCliente = new byte[1024];
        byte[] respCliente = new byte[1024];
        byte dato;

        //empacar CODOP
        solCliente[8] = (byte) codigoOperacion;

        //empacar datos relativos a la operacion
        for (int i = 0; i < datosOperacion.length(); i++) {
            solCliente[i + 10] = (byte) datosOperacion.charAt(i);
        }//fin de for

        imprimeln("Senialamiento al nucleo para envio de mensaje");
        Nucleo.send(248, solCliente);
        imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
        Nucleo.receive(dameID(), respCliente);
        imprimeln("Invocando a receive()");
        dato = respCliente[8];
        imprimeln("Procesando respuesta recibida del servidor");

        //desempacar datos relativos a la operacion
        String respuestaDesempacada = "";
        for (int i = 0; i < respCliente.length; i++) {
            respuestaDesempacada += (char) respCliente[i];
        }
        respuestaDesempacada = respuestaDesempacada.trim();

        imprimeln("Respuesta del servidor: " + respuestaDesempacada);
        imprimeln("Fin del proceso");
    }//fin del metodo run

    public void enviarComando(byte CODOP) {

        switch (CODOP) {
            case 0: //crear
                codigoOperacion = crearArchivo;
                break;

            case 1: //eliminar
                codigoOperacion = eliminarArchivo;
                break;

            case 2: //leer
                codigoOperacion = leerArchivo;
                break;

            case 3: //escribir
                codigoOperacion = escribirArchivo;
                break;

        }//fin de switch
    }//fin del metodo enviarComando

    public String recibirDatosOperacion(String datosOperacion) {
        return this.datosOperacion = datosOperacion;
    }//fin del metodo recibirDatosOperacion
}
