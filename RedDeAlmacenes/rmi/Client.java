import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;

public class Client{
	//private String nodeName;

	public static void client(String nameNextNode){
		//System.setSecurityManager(new RMISecurityMananger());
		String url = "rmi://localhost/";

		try {
 			RingServerInterface nextNode = (RingServerInterface)Naming.lookup(url+nameNextNode);

			System.out.println(nextNode.verifyConnection());
			String[][] transport = {{"Node1", "Carga 1"}, {"Node2", "Carga 2"}, {"Node2", "Carga 3"}, {"Node2", "Carga 4"}}
			receiveTransportation(transport);
		} 

		catch (Exception e) {
 			System.out.println("Error: "+e);
		}
		//System.exit(0);
	}

	public static void server(String nodeName){
		try {
			RingServer node = new RingServer(nodeName);
			Naming.rebind(nodeName,node);
		}	 

		catch (Exception e) {
 			System.out.println("Error: "+e);
		}
	}

	public static void main(String args[]) {
		Scanner inputClient; 

		System.out.println("Name next node please...");
		inputClient = new Scanner (System.in);
		String nameNextNode = inputClient.nextLine ();

		server(nodeName);

		System.out.println("Enter for beginning please...");
		inputClient = new Scanner (System.in);
		String verifyConnection = inputClient.nextLine ();

		while (verifyConnection == "\n")
			client(nameNextNode);
	}
}