import java.net.*;
import java.io.*;

class ClientServer extends Thread {
    private int portClientServer;
    
    public ClientServer(int port){
        this.portClientServer = port;
    }

	public void run(){
        int clientCounter = 1;
        try {
            System.out.println("Iniciando Cliente/Servidor...");
            ServerSocket serverSocket = new ServerSocket(portClientServer); //Levantando cliente/servidor que escucha por el puerto 8190.
            for (;;) {
                Socket clientSocket = serverSocket.accept(); //Aceptando los clientes que llaman al servidor.
                System.out.println("CLIENT CONNECT FOR DOWNLOAD " + clientCounter + " " + clientSocket.getLocalAddress());
               
               // BufferedReader inputDownloads = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
              //  String videoRequest = inputDownloads.readLine();

                new ThreadedClientServer(clientSocket).start(); //Enviando cliente a ejecutarse en un Hilo para liberar el puerto de escucha del servidor.
                clientCounter++;
            }
        } 
        catch (Exception e) { System.out.println(e); }
    }
}
