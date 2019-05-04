package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import model.Jogador;
import model.Model;

/**
 * Um servidor para um jogo de batalha. este
 * classe se comunica com jogador atrav�s de
 * TCP_Server_Help.
 * 
 * @author   Giulia Falcao
 *
 */
public class TCP_Server {
	ServerSocket server;
	Model model;
	Jogador[] jogadores = new Jogador[2];
	TCP_Communicator[] comms = new TCP_Communicator[2];
	TCP_Server_Helper[] helpers = new TCP_Server_Helper[2];
	char[] barcosMarcadores = { 'A', 'B' };
	char[] hitMarcadores = { 'a', 'b' };
	int port = 4455;
	
	/**
	 * Construtor que inicializa o server e o model 
	 */
	public TCP_Server(){
		
		try{
			server = new ServerSocket( port );
		}
		catch ( SocketException e ) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		model = new Model();
	}
	
	/**
	 * Esse m�todo aguarda para receber uma conex�o de dois
	 * jogadores. Se n�o ouvir de um jogador dentro
	 * tr�s minutos fecha automaticamente o servidor.
	 * 
	 * @return boolean true se dois jogadores se conectarem,
	 * false se o tempo limite ocorreu
	 */
	public boolean getJogadores(){
		
		try {
			server.setSoTimeout( 180000 );

			int jogador = 0;

			while( jogador != 2 ) {

				Socket client = server.accept();
				ObjectInputStream ois = new ObjectInputStream( client.getInputStream() );
				ObjectOutputStream oos = new ObjectOutputStream( client.getOutputStream() );

				String jogadorNome = (String) ois.readObject();
				jogadores[jogador] = new Jogador( jogadorNome, barcosMarcadores[jogador], hitMarcadores[jogador] );

				comms[jogador] = new TCP_Communicator( jogadorNome );
				oos.writeObject( comms[jogador] );				
				helpers[jogador] = new TCP_Server_Helper( jogador, client, this, ois, oos );
				jogador++;
			}
			
			return true;
		
		} catch ( SocketTimeoutException e ) {
			System.out.println( "Sem jogadores Suficiente" );
			if( helpers[0] != null ) {
				
			}
			return false;
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * Este m�todo configura um jogo de BattleShip.
	 * Se o servidor n�o receber conex�es
	 * para dois jogadores, este m�todo retornar� falso.
	 * 
	 * @return boolean true se dois jogadores conectados,
	 * falso de outra forma
	 */
	public boolean controlSetUp(){
		if( getJogadores() ) {
			model.setUpGame( jogadores[0].getBarcoMarcador(), jogadores[1].getBarcoMarcador() );
			helpers[0].start();
			helpers[1].start();
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	* Este m�todo cria um navio
	* de muitos. � chamado por um
	* thread auxiliar para verificar se o
	* navio pode realmente ser constru�do no
	* ponto desejado e no desejado
	* dire��o.
	* 
	* @param jogador o id num�rico de
	* 		o jogador para que o navio est� sendo constru�do
	* 
	* @param input insere o local desejado e a dire��o
	* 
	* @return boolean true se puder ser constru�do, falso de outra forma
	*/

	public boolean barcoSetUp( int jogador, Object[] inputs ) {
		int linha = 0;
		int coluna = 0;
		String direction = "";
		linha = ( Integer ) inputs[0];
		coluna = ( Integer ) inputs[1];
		direction = ( String ) inputs[2];

		return model.setUpBarcos( jogador, linha, coluna, direction );
	}
	
	/**
	 * Checa se o jogo ainda esta ocorrendo
	 * 
	 * @return   boolean   true se sim, false se
	 *                     nao
	 */
	public boolean checkStatus() {
		return model.checkStatus();
	}
	
	/**
	* Obter e retornar uma representa��o de string
	* do tabuleiro pertencente ao jogador
	* argumento.
	* @param jogador o id num�rico do jogador
	* cujo tabuleiro queremos retornar
	* @param yesOrNo um valor booleano, indicando
	* se queremos os navios inclu�dos
	* 
	* @return String uma representa��o de string da
	* String do tabuleiro do jogador
	*/
	public String printTabuleiro( int jogador, boolean yesOrNo ) {
		return model.verTabuleiro(jogador, yesOrNo);
	}
	

	/**
	* Verifique o modelo para ver se as coordenadas dadas
	* acertar um navio ou n�o
	* 
	* @param jogadorAlvo o jogador que est� sendo atacado
	* @param alvo segmenta as coordenadas que est�o sendo segmentadas
	* @param attackingPlayer o jogador que est� atacando
	* 
	* @return boolean true se foi um hit, false de outra forma
	*/
	public boolean checkHit( int jogadorAlvo, int[] alvo, int jogadorAtacante ) {
		return model.resultadoJogada( jogadorAlvo, alvo[0], 
				alvo[1], jogadores[jogadorAtacante].getHitMarcador() );
	}
	
	/**
	 * Verifica quem foi o vencedor
	 * @return String nome do vencedor
	 */
	public String victory(){
		int winner = model.winner();
		
		return jogadores[winner].getNomeJogador() + " has won!";
	}
	
	/**
	* O m�todo principal. Inicia o servidor, escutando
	* em uma porta pr�-designada no host local.
	* 
	* @param args argumentos da linha de comando (n�o usados)
	*/
	public static void main( String [] args ) {
		TCP_Server server = new TCP_Server();
		server.controlSetUp();
	}
}

