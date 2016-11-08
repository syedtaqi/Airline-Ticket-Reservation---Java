
	import java.rmi.Remote;
	import java.rmi.RemoteException;

	public interface ReservationServerINTF extends Remote {
		/**
		 * Author: Syed Taqi Raza 
		 * NET 4005:	Airline ticket Reservation System
		 * 
		 */
	    public String getAvailableSeats() throws RemoteException;
	    public String reserveSeat(String name, String pClass, Integer seatNumber) throws RemoteException;
	    public String getReservedSeats() throws RemoteException;
	    
	}
