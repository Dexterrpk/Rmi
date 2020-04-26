package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for client classes
 * A method to receive a string
 * A method to update changes to user list
 *
 * @author Cleiton Neri
 */
public interface ChatClienteIF extends Remote{

	public void menssageParaServidor(String message) throws RemoteException;

	public void updateListaUsuario(String[] currentUsers) throws RemoteException;
        
	
}
/**
 * 
 * 
 * 
 *
 */