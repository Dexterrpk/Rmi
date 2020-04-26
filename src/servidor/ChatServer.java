package servidor;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import cliente.ChatClienteIF;

/**
 *
 * @author Cleiton Neri
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerIF {
	String line = "---------------------------------------------\n";
	private Vector<Conversas> conversas;
	private static final long serialVersionUID = 1L;
	
	//Constructor
	public ChatServer() throws RemoteException {
		super();
		conversas = new Vector<Conversas>(10, 1);
	}
	
	//-----------------------------------------------------------
	/**
	 * METODOS LOCAL 
	 */	
	public static void main(String[] args) {
		iniciarRegistroRMI();	
		String hostName = "localhost";
		String serviceName = "GroupChatService";
		
		if(args.length == 2){
			hostName = args[0];
			serviceName = args[1];
		}
		
		try{
			ChatServerIF hello = new ChatServer();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
			System.out.println("Grupo de Chat RMI Server rodando...");
		}
		catch(Exception e){
			System.out.println("Problemas ao iniciar servidor");
		}	
	}

	
	/**
	 * Iniciaregistro no RMI 
         */
	public static void iniciarRegistroRMI() {
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println(" Servidor RMI pronto");
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
	}
		
	
	//-----------------------------------------------------------
	/*
	 *  METODOS REMOTE 
	 */
	
	/**
	 * Retorna a mensagem do cliente
	 */
	public String ola(String ClientName) throws RemoteException {
		System.out.println(ClientName + " enviar mensagem");
		return "Olá " + ClientName + " do servidor de bate-papo em grupo";
	}
	
        
	/**
	 * Send a string ( the latest post, mostly ) 
	 * to all connected clients
	 */
	public void updateChat(String name, String nextPost) throws RemoteException {
		String message =  name + " : " + nextPost + "\n";
		enviarTodos(message);
	}
	
	/**
	 * Receive a new client remote reference
	 */
	@Override
	public void passIDentity(RemoteRef ref) throws RemoteException {	
		try{
			System.out.println(line + ref.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

        
	
	
	@Override
	public void registerListener(String[] details) throws RemoteException {	
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println(details[0] + " entrou na sala de bate-papo");
		System.out.println(details[0] + "'s nome : " + details[1]);
		System.out.println(details[0] + "'sRMI serviço : " + details[2]);
		registerChatter(details);
	}

	
	/**
	 * register the clients interface and store it in a reference for 
	 * future messages to be sent to, ie other members messages of the chat session.
	 * send a test message for confirmation / test connection
	 * @param details
	 */
	private void registerChatter(String[] details){		
		try{
			ChatClienteIF nextClient = ( ChatClienteIF )Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			
			conversas.addElement(new Conversas(details[0], nextClient));
			
			nextClient.menssageParaServidor("[Server] : Olá " + details[0] + " agora você já pode conversar.\n");
			
			enviarTodos("[Server] : " + details[0] + " se juntou ao grupo.\n");
			
			updateListaUser();		
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Update all clients by remotely invoking their
 updateListaUsuario RMI method
	 */
	private void updateListaUser() {
		String[] currentUsers = getUserList();	
		for(Conversas c : conversas){
			try {
				c.getClient().updateListaUsuario(currentUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
	

	/**
	 * generate a String array of current users
	 * @return
	 */
	private String[] getUserList(){
		// generate an array of current users
		String[] allUsers = new String[conversas.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = conversas.elementAt(i).getName();
		}
		return allUsers;
	}
	

	
	public void enviarTodos(String novaMensagem){	
		for(Conversas c : conversas){
			try {
				c.getClient().menssageParaServidor(novaMensagem);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

	
	@Override
	public void sairChat(String userName) throws RemoteException{
		
		for(Conversas c : conversas){
			if(c.getName().equals(userName)){
				System.out.println(line + userName + " saiu do chat");
				System.out.println(new Date(System.currentTimeMillis()));
				conversas.remove(c);
				break;
			}
		}		
		if(!conversas.isEmpty()){
			updateListaUser();
		}			
	}
	


	@Override
	public void enviarPV(int[] privado, String mensagemPrivada) throws RemoteException{
		Conversas pc;
		for(int i : privado){
			pc= conversas.elementAt(i);
			pc.getClient().menssageParaServidor(mensagemPrivada);
		}
	}
	
}



