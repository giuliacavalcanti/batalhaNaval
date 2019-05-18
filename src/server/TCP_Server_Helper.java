package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Esta classe lida com a conexão a um
 * jogador para o servidor. Também contém parte
 * da lógica necessária para jogar o jogo.	
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
	* com todas as informações necessárias para lidar com o
	* conexão a um jogador
	* 
	* @param player o número de identificação do player 
	* ao qual esse thread auxiliar está conectado
	* @param player o número de identificação do player 
	* ao qual esse thread auxiliar está conectado
	* @param client o soquete através do qual este thread 
	* auxiliar está conectado ao player
	* @param server uma referência ao servidor principal
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
	
	 * Este método cria uma frota
	 * de barcos para um jogo de
	 * navio de batalha de acordo com o usuário
	 * entrada. No entanto, os barcos não podem
	 * se tocar ou se sobrepuser.
	 */
	private void frotaSetUp( int player ) {
		
		try{
			
			// Índice no array de barcos onde colocaremos o novo navio
			int index = 0;
			
			// Continue até que o usuário tenha preenchido o array de barcos
			while( index != 4 ) {
				
				// Avise ao jogador qual barco esta sendo
				// construido 
				oos.writeObject( new Integer(index) );
				
				// Obtenha o ponto de partida de construção do barco e direção desejados
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
	 *  O método de execução. Ele lida com a configuração do player
	 * frota e contém a lógica do lado do servidor para
	 * jogando o jogo.
	 */
	public void run() {
				
		try{
			
			// Falar para o jogador que tudo pronto para começar
			oos.writeObject( new Boolean( comecarJogo ) );
			
			// Se estiver pronto, começar o joogo
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
						// Isto é para que o jogador adivinhador possa ver seus acertos e erros.
						oos.writeObject( server.printTabuleiro( turnoAtual + jogadorDaVez, false ) );
						
						// Pega o alvo
						int[] target = ( int[] ) ois.readObject();
												
						// Passa o alvo para o model. Se retornar true, é um 
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