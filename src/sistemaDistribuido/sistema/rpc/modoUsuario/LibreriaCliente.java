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

        byte[] solCliente= new byte[1024];
        byte[] arrayRes= new byte[1024];
        int result2;

        solCliente[8]=SUMA;

        int noParametros=(Integer)pilaParametros.pop();

        empaca(10,noParametros,solCliente);

        System.out.println("TamañoPila:"+noParametros);

        int posIniA=14,num;

        for(int i=0;i<noParametros;i++){

            num=(Integer)pilaParametros.pop();

            empaca(posIniA,num,solCliente);

            posIniA=posIniA+4;
        }

        imprimeln("Enviando Datos");
        Nucleo.send(248, solCliente);
        imprimeln("Recibiendo Datos");
        Nucleo.receive(Nucleo.dameIdProceso(),arrayRes);

        result2=desempaqueta(8,arrayRes);

        pilaParametros.push(result2);

    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {

        //variables locales
        byte[] solCliente = new byte[1024];
        byte[] respServidor = new byte[1024];

        solCliente[8] = MULTIPLICACION;

        int numeroParametros = pilaParametros.pop().intValue();

        solCliente[10]=(byte)numeroParametros;
        solCliente[10+1]=(byte)(numeroParametros>>>8);
        solCliente[10+2]=(byte)(numeroParametros>>>16);
        solCliente[10+3]=(byte)(numeroParametros>>>24);

        System.out.println("Pila Multiplicacion: " + numeroParametros);

        int index = 14;
        int elemento;
        int aux;

        for(int i=0;i<numeroParametros;i++){

            elemento=(Integer)pilaParametros.pop();

            solCliente[index]=(byte)elemento;
            solCliente[index+1]=(byte)(elemento>>>8);
            solCliente[index+2]=(byte)(elemento>>>16);
            solCliente[index+3]=(byte)(elemento>>>24);

            index=index+4;
        }

        Nucleo.send(248, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(),respServidor);

        int resultado = 0;
        resultado=(int)(resultado|respServidor[8+3]);
        resultado=(int)(resultado<<8);
        resultado=(int)(resultado|(respServidor[8+2]&0x00FF));
        resultado=(int)(resultado<<8);
        resultado=(int)(resultado|(respServidor[8+1]&0x00FF));
        resultado=(int)(resultado<<8);
        resultado=(int)(resultado|(respServidor[8]&0x00FF));

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

    public void empaca(int posIni,int num,byte[] array){

        array[posIni]=(byte)num;
        array[posIni+1]=(byte)(num>>>8);
        array[posIni+2]=(byte)(num>>>16);
        array[posIni+3]=(byte)(num>>>24);//aqui cambie

    }

    public int desempaqueta(int pos,byte[] arreglo2){

        int aux=0;

        aux=(int)(aux|arreglo2[pos+3]);
        aux=(int)(aux<<8);
        aux=(int)(aux|(arreglo2[pos+2]&0x00FF));
        aux=(int)(aux<<8);
        aux=(int)(aux|(arreglo2[pos+1]&0x00FF));
        aux=(int)(aux<<8);
        aux=(int)(aux|(arreglo2[pos]&0x00FF));

        //System.out.println(String.format("Entero Desempaquetado en Hexadecimal: %02X",aux));

        return aux;
    }
}//fin de la clase LibreriaCliente
