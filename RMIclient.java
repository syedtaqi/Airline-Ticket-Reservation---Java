
import java.rmi.Naming;
import java.util.Scanner;

public class RMIclient { 
	/**
	 * Author: Syed Taqi Raza 
	 * NET 4005:	Airline ticket Reservation System
	 * RMIclient.java reservation client interacts with user 
	 * and sends requests to the Reservation server based on user input. User input is 
	 * taken as command line parameters, output is printed on 
	 * the screen as text 
	 */
    public static void main(String args[]) throws Exception {

    	System.out.println("Welcome to Airline Reservation Client!");
    	System.out.println("Source code By: Syed Taqi Raza");
        System.out.println("Use the following commands to interact: ");
        System.out.println("rsvclient list <server_name>");
        System.out.println("rsvclient reserve <server_name> <class> <passenger_name> <seat_number>");
        System.out.println("rsvclient passengerlist <server_name>");
        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        while(true) {
        
        	System.out.println("Enter command:");
        	String input = reader.nextLine();
        	
            // client is exiting
            if (input.equals("exit")) {
            	reader.close();
            	System.out.println("Good Bye!");
                break;
            }
            
            ReservationServerINTF reservationServer = null;
            String serverName = null;
            if(serverName == null) {
            	serverName = "localhost";			//Defining the server name, 
            										//It can be changed based on the needs
               
            }
            
            reservationServer = (ReservationServerINTF) Naming.lookup("//" + serverName + "/ReservationServer");
            String[] inputs = input.split(" ");
            
            String result = null;
            if(inputs.length < 2) {
            	System.err.println("Input: " + input + " is not supported");
            }
            //rsvclient list <localhost>
            else if(inputs[1].equals("list")) {
            	result = reservationServer.getAvailableSeats();
            }
            //rsvclient reserve <localhost> <class> <passenger-name> <seatNumber>
            else if(inputs[1].equals("reserve")) {
               	String pClass = inputs[3];		//Takes input (3) from user which is <class>
            	String name = inputs[4];		//Takes input (4) from user which is <passenger-name>
            	Integer seatNumber = Integer.parseInt(inputs[5]);	//Takes input (5) from user which is <seatNumber>
            	result = reservationServer.reserveSeat(name, pClass, seatNumber);
            }
            //rsvclient passengerlist <localhost>
            else if(inputs[1].equals("passengerlist")) {            	
            	result = reservationServer.getReservedSeats();
            }       
            else {
            	System.err.println("Input: " + input + " is not supported");
            }
            
            if(result != null) {
            	System.out.println(result);
            }
        }
    }
}