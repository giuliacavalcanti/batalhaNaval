package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import client.View;
import model.Tabuleiro;

/**
 * Essa classe lida com a comunica��o TCP
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
	 * Este m�todo adquire o socket, ObjectOutputStream,
	 * e ObjectInputStream necess�rios para se comunicar com o
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
	 * Este m�todo pede ao usu�rio para escolher pontos para os seus qtdBarcosCriados. 
	 * Em seguida, ele se comunica com o servidor para garantir que eles sejam 
	 * posi��es e dire��es v�lidas.
	 */
	public void frotaSetUp(){
		
		try{
			int qtdBarcosCriados = 0;
			while( qtdBarcosCriados < 4 ) {
				int tamanhoDoBarcoASerCriado = ( int ) ois.readObject();
				
				// Obtenha a escolha do usu�rio atrav�s do View e 
				// envie-o para o servidor para verifica��o
				oos.writeObject( view.getPositions( nomeJogador, tamanhoDoBarcoASerCriado ) );
				
				// Se o navio puder ser constru�do nessa posi��o e nessa dire��o, 
				// um array com os valores inseridos ser� retornado

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
	 * Este m�todo cont�m parte da l�gica necess�ria para jogar um jogo de batalha. 
	 * Ele mostra ao usu�rio seus palpites at� o momento, o estado de seu conselho, 
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
					
					// Bloqueia at� o servidor enviar um valor booleano. 
					// Uma vez que recebe um valor, este deve ser o turno deste jogador. 
					// Se o valor � falso, ent�o o jogo acabou.
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