package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

/**
 * Server RMI interface
 *
 * @author Cleiton Neri
 */
public interface ChatServerIF extends Remote {
		
	public void updateChat(String userName, String chatMessage)throws RemoteException;
	
	public void passIDentity(RemoteRef ref)throws RemoteException;
	
	public void registerListener(String[] details)throws RemoteException;
	
	public void sairChat(String userName)throws RemoteException;
	
	public void enviarPV(int[] privateGroup, String privateMessage)throws RemoteException;
        
       
        
        
        
       
}


