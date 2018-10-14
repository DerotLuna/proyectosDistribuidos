import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerE {

    private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";
    private static int myPort;
    private static String myName;
    private static int node;
    private static int serverMain;

    public static void information(){
        try{
            File archivo = new File (DIR + "serversInformation.txt");
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
        
            String line = "";
            String[] lineParts; int nodeRead = 0;
            while((line = br.readLine())!=null){
                lineParts = line.split(",");
                nodeRead = Integer.parseInt(lineParts[0]);
                if (nodeRead == node){ 
                    myPort = Integer.parseInt(lineParts[3]);
                    myName = lineParts[1];
                    myIP = lineParts[2];
                    serverMain = Integer.parseInt(lineParts[4]);
                }
            }
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("My name is: " + myName);
        System.out.println("My port server is: " + myPort);
    }

    public static int hashCode(String port, String ip) {
        return Objects.hash(port, ip);
    }

    

    public static void saveData(String nameArchive){
        File archive = new File(DIR + nameArchive);
            if(archive.exists()){
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(archive));
                bw.write(data"\n" + data);
            } 
            else {
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(archive));
                bw.write(data);
            }
            bw.close();
    }
    
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        System.out.println("Node number please (Server's)...");
        BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente
        String key = inputUser.readLine();
        node = Integer.parseInt(key);
        information();

        Remote bridge = UnicastRemoteObject.exportObject(new Bridge() {

            public void verifyConnection(float numero1, float numero2) throws RemoteException {
                return "I a'm: " myName + " my port is: " + myPort;
            };

            public void amountPackages(String data) throws RemoteException {
                saveData("amountPackages.txt");
            };

        }, 0);
        
        Registry registry = LocateRegistry.createRegistry(myPort);
       	System.out.println("Server init");
        registry.bind(myName, bridge);

        if (serverMain == 1){
            new ThreadedServerMain ().start();
            ServerSocket serverSocket = new ServerSocket(myPort);
            for(;;){
                ServerSocket serverSocket = new ServerSocket(myPort);
                Socket clientSocket = serverSocket.accept(); //Aceptando los clientes que llaman al servidor.
                clientSocket.close();
            }
        }
        else{
            try{
                File archivo = new File (DIR + "serversInformation.txt");
                FileReader fr = new FileReader (archivo);
                BufferedReader br = new BufferedReader(fr);

                String port = "", ip = "", otherPort = "", otherIP = "", mainIP = ""; int mainPort = 0;
            
                String line = "";
                String[] lineParts; int nodeRead = 0;
                while((line = br.readLine())!=null){
                    lineParts = line.split(",");
                    nodeRead = Integer.parseInt(lineParts[0]);
                    if (nodeRead == node){ 
                        myPort = Integer.parseInt(lineParts[3]);
                        myName = lineParts[1];
                        myIP = lineParts[2];
                        serverMain = Integer.parseInt(lineParts[4]);
                    }
                }
                fr.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            while(true){
                try {     
                    Socket socket = new Socket(mainIP, mainPort);
                    socket.close();
                }                                    
                catch (Exception ex) {
                    try {        
                        
                    }
                    catch(Exception e){
                      
                    }
                }
            }
        }
    }
}