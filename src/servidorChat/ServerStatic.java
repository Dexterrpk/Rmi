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
	public void registerListener(String[] detalhes)throws RemoteException;
	public void updateChat(String userName, String chatMensagem)throws RemoteException;
	public void sairChat(String userName)throws RemoteException;
	public void enviarPV(int[] grupoPrivado, String mensagemPrivada)throws RemoteException;
        
}


