import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bridge extends Remote {
    public String veriryConnection() throws RemoteException;
    public void amountPackages(String data) throws RemoteException;
}