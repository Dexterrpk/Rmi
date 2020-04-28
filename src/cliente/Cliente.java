package cliente;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import servidorChat.ServerStatic;

/**
 *
 * @author Cleiton Neri
 */
public class Cliente  extends UnicastRemoteObject implements ClienteStatic {

    private static final long serialVersionUID = 7468891722773409712L;
	ClienteGUI chatGUI;
	private String hostName = "192.168.0.155";
	private String servico = "GroupChatService";
	private String servicoCliente;
	private String nome;
	protected ServerStatic serverStatic;
	protected boolean problemaConexao = false;

	
	public Cliente(ClienteGUI chatGUI, String nomeUsuario) throws RemoteException {
		super();
		this.chatGUI = chatGUI;
		this.nome = nomeUsuario;
		this.servicoCliente = "ClientListenService_" + nomeUsuario;
	}

	
	public void IniciarCliente() throws RemoteException {		
		String[] details = {nome, hostName, servicoCliente};	

		try {
			Naming.rebind("rmi://" + hostName + "/" + servicoCliente, this);
			serverStatic = ( ServerStatic )Naming.lookup("rmi://" + hostName + "/" + servico);	
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
		System.out.println("Servidor RMI rodando...\n");
	}


	public void registrarComServer(String[] details) {		
		try{
			serverStatic.entradaIdentificacao(this.ref);//now redundant ??
			serverStatic.registerListener(details);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	@Override
	public void menssagemServidor(String menssagem) throws RemoteException {
		System.out.println(menssagem );
		chatGUI.campoTexto.append(menssagem );
		chatGUI.campoTexto.setCaretPosition(chatGUI.campoTexto.getDocument().getLength());
	}

        
	@Override
	public void updateListaUsuario(String[] usuariosAtuais) throws RemoteException {

		if(usuariosAtuais.length < 2){
			chatGUI.botaoMensagemPV.setEnabled(false);
		}
		chatGUI.painelUsuario.remove(chatGUI.painelCliente);
		chatGUI.setPainelCliente(usuariosAtuais);
		chatGUI.painelCliente.repaint();
		chatGUI.painelCliente.revalidate();
	}

}













