package sistdist2daspruebas;

// <editor-fold defaultstate="uncollapsed" desc="Imports">

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

// </editor-fold>


public class Metodos {

    static String anchoDeBanda = "";
    
    // <editor-fold defaultstate="collapsed" desc="Funcion ComandoCMD">
    public static String ComandoCMD(String comando) throws InterruptedException {
        String respuesta = "";
        boolean primeraLineaLeida = false; // Variable para controlar si la primera línea ya se ha leído
        try {
            Process process = Runtime.getRuntime().exec(comando);
            BufferedReader lector = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!primeraLineaLeida) {
                    primeraLineaLeida = true;
                } else {
                    if (linea.length() != 0) {
                        respuesta = respuesta.concat(linea + ", ");
                        respuesta = respuesta.replace("    ", "");
                        respuesta = respuesta.replace("   ", "");
                        respuesta = respuesta.replace("                  ", "");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Funcion Internet">
    public static String Internet() {
        String urlStr = "https://www.youtube.com/";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            long startTime = System.currentTimeMillis();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream in = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytes = 0;
                while ((bytesRead = in.read(buffer)) != -1) {
                    totalBytes += bytesRead;
                }
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                double anchoDeBandaMbps = ((totalBytes / 8) / elapsedTime * 1000) / 10000;
                anchoDeBanda = String.format("%.2f", anchoDeBandaMbps) + " Mbps";
            } else {
                System.err.println("La solicitud no fue exitosa. Código de respuesta: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            return "0 Mbs/s";
        }
        return anchoDeBanda;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    public static float getProcesador(/*SystemInfo systemInfo, HardwareAbstractionLayer hardware, long[] prevTicks*/) {      
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        boolean verificacion = true;
        float procesador = 0;
        while (verificacion) {
            if (bean.getProcessCpuLoad() > 0) {
                procesador = (float) (bean.getSystemCpuLoad() * 100);
                verificacion = false;
            }
        }
        
        return 100 - procesador;
        
    }
    

    public static double getRAM(SystemInfo systemInfo, HardwareAbstractionLayer hardware) {
        // Obtener el porcentaje de uso de la CPU
        GlobalMemory memory = hardware.getMemory();
        double availableMemory = ((double) (memory.getAvailable()) / (1024 * 1024 * 1024));

        return availableMemory;
    }

    public static double getAlmacenamiento(SystemInfo systemInfo, HardwareAbstractionLayer hardware) {
        // Obtener información del almacenamiento
        List<OSFileStore> fileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
        long totalSpace = 0;
        long usableSpace = 0;

        for (OSFileStore fileStore : fileStores) {
            totalSpace += fileStore.getTotalSpace();
            usableSpace += fileStore.getUsableSpace();
        }
        return ((double) (usableSpace) / (1024 * 1024 * 1024));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Funcion Obtener IP">
    public static String obtenerIPv4() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // Verifica si es una dirección IPv4 y no es la dirección loopback
                    if (addr instanceof java.net.Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si no se pudo obtener la dirección IPv4
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funcion Verificar socket">
    public static boolean verificarSocket(String direccionIP , int puerto, int timeout) {
        InetSocketAddress IP = new InetSocketAddress(direccionIP, puerto);
        try (Socket socket = new Socket()) {
            socket.connect(IP, timeout);
            return true; // Si la conexión se realiza con éxito, el socket está abierto.
        } catch (Exception e) {
            // Si hay una excepción, el socket está cerrado o inaccesible.
            return false;
        }
    }
    // </editor-fold>
    
    public static String buscarIP() {
        //BUSCAR UNA IP CON SERVER ABIERTO PARA CONECTARSE 
        String IPAbierta = null;
        boolean IPEncontrada = false;
        int i = 2;
        Socket sTemp = null;
        while (IPEncontrada == false) {
            //DEBE SER DIFERENTE A LA IP PROPIA
            if ((Metodos.obtenerIPv4().equals("192.168.1." + String.valueOf(i))) == false) {
                if (Metodos.verificarSocket("192.168.1." + String.valueOf(i), 2635, 10) == true) {
                    IPAbierta = "192.168.1." + String.valueOf(i);
                    IPEncontrada = true;
                } else {
                    System.out.println("192.168.1." + String.valueOf(i) + " CERRADA");
                    i++;
                    if (i > 255) {
                        System.out.println("No se ubico al servidor, se asignara el local como servidor");
                        IPAbierta = Metodos.obtenerIPv4();
                        IPEncontrada = true;
                    }
                }
            } else {
                i++;
            }
        }
        return IPAbierta;
    }
}
