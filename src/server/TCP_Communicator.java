package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import client.View;
import model.Tabuleiro;

/**
 * Essa classe lida com a comunicação TCP
 * entre servidor e jogador.
 * 
 * @author Falcao
 *
 */
@SuppressWarnings("serial")
public class TCP_Communicator implements Serializable{
	// Usado pra se comunicar com o jogador
	View view;	
	
	Tabuleiro tabuleiro;
	// Nome do jogador
	String nomeJogador;	
	// Socket que usamos para conectar o server
	Socket socket;	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	/**
	 * Construtor. Inicializa o nome do jogador.
	 * @param   nomeJogador   the name of this player
	 */
	public TCP_Communicator( String nomeJogador ){
		this.nomeJogador = nomeJogador;
	}
	
	/**
	 * Este método adquire o socket, ObjectOutputStream,
	 * e ObjectInputStream necessários para se comunicar com o
	 * servidor
	 * @param   socket   Socket que estamos conectados ao
	 * 					 servidor
	 * @param   oos      O ObjectOutputStream
	 * @param   ois      O ObjectInputStream
	 */
	public void setSocket( Socket socket, ObjectOutputStream oos, ObjectInputStream ois ) {
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		view = new View();
		tabuleiro = new Tabuleiro(0,0,10,10);
	}
	
	
	/**
	 * Este método pede ao usuário para escolher pontos para os seus qtdBarcosCriados. 
	 * Em seguida, ele se comunica com o servidor para garantir que eles sejam 
	 * posições e direções válidas.
	 */
	public void frotaSetUp(){
		
		try{
			int qtdBarcosCriados = 0;
			while( qtdBarcosCriados < 4 ) {
				int tamanhoDoBarcoASerCriado = ( int ) ois.readObject();
				
				// Obtenha a escolha do usuário através do View e 
				// envie-o para o servidor para verificação
				oos.writeObject( view.getPositions( nomeJogador, tamanhoDoBarcoASerCriado ) );
				
				// Se o navio puder ser construído nessa posição e nessa direção, 
				// um array com os valores inseridos será retornado

				if( (boolean) ois.readObject() ) {
					qtdBarcosCriados++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Este método contém parte da lógica necessária para jogar um jogo de batalha. 
	 * Ele mostra ao usuário seus palpites até o momento, o estado de seu conselho, 
	 * e pede-lhes para escolher um alvo.
	 */
	public void game(){
		
		try{
			
			// Se o servidor esta pronto, true
			// Se nao tem jogadores suficiente, falso
			if( (boolean) ois.readObject() ){
				frotaSetUp();
				boolean jogoOcorrendo = true;
				
				// Enquanto nao houver vencedores, continua
				while( jogoOcorrendo ) {
					
					// Bloqueia até o servidor enviar um valor booleano. 
					// Uma vez que recebe um valor, este deve ser o turno deste jogador. 
					// Se o valor é falso, então o jogo acabou.
					if( (boolean) ois.readObject() ){
						
						// Tabuleiros
						view.mensagem( (String) ois.readObject() );
						
						// Jogadores
						view.mensagem( (String) ois.readObject() );
						
						// Alvo desejado
						oos.writeObject( view.getInput(nomeJogador) );
						
						// Verificar se acertou o alvo ou errou
						view.mensagem( ( String ) ois.readObject() );
					}
					else{
						jogoOcorrendo = false;
					}
				}
				
				// Quem venceu
				view.mensagem( (String) ois.readObject()); 
			}
			else{
				view.mensagem( "Sem jogadores suficiente" );
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}