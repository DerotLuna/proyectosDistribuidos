import java.net.*;
import java.io.*;

class ThreadedClient extends Thread {
    //private final static int PORT = 8189; // Puerto de escucha del servidor.
    //private final static String SERVER = "localhost"; // Host
    Socket socket;//Socket para la comunicacion de cliente al servidor
    String requestClient;

    public ThreadedClient(Socket s, String rC){
        this.socket = s;
        this.requestClient = rC;
    }       

	public void run(){       
        try {
            //System.out.println("Iniciando Cliente...");

            boolean exit = false;//bandera para controlar ciclo del programa
                               
            System.out.println("Peticion Cliente...");  
           // while( !exit ){//ciclo repetitivo                                
                //socket = new Socket(SERVER, PORT);//abre socket                       
                BufferedReader inputServer = new BufferedReader( new InputStreamReader(socket.getInputStream())); //Leer respuestas del servidor               
                PrintStream outputServer = new PrintStream(socket.getOutputStream()); //Imprimir datos del servidor 

               /* BufferedReader inputClient = new BufferedReader(new InputStreamReader(System.in)); //Leer comandos del cliente          
                System.out.println("Cliente> Escriba comando");                
                String requestClient = inputClient.readLine(); //Captura los comandos del cliente  */    

                outputServer.println(requestClient); //Envia comandos al servidor
                System.out.println("ENVIANDO Y ESPERANDO AL SERVIDOR..."); 
                String outputClient = inputServer.readLine(); //Captura respuesta del servidor
                if(outputClient != null ) System.out.println("Servidor> " + socket.getLocalAddress() + " " + outputClient );    
              
               /* if(requestClient.equals("BYE")){//terminar aplicacion
                    exit=true;                  
                    socket.close();
                    System.out.println("CLOSE SOCKET CLIENT... " + socket.getLocalAddress());
                    stop();
                    destroy();   
                }  */
           // } //end while 
        }                                    
        catch (Exception ex) {        
            System.err.println("Cliente> " + ex.getMessage());   
        }
    }
}