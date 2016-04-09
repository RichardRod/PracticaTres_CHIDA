package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para practica 4

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 *
 */
public class ProcesoServidor extends Proceso {
    private LibreriaServidor ls;   //para practica 3
    public final byte DIVISION = 0;
    public final byte SUMA = 1;
    public final byte MULTIPLICACION = 2;
    public final byte CUADRADO = 3;

    /**
     *
     */
    public ProcesoServidor(Escribano esc) {
        super(esc);
        ls = new LibreriaServidor(esc);   //para practica 3
        start();
    }

    /**
     * Resguardo del servidor
     */
    public void run() {
        imprimeln("Proceso servidor en ejecucion.");
        //idUnico=RPC.exportarInterfaz("FileServer", "3.1", asa)  //para practica 4
        byte[] solCliente = new byte[1024];
        byte codigoOperacion;

        while (continuar()) {
            Nucleo.receive(dameID(), solCliente);

            codigoOperacion = solCliente[8]; //asignar operacion a realizar

            System.out.println("Comando: " + codigoOperacion);

            switch (codigoOperacion) {
                case DIVISION:
                    ejecutarDivision(solCliente);
                    break;

                case SUMA:
                    ejecutarSuma(solCliente);
                    break;

                case MULTIPLICACION:
                    ejecutarMultiplicacion(solCliente);
                    break;

                case CUADRADO:
                    ejecutarCuadrado(solCliente);
                    break;
            }//fin de switch


        }//fin de while

        //RPC.deregistrarInterfaz(nombreServidor, version, idUnico)  //para practica 4
    }

    private void ejecutarDivision(byte[] solCliente) {
        //variables locales
        byte[] respServidor = new byte[1024];

        imprimeln("Solicitando: Operacion Division");
    }

    private void ejecutarSuma(byte[] solCliente) {
        //variables locales
        byte[] respServidor = new byte[1024];

        imprimeln("Solicitando: Operacion Suma");
    }

    private void ejecutarMultiplicacion(byte[] solCliente) {
        //variables locales
        byte[] respServidor = new byte[1024];

        imprimeln("Solicitando: Operacion Multiplicacion");
    }

    private void ejecutarCuadrado(byte[] solCliente) {
        //variables locales
        byte[] respServidor = new byte[1024];
        int destino = solCliente[0];

        imprimeln("Solicitando: Operacion Cuadrado");

        int valorOperacion = 0;

        valorOperacion = (valorOperacion | solCliente[13]);
        valorOperacion = (valorOperacion << 8);
        valorOperacion = (valorOperacion | (solCliente[12] & 0x00FF));
        valorOperacion = (valorOperacion << 8);
        valorOperacion = (valorOperacion | (solCliente[11] & 0x00FF));
        valorOperacion = (valorOperacion << 8);
        valorOperacion = (valorOperacion | (solCliente[10] & 0x00FF));

        int resultado = ls.miCuadrado(valorOperacion);

        //empacar resultado
        respServidor[8] = (byte) (resultado);
        respServidor[9] = (byte) (resultado >>> 8);
        respServidor[10] = (byte) (resultado >>> 16);
        respServidor[11] = (byte) (resultado >>> 24);

        System.out.println("Resultado Cuadrado Magico: " + resultado);

        imprimeln("Enviando Respuesta al cliente");
        Nucleo.send(destino, respServidor);
        imprimeln("Respuesta Enviada");

    }//fin del metodo ejecutarCuadrado


}
