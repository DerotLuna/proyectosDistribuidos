import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

class ThreadedClient extends Thread{
    private String transport;
    private String sendPackages;
    private int myPort;
    private int nextPort;
    private String nextIP;
    private String myIP;
    private String packagesTransport;
    private Socket clientSocket;
    private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";
    

    public ThreadedClient(Socket clientSocket, String transport, int port, int nextPort, String nextIP, String myIP){

        this.transport = transport;
        this.myPort = port;
        this.myIP = myIP;
        this.nextPort = nextPort;
        this.nextIP = nextIP;
        this.sendPackages = "";
        this.packagesTransport = "";
        this.clientSocket = clientSocket;
    }

    private synchronized void updateSendPackages(){
        try{
            File archive = new File (DIR + "sendPackages"+ myPort +".txt");
            FileWriter fr = new FileWriter (archive);
            PrintWriter pw = new PrintWriter(fr);
            pw.print(sendPackages);
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void download_Data(int numberDelete){
        String[] packages = sendPackages.split(",");

        for (int counter = 0; counter < packages.length; counter++){
           if (counter >= numberDelete){
                if (counter == numberDelete) sendPackages = packages[counter];
                else sendPackages = sendPackages + "," + packages[counter];
           }
           else {
                if (counter == 0) packagesTransport = packages[counter];
                else packagesTransport = packagesTransport + "," + packages[counter];
           }
        }
        updateSendPackages();
    }

    public void newIPPort(){
        try{
            File archivo = new File (DIR + "ringInformation.txt");
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
        
            String line = "";
            String[] lineParts; int portRead = 0; String ipRead = ""; System.out.println("Assigning...");
            while((line = br.readLine())!=null){
                
                lineParts = line.split(",");
                ipRead = lineParts[1];
                portRead = Integer.parseInt(lineParts[2]);
                if (portRead == nextPort && ipRead.equals(nextIP)){ 
                    nextIP = lineParts[3];
                    nextPort = Integer.parseInt(lineParts[4]);
                }
            }
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("New successor IP is: " + nextIP);
        System.out.println("New successor port server is: " + nextPort);
    }

    private void load_Transport(){
        if (transport.split(",").length != 6){
            try{
                boolean exit = false;
                File archive = new File(DIR + "reservingPackages.txt");
                while(!exit){
                    if(!archive.exists()){
                        BufferedWriter bw;
                        bw = new BufferedWriter(new FileWriter(archive));
                        bw.write("Reserving packages to transport " + transport.split(",")[0]);
                        bw.close();

                        File read = new File (DIR + "sendPackages" + myPort + ".txt");
                        FileReader fr = new FileReader (read);
                        BufferedReader br = new BufferedReader(fr);
                        sendPackages = br.readLine();
                        br.close();
                        download_Data(6 - transport.split(",").length);

                        archive.delete();
                        exit = true;
                    }
                    else{
                        try{Thread.sleep(1000);}
                        catch(Exception e){e.printStackTrace();}
                    }
                }

                for (int counter = 0; counter < packagesTransport.split(",").length; counter++) {
                    //System.out.println("      Packages Transport " +  packagesTransport.split(",")[counter]);
                    new ThreadedDownloadLoad("load",transport, counter + 1, packagesTransport.split(",")[counter]).start();
                    try{Thread.sleep(1000);}
                    catch(Exception e){e.printStackTrace();} 
                }
            }  
            catch(Exception e){
                System.out.println("");
            }
            transport = transport + "," + packagesTransport;
        }
    }
    

	public void run(){
            try {     
                load_Transport();
                clientSocket.close();

               BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente
               String key = inputUser.readLine();

                Socket socket = new Socket(nextIP, nextPort);
                System.out.println("Connect to server IP: " + nextIP + " Port: " + nextPort);
                BufferedReader inputServer = new BufferedReader( new InputStreamReader(socket.getInputStream())); //Catch next server             
                PrintStream outputServer = new PrintStream(socket.getOutputStream()); //Send msg to next server
                //if (inputServer.readLine() == "I'm here!")
                System.out.println(inputServer.readLine());
                outputServer.println(transport);
                
            }                                    
            catch (Exception ex) {
                try {        
                    System.out.println("Looking for destination transport: " + transport.split(",")[0] + " ...");
                    newIPPort();

                    if (nextIP.equals(myIP) && nextPort == myPort){
                        System.out.println("Ring fallen in its entirety");
                        stop();
                    }

                    Socket socket = new Socket(nextIP, nextPort);
                    System.out.println("Connect to server IP: " + nextIP + " Port: " + nextPort);
                    BufferedReader inputServer = new BufferedReader( new InputStreamReader(socket.getInputStream())); //Catch next server             
                    PrintStream outputServer = new PrintStream(socket.getOutputStream()); //Send msg to next server
                    //if (inputServer.readLine() == "I'm here!")
                    System.out.println(inputServer.readLine());
                    outputServer.println(transport);
                    
                }
                catch(Exception e){
                  
                }
            }
        
    }
}