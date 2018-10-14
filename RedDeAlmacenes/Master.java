import java.net.*;
import java.io.*;

public class Master{
	public String nameNode;
	public int myPort;
	public int myIP;
	public int ipDest;
	public int portDest;

	public static string[] information(){
		File archivo = new File ("home/derot/Desktop/RedDeAlmacenes");
		FileReader fr = new FileReader (archivo);
		BufferedReader br = new BufferedReader(fr);
	
		String line = "";
		String[] lineParts;
        while((line = br.readLine())!=null){
 			lineParts = line.spit(",")
 			if (lineParts[0] == nameNode){ 
 				myPort = Integer.parseInt(lineParts[2]);
 				myIP = Integer.parseInt(lineParts[1]);
 				ipDest = Integer.parseInt(lineParts[4]);
 				portDest = Integer.parseInt(lineParts[3]);
 			}
        }
	}

	public static void main(String[] args) {
		int clientCounter = 1;
		try {

	        System.out.println("Name node please...");
	        BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente
	        nameNode = inputUser.readLine();
	        information();
	        System.out.println("Init server");
		   	ServerSocket serverSocket = new ServerSocket(myPort); //Levantando servidor que escucha por el puerto myPort.
		   	for (;;) {
				Socket clientSocket = serverSocket.accept(); //Aceptando los clientes que llaman al servidor.
	         	new ThreadedMaster(clientSocket, nameNode, myIP, myPort, ipDest, portDest).start(); //Enviando cliente a ejecutarse en un Hilo para liberar el puerto de escucha del servidor.
	         	clientCounter++;
		    }
		} 
		catch (Exception e) { System.out.println(e); }
	}
}
