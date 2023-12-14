package sistdist2daspruebas;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class DatosCliente {

    String IP_A;
    double almacenamiento_A;
    double procesador_A;
    double RAM_A;
    String Internet_A;
    double Ranking_A;

    public DatosCliente(String IP, double almacenamiento, double procesador, double RAM, String Internet, double Ranking) {
        IP_A = IP;
        almacenamiento_A = almacenamiento;
        procesador_A = procesador;
        RAM_A = RAM;
        Internet_A = Internet;
        Ranking_A = Ranking;
    }

    //GETTERS
    public String getIP() {
        return IP_A;
    }

    public double getAlmacenamiento() {
        return almacenamiento_A;
    }

    public double getprocesador() {
        return procesador_A;
    }

    public double getRAM() {
        return RAM_A;
    }

    public String getInternet() {
        return Internet_A;
    }
    
    public double getRanking() {
        return Ranking_A;
    }
    /////////////////////////////////////

    public String toString() {
        return "DatosEquipo{" +
            "IP='" + IP_A + '\'' +
            ", almacenamiento=" + almacenamiento_A +
            ", procesador=" + procesador_A +
            ", RAM=" + RAM_A +
            ", Internet='" + Internet_A + '\'' +
            ", Ranking=" + Ranking_A +
            '}';
    }
    
    public List<Object> toTableRow() {
        return Arrays.asList(IP_A , procesador_A, RAM_A, Internet_A, almacenamiento_A, "Conectado");
    }

}
