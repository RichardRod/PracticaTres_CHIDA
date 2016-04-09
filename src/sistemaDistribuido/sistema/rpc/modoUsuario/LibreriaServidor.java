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
        int parametros = pilaParametros.pop();
        int[] arreglo = new int[parametros];

        for (int i = 0; i < parametros; i++) {
            arreglo[i] = pilaParametros.pop();
        }//fin de for

        int resultado = 0;

        for(int i = 0; i < arreglo.length; i++){
            resultado += arreglo[i];
        }//fin de for

        pilaParametros.push(new Integer(resultado));
    }

    @Override
    protected void miMultiplicacion() {

        int parametros = pilaParametros.pop().intValue();
        int[] arreglo = new int[parametros];

        for(int i = 0; i < parametros; i++){
            arreglo[i] = pilaParametros.pop().intValue();
        }//fin de for

        int resultado = 1;

        for(int i = 0; i < arreglo.length; i++){
            resultado *= arreglo[i];
        }//fin de for

        pilaParametros.push(new Integer(resultado));

    }//fin de la funcion muiMultiplicacion

    @Override
    protected void miDivision() {

    }

    @Override
    protected void miCuadrado() {
        int base = pilaParametros.pop().intValue();
        pilaParametros.push(new Integer(base * base));
    }//fin del metodo miCuadrado

}