package sistemaDistribuido.sistema.clienteServidor.modoMonitor;


public class TransmisionProceso {

    //atributos
    private int idProceso;
    private String ipProceso;

    //constructor
    public TransmisionProceso(int idProceso, String ipProceso){
        this.idProceso = idProceso;
        this.ipProceso = ipProceso;
    }//fin del constructor

    public int getIdProceso() {
        return idProceso;
    }

    public String getIpProceso() {
        return ipProceso;
    }
}//fin de la clase TransmisionProceso
