package sistemaDistribuido.visual.rpc;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame {
    private static final long serialVersionUID = 1;
    private ProcesoCliente proc;
    private TextField campo1, campo2, campo3, campo4;
    private Button botonSolicitud;

    public ClienteFrame(RPCFrame frameNucleo) {
        super(frameNucleo, "Cliente de Archivos");
        add("South", construirPanelSolicitud());
        validate();
        proc = new ProcesoCliente(this);
        fijarProceso(proc);
    }

    public Panel construirPanelSolicitud() {
        Panel pSolicitud, pcodop1, pcodop2, pcodop3, pcodop4, pboton;
        pSolicitud = new Panel();
        pcodop1 = new Panel();
        pcodop2 = new Panel();
        pcodop3 = new Panel();
        pcodop4 = new Panel();
        pboton = new Panel();
        campo1 = new TextField(10);
        campo2 = new TextField(10);
        campo3 = new TextField(10);
        campo4 = new TextField(10);
        pSolicitud.setLayout(new GridLayout(5, 1));

        pcodop1.add(new Label("Divicion"));
        pcodop1.add(new Label("<Parametros: 2>"));
        pcodop1.add(campo1);

        pcodop2.add(new Label("Suma"));
        pcodop2.add(new Label("<Parametros n>"));
        pcodop2.add(campo2);

        pcodop3.add(new Label("Multiplicacion"));
        pcodop3.add(new Label("<Parametros n>"));
        pcodop3.add(campo3);

        pcodop4.add(new Label("Cuadrado"));
        pcodop4.add(new Label("<Parametros 1>"));
        pcodop4.add(campo4);


        botonSolicitud = new Button("Solicitar");
        pboton.add(botonSolicitud);
        botonSolicitud.addActionListener(new ManejadorSolicitud());

        pSolicitud.add(pcodop1);
        pSolicitud.add(pcodop2);
        pSolicitud.add(pcodop3);
        pSolicitud.add(pcodop4);
        pSolicitud.add(pboton);

        return pSolicitud;
    }

    class ManejadorSolicitud implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String com = e.getActionCommand();
            if (com.equals("Solicitar")) {

                if (validarCampos()) {

                    botonSolicitud.setEnabled(false);

                    //solicitud 1 DIVISION
                    proc.setParametrosDivision(castearCampos(campo1.getText().toString().trim().split(" ")));

                    //solicitud 2 SUMA
                    proc.setParametrosSuma(castearCampos(campo2.getText().toString().trim().split(" ")));

                    //solicitud 3 MULTIPLICACION
                    proc.setParametrosMultiplicacion(castearCampos(campo3.getText().toString().trim().split(" ")));

                    //solicitud 4 CUADRADO
                    proc.setCuadrado(Integer.parseInt(campo4.getText().toString().trim()));

                    Nucleo.reanudarProceso(proc);

                }//fin de if
                else {
                    imprimeln("Llena todos los campos");
                }//fin de else

            }//fin de if
        }//fin de actionPerformed
    }//fin de la clase ManejadorSolicitud

    private int[] castearCampos(String[] arreglo) {
        int arregloCasteado[] = new int[arreglo.length];

        for (int i = 0; i < arreglo.length; i++) {
            arregloCasteado[i] = Integer.parseInt(arreglo[i]);
        }//fin de for

        return arregloCasteado;
    }//fin del metodo castearCampos

    private boolean validarCampos() {
        if (campo1.getText().length() == 0 || campo2.getText().length() == 0 ||
                campo3.getText().length() == 0 || campo4.getText().length() == 0) {
            return false;
        } else {
            return true;
        }
    }//fin del metodo validarCampos

    private boolean validarDivision() {
        if (campo1.getText().length() != 2) {
            return false;
        } else {
            return true;
        }
    }

}//fin de la clase ClienteFrame
