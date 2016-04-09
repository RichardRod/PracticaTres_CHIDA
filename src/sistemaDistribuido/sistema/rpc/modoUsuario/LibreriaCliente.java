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

        System.out.println("Parametros LibreriaCliente.java: " + parametros);

        /*for(int i = 0; i < pilaParametros.size(); i++){
            System.out.println("Elementos: " + pilaParametros.get(i));
        }*/

        //empacar datos relativos a la operacion
        solCliente[10] = (byte) parametros;
        solCliente[11] = (byte) (parametros >>> 8);
        solCliente[12] = (byte) (parametros >>> 16);
        solCliente[13] = (byte) (parametros >>> 24);

        int elemento;
        int index = 14;
        for (int i = 0; i < parametros; i++) {
            elemento = pilaParametros.pop().intValue();
            System.out.println("Cuajo: " + elemento);

            solCliente[index] = (byte) elemento;
            solCliente[index + 1] = (byte) (elemento >>> 8);
            solCliente[index + 2] = (byte) (elemento >>> 16);
            solCliente[index + 3] = (byte) (elemento >>> 24);

        }//fin de for

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        for (int i = 0; i < respServidor.length; i++) {
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

        System.out.println("Respuesta Suma: " + respuesta);

        pilaParametros.push(respuesta);

    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {

    }

    @Override
    protected void miDivision() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        int parametroUno = pilaParametros.pop().intValue();
        int parametroDos = pilaParametros.pop().intValue();

        System.out.println("Parametros Division: " + parametroUno + " - " + parametroDos);

        solCliente[8] = DIVISION;

        solCliente[10] = (byte)parametroDos;
        solCliente[11]=(byte)(parametroDos>>>8);
        solCliente[12]=(byte)(parametroDos>>>16);
        solCliente[13]=(byte)(parametroDos>>>24);

        solCliente[14] = (byte)parametroUno;
        solCliente[15]=(byte)(parametroUno>>>8);
        solCliente[16]=(byte)(parametroUno>>>16);
        solCliente[17]=(byte)(parametroUno>>>24);

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        int elemento = 0;
        elemento=(int)(elemento|respServidor[8+3]);
        elemento=(int)(elemento<<8);
        elemento=(int)(elemento|(respServidor[8+2]&0x00FF));
        elemento=(int)(elemento<<8);
        elemento=(int)(elemento|(respServidor[8+1]&0x00FF));
        elemento=(int)(elemento<<8);
        elemento=(int)(elemento|(respServidor[8]&0x00FF));


        int resultado = elemento;

        pilaParametros.push(resultado);

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

    private void empacarDatosDivision()
    {

    }

}//fin de la clase LibreriaCliente
