package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaServidor extends Libreria {

    /**
     *
     */
    public LibreriaServidor(Escribano esc) {
        super(esc);
    }

    @Override
    protected void miSuma() {
        System.out.println("SI SE LLAMA");
        int parametros = pilaParametros.pop();
        int resultado = 0;

        System.out.println("Mi pinche pila");
        for (int i = 0; i < pilaParametros.size(); i++) {
            System.out.println(pilaParametros.get(i));
        }

        //aquii errrrrooorrrrr
        int elemento;
        for (int i = 0; i < parametros; i++) {
            elemento = pilaParametros.pop();
            System.out.println("This is: " + elemento);
            resultado += elemento;
        }//fin de for

        System.out.println("Resultado LibreriaServidor.java: " + resultado);

        pilaParametros.push(new Integer(resultado));
    }

    @Override
    protected void miMultiplicacion() {

        int parametros = pilaParametros.pop().intValue();
        int[] arreglo = new int[parametros];

        for (int i = 0; i < parametros; i++) {
            arreglo[i] = pilaParametros.pop().intValue();
        }//fin de for

        int resultado = 1;

        for (int i = 0; i < arreglo.length; i++) {
            resultado *= arreglo[i];
        }//fin de for

        pilaParametros.push(new Integer(resultado));

    }//fin de la funcion muiMultiplicacion

    @Override
    protected void miDivision() {

        int parametroUno = pilaParametros.pop().intValue();
        int parametroDos = pilaParametros.pop().intValue();

        pilaParametros.push(parametroDos / parametroUno);

    }

    @Override
    protected void miCuadrado() {
        int base = pilaParametros.pop().intValue();
        pilaParametros.push(new Integer(base * base));
    }//fin del metodo miCuadrado

}