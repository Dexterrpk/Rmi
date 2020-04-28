package servidorChat;

import cliente.ClienteStatic;


public class Conversas {

	public String nome;
	public ClienteStatic cliente;
	
	public Conversas(String nome, ClienteStatic cliente){
		this.nome = nome;
		this.cliente = cliente;
	}

	
	public String getNome(){
		return nome;
	}
	public ClienteStatic getCliente(){
		return cliente;
	}
	
	
}
