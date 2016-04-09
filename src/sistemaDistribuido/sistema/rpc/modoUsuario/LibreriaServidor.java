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

        int numeroParametros = pilaParametros.pop().intValue();

        int suma = 0;
        for(int i = 0; i < numeroParametros; i++){
            suma += pilaParametros.pop().intValue();
        }//fin de for

        System.out.println("Resultado Suma LibreriaServidor,java: " + suma);

        pilaParametros.push(new Integer(suma));
    }//fin del metodo miSuma

    @Override
    protected void miMultiplicacion() {

        int noParametros=(Integer)pilaParametros.pop();
        int result=0,num;

        for(int i=0;i<noParametros;i++){
            num=(Integer)pilaParametros.pop();
            result=result*num;
        }
        pilaParametros.push(result);

    }//fin de la funcion muiMultiplicacion

    @Override
    protected void miDivision() {

        int parametroUno = pilaParametros.pop().intValue();
        int parametroDos = pilaParametros.pop().intValue();
        int result1 = parametroDos / parametroUno;
        pilaParametros.push(result1);

    }//fin del metodo miDivision

    @Override
    protected void miCuadrado() {
        int base = pilaParametros.pop().intValue();
        pilaParametros.push(new Integer(base * base));
    }//fin del metodo miCuadrado

}