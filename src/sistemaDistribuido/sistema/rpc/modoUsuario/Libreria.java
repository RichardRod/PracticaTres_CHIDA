package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.util.Escribano;

import java.util.Stack;

public abstract class Libreria {
    private Escribano esc;
    protected Stack<Integer> pilaParametros = new Stack<>();

    /**
     *
     */
    public Libreria(Escribano esc) {
        this.esc = esc;
    }

    /**
     *
     */
    protected void imprime(String s) {
        esc.imprime(s);
    }

    /**
     *
     */
    protected void imprimeln(String s) {
        esc.imprimeln(s);
    }

    public int miDivision(int numeroParametros, int[] arreglo)
    {
        int parametroUno = arreglo[0];
        int parametroDos = arreglo[1];

        pilaParametros.push(new Integer(parametroUno));
        pilaParametros.push(new Integer(parametroDos));

        miDivision();

        return pilaParametros.pop().intValue();

    }//fin del metodo miDivision

    public int miSuma(int numeroParametros, int[] arreglo)
    {
        int resultado;
        for(int i = numeroParametros - 1; i >= 0; i--){
            pilaParametros.push(new Integer(arreglo[i]));
        }//fin de for

        pilaParametros.push(new Integer(numeroParametros));
        miSuma();
        resultado = (Integer) pilaParametros.pop();
        return resultado;
    }//fin del metodo miSuma

    public int miMultiplicacion(int numeroParametros, int[] arreglo)
    {
        int resultado;
        for(int i = numeroParametros - 1; i >= 0; i--){
            pilaParametros.push(new Integer(arreglo[i]));
        }//fin de for

        pilaParametros.push(new Integer(numeroParametros));
        miMultiplicacion();
        return resultado = pilaParametros.pop().intValue();
    }//fin del metodo miMultiplicacion

    public int miCuadrado(int valor)
    {
        pilaParametros.push(new Integer(valor));
        miCuadrado();

        return pilaParametros.pop();
    }//fin del metodo miCuadrado

    /**
     * Servidor suma verdadera generable por un compilador estandar
     * o resguardo de la misma por un compilador de resguardos.
     */
    protected abstract void miSuma();

    protected abstract void miMultiplicacion();

    protected abstract void miDivision();

    protected abstract void miCuadrado();
}