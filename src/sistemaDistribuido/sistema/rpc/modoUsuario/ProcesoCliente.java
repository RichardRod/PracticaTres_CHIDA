package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 *
 */
public class ProcesoCliente extends Proceso {

    //atributos
    private Libreria lib;
    private int[] parametrosSuma;
    private int[] parametrosMultiplicacion;
    private int[] parametrosDivision;
    private int cuadrado;

    /**
     *
     */
    public ProcesoCliente(Escribano esc) {
        super(esc);
        //lib = new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
        lib=new LibreriaCliente(esc);  //luego con esta comentando la anterior, para subrutina servidor remota
        start();
    }

    public void setParametrosSuma(int[] parametrosSuma) {
        this.parametrosSuma = parametrosSuma;
    }

    public int[] getParametrosSuma() {
        return parametrosSuma;
    }

    public void setParametrosMultiplicacion(int[] parametrosMultiplicacion) {
        this.parametrosMultiplicacion = parametrosMultiplicacion;
    }

    public int[] getParametrosMultiplicacion() {
        return parametrosMultiplicacion;
    }

    public void setParametrosDivision(int[] parametrosDivision) {
        this.parametrosDivision = parametrosDivision;
    }

    public int[] getParametrosDivision() {
        return parametrosDivision;
    }

    public void setCuadrado(int cuadrado) {
        this.cuadrado = cuadrado;
    }

    public int getCuadrado() {
        return cuadrado;
    }

    /**
     * Programa Cliente
     */
    public void run() {

        imprimeln("Proceso cliente en ejecucion.");
        imprimeln("Esperando datos para continuar.");
        Nucleo.suspenderProceso();
        imprimeln("Salio de suspenderProceso");

        int resultado;

        resultado = lib.miDivision(parametrosDivision.length, parametrosDivision);
        imprimeln("Resultado Operacion Division: " + resultado);

        resultado = lib.miSuma(parametrosSuma);
        imprimeln("Resultado Operacion Suma: " + resultado);

        //resultado = lib.miMultiplicacion(parametrosMultiplicacion);
        //imprimeln("Resultado Operacion Multiplicacion: " + resultado);

        resultado = lib.miCuadrado(cuadrado);
        imprimeln("Resultado Operacion Cuadrado: " + resultado);

        imprimeln("Fin del cliente.");
    }//fin del metodo run
}
