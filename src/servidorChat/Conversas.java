package servidorChat;

import cliente.ClienteStatic;


public class Conversas {

	public String nome;
	public ClienteStatic cliente;
	
	//construtor
	public Conversas(String nome, ClienteStatic cliente){
		this.nome = nome;
		this.cliente = cliente;
	}

	
	//getters
	public String getNome(){
		return nome;
	}
	public ClienteStatic getCliente(){
		return cliente;
	}
	
	
}
