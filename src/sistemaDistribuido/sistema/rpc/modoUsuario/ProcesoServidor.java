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
        int destino = solCliente[0];

        imprimeln("Solicitando: Operacion Division");

        int parametroUno;
        int parametroDos;

        parametroUno = desempacarDatos(solCliente);
        parametroDos = desempacarDatosArreglo(14, solCliente);

        int arreglo[] = new int[2];
        arreglo[0] = parametroUno;
        arreglo[1] = parametroDos;

        int resultado = ls.miDivision(2, arreglo);

        respServidor = empacarDatos(respServidor, resultado);

        imprimeln("Enviando Respuesta Division al cliente: " + resultado);
        Nucleo.send(destino, respServidor);
        imprimeln("Respuesta Enviada");

    }

    private void ejecutarSuma(byte[] solCliente) {

        //variables locales
        byte[] respServidor = new byte[1024];
        int destino = solCliente[0];
        int numeroParametros = desempacarDatos(solCliente);

        int arregloElementos[] = new int[numeroParametros];

        imprimeln("Solicitando: Operacion Suma");

        for (int i = 0, j = 14; i < numeroParametros; i++, j += 4) {
            arregloElementos[i] = desempacarDatosArreglo(j, solCliente);
        }

        int resultado = ls.miSuma(arregloElementos);

        respServidor = empacarDatos(respServidor, resultado);

        imprimeln("Enviando Respuesta Suma al cliente: " + resultado);
        Nucleo.send(destino, respServidor);

    }//fin del metodo ejecutarSuma

    private void ejecutarMultiplicacion(byte[] solCliente) {

    }

    private void ejecutarCuadrado(byte[] solCliente) {

        //variables locales
        byte[] respServidor = new byte[1024];
        int destino = solCliente[0];

        imprimeln("Solicitando: Operacion Cuadrado");

        int valorOperacion = desempacarDatos(solCliente);

        int resultado = ls.miCuadrado(valorOperacion);

        //empacar resultado
        respServidor = empacarDatos(respServidor, resultado);

        System.out.println("Resultado Cuadrado Magico: " + resultado);

        imprimeln("Enviando Respuesta Cuadrado al cliente: " + resultado);
        Nucleo.send(destino, respServidor);
        imprimeln("Respuesta Enviada");

    }//fin del metodo ejecutarCuadrado


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
            arregloAux[i + 8] = (byte) (valor >>> corrimiento);
        }

        return arregloAux;

    }//fin del metodo empacarDatos


}
