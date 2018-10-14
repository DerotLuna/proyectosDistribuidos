import java.rmi.*;
import java.rmi.server.*;

public class RingServer extends UnicastRemoteObject implements RingServerInterface{
	private String name;

	public RingServer (String name)  throws RemoteException{ 
		this.name = name; 
	}

	public String verifyConnection() throws RemoteException{
		return "I'm here, my name is: " + name;
	}

	public void receiveTransportation(String data) throws RemoteException{
		
	}
}	