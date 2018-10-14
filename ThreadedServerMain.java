import java.net.*;
import java.io.*;

class ThreadedServerE extends Thread{

	private final static String DIR = "/home/derot/Desktop/ProyectoDistribuidos/data/";

	public static void printAmountPackages(String nameArchive) {
        File archivo = new File (DIR + nameArchive);
        FileReader fr = new FileReader (archivo);
        BufferedReader br = new BufferedReader(fr);
        
        String line = "";
        while((line = br.readLine())!=null){
           System.out.println("My name is: " + line);
        }
        fr.close();
    }

	public synchronized void run() {
	  	try {
	   		for(;;){ 
                System.out.println("Menu");
                System.out.println("1- Cantidad de paquetes que no suben en su primer intento");
                try {
                    inputUser = new BufferedReader(new InputStreamReader(System.in));
                    read = inputUser.readLine();
                    option = Integer.parseInt(read);
                }
                catch (Exception e) {
                System.out.println("Error: "+e);
                }

                switch (option) {
                    case 1:
                        printAmountPackages("amountPackages.txt");
                        break;  
                }
            }
	  	}  
	  	catch (Exception e) { System.out.println(e); }
	}
}