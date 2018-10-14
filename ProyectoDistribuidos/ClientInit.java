import java.io.*;
import java.net.*;

public class ClientInit {

    public static String firstTransportations(int number){
		if (number == 1) return "1,8191:carga0-Init0,8189:carga1-Init1,8192:carga2-Init2";
		else if (number == 2) return "2,8189:carga3-Init3,8190:carga4-Init4";
		else return "3,8192:carga5-Init5,8190:carga6-Init6,8189:carga7-Init7";

	}

    public static void main(String[] args) {
    	try {
	        //--------------------- CLIENTE-------------------------------------------------------
	        System.out.println("first Transportations");
	       
	        for (int counter = 1; counter <= 3; counter++) {
	        	Socket socket = new Socket("localhost", 8189);//abre socket para conectarse al servidor
				PrintStream output = new PrintStream(socket.getOutputStream()); //Send msg to next server
				BufferedReader input = new BufferedReader (new InputStreamReader(socket.getInputStream()));
				System.out.println(input.readLine());
				System.out.println("Go transport: " + counter);
				output.println(firstTransportations(counter));
				//TimeUnit.SECONDS.sleep(5);
				Thread.sleep(1000);
				socket.close();
			}
			//--------------------------------------------------------------------------------------
	    	
		}
		catch (Exception e) { System.out.println(e); }
    }
}