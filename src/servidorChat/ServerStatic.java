package servidorChat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

/**
 * Server RMI interface
 *
 * @author Cleiton Neri
 */
public interface ServerStatic extends Remote {
		
	
	
	public void entradaIdentificacao(RemoteRef ref)throws RemoteException;
	public void registerListener(String[] details)throws RemoteException;
	public void updateChat(String userName, String chatMessage)throws RemoteException;
	public void sairChat(String userName)throws RemoteException;
	public void enviarPV(int[] privateGroup, String privateMessage)throws RemoteException;
        
}


