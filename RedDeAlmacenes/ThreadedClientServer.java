import java.net.*;
import java.io.*;

class ThreadedClientServer extends Thread {
    
    private final static String FILE_SEND = "/home/derot/Desktop/Videos/";    
    private final static String FILE_TO_RECEIVED = "/home/derot/Desktop/video.mp4";
    private final static int FILE_SIZE = 104857600;
    private final static String SERVER = "localhost"; // Host
    private final static int SERVER_PORT = 8189; // Host
    private Socket socket;
    private int portSendReceived;
    //private String requestClient;

    public ThreadedClientServer(Socket s){
        this.socket = s;
        this.portSendReceived = 0;
        //this.requestClient = rC;
    }

	public void run(){

        String[] partsReadLine = null;
        System.out.println("Puerto socket local " + socket.getLocalPort());
        System.out.println("Puerto socket sin local " + socket.getPort());
        //System.out.println("requestClient" + requestClient);

        try{
            BufferedReader input = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            partsReadLine = input.readLine().split(" ", 4);
            portSendReceived = Integer.parseInt(partsReadLine[3]);
            System.out.println("try RECIBIR");
        }
        catch(Exception e){ System.out.println("catch RECIBIR"); portSendReceived = 0; }

        if(portSendReceived == SERVER_PORT) //enviar video
        {
            System.out.println("IF ENVIAR");
            try {
                //BufferedReader input = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                //PrintStream output = new PrintStream(socket.getOutputStream());
                //String separador = Pattern.quote("@");
                //String[] partsReadLine = input.readLine().split(" ", 3);
                String videoRequest = partsReadLine[0];
                //String videoRequest = input.readLine();
                int partDownload = Integer.parseInt(partsReadLine[1]);
                //int partDownload = 0;
                int port = Integer.parseInt(partsReadLine[2]);
                System.out.println(videoRequest);
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                Socket sock = new Socket(SERVER, port);
                //PrintStream outputSR = new PrintStream(sock.getOutputStream());
                //outputSR.println(videoRequest);
                File myFile = new File (FILE_SEND + videoRequest);
                if (partDownload == 0){     
                    System.out.println("IF ENVIAR 0");       
                    try{                
                        byte [] mybytearray  = new byte [(int)myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray,0,mybytearray.length);
                        os = sock.getOutputStream();
                        System.out.println("Sending " + FILE_SEND + videoRequest + "(" + mybytearray.length + "bytes)");    
                        os.write(mybytearray,0,mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                    }               
                     finally {
                        if (bis != null) bis.close();
                        if (os != null) os.close();
                        if (sock!=null) sock.close();
                    }
                }   
            }
            catch (IOException e) { System.out.println(e); }
        }

        else{ //recibir video
            System.out.println("IF RECIBIR");
            try{
                int bytesRead;
                int current = 0;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    // receive file
                    byte [] mybytearray  = new byte [FILE_SIZE];
                    InputStream is = socket.getInputStream();
                    fos = new FileOutputStream(FILE_TO_RECEIVED);
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(mybytearray,0,mybytearray.length);
                    current = bytesRead;

                    do {
                        bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                    } while(bytesRead > -1);

                    bos.write(mybytearray, 0 , current);
                    bos.flush();
                    System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current + " bytes read)");
                }
                finally {
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (socket != null) socket.close();
                }
            }
            catch(IOException e){}
        }
    }      
}


