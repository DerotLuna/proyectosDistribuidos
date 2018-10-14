import java.io.*;
import java.net.*;

public class Client {
	private int port_Server; // Puerto de escucha del servidor.
    private String server = "localhost"; // Host



    public static void main(String[] args) {
    	try {
	    	Socket socket = new Socket(server, port_Server);//abre socket para conectarse al servidor
	    	//--------------------- CLIENTE/SERVIDOR-------------------------------------------
	    	


	        //--------------------- CLIENTE-------------------------------------------------------
	        System.out.println("Iniciando Cliente...");
	        System.out.println("Cliente> Escriba los comando");
	        String requestClient;
	        BufferedReader inputClient = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente

	        for (;;) {
	        	//System.out.println("Cliente> Escriba comando");                
	        	requestClient = inputClient.readLine(); //Captura los comandos del cliente
				new ThreadedClient(socket, requestClient).start();
			}
			//--------------------------------------------------------------------------------------
	    	
	    	//socket.close();
	        //Thread threadedClient = new ThreadedClient();      
	        //threadedClientServer.start();
		}
		catch (Exception e) { System.out.println(e); }
    }
// }