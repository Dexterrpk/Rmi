package servidorChat;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import cliente.ClienteStatic;

/**
 *
 * @author Cleiton Neri
 */
public class ChatServidor extends UnicastRemoteObject implements ServerStatic {
	String linha = "---------------------------------------------\n";
	private Vector<Conversas> conversas;
	private static final long serialVersionUID = 1L;
	
	public ChatServidor() throws RemoteException {
		super();
		conversas = new Vector<Conversas>(10, 1);
	}
	
	public static void main(String[] args) {
		iniciarRegistroRMI();	
		String hostName = "localhost";
		String serviceName = "GroupChatService";
		
		if(args.length == 2){
			hostName = args[0];
			serviceName = args[1];
		}
		
		try{
			ServerStatic hello = new ChatServidor();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
			System.out.println("Grupo de Chat RMI Server rodando...");
		}
		catch(Exception e){
			System.out.println("Problemas ao iniciar servidor");
		}	
	}

	
	public static void iniciarRegistroRMI() {
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println(" Servidor RMI pronto");
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
	}
		
	public String saudacao(String ClientName) throws RemoteException {
		System.out.println(ClientName + " enviar mensagem");
		return "Olá " + ClientName + " do servidor de bate-papo em grupo";
	}
	
        
	public void updateChat(String name, String nextPost) throws RemoteException {
		String message =  name + " : " + nextPost + "\n";
		enviarTodos(message);
	}
	
	@Override
	public void entradaIdentificacao(RemoteRef ref) throws RemoteException {	
		try{
			System.out.println(linha + ref.toString());
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
		registrConversa(details);
	}

	private void registrConversa(String[] details){		
		try{
			ClienteStatic nextClient = ( ClienteStatic )Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			
			conversas.addElement(new Conversas(details[0], nextClient));
			
			nextClient.menssagemServidor("[Server] : Olá " + details[0] + " agora você já pode conversar.\n");
			
			enviarTodos("[Server] : " + details[0] + " se juntou ao grupo.\n");
			
			updateListaUsuario();		
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}
	
	private void updateListaUsuario() {
		String[] currentUsers = getListaUsuario();	
		for(Conversas c : conversas){
			try {
				c.getCliente().updateListaUsuario(currentUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private String[] getListaUsuario(){
		String[] allUsers = new String[conversas.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = conversas.elementAt(i).getNome();
		}
		return allUsers;
	}
	

	
	public void enviarTodos(String novaMensagem){	
		for(Conversas c : conversas){
			try {
				c.getCliente().menssagemServidor(novaMensagem);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

	
	@Override
	public void sairChat(String userName) throws RemoteException{
		
		for(Conversas c : conversas){
			if(c.getNome().equals(userName)){
				System.out.println(linha + userName + " saiu do chat");
				System.out.println(new Date(System.currentTimeMillis()));
				conversas.remove(c);
				break;
			}
		}		
		if(!conversas.isEmpty()){
			updateListaUsuario();
		}			
	}
	


	@Override
	public void enviarPV(int[] privado, String mensagemPrivada) throws RemoteException{
		Conversas pc;
		for(int i : privado){
			pc= conversas.elementAt(i);
			pc.getCliente().menssagemServidor(mensagemPrivada);
		}
	}
	
}



