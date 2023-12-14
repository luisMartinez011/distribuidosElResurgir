package sistdist2daspruebas;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class SistDist2dasPruebas {

    public static void main(String[] args) {
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        boolean verificacion = true;
        double procesador;
        while (verificacion) {
            if (bean.getProcessCpuLoad() > 0) {
                procesador = (bean.getSystemCpuLoad() * 100);
                System.out.println(procesador);
                verificacion = false;
            }
        }

    }
}


