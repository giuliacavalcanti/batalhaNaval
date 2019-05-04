package model;
/**
 * Classe que representa o tabuleiro
 * e suas regras de funcionamento
 * 
 * @author Falcao
 *
 */
public class Tabuleiro {
	char [][] tabuleiro;
	
	// Tamanho do tabuleiro
	int tamanhoLinhas;
	int tamanhoColunas;
	int minimoLinhas;
	int minimoColunas;
	int maximoLinhas;
	int maximoColunas;
	
	// Parte do barco
	int barco = 0;
	
	/**
	 * Construtor
	 * 
	 * @param   minLinhas  minimio de linhas do tabuleiro
	 * 
	 * @param   minColunas minimo de colunas do tabuleiro
	 * 
	 * @param   maximoLinhas maximo de linhas do tabuleiro
	 * 
	 * @param   maximoColunas  maximo de colunas do tabuleiro
	 */
	public Tabuleiro( int minLinhas, int minColunas ,
			int maximoLinhas, int maximoColunas ) {
		
		this.minimoLinhas = minLinhas;
		this.minimoColunas = minColunas;
		this.maximoLinhas = maximoLinhas;
		this.maximoColunas = maximoColunas;
		
		// Determina tamanho de linhas
		tamanhoLinhas = maximoLinhas - minLinhas;
		
		// Determina tamanho da coluna
		tamanhoColunas = maximoColunas - minColunas;
		
		// Inicializa o tamanho do tabuleiro
		tabuleiro = new char [tamanhoLinhas] [tamanhoColunas];
		
		// Inicializa o tabuleiro com  ' '
		for( int linhas = 0; linhas < tamanhoLinhas; linhas++ ) {
			for( int colunas = 0; colunas < tamanhoColunas; colunas++ ) {
				tabuleiro[linhas][colunas] = ' ';
			}
		}
	}
	
	/**
	 * Esse metodo representa um pedaço do barco
	 * 
	 * @param   barco  representa um pedaço do barco
	 */
	public void parteBarco( int barco ) {
		this.barco = barco;
	}
	
	/**
	 * Marca o tabuleiro com um acerto ou erro 
	 * @param   linha  A linha do tiro
	 * @param   coluna A coluna do tiro 
	 * @param   tiro A marca que vai representar acerto ou erro
	 */
	public void acertoErro( int linha, int coluna, char tiro ) {	
		tabuleiro[linha][coluna] = tiro;
	}
	
	/**
	 * Checa se o tiro acertou ou nao
	 * @param    linha    
	 * @param    coluna   
	 *                   
	 * @param    tiro     
	 *                   
	 * @return   boolean  
	 */
	public boolean checaTiro( int linha, int coluna, char tiro ) {
		
		// Procure a marca de barco
		if( tabuleiro[linha][coluna] == tiro ) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	/**
	 * Valida se as coordenadas fornecidas pelo usuario estão
	 * dentro do tabuleiro.
	 * @param   linha  
	 * @param   coluna 
	 * @return  boolean  true se estão no tabuleiro, falso se não
	 */
	public boolean checaTiroValido( int linha, int coluna ) {
		
		// Verifica se o tiro é válido
		if( linha >= maximoLinhas || coluna >= maximoColunas ||
				linha < tamanhoLinhas || coluna < tamanhoColunas ) {
			System.out.println( "Tiro fora do tabuleiro!" );
			return false;
		}
		
		else {
			return true;
		}
	}
	
	/**
	 * Esse metodo mostra o tabuleiro. 
	 * Se o parametro for true, os barcos
	 * sao mostrados. Se falso, 
	 * os barcos nao sao mostrados.
	 * @param   yesOrNo  Determina se mostra os barcos ou nao
	 */
	public String printTabuleiro( boolean yesOrNo ) {
		
		//Tabuleiro
		String rep = "";
		
		// Limites superiores
		for( int limiteSuperior = 0; limiteSuperior < tamanhoColunas + 2; limiteSuperior++ ) {
			rep += "-";
		}
		rep += "\n";
		
		// Print do tabuleiro
		for( int j = 0; j < tamanhoLinhas; j++ ) {
			
			// Borda esquerda
			rep += "|";
			for( int i = 0; i < tamanhoColunas; i++ ) {
				
				if( tabuleiro[j][i] == barco && !yesOrNo ) {
					rep += ' ';
				}
				
				else {
					rep += tabuleiro[j][i];
				}
			}
			
			// direita Borda
			rep += "|" + "\n";
		}
		
		// Limites inferiores
		for( int limiteInferior = 0; limiteInferior < tamanhoColunas + 2; limiteInferior++ ) {
			rep += "-";
		}
		rep += "\n";
		return rep;
	}
}
