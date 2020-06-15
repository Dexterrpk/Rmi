package cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Cleiton Neri
 */
public interface ClienteStatic extends Remote{

	public void menssagemServidor(String message) throws RemoteException;

	public void updateListaUsuario(String[] usuariosAtuais) throws RemoteException;
        
	
}
/**
 * 
 * 
 * 
 *
 */
