package model;
import java.util.Arrays;

/**
 * Essa classe cria os barcos do jogo.
 * 
 * @author Falcao
 */

public class Barcos {
	
	// Tiros tomados pelo barco
	int tirosTomados = 0;
	
	// Se o barco esta afundado ou nao
	boolean statusBarco = true;
	
	// Posição que o barco ocupa no tabuleiro
	int[][] positions;
	
	// Tamanho do barco
	int tamanhoBarco;

	Tabuleiro tabuleiro;
	
	
	/**
	 * Construtor
	 * @param positions
	 * @param tamanhoBarco
	 * @param pedacoBarco
	 * @param tabuleiro
	 */
	public Barcos ( int[][] positions, int tamanhoBarco, char pedacoBarco, Tabuleiro tabuleiro ) {
		this.positions = positions;
		this.tamanhoBarco = tamanhoBarco;
		this.tabuleiro = tabuleiro;
		
		//Registra o marcador que representa o navio no tabuleiro de jogo
		tabuleiro.parteBarco(pedacoBarco);
		
		// Bota o 
		for( int i = 0; i < tamanhoBarco; i++ ) {
				tabuleiro.acertoErro( positions[i][0],
						positions[i][1], pedacoBarco);
		}
	}
	
	/**
	 * O objetivo deste método é ver se as coordenadas 
	 * do ataque recebido irão atingir ou não o navio.
	 * @param linha
	 * @param coluna
	 * @param jogada
	 * @return
	 */
	public boolean resultadoJogada( int linha, int coluna, char jogada ) {
		
		//Bota as coordenasdas em um array pra comparação
		int[] alvo = { linha, coluna };
		
		// Verdadeiro se acertou o alvo
		boolean hit = false;
		
		// Realizar um loop nas diferentes coordenadas
		for( int i = 0; i < tamanhoBarco; i++ ) {
			
			// Checa se acertou 
			if( statusBarco && Arrays.equals( alvo, positions[i] ) ) {
				tabuleiro.acertoErro( linha, coluna, jogada );
				tirosTomados++;
				hit = true;
			}
		}
		
		if( !hit ) {
			
			// Bota um marca de errou
			tabuleiro.acertoErro( linha, coluna, '*' );
			return false;
		}
		else {
			// Checa o status do barco
			if ( tirosTomados == tamanhoBarco ) {
				statusBarco = false;
				System.out.println( "Você acertou meu barco " + name(tamanhoBarco) + "!" );
			}
			
			return true;
		}
	}
	
	/**
	 * Checa se o barco está destruido ou inteiro
	 * @return   boolean   
	 */
	public boolean checkBarco() {
		return statusBarco;
	}
	
	/**
	 * Este método retorna a classe de ship que corresponde ao tamanho do dadoBarco.
	 * @param   tamanhoBarco  
	 * @return  String   a classe do barco
	 */
	private String name( int tamanhoBarco ) {
		
		switch( tamanhoBarco ) {
			
			case 2: return "Destroyer";
			
			case 3: return "Cruiser";
			
			case 4: return "Battleship";
			
			case 5: return "Carrier";
			
			default:
				return "Nemo";
		}
	}
}