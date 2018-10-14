import java.rmi.*;

public interface RingServerInterface extends Remote {
	public String verifyConnection() throws RemoteException; //Server status method
	public void receiveData(String ) throws RemoteException; //Server receives transport from its predecessor
}