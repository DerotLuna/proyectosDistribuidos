import java.net.*;
import java.io.*;

class ThreadedMaster extends Thread {
    //private final static int PORT = 8189; // Puerto de escucha del servidor.
    //private final static String SERVER = "localhost"; // Host
    public Socket socket;//Socket para la comunicacion de cliente al servidor
    public String nameNode;
    public int myPort;
    public int myIP;
    public int ipDest;
    public int portDest;
    public String dataSend;

    public ThreadedMaster(Socket s, String nameNode, int myIP, int myPort, int ipDest, int portDest){
        this.socket = s;
        this.myIP = myIP;
        this.myPort = myPort;
        this.ipDest = ipDest;
        this.portDest = portDest;
        this.dataSend = "nodo3:carga0,nodo1:carga1,nodo1:carga2,nodo1:carga3,nodo3:carga4,nodo3:carga5,nodo1:carga6,nodo3:carga7" 
    }       

	public void run(){       
        try {
            BufferedReader inputClient = new BufferedReader( new InputStreamReader(socket.getInputStream())); //Capturar data del cliente     
            PrintStream outputClient = new PrintStream(socket.getOutputStream()); //Enviar data al cliente

            String transport = inputClient.readLine();
            String[] transportPats = transport.split(",");
            String newTransport = "";

            for (int counter = 0; transportPats.length; counter++) {
                if(transportPats[counter].split(":")[0] == nameNode) {
                    System.out.println(transportPats[counter].split(":")[1]);
                }
                else{
                    if (counter == 0) newTransport = transportPats[counter]
                    else newTransport = newTransport + "," + transportPats[counter]
                }
            }
            
            String[] dataSendParts = dataSend.split(",");

            boolean exit = false; counter = 0;
            while (exit)
                transportPats = newTransport.split(",");
                if (transportPats.length == 5) exit = true
                else {
                    newTransport
                }

        }                                    
        catch (Exception ex) {        
            System.err.println("Cliente> " + ex.getMessage());   
        }
    }
}