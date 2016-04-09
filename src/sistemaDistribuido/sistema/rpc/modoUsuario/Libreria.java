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

    public int miSuma(int[] arreglo)
    {
        System.out.println("Suma invocada Libreria.java");
        int numeroParametros = arreglo.length;

        for(int i = 0; i < numeroParametros; i++){
            pilaParametros.push(new Integer(arreglo[i]));
        }//fin de for

        pilaParametros.push(new Integer(numeroParametros));

        miSuma();

        return pilaParametros.pop().intValue();
    }//fin del metodo miSuma

    public int miMultiplicacion(int[] arreglo)
    {

        int num;
        for(int i=0;i<arreglo.length;i++){
            num=arreglo[i];
            pilaParametros.push(num);
        }


        int numPam=arreglo.length;
        pilaParametros.push(numPam);


        miMultiplicacion();

        int result2=(Integer)pilaParametros.pop();

        return result2;
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