import java.net.*;
import java.io.*;

class ThreadedServer extends Thread{
	private Socket clientSocket;
	private int myPort;
	private int nextPort;
	private String nextIP;
	private String myIP;
	private int myProcess;
	private int nonDownload;
	private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";

	public ThreadedServer(Socket client, int port, int nextPort, String nextIP, String myIP){
 		this.clientSocket = client;
 		this.myPort = port;
 		this.nextPort = nextPort;
 		this.nextIP = nextIP;
 		this.myIP = myIP;
 		this.myProcess = 0;
 		this.nonDownload = 0;
	}

	private synchronized String download_Transport(String transport){
		String[] transportPats = transport.split(",");
        String newTransport = transport.split(",")[0];

        for (int counter = 1; counter <= transportPats.length - 1; counter++) {
        	int portDest = Integer.parseInt(transportPats[counter].split(":")[0]);
            if(portDest == myPort) {
            	new ThreadedDownloadLoad("download",transport, counter).start();
            	try{Thread.sleep(1000);}
        		catch(Exception e){e.printStackTrace();}
        		myProcess ++;
            }
            else{
                newTransport = newTransport + "," + transportPats[counter];
            }
        }
        if (transportPats.length == newTransport.split(",").length) nonDownload = 1;
        return newTransport;
	}

	public synchronized void run() {
	  	try {
	   		BufferedReader input = new BufferedReader (new InputStreamReader(clientSocket.getInputStream())); //Catch client msg
	   		PrintStream output = new PrintStream(clientSocket.getOutputStream()); //Send msg to client
	   		output.println("I'm here, my IP: " + myIP + " and my Port: " + myPort);
	   		String transport = input.readLine();

	   		transport = download_Transport(transport);
	   		

	   		BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente
            String key = inputUser.readLine();

	   		new ThreadedClient(clientSocket, transport, myPort, nextPort, nextIP, myIP).start();

	   	
	   	/*	boolean exit = false;
	   		while (!exit)
	   		{
	   			if (nonDownload == 1) {
	   				new ThreadedClient(clientSocket, transport, myPort, nextPort, nextIP, myIP).start();
	   				exit = true;
	   			}
	   			else if (myProcess != 0) {
	   				try{Thread.sleep(10000*myProcess);}
        			catch(Exception e){e.printStackTrace();}
	   				new ThreadedClient(clientSocket, transport, myPort, nextPort, nextIP, myIP).start();
	   			}
	   		}*/
	  	}  
	  	catch (Exception e) { System.out.println(e); }
	}
}