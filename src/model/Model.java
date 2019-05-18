package model;

/**
 * Esta classe contém o estado e as regras básicas do jogo
 * 
 * @author  Falcao
 */
public class Model {
	
	//Tabuleiro para cada 
	Tabuleiro[] tabuleiros = new Tabuleiro[2];
	
	// Frotas de cada jogador
	Frota[] frotas = new Frota[2];
	
	/**
	 * Configura o tabuleiro, frota objects e
	 * o hitMarcador para cada jogador
	 * @param   jogadorA  
	 * @param   jogadorB   
	 */
	public void setUpGame( char jogadorA, char jogadorB ){
		
		int minimoLinhas = 0;
		int minimoColunas = 0;

		int maxLinhas = 10;
		int maxColunas = 10; 
		
		tabuleiros[0] = new Tabuleiro(minimoLinhas, minimoColunas, maxLinhas, maxColunas);
		tabuleiros[1] = new Tabuleiro(minimoLinhas, minimoColunas, maxLinhas, maxColunas);
		

		frotas[0] = new Frota( tabuleiros[0], jogadorA );
		frotas[1] = new Frota( tabuleiros[1], jogadorB );
	}
	
	/**
	 * Construção do barco
	 * @param    player    O ID do jogador do qual percente o barc
	 *                     que esta sendo construido
	 * @param    linha      a linha do tabuleiro onde o primeiro ponto 
	 * 						deste navio será colocado
	 * @param    coluna      a coluna do tabuleiro onde o primeiro ponto 
	 * 						deste navio será colocado
	 * @param    direction   a direção que o barco sera construido
	 * @return   boolean     verdadeiro se foi construido, falso se nao conseguiu
	 */
	public boolean setUpBarcos( int player, int linha, int coluna, String direction ){
		return frotas[player].construcaoBarco( linha, coluna, direction );
	}
	
	/**
	 * Checa se a jogada teve algum acerto.
	 * @param   linha      
	 * @param   coluna   
	 * @return  boolean  
	 */
	public boolean resultadoJogada( int player, int linha, int coluna, char tiro ) {
		if( tabuleiros[player].checaTiroValido( linha, coluna) ){
			return frotas[player].checkHit( linha, coluna, tiro );
		}
		else{
			return false;
		}
	}
	
	/**
	 * Verificar se todos os barcos foram afundados ou nao.
	 * @return   boolean   
	 */
	public boolean checkStatus() {
		return frotas[0].checkShips() && frotas[1].checkShips();
	}
	
	/**
	 * Mostra o tabuleiro
	 * @param   jogador
	 * @param   yesOrNo   
	 */
	public String verTabuleiro(int jogador, boolean yesOrNo) {
		return tabuleiros[jogador].printTabuleiro(yesOrNo);
	}
	
	/**
	 * Verifica quem ganhou o jogo.
	 * @return   int   id de quem ganhou
	 */
	public int winner() {
		return frotas[0].checkShips() ? 0 : 1;
		
	}
}