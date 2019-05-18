package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/** 
 *Esta classe é uma classe de cliente simples que se conecta via TCP a 
 *um servidor de jogo para jogar algum jogo.
 *
 * @author Falcao
 *
 */
public class TCP_PlayerClient {
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	/**
	 * Este método configura a conexão TCP e aguarda o servidor enviar um objeto de jogo. 
	 * Se nada chegar dentro de um certo período de tempo, a conexão é fechada.
	 * 
	 * @param    jogadorNome  Nome do jogador
	 * @param    server       Endereco IP do servidor
	 * @param    port         Porta do servidor
	 *                        
	 */
	public void connect( String jogadorNome, InetAddress server, int port ) {
		
		try {
		
			System.out.println("Conectando...");

			// Configuação do cliente
			socket = new Socket( server, port );
			oos = new ObjectOutputStream( socket.getOutputStream() );
			ois = new ObjectInputStream( socket.getInputStream() );
			
			//Tempo de espera para conexão com o servidor
			socket.setSoTimeout( 10000 );
			
			// Enviando nome do jogador para o servidor
			oos.writeObject(jogadorNome);
			
			// Obtendo um TCP Communicator do servidor
			TCP_Communicator communicate = ( TCP_Communicator ) ois.readObject();
			socket.setSoTimeout( 0 );
			
			// Enviando o socket, Outputstream, and Inputstream
			// para p  TCP_Communicator.
			communicate.setSocket( socket, oos, ois );
			
			// Start o jogo
			communicate.game();
			
		} catch ( SocketException e) {
			e.printStackTrace();
		} catch ( SocketTimeoutException e ) {
			System.out.println( "No response." );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				
				//jogo acabou
				socket.close();
				oos.close();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * O main lê nome do jogador, o nome do servidor e o número da porta do servidor 
	 * a partir dos argumentos da linha de comando. 
	 * Em seguida, chama o método connect () para configurar a conexão TCP e iniciar o jogo.
	 * @param   args  
	 */
	public static void main( String [] args ) {
		try {
			
			String jogadorNome = args[0];
			
			// Nome do servidor
			String servidorNome = args[1];
			
			// O objeto do tipo InetAddress é necesario 
			// para enviar os DatagramPackets
			InetAddress server = InetAddress.getByName( servidorNome );
			
			// Porta do Servidor
			int port = Integer.parseInt( args[2] ); 
			
			// Configura a conexão TCP
			new TCP_PlayerClient().connect( jogadorNome, server, port );	
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}