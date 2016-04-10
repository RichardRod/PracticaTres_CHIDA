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

        solCliente[8] = SUMA; //establecer codigoOperacion

        int numeroParametros = pilaParametros.pop().intValue();

        solCliente = empacarDatos(solCliente, numeroParametros);

        for (int i = 0, j = 14; i < numeroParametros; i++, j += 4) {
            empacarDatosArreglo(solCliente, pilaParametros.pop().intValue(), j);
        }//fin de for

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        pilaParametros.push(desempacarDatosArreglo(8, respServidor)); //agregar respuesta a la pila

    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {



    }//fin del metodo miMultiplicacion

    @Override
    protected void miDivision() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        int parametroUno = pilaParametros.pop().intValue();
        int parametroDos = pilaParametros.pop().intValue();

        System.out.println("Parametros Division: " + parametroUno + " - " + parametroDos);

        solCliente[8] = DIVISION; //establecer codigoOperacion

        solCliente = empacarDatosArreglo(solCliente, parametroDos, 10);
        solCliente = empacarDatosArreglo(solCliente, parametroUno, 14);

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        int resultado = desempacarDatosArreglo(8, respServidor);

        pilaParametros.push(resultado);
    }

    @Override
    protected void miCuadrado() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        //empacar codop
        solCliente[8] = CUADRADO; //establecer CodigoOperacion

        //empacar datos relativos a la operacion
        solCliente = empacarDatos(solCliente, pilaParametros.pop().intValue());

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        int respuesta = desempacarDatosArreglo(8, respServidor);
        System.out.println("Respuesta Cuadrado: " + respuesta);

        pilaParametros.push(respuesta);

    }//fin del metodo miCuadrado

    public int desempacarDatosArreglo(int indice, byte[] datosEmpacados) {

        int valor = 0x0;

        valor = (int) ((datosEmpacados[indice] & 0x000000FF) | (datosEmpacados[indice + 1] << 8 & 0x0000FF00) | (datosEmpacados[indice + 2] << 16 & 0x00FF0000) | (datosEmpacados[indice + 3] << 24 & 0xFF000000));

        return valor;
    }//fin del metodo desempacarDatosArreglo

    public byte[] empacarDatos(byte[] arreglo, int valor){

        byte[] arregloAux = arreglo;

        for(int i = 0, corrimiento = 0; i < 4; i++, corrimiento += 8){
            arregloAux[i + 10] = (byte) (valor >>> corrimiento);
        }//fin de for

        return arregloAux;

    }//fin del metodo empacarDatos

    public byte[] empacarDatosArreglo(byte[] arreglo, int valor, int indice){

        byte[] arregloAux = arreglo;

        for(int i = 0, corrimiento = 0; i < 4; i++, corrimiento += 8){
            arregloAux[i + indice] = (byte) (valor >>> corrimiento);
        }//fin de for

        return arregloAux;

    }//fin del metodo empacarDatos
}//fin de la clase LibreriaCliente
