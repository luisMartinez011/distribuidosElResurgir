package sistdist2daspruebas;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.jna.platform.mac.SystemConfiguration;

// </editor-fold>

public class SistemaDistribuidoElResurgir extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="VariablesGlobales">
    // OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI  OSHI  OSHI 
    public static SystemInfo systemInfo = new SystemInfo();
    public static HardwareAbstractionLayer hardware = systemInfo.getHardware();
    // OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI OSHI  OSHI  OSHI 

    // INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ
    private static DefaultTableModel modelo;
    private static javax.swing.JButton BtnConectar;
    private static javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private static SistemaDistribuidoElResurgir interfaz = new SistemaDistribuidoElResurgir();
    // INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ INTERFAZ

    // Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente 
    public static DatosCliente[] datoscliente = new DatosCliente[20];
    static int contadorDeClientes = 0;
    // Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente Cliente 

    // SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET 
    private static Socket sCte = null;
    static DataOutputStream dos = null;
    static DataInputStream dis = null;
    static String IPServerActual;
    // SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET SOCKET 

    // SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    // SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 

    //ESPECIAL : Evitara que se creen multiples hiloxserver
    static int ControladoraDeHilosxServer = 0;
    //ESPECIAL ESPECIAL ESPECIAL ESPECIAL ESPECIAL ESPECIAL ESPECIAL ESPECIAL ESPECIAL 
    
    
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Interfaz">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        BtnConectar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(972, 460));
        setSize(new java.awt.Dimension(972, 460));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel1.setText("Clientes Conectados");

        String datos[][] = {};
        String columna[] = {"IP", "% libre del procesador ", "Memoria libre (GB)", "Ancho de banda", "Almacenamiento Disponible (GB)", "Estatus"};
        modelo = new DefaultTableModel(datos, columna) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jTable1.setModel(modelo);
        jTable1.setRowHeight(25);
        jTable1.setEnabled(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setFillsViewportHeight(true);

        jScrollPane1.setViewportView(jTable1);

        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(83);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(83);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(83);
        }
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(tcr);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(tcr);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(tcr);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(tcr);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(tcr);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(tcr);

        BtnConectar.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        BtnConectar.setForeground(new java.awt.Color(51, 153, 0));
        BtnConectar.setText("Conectar");
        BtnConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setActionCommand("Sobrecarga");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setLabel("Sobrecarga");
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(87, 87, 87)
                                                .addComponent(BtnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(443, 443, 443)
                                .addComponent(jButton1)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(BtnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap())
        );

        pack();
    }

    public SistemaDistribuidoElResurgir() {
        initComponents();
        setLocationRelativeTo(null);
    }
    // </editor-fold>

    public static void main(String[] args) throws Exception {
        new SistemaDistribuidoElResurgir().setVisible(true);
        BtnConectar.setVisible(false);
        ObtenerDatosLocales();
        BtnConectar.setVisible(true);
        Server();
        
    }

    // <editor-fold defaultstate="collapsed" desc="Botones">
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        BtnConectar.setVisible(false);
        ColocarEnPosicion(0);
        // <editor-fold defaultstate="collapsed" desc="BACKUP">
        //BUSCAR UNA IP CON SERVER ABIERTO PARA CONECTARSE 
        /*boolean IPEncontrada = false;
        int i = 2;
        Socket sTemp = null;
        while (IPEncontrada == false) {
            //DEBE SER DIFERENTE A LA IP PROPIA
            if ((Metodos.obtenerIPv4().equals("192.168.1." + String.valueOf(i))) == false) {
                if (Metodos.verificarSocket("192.168.1." + String.valueOf(i), 2635, 10) == true) {
                    IPServerActual = "192.168.1." + String.valueOf(i);
                    System.out.println(IPServerActual + " Abierta. Comnectando...");
                    ConexionServer(IPServerActual);
                    IPEncontrada = true;
                } else {
                    System.out.println("192.168.1." + String.valueOf(i) + " CERRADA");
                    i++;
                    if (i > 255) {
                        System.out.println("No se ubico al servidor, se asignara el local como servidor");
                        IPServerActual = Metodos.obtenerIPv4();
                        ConexionServer(IPServerActual);
                        IPEncontrada = true;
                    }
                }
            } else {
                i++;
            }
        }*/
        // </editor-fold>
        IPServerActual = Metodos.buscarIP();
        ConexionServer(IPServerActual);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Sobrecargando al server: " + IPServerActual);
        Socket sAttack = null;
        try {
            sAttack = new Socket(IPServerActual, 2635);

            System.out.println(": Se conecto exitosamente con el SERVER " + IPServerActual + ". Enviando ataque");

            DataOutputStream dosCte = null;

            try {
                dosCte = new DataOutputStream(sAttack.getOutputStream());
                
                boolean EmvioDeDatos = false;
                dosCte.writeBoolean(EmvioDeDatos);
                
                boolean EsAtaque = true;
                dosCte.writeBoolean(EsAtaque);

            } catch (IOException ex) {
                System.out.println(ex);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ConexionServer">
    public static void ConexionServer(String IPSocket) {
        System.out.println("Servidor IP: " + IPSocket);
        try {
            sCte = new Socket(IPSocket, 2635);

            System.out.println(": Se conecto exitosamente con el servidor " + IPSocket + ". Enviando datos...");
            
            ApagarSchedulers();
            scheduler = Executors.newScheduledThreadPool(0);
            scheduler.scheduleAtFixedRate(HiloxServer, 0, 10, TimeUnit.SECONDS);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Server">
    public static void Server() throws IOException {
        //boolean soyServer;
        Socket sSvr = null;
        ServerSocket ssSvr = new ServerSocket(2635);
        System.out.println("Servidor local en eschucha...");

        while (true) {
            try {
                boolean soyServer;
                //Recibe datos de todos los clientes cada que pasen 10 segundos para cada cliente
                sSvr = ssSvr.accept();
                InetAddress ip = sSvr.getInetAddress();

                dos = new DataOutputStream(sSvr.getOutputStream());
                dis = new DataInputStream(sSvr.getInputStream());
                
                if(IPServerActual.equals(Metodos.obtenerIPv4())) {
                    soyServer = true;
                } else {
                    soyServer = false;
                }
                
                System.out.println("Se conectaron desde " + ip);
                System.out.println("Soy server? " + soyServer);
                
                boolean RecibirDatos = dis.readBoolean();
                int numHilo = contadorDeClientes;
                
                
               
                if (RecibirDatos == true) {
                    if (soyServer == true) {
                        jButton1.setVisible(false);
                        contadorDeClientes++;

                        if ((ip.toString().replace("/", "")).equals(datoscliente[0].getIP())) {
                            numHilo = 0;
                            contadorDeClientes--;
                        }

                        (new HiloxCliente(sSvr, ssSvr, numHilo)).start();
                    } else {
                        jButton1.setVisible(true);
                        

                        //Verifica si la ip desde donde se conectaron es diferente a la ip local
                        if (((ip.toString().replace("/", "")).equals(datoscliente[0].getIP())) == false) {
                            //Verifica si la ip desde donde se conectaron es la ip del servidor actual,
                            //TRUE - Asigna al servidor local como servidor general
                            //FALSE - Envia a la ip conectada la ip del server general actual
                            if ((ip.toString().replace("/", "")).equals(IPServerActual)) {
                                System.out.println("Se cambiará la direccion del servidor a la ip local...");
                                
                                IPServerActual = Metodos.obtenerIPv4();
                                ConexionServer(IPServerActual);
                                
                                contadorDeClientes++;
                                (new HiloxCliente(sSvr, ssSvr, numHilo)).start();
                            } else {
                                //Tiene que recibir todos los datos que el cliente quiere enviar para evitar errores??
                                float inecesario1 = dis.readFloat();
                                double inecesario2 = dis.readDouble();
                                double inecesario3 = dis.readDouble();
                                String inecesario4 = dis.readUTF();
                                
                                System.out.println("Enviando ip del server al cliente conectado");
                                EnviarIP(ip.toString().replace("/", ""), IPServerActual);
                            }
                        }
                    }
                }

                if (RecibirDatos == false) {
                    boolean EsAtaque=false;
                    try {
                        EsAtaque = dis.readBoolean();
                    } catch (Exception e) {
                        System.out.println("Booleano de nuevo");
                    }
                    if (EsAtaque == false) {
                        IPServerActual = dis.readUTF();
                        ConexionServer(IPServerActual);
                        System.out.println("Se cambio la direccion del servidor... ");
                    }
                    if (EsAtaque == true) {
                        System.out.println("Recibiendo ataque de " + ip.toString());
                        Process process = Runtime.getRuntime().exec("call ../../batch/ataque.bat");
                    }
                        
                    
                }
                
            } catch (Exception ex) {
                System.out.println("Error al leer el booleano. Reintentando...");
            }
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="obtenerDatosLocales">
    public static void ObtenerDatosLocales() {
        try {

            float procesadorLocal = Metodos.getProcesador();
            System.out.println(procesadorLocal + " %");

            double RAMLocal = Metodos.getRAM(systemInfo, hardware);
            System.out.println(RAMLocal + " GB");

            String InternetLocal = Metodos.Internet();
            System.out.println(InternetLocal);

            double almacenamientoLocal = Metodos.getAlmacenamiento(systemInfo, hardware);
            System.out.println(almacenamientoLocal + " GB");

            double RankingLocal = RAMLocal * (procesadorLocal / 100);
            System.out.println("Ranking " + RankingLocal);

            datoscliente[0] = new DatosCliente(Metodos.obtenerIPv4(), almacenamientoLocal, procesadorLocal, RAMLocal, InternetLocal, RankingLocal);
            //modelo.addRow(datoscliente[0].toTableRow().toArray());
            contadorDeClientes++;

        } catch (Exception e) {
            System.out.println("Error en la obtencion de datos locales");
        }

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Colocar en Posicion">
    public static void ColocarEnPosicion(int numHilo) {

        System.out.println("\n");
        System.out.println(numHilo + " " + "Actualizando: Colocando en posicion...");
        //Si aun no hay filas solo se agrega la Actual

        if (modelo.getRowCount() == 0) {
            System.out.println(numHilo + " " + "No hay filas existentes. Creando fila...");
            modelo.insertRow(0, datoscliente[numHilo].toTableRow().toArray());
            System.out.println(numHilo + " " + "Creada correctamente...");
        } else {
            //Verificar si ya esta esa ip o es nueva
            boolean YaExiste = false;
            int posicion = 0;
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String actual = datoscliente[numHilo].IP_A;
                String IPaComparar = modelo.getValueAt(i, 0).toString();
                if (actual.equals(IPaComparar)) {
                    System.out.println(numHilo + " " + "Actualizando: La fila con la ip ya existe...");
                    YaExiste = true;
                    posicion = i;
                }
            }
            //Eliminarla si ya existe
            if (YaExiste == true) {
                System.out.println(numHilo + " " + "Eliminando para actualizar...");
                modelo.removeRow(posicion);
            } else {
                //Clientes.add(numHilo, datoscliente[numHilo].getIP());
            }

            //Colocarla en posicion 
            boolean posicionado = false;
            ///*
            try {
                for (int i = 0; i < modelo.getRowCount(); i++) {

                    Float RankingAComparar = (Float.valueOf(modelo.getValueAt(i, 2).toString())) * (Float.valueOf(modelo.getValueAt(i, 1).toString()) / 100);

                    /*if ((datoscliente[numHilo].getRAM().toString()) != (RAMaComparar.toString())) {
                        
                    }*/
                    System.out.println("Ranking actual " + datoscliente[numHilo].getRanking() + " Ranking a comparar " + RankingAComparar);
                    if (datoscliente[numHilo].getRanking() > Double.parseDouble(RankingAComparar.toString())) {

                        System.out.println(numHilo + " " + "El ranking actual es mayor que alguno otro en la fila. Actualizando posiciones");
                        //Insertar fila vacia al final
                        modelo.addRow(new Object[]{});
                        //Bucle para ir recorriendo los datos de las filas
                        for (int j = modelo.getRowCount() - 1; j > i; j--) {

                            modelo.moveRow(j - 1, j - 1, j);

                        }
                        System.out.println(numHilo + " " + "Insertando en posicion correspondiente " + i);
                        modelo.insertRow(i, datoscliente[numHilo].toTableRow().toArray());
                        //Elimina fila vacia generada
                        modelo.removeRow(i + 1);
                        i = modelo.getRowCount() + 1;
                        System.out.println(numHilo + " " + "Finalizando. Bucle " + numHilo + " terminado");
                        posicionado = true;
                    }
                }

            } catch (Exception e) {
                System.out.println("Prueba " + e);
            }

            if (modelo.getRowCount() == 0) {
                System.out.println(numHilo + " " + "Actualizada. Volviendo a insertar fila");
                modelo.insertRow(0, datoscliente[numHilo].toTableRow().toArray());
                posicionado = true;
                System.out.println(numHilo + " " + "Insertada correctamente...");
            }

            if (posicionado == false) {
                System.out.println(numHilo + " " + "El dato nuevo para " + numHilo + ": " + datoscliente[numHilo].getRAM() + " es el menor. Insertando al final...");
                modelo.addRow(datoscliente[numHilo].toTableRow().toArray());
            }

        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HiloxCliente">
    static class HiloxCliente extends Thread {

        private Socket sSvr = null;
        private ServerSocket ssSvr = null;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private int filaHilo;
        private int inecesario = 0;
        private boolean inecesario1;

        public HiloxCliente(Socket socketSvr, ServerSocket serversocketSvr, int numHilo) {
            this.sSvr = socketSvr;
            this.ssSvr = serversocketSvr;
            this.filaHilo = numHilo;
        }

        public void run() {
            String IP = null;
            double almacenamiento;
            float procesador;
            double RAM;
            String Internet = null;
            String estatus = "Conectado";
            double Ranking;
            try {

                InetAddress ips = sSvr.getInetAddress();
                System.out.println("Se conectaron desde la IP: " + ips);

                dis = new DataInputStream(sSvr.getInputStream());
                dos = new DataOutputStream(sSvr.getOutputStream());

                boolean conectado = true;
                inecesario = 0;
                int posicionIP = 0;
                while (conectado) {
                    try {
                        if (inecesario > 0) {
                            inecesario1 = dis.readBoolean();
                        }
                        inecesario++;

                        IP = ips.toString().replace("/", "");

                        procesador = dis.readFloat();
                        
                        almacenamiento = dis.readDouble();
                        
                        RAM = dis.readDouble();

                        Internet = dis.readUTF();

                        Ranking = (procesador / 100) * RAM;
                        
                        
                        System.out.println("Se obtuvieron los datos del cliente " + IP);
                        datoscliente[filaHilo] = new DatosCliente(IP, almacenamiento, procesador, RAM, Internet, Ranking);

                        System.out.println(datoscliente[filaHilo]);

                        try {
                            ColocarEnPosicion(filaHilo);
                        } catch (Exception e) {
                            System.out.println("No se pudo colocar en posicion");
                        }

                        if ((Metodos.obtenerIPv4().equals(modelo.getValueAt(0, 0).toString())) == false) {
                            IPServerActual = modelo.getValueAt(0, 0).toString();
                            System.out.println("La ip original ya no es la mejor opcion (ahora es " + modelo.getValueAt(0, 0) + ")");
                            

                            int cantidadDeClientes = modelo.getRowCount();
                            System.out.println("Enviando ips de todos los clientes  cantidad:" + cantidadDeClientes);

                            for (int i = 1; i < cantidadDeClientes - 1; i++) {
                                //Se envia la ip del nuevo mejor server a todos los clientes menos el actual y el nuevoMejorServer
                                
                                if ((IPServerActual.equals(datoscliente[0].getIP())) == false) {
                                    System.out.println("Enviando IP del nuevo server a "+ datoscliente[i].getIP());
                                    EnviarIP(datoscliente[i].getIP(), IPServerActual);
                                }
                            }
                            
                            System.out.println("Conectando al nuevo Cliente/Servidor " + IPServerActual);
                            ConexionServer(IPServerActual);
                            System.out.println("IP del servidor principal cambiada correctamente");
                            
                            System.out.println("Eliminando Filas del modelo tabla actual (solo dejando la original)");
                            //Eliminar filas del modelo actual
                            int rowCount = modelo.getRowCount();
                            for (int i = rowCount - 1; i >= 0; i--) {
                                modelo.removeRow(i);
                            }
                            modelo.addRow(datoscliente[0].toTableRow().toArray());

                            contadorDeClientes = 1;

                           
                            System.out.println("Teminando este proceso...");
                        } else {
                            System.out.println("Sigue siendo la mejor opcion");
                        }

                    } catch (Exception ex) {
                        estatus = "Desconectado";

                        estatus = "Conectao";
                        System.out.println("Se cerro aqui" + ex);
                        conectado = false;
                    }
                }
            } catch (Exception ex) {
                System.out.println("Se cerro aca");
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="EnviarDatosAlServer">
    //Se actualiza cada 10 segundos
    static Runnable HiloxServer = new Runnable() {
        @Override
        public void run() {
            if((IPServerActual.equals(Metodos.obtenerIPv4())) == false) {
                jButton1.setVisible(true);
            }

            DataOutputStream dosCte = null;

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            try {
                dosCte = new DataOutputStream(sCte.getOutputStream());
                
                float procesador = Metodos.getProcesador();

                double RAM = Metodos.getRAM(systemInfo, hardware);
                
                String Internet = Metodos.Internet();

                double almacenamiento = Metodos.getAlmacenamiento(systemInfo, hardware);

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                try {
                    boolean EnvioDeDatos = true;
                    dosCte.writeBoolean(EnvioDeDatos);
                    
                    dosCte.writeFloat(procesador);

                    dosCte.writeDouble(almacenamiento);

                    dosCte.writeDouble(RAM);

                    dosCte.writeUTF(Internet);
                } catch (Exception ex) {
                    System.out.println("Error A: Socket cerrado, Buscando nuevo server");
                    IPServerActual = Metodos.buscarIP();
                    contadorDeClientes = 1;
                    ConexionServer(IPServerActual);
                } finally {
                    if (ControladoraDeHilosxServer > 0) {
                        dosCte.close();
                        sCte.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            System.out.println("\nACTUALIZANDO CTE");
        }
    };
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="EnviarIP">
    //Conectarse a los clientes y enviar IP del nuevo servidor
    public static void EnviarIP(String IPCliente, String IPnuevoSvr) {
        System.out.println("Enviando nueva IP a: " + IPCliente);

        try {
            sCte = new Socket(IPCliente, 2635);

            System.out.println(": Se conecto exitosamente con el cliente " + IPCliente + ". Enviando IP del nuevo servidor..." + IPnuevoSvr);

            DataOutputStream dosCte = null;

            try {
                dosCte = new DataOutputStream(sCte.getOutputStream());

                boolean EnvioDeDatos = false;
                dosCte.writeBoolean(EnvioDeDatos);
                boolean EnvioDeAtaque = false;
                dosCte.writeBoolean(EnvioDeAtaque);
                
                //No es envio de datos ni de ataque, solo de ip
                dosCte.writeUTF(IPnuevoSvr);

            } catch (IOException ex) {
                System.out.println(ex);
            }finally {
                dosCte.close();
                sCte.close();
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ApagarScheduler">
    public static void ApagarSchedulers() {
        // Supongamos que 'scheduler' es tu ScheduledExecutorService
        scheduler.shutdown();

        try {
            // Esperar hasta que todas las tareas estén completas o hasta que se alcance el tiempo máximo de espera
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                // Si el tiempo de espera expira, intenta apagar forzosamente el scheduler
                scheduler.shutdownNow();
                System.out.println("Apagando schedulers");
                // Espera adicional si es necesario
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("No se pudo apagar el scheduler");
                }
            }
        } catch (InterruptedException e) {
            // Manejar la interrupción si es necesario
            e.printStackTrace();
        }

    }
    // </editor-fold>
}
