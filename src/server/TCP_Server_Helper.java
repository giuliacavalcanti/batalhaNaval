package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Esta classe lida com a conex�o a um
 * jogador para o servidor. Tamb�m cont�m parte
 * da l�gica necess�ria para jogar o jogo.	
 * 
 * @author   Giulia Falcao
 *
 */
public class TCP_Server_Helper extends Thread {
	ObjectInputStream ois;
	ObjectOutputStream oos;
	TCP_Server server;
	Socket client;
	int player;
	static int turnoAtual = 0;
	static int jogadorDaVez = 1;
	static int setUp = 0;
	boolean comecarJogo = true;
	
	/**
	* O construtor. Ele inicializa esse segmento auxiliar
	* com todas as informa��es necess�rias para lidar com o
	* conex�o a um jogador
	* 
	* @param player o n�mero de identifica��o do player 
	* ao qual esse thread auxiliar est� conectado
	* @param player o n�mero de identifica��o do player 
	* ao qual esse thread auxiliar est� conectado
	* @param client o soquete atrav�s do qual este thread 
	* auxiliar est� conectado ao player
	* @param server uma refer�ncia ao servidor principal
 	* @param ois usado para ler objetos de um fluxo de bytes
 	* @param oos usado para escrever objetos em um fluxo de bytes
 	*/
	public TCP_Server_Helper( int player, Socket client, TCP_Server server, 
			ObjectInputStream ois, ObjectOutputStream oos ){
		this.player = player;
		this.client = client;
		this.server = server;
		this.ois = ois;
		this.oos = oos;
	}
	
	/** 
	
	 * Este m�todo cria uma frota
	 * de barcos para um jogo de
	 * navio de batalha de acordo com o usu�rio
	 * entrada. No entanto, os barcos n�o podem
	 * se tocar ou se sobrepuser.
	 */
	private void frotaSetUp( int player ) {
		
		try{
			
			// �ndice no array de barcos onde colocaremos o novo navio
			int index = 0;
			
			// Continue at� que o usu�rio tenha preenchido o array de barcos
			while( index != 4 ) {
				
				// Avise ao jogador qual barco esta sendo
				// construido 
				oos.writeObject( new Integer(index) );
				
				// Obtenha o ponto de partida de constru��o do barco e dire��o desejados
				Object[] inputs = ( Object[] ) ois.readObject();
				
				
				// // Se o barco pode ser construido, incremente o index
				if( server.barcoSetUp( player, inputs ) ) {
					
					// // Se o barco foi construido, retorne true
					oos.writeObject( new Boolean(true) );
					index++;
				}
				else{
					oos.writeObject( new Boolean(false) );
				}
			}
		} catch ( IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  O m�todo de execu��o. Ele lida com a configura��o do player
	 * frota e cont�m a l�gica do lado do servidor para
	 * jogando o jogo.
	 */
	public void run() {
				
		try{
			
			// Falar para o jogador que tudo pronto para come�ar
			oos.writeObject( new Boolean( comecarJogo ) );
			
			// Se estiver pronto, come�ar o joogo
			if( comecarJogo ) {
				
				// SetUp as frotas
				frotaSetUp(player);
				
				// Assim que a frota estiver pronta 
				// incremente setUp
				setUp++;
				
				// Esperando jogadores configurarem
				// as frotas
				while( setUp != 2 ){}
				
				//  Enquanto um dos jogadores tiver pelo menos um navio ainda
				// o jogo continua
				while( server.checkStatus() ) {
					
					// Turno Atual
					if( turnoAtual == player ) {
						
						// Avise ao jogador que o jogo ainda esta acontecendo
						oos.writeObject(new Boolean(true));
						
						// Envie o tabuleiro do jogador atual com seus barcos
						oos.writeObject( server.printTabuleiro( turnoAtual, true ) );
						
						// Envia o tabuleiro do jogador em espera sem seus barcos. 
						// Isto � para que o jogador adivinhador possa ver seus acertos e erros.
						oos.writeObject( server.printTabuleiro( turnoAtual + jogadorDaVez, false ) );
						
						// Pega o alvo
						int[] target = ( int[] ) ois.readObject();
												
						// Passa o alvo para o model. Se retornar true, � um 
						// ele acertou o barco, se falso errou
						if( server.checkHit(turnoAtual+jogadorDaVez, target, turnoAtual)){
							oos.writeObject( new String( "Acertou!" ) );
						}
						else {
							oos.writeObject( new String( "Errou!" ) );
						}
						
						// Troca o turno
						turnoAtual += jogadorDaVez;
						jogadorDaVez *= -1;
					}
				}
				
				// Indica que o jogo acabou
				oos.writeObject( new Boolean( false ) );
				
				// Indica o vencedor
				oos.writeObject( server.victory());
			}
		} catch ( IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}