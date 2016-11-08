import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject; 
public class ReservationServer extends UnicastRemoteObject implements ReservationServerINTF {
    /**
	 * Author: Syed Taqi Raza 
	 * NET 4005:	Airline ticket Reservation System
	 * ReservationServer.java The reservation server 
	 * maintains the seat availability information and responds 
	 * to RMIclient requests for reservation, availabiliy or final passengerlist output
	 */
	private static final long serialVersionUID = 1L;
	
	//Initializing counters for economy and bussiness class
	private int reservedEconomySeatsCounter = 0;
	private int reservedBusinessSeatsCounter = 0;
	
	//Initializing Array for reservedSeats
	private String[] reservedSeats = new String[30];
    
	
    public ReservationServer() throws RemoteException {
        super(0);    // required to avoid the 'rmic' step, see below
    }

    public static void main(String args[]) throws Exception {
        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099); 
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }
           
        //Instantiate RmiServer

        ReservationServer reservationServer = new ReservationServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/ReservationServer", reservationServer);
        System.out.println("Peer Server bound in registry");
    }

	@Override
	public String getAvailableSeats() throws RemoteException {
		String result = "";
		
		//Seatnumbers from 1-5 are in business
		if(reservedBusinessSeatsCounter <= 5) {
			result += "business class:\n";
			if(reservedBusinessSeatsCounter < 3) {
				result += (3 - reservedBusinessSeatsCounter) + " seats at $500 each\n";
				result += "2 seats at $800 each\n";
			}
			else {
				result += (5 - reservedBusinessSeatsCounter) + " seats at $800 each\n";
				}
			
			//Available seat numbers in Business
			result += "Seats numbers: " + getAvailableSeatNumbers("business") + "\n";
		}
		
		//Seat numbers 6-30 in economy
		if(reservedEconomySeatsCounter <= 30) {
			result += "economy class:\n";
			if(reservedEconomySeatsCounter < 10) {
				result += "First " + (10 - reservedEconomySeatsCounter) + " seats at $200 each\n";
				result += "Second 10 seats at $300 each\n";
				result += "5 seats at $450 each\n";
			}
			else if(reservedEconomySeatsCounter < 20) {
				result += (20 - reservedEconomySeatsCounter) + " seats at $300 each\n";
				result += "5 seats at $450 each\n";
			}
			else {
				result += (30 - reservedBusinessSeatsCounter) + " seats at $450 each\n";
			}
			//Available seat numbers in economy class
			result += "Seats numbers: " + getAvailableSeatNumbers("economy") + "\n";
		}
		
		return result;
	}

	@Override
	public String reserveSeat(String name, String pClass, Integer seatNumber) throws RemoteException {
		
		//Checkpointa: 
		// Checking if the seat number entered is valid within 1 to 30 seats if not it fails to reserve
		if(seatNumber < 1 || seatNumber > 30) {
			return "Failed to reserve: invalid seat number";
		}
		
		//Checkpointb:
		// Checking if business class correspond to e.g seat 25 in economy if then return error "Invalid seat for business
		if (seatNumber > 5 && pClass.equalsIgnoreCase("business")) {
			return "Failed to reserve: invalid seat for business class";
		}
		
		//Checkpointc:
		// Checking if economy class correspond to e.g seat 3 in business if not then return error "Invalid seat for economy

		if (seatNumber <= 5 && pClass.equalsIgnoreCase("economy")){
			return "Failed to reserve: invalid seat for economy class";
		}
		
		//Checkpointd:
		//Checking if the seat is not available then, return error "fail to reserve: seat not available"
		if(reservedSeats[seatNumber-1] != null) {
			return "Failed to reserve: seat not available";
		}
		
		
		reservedSeats[seatNumber-1] = name;	//Defining name variable for passenger name
		
		if(seatNumber < 5) {
			
			reservedBusinessSeatsCounter++;
		}
		else {
			reservedEconomySeatsCounter++;
		}
		
		//Successfully reserve seat for passenger
		return "Successfully reserved seat number " + seatNumber + " for passenger " + name;
	}

	
	
	//Implementation for passengerlist
	public String getReservedSeats() throws RemoteException {
		
		String result = "";
		
		//Using for loop through array of reservedSeats, and checking if reservedSeats[i] is not null
		//then print the name of user, the class and the seat number reserved
		for(int i = 0; i < reservedSeats.length; i++) {
			
			String name = reservedSeats[i]; 			//Initializing variable name for reservedSeats[i]
			
			//Checking if name is not null
			//Since array starts from 0 to 29 --->>>>  i+1 is used so that seatNumbers point betweeen 1 to 30 
			if(name != null) {
				int seatNumber = i + 1;	
				
				//If seatnumbers less then 5 then business class, else economy class
				if(seatNumber <= 5) {
					result += name + " business " + seatNumber + "\n"; 
				}
				else {
					result += name + " economy " + seatNumber + "\n"; 
				}
			}
		}
		
		return result;
	}
	
	private String getAvailableSeatNumbers(String pClass) {
		String result = null;
		
		// defaults to business seats
		int start = 0; 
		int limit = 5;
		
		if(pClass.equals("economy")) {
			start = 5;
			limit = reservedSeats.length;
		}
		
		//Using for loop through array of reservedSeats, and checking if name = reservedSeats[i] is null
		//if null, than print out the available seatNumbers
		for(int i = start; i < limit; i++) {
			String name = reservedSeats[i];
			if(name == null) {
				int seatNumber = i + 1;
				if(result == null) {
					result = Integer.toString(seatNumber);	
				}
				else {
					result += "," + seatNumber; 
				}
			}
		}
		
		return result;
	}

}
