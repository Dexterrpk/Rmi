package servidor;

import cliente.ChatClienteIF;


public class Conversas {

	public String name;
	public ChatClienteIF client;
	
	//construtor
	public Conversas(String nome, ChatClienteIF cliente){
		this.name = nome;
		this.client = cliente;
	}

	
	//getters
	public String getName(){
		return name;
	}
	public ChatClienteIF getClient(){
		return client;
	}
	
	
}
