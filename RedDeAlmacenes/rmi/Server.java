import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;

public class Server {
	//private String nodeName;

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

		System.out.println("Name node please...");
		inputClient = new Scanner (System.in);
		String nodeName = inputClient.nextLine ();

		server(nodeName);

		if 
	}
}