import java.net.*;
import java.io.*;

class ThreadedDownloadLoad extends Thread{
    //private final static int PORT = 8189; // Puerto de escucha del servidor.
    //private final static String SERVER = "localhost"; // Host
    private String type;
    private String transport;
    private String receiver;
    private String loader;
    private int myProcess;
    private int counter;
    private String packageLoad;
    private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";

    public ThreadedDownloadLoad(String type, String transport, int counter){
        this.type = type;
        this.transport = transport;
        this.myProcess = 0;
        this.counter = counter;
        this.receiver = "";
        this.packageLoad = "";
    }

    public ThreadedDownloadLoad(String type, String transport, int counter, String packageLoad){
        this.type = type;
        this.transport = transport;
        this.myProcess = 0;
        this.counter = counter;
        this.receiver = "";
        this.packageLoad = packageLoad;
    }

    private void acceptPackage(String idTransport, String pack){
        try{
            boolean exit = false; int counter = 1, first = 0; 
            while(!exit){
                File archive = new File(DIR + "receiver" + counter + ".txt");
                if(archive.exists()){
                    //System.out.println("Receiver " + counter + "occupied");
                    counter ++;
                } 
                else {
                    BufferedWriter bw;
                    bw = new BufferedWriter(new FileWriter(archive));
                    bw.write("Transport " + idTransport +" downloading package " + pack);
                    bw.close();
                    exit = true;
                    receiver = "receiver" + counter + ".txt";
                }

                if (counter == 4) {
                    if (first == 0) {
                        System.out.println("\033[33m[&][&]\u001B[0mTransport " + idTransport +" wait for download package " + counter);
                        first = 1;
                        try{Thread.sleep(1000);}
                         catch(Exception e){e.printStackTrace();}
                    }
                    counter = 1;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void freeReceiver(String idTransport){
        try{
            File archive = new File(DIR + receiver);
            if(archive.exists()){
                System.out.println("\033[34m[][]\u001B[0mFinalized download [" + counter + "] from transport " + idTransport);
                archive.delete();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private synchronized void download(){
        String[] transportPats = transport.split(",");
        acceptPackage(transportPats[0], transportPats[counter].split(":")[1]);
        System.out.println("\033[32m[+][+]\u001B[0mDownloading [" + counter + "] from transport: " + transportPats[0] + " package: " + transportPats[counter].split(":")[1] + "...");
        try{Thread.sleep(10000);}
        catch(Exception e){e.printStackTrace();}
        freeReceiver(transportPats[0]);
    }

    private void load(){
        String[] transportPats = transport.split(",");
        loadPackage(transportPats[0]);
        System.out.println("\033[31m[+][+]\u001B[0mLoading [" + counter + "] from transport: " + transportPats[0] + " package: " + packageLoad + "...");
        try{Thread.sleep(10000);}
        catch(Exception e){e.printStackTrace();}
        freeLoader(transportPats[0]);
    }

    private void loadPackage(String idTransport){
        try{
            boolean exit = false; int counter = 1, first = 0; 
            while(!exit){
                File archive = new File(DIR + "loading" + counter + ".txt");
                if(archive.exists()){
                    //System.out.println("Receiver " + counter + "occupied");
                    counter ++;
                } 
                else {
                    BufferedWriter bw;
                    bw = new BufferedWriter(new FileWriter(archive));
                    bw.write("Transport " + idTransport +" loading package " + packageLoad);
                    bw.close();
                    exit = true;
                    loader = "loading" + counter + ".txt";
                }

                if (counter == 4) {
                    if (first == 0) {
                        System.out.println("\033[33m[&][&]\u001B[0mTransport " + idTransport +" wait for load package " + counter);
                        first = 1;
                        try{Thread.sleep(1000);}
                         catch(Exception e){e.printStackTrace();}
                    }
                    counter = 1;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void freeLoader(String idTransport){
        try{
            File archive = new File(DIR + loader);
            if(archive.exists()){
                System.out.println("\033[34m[][]\u001B[0mFinalized load [" + counter + "] from transport " + idTransport);
                archive.delete();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){       
        try {
            if (type.equals("download")) download();
            else if (type.equals("load")) load();
        }                                    
        catch (Exception ex) {        
            System.err.println("Error > " + ex.getMessage());   
        }
    }
}