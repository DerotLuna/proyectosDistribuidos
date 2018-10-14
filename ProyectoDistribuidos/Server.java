import java.net.*;
import java.io.*;

public class Server {
	private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";

	private static int myPort;
	private static int nextPort;
	private static String myIP;
	private static String nextIP;
	private static int node;

	public static void information(){
		try{
			File archivo = new File (DIR + "ringInformation.txt");
			FileReader fr = new FileReader (archivo);
			BufferedReader br = new BufferedReader(fr);
		
			String line = "";
			String[] lineParts; int nodeRead = 0;
	        while((line = br.readLine())!=null){
	 			lineParts = line.split(",");
	 			nodeRead = Integer.parseInt(lineParts[0]);
	 			if (nodeRead == node){ 
	 				myPort = Integer.parseInt(lineParts[2]);
	 				myIP = lineParts[1];
	 				nextIP = lineParts[3];
	 				nextPort = Integer.parseInt(lineParts[4]);
	 			}
	        }
	        fr.close();
		}
		catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("My IP is: " + myIP);
        System.out.println("My port server is: " + myPort);
        System.out.println("Successor IP is: " + nextIP);
        System.out.println("Successor port server is: " + nextPort);
	}

	public static void main(String[] args) {
		int clientCounter = 1;
		try {

	        System.out.println("Node number please...");
	        BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente
	        String key = inputUser.readLine();
	        node = Integer.parseInt(key);
	        information();


			System.out.println("Init Server...");
		   	ServerSocket serverSocket = new ServerSocket(myPort); //Levantando servidor que escucha por el puerto 8189.
		
		   	for (;;) {
				Socket clientSocket = serverSocket.accept(); //Aceptando los clientes que llaman al servidor.
	         	System.out.println("Client connect " + clientCounter + " " + clientSocket.getLocalAddress());
	         	new ThreadedServer(clientSocket, myPort, nextPort, nextIP, myIP).start(); //Enviando cliente a ejecutarse en un Hilo para liberar el puerto de escucha del servidor.
	         	clientCounter++;	
		    }
		} 
		catch (Exception e) { System.out.println(e); }
	}
}