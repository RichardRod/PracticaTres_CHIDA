package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para practica 4

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria {

    //atributos
    public final byte DIVISION = 0;
    public final byte SUMA = 1;
    public final byte MULTIPLICACION = 2;
    public final byte CUADRADO = 3;

    /**
     *
     */
    public LibreriaCliente(Escribano esc) {
        super(esc);
    }

    /**
     * Ejemplo de resguardo del cliente suma
     */
    protected void suma() {
        int asaDest = 0;
        //...

        //asaDest=RPC.importarInterfaz(nombreServidor, version)  //para practica 4
        Nucleo.send(asaDest, null);
        //...
    }

    @Override
    protected void miSuma() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        int parametros = pilaParametros.pop().intValue();

        solCliente[8] = SUMA;

        System.out.println("Parametros: " + parametros);





    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {

    }

    @Override
    protected void miDivision() {

    }

    @Override
    protected void miCuadrado() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        int valorCuadrado = pilaParametros.pop().intValue();

        //empacar codop
        solCliente[8] = CUADRADO;

        //empacar datos relativos a la operacion
        solCliente[10] = (byte) valorCuadrado;
        solCliente[11] = (byte) (valorCuadrado >>> 8);
        solCliente[12] = (byte) (valorCuadrado >>> 16);
        solCliente[13] = (byte) (valorCuadrado >>> 24);

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        for(int i = 0; i < respServidor.length; i++){
            System.out.print(respServidor[i] + "-");
        }

        int respuesta = 0;

        respuesta = respuesta | respServidor[11];
        respuesta = respuesta << 8;
        respuesta = (respuesta | (respServidor[10] & 0x00FF));
        respuesta = (respuesta << 8);
        respuesta = (respuesta | (respServidor[9] & 0x00FF));
        respuesta = (respuesta << 8);
        respuesta = (respuesta | (respServidor[8] & 0x00FF));

        System.out.println("Respuesta Cuadrado: " + respuesta);

        pilaParametros.push(respuesta);

    }//fin del metodo miCuadrado

}//fin de la clase LibreriaCliente