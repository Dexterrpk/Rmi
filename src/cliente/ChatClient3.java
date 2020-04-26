package cliente;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import servidor.ChatServerIF;

/**
 *
 * @author Cleiton Neri
 */
public class ChatClient3  extends UnicastRemoteObject implements ChatClienteIF {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7468891722773409712L;
	ClientRMIGUI chatGUI;
	private String hostName = "localhost";
	private String serviceName = "GroupChatService";
	private String clientServiceName;
	private String name;
	protected ChatServerIF serverIF;
	protected boolean problemaConexao = false;

	
	/**
	 * class constructor,
	 * note may also use an overloaded constructor with 
	 * a port no passed in argument to super
	 * @throws RemoteException
	 */
	public ChatClient3(ClientRMIGUI aChatGUI, String nomeUsuario) throws RemoteException {
		super();
		this.chatGUI = aChatGUI;
		this.name = nomeUsuario;
		this.clientServiceName = "ClientListenService_" + nomeUsuario;
	}

	
	/**
	 * Register our own listening service/interface
	 * lookup the server RMI interface, then send our details
	 * @throws RemoteException
	 */
	public void IniciarCliente() throws RemoteException {		
		String[] details = {name, hostName, clientServiceName};	

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			serverIF = ( ChatServerIF )Naming.lookup("rmi://" + hostName + "/" + serviceName);	
		} 
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					chatGUI.frame, "O servidor parece estar indisponível\nPor favor tente mais tarde",
					"Problema de conexão", JOptionPane.ERROR_MESSAGE);
			problemaConexao = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			problemaConexao = true;
			me.printStackTrace();
		}
		if(!problemaConexao){
			registrarComServer(details);
		}	
		System.out.println("Servidor RMI em execução...\n");
	}


	/**
	 * pass our username, hostname and RMI service name to
	 * the server to register out interest in joining the chat
	 * @param details
	 */
	public void registrarComServer(String[] details) {		
		try{
			serverIF.passIDentity(this.ref);//now redundant ??
			serverIF.registerListener(details);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//=====================================================================
	/**
	 * Receive a string from the chat server
	 * this is the clients RMI method, which will be used by the server 
	 * to send messages to us
	 */
	@Override
	public void menssageParaServidor(String menssagem) throws RemoteException {
		System.out.println(menssagem );
		chatGUI.campoTexto.append(menssagem );
		//make the gui display the last appended text, ie scroll to bottom
		chatGUI.campoTexto.setCaretPosition(chatGUI.campoTexto.getDocument().getLength());
	}

	/**
	 * A method to update the display of users 
	 * currently connected to the server
	 */
	@Override
	public void updateListaUsuario(String[] currentUsers) throws RemoteException {

		if(currentUsers.length < 2){
			chatGUI.botaoMensagemPV.setEnabled(false);
		}
		chatGUI.painelUsuario.remove(chatGUI.painelCliente);
		chatGUI.setPainelCliente(currentUsers);
		chatGUI.painelCliente.repaint();
		chatGUI.painelCliente.revalidate();
	}

}//end class













