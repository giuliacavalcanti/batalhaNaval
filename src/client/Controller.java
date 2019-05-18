package client;

import model.Jogador;
import model.Model;

/**
 * Logica MVC
 * @author   Falcao
 *
 */
public class Controller {
	View view;
	Model model;
	Jogador[] jogadores = new Jogador[2];
	int turno = 0;
	int sign = 1;

	/**
	 * Construtor.
	 */
	public Controller(){
		view = new View();
		model = new Model();
		
		// Jogador objects
		jogadores[0] = new Jogador ( "jogador a", 'A', 'a' );
		jogadores[1] = new Jogador ( "jogador b", 'B', 'b' );
	}
	
	/**
	 * Esse medotodo configura model e a frota
	 */
	public void controlSetUp(){
		model.setUpGame( jogadores[0].getBarcoMarcador(), jogadores[1].getBarcoMarcador());
		frotaSetUp(0);
		frotaSetUp(1);	
	}
	
	/** 
	 * Este método cria uma frota de barcos de 
	 * acordo com a entrada do usuário. 
	 * No entanto, os barcos não podem tocar ou se sobrepor.
	 */
	private void frotaSetUp( int jogador ) {
		int linha = 0;
		int coluna = 0;
		String direcao = "";
		
		// indice onde botaremos o novo naviol
		int index = 0;
		
		// continua até o array de barcos está completo
		while( index != 4 ) {
			Object[] inputs = view.getPositions( jogadores[jogador].getNomeJogador(), index );
			linha = (Integer) inputs[0];
			coluna = (Integer) inputs[1];
			direcao = (String) inputs[2];
			
			// Se o barco puder ser construido
			if( model.setUpBarcos( jogador, linha, coluna, direcao )) {
				index++;
			}
		}		
	}
	
	/**
	 * Esse metodo é onde o jogo ocorre.
	 */
	public void battleship(  ) {
		Jogador jogadorAtual = jogadores[0];
		Jogador jogadorEsperando = jogadores[1];
		Jogador mudancasJogadorAtual;
		
		// Verifica se cada jogador tem ao menos um barco
		while( model.checkStatus() ) {
			
			// Mostra o tabuleiro do jogador atual
			view.mensagem( "Seu tabuleiro: " );
			view.mensagem( model.verTabuleiro( turno, true ) );
			
			// Mostra o tabuleiro do oponente sem os barcos
			// barcos. O jogador atual pode ver os acertos e erros
			view.mensagem( "Tabuleiro do oponente: " );
			view.mensagem( model.verTabuleiro( turno + sign, false ) );
			
			// Pergunta ao jogador atual onde ele quer atacar
			int[] alvo = view.getInput( jogadores[turno].getNomeJogador());
			
			// Passe o alvo para o método resultadoJogada() do jogador em espera.
			// Se ele retorna verdadeiro, então é atingido, caso contrário é uma falta.
			if( model.resultadoJogada( turno + sign, alvo[0], alvo[1], jogadores[turno].getHitMarcador()) ) {
				view.mensagem( "Acertou!" );
			}
			else {
				view.mensagem( "Errou!" );
			}
			
			// Muda os jogadores
			mudancasJogadorAtual = jogadorAtual;
			jogadorAtual = jogadorEsperando;
			jogadorEsperando = mudancasJogadorAtual;
			
			// Separar os turnos dos jogadores 
		    view.mensagem( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
			
		    // Muda o turno
			turno += sign;
			sign *= -1;
		}
		
		// Diz quem ganhou
		int winner = model.winner();
		view.mensagem( jogadores[winner].getNomeJogador() + "ganhou" );
	
	}
	
	/**
	 * O principal método cria o Controller , configura um jogo de batalha e inicia o jogo.
	 * @param   args (nao usado)
	 * 	 */
	public static void main( String [] args ) {
		Controller controller = new Controller();
		controller.controlSetUp();
		controller.battleship();
	}
}