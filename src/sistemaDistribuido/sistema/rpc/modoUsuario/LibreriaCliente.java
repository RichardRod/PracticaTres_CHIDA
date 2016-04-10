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

        pilaParametros.push(desempaqueta(8, respServidor)); //agregar respuesta a la pila

    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        solCliente[8] = MULTIPLICACION;

        int numeroParametros = pilaParametros.pop().intValue();

        solCliente[10] = (byte) numeroParametros;
        solCliente[10 + 1] = (byte) (numeroParametros >>> 8);
        solCliente[10 + 2] = (byte) (numeroParametros >>> 16);
        solCliente[10 + 3] = (byte) (numeroParametros >>> 24);

        System.out.println("Pila Multiplicacion: " + numeroParametros);

        int index = 14;
        int elemento;
        int aux;

        for (int i = 0; i < numeroParametros; i++) {

            elemento = (Integer) pilaParametros.pop();

            solCliente[index] = (byte) elemento;
            solCliente[index + 1] = (byte) (elemento >>> 8);
            solCliente[index + 2] = (byte) (elemento >>> 16);
            solCliente[index + 3] = (byte) (elemento >>> 24);

            index = index + 4;
        }

        Nucleo.send(248, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        int resultado = 0;
        resultado = (int) (resultado | respServidor[8 + 3]);
        resultado = (int) (resultado << 8);
        resultado = (int) (resultado | (respServidor[8 + 2] & 0x00FF));
        resultado = (int) (resultado << 8);
        resultado = (int) (resultado | (respServidor[8 + 1] & 0x00FF));
        resultado = (int) (resultado << 8);
        resultado = (int) (resultado | (respServidor[8] & 0x00FF));

        pilaParametros.push(resultado);

    }//fin del metodo miMultiplicacion

    @Override
    protected void miDivision() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        int parametroUno = pilaParametros.pop().intValue();
        int parametroDos = pilaParametros.pop().intValue();

        System.out.println("Parametros Division: " + parametroUno + " - " + parametroDos);

        solCliente[8] = DIVISION;

        solCliente[10] = (byte) parametroDos;
        solCliente[11] = (byte) (parametroDos >>> 8);
        solCliente[12] = (byte) (parametroDos >>> 16);
        solCliente[13] = (byte) (parametroDos >>> 24);

        solCliente[14] = (byte) parametroUno;
        solCliente[15] = (byte) (parametroUno >>> 8);
        solCliente[16] = (byte) (parametroUno >>> 16);
        solCliente[17] = (byte) (parametroUno >>> 24);

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(), respServidor);

        int elemento = 0;
        elemento = (int) (elemento | respServidor[8 + 3]);
        elemento = (int) (elemento << 8);
        elemento = (int) (elemento | (respServidor[8 + 2] & 0x00FF));
        elemento = (int) (elemento << 8);
        elemento = (int) (elemento | (respServidor[8 + 1] & 0x00FF));
        elemento = (int) (elemento << 8);
        elemento = (int) (elemento | (respServidor[8] & 0x00FF));


        int resultado = elemento;

        pilaParametros.push(resultado);

    }

    @Override
    protected void miCuadrado() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        //empacar codop
        solCliente[8] = CUADRADO;

        //empacar datos relativos a la operacion
        solCliente = empacarDatos(solCliente, pilaParametros.pop().intValue());
        /*solCliente[10] = (byte) valorCuadrado;
        solCliente[11] = (byte) (valorCuadrado >>> 8);
        solCliente[12] = (byte) (valorCuadrado >>> 16);
        solCliente[13] = (byte) (valorCuadrado >>> 24);*/

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

    public void empaca(int posIni, int num, byte[] array) {

        array[posIni] = (byte) num;
        array[posIni + 1] = (byte) (num >>> 8);
        array[posIni + 2] = (byte) (num >>> 16);
        array[posIni + 3] = (byte) (num >>> 24);

    }

    public int desempaqueta(int pos, byte[] arreglo2) {

        int aux = 0;

        aux = (int) (aux | arreglo2[pos + 3]);
        aux = (int) (aux << 8);
        aux = (int) (aux | (arreglo2[pos + 2] & 0x00FF));
        aux = (int) (aux << 8);
        aux = (int) (aux | (arreglo2[pos + 1] & 0x00FF));
        aux = (int) (aux << 8);
        aux = (int) (aux | (arreglo2[pos] & 0x00FF));

        return aux;
    }

    public int desempacarDatos(byte[] datosEmpacados) {

        int valor = 0x0;

        valor = (int) ((datosEmpacados[10] & 0x000000FF) | (datosEmpacados[11] << 8 & 0x0000FF00) | (datosEmpacados[12] << 16 & 0x00FF0000) | (datosEmpacados[13] << 24 & 0xFF000000));

        return valor;
    }

    public int desempacarDatosArreglo(int indice, byte[] datosEmpacados) {

        int valor = 0x0;

        valor = (int) ((datosEmpacados[indice] & 0x000000FF) | (datosEmpacados[indice + 1] << 8 & 0x0000FF00) | (datosEmpacados[indice + 2] << 16 & 0x00FF0000) | (datosEmpacados[indice + 3] << 24 & 0xFF000000));

        return valor;
    }

    public byte[] empacarDatos(byte[] arreglo, int valor){

        byte[] arregloAux = arreglo;

        for(int i = 0, corrimiento = 0; i < 4; i++, corrimiento += 8){
            arregloAux[i + 10] = (byte) (valor >>> corrimiento);
        }

        return arregloAux;

    }//fin del metodo empacarDatos

    public byte[] empacarDatosArreglo(byte[] arreglo, int valor, int indice){

        byte[] arregloAux = arreglo;

        for(int i = 0, corrimiento = 0; i < 4; i++, corrimiento += 8){
            arregloAux[i + indice] = (byte) (valor >>> corrimiento);
        }

        return arregloAux;

    }//fin del metodo empacarDatos
}//fin de la clase LibreriaCliente
