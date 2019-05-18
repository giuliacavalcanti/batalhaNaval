package model;
import java.util.Arrays;

/**
 * Essa classe cria e gerenciar 
 * uma frota de barcos para um jogo de batalha
 * 
 *  @author Falcao
 */

public class Frota {
	
	// Os quatro tamanhos de barcos usados em um jogo de batalha.
	final int[] tamanhosBarcos = {2, 3, 4, 5};
	
	// Posição do tamanho do barco no array
	int posicaoTamanhoBarcoNoArray = 0;
	
	Barcos[] barcos = new Barcos[4];
	
	// A reference to an tabuleiro
	// object
	Tabuleiro tabuleiro;
	
	// set o barco
	char marcouBarco;
	
	/**
	 * Construtor
	 * @param tabuleiro
	 * @param marcouBarco
	 */
	public Frota( Tabuleiro tabuleiro, char marcouBarco){
		this.tabuleiro = tabuleiro;
		this.marcouBarco = marcouBarco;
	}
	
	/**
	 * Verifica se o barco pode ser construido
	 * @param linha
	 * @param coluna
	 * @param tamanhosBarcos
	 * @param linhaColuna
	 * @return boolean
	 */
	private boolean validacaoConstrucaoFrota( int linha, int coluna,
			int tamanhosBarcos, int[] linhaColuna ) {
			
		//Valida a direção que está se construindo
		if( linhaColuna != null ) {
			
			// Salva as mudanças na linha e coluna
			int mudancaLinha = linhaColuna[0];
			int mudancaColuna = linhaColuna[1];
			
			// Controla quantas posições de tamanhoBarcos checamos
			int spot = 0;
			
			// se a frota está construida nos limites do tabuleiro
			boolean validaDentroTabuleiro = true;
	
			while( spot < tamanhosBarcos && validaDentroTabuleiro ) {
				// True se está dentro dos limites
				validaDentroTabuleiro = tabuleiro.checaValidadeLinhaColuna( linha, coluna );
				
				//Colocar as posições um array para comparação com os locais dos barcos que já estão no tabuleiro.
				int[] tiroXbarcos = { linha, coluna };
				
				// Verifica que aquele lugar no tabuleiro já está ocupado
				// Loop sobre o array de barcos ande a posição destes barcos.
				// E ao mesmo tempo, checa se o spot está no tabuleiro.
				int numeroBarco = 0;
				while( barcos[numeroBarco] != null && numeroBarco < 4 && validaDentroTabuleiro ) {
					
					// Faça um loop sobre as posições tomadas pelo navio selecionado
					for( int position = 0; position < barcos[numeroBarco].tamanhoBarco; position++ ) {
						
						// Se ainda não encontramos uma posição fora do tabuleiro ou 
						// tomada por outro navio, compare a posição atual
						if( validaDentroTabuleiro ) {
							
							// Se a posição atual já tiver sido tomada, 
							// então validaDentroTabuleiro será falso e nós sairemos de todos os loops.
							validaDentroTabuleiro = !Arrays.equals( barcos[numeroBarco].positions[position], tiroXbarcos );
						}
					}
					
					// Olhe para o próximo navio já no tabuleiro
					numeroBarco++;
				}
						
				//Altera valores de lina e coluna
				linha += mudancaLinha;
				coluna += mudancaColuna;
				
				// Proximo spot
				spot++;
			}
		
			return validaDentroTabuleiro;
		}
		
		// Se a direção nao for valida 
		else {
			return false;
		}		
	}
	
	/**
	 * Esse metodo faz duas coisas:
	 * Primeiro, determina qual linha e coluna 
	 * devem ser alteradas para dada uma direção. 
	 * Segundo, determina se a direção é valida
	 * 
	 * @param direcao
	 * @return se a direção é valida ou não
	 */
	private int[] direcao( String direcao ) {
		
		// Depende da direção
		int mudancaLinha = 0;
		int mudancaColuna = 0;
		
		// Determinar a direçãoq ue o barco foi construido
		if( direcao.equalsIgnoreCase( "Leste" ) ||
				direcao.equalsIgnoreCase( "Direita" ) ) {
			mudancaColuna = 1;
		}
		
		else if ( direcao.equalsIgnoreCase( "Oeste" ) ||
				direcao.equalsIgnoreCase( "Esquerda" ) ) {
			mudancaColuna = -1;
		}
		
		else if ( direcao.equalsIgnoreCase( "Norte" ) ||
				direcao.equalsIgnoreCase( "Cima" ) ) {
			mudancaLinha = -1;
		}
		
		else if ( direcao.equalsIgnoreCase( "Sul" ) ||
				direcao.equalsIgnoreCase( "Baixo" ) ) {
			mudancaLinha = 1;
		}
		
		else {
			
			System.out.println( "Nao é uma direção valida. As direção validas sao" +
					"Cima, Baixo, Esquerda, Direita.");
			return null;
		}

		int[] dirValue = { mudancaLinha, mudancaColuna };
		
		return dirValue;
	}
	
	/**
	 * O objetivo deste método é chamar validacaoConstrucaoFrota para ver se podemos 
	 * construir um navio no local determinado, dos tamanhos descritosBarcos
	 * e na direção dada. Se pudermos, criamos um navio de acordo com esses parâmetros.
	 * @param   linha         
	 * @param   coluna     
	 * @param   tamanhosBarcos     
	 * @param   direcao   
	 * @return  boolean    
	 */
	public boolean construcaoBarco(int linha, int coluna, String direcao ) {
		
		// pega a alteração de linha e coluna dada uma direção
		int[] linhaColuna = direcao( direcao );
		
		// Se true, construa
		if( validacaoConstrucaoFrota( linha, coluna, tamanhosBarcos[posicaoTamanhoBarcoNoArray], linhaColuna ) ) {
			
			// Indica onde colocaremos o navio
			int index = 0;
			
			// Encontre o primeiro valor nulo no array de barcos
			while( barcos[index] != null && index < barcos.length ) {
				index++;
			}
			
			// Posição do barco
			int[][] positions = new int[tamanhosBarcos[posicaoTamanhoBarcoNoArray]][];
			
			// Calcula as diferentes posições 
			for( int position = 0; position < tamanhosBarcos[posicaoTamanhoBarcoNoArray]; position++ ) {
				int [] spot = { linha, coluna };
				positions[position] = spot;
				
				// Calcula o proximo spot
				linha += linhaColuna[0];
				coluna += linhaColuna[1];
			}
			
			// Cria o barco
			barcos[index] = new Barcos(positions, tamanhosBarcos[posicaoTamanhoBarcoNoArray], marcouBarco, tabuleiro);
			posicaoTamanhoBarcoNoArray++;
			return true;
		}
		
		else {
			System.out.println( "Nao é uma posicao ou direcao valida!" );
			return false;
		}
	}
	
	/**
	 * O objetivo deste método é verificar se o ataque atinge 
	 * qualquer um dos barcos da frota
	 * @param   linha     
	 * @param   coluna   
	 * @return  boolean  
	 */
	public boolean checkHit( int linha, int coluna, char tiro ) {
		
		// Inicialize como falso, já que não sabemos 
		// quando todos os barcos são verificados.
		boolean hit = false;
		
		// Posição na matriz de barcos do navio atual que estamos verificando
		int numeroBarco = 0;
		
		// Loop em barcos
		while( numeroBarco < 4 && barcos[numeroBarco] != null && !hit ) {
			
			// Verifica a jogada
			hit = barcos[numeroBarco].resultadoJogada( linha, coluna, tiro );
			
			// Verifica o proximo navio
			numeroBarco++;
		}
		
		return hit;
	}
	
	/**
	 * O objetivo deste método é verificar 
	 * todos os nossos barcos para ver se eles ainda estão flutuando .
	 * @return   boolean  
	 */
	public boolean checkShips() {
		
		//Se no minimo um navio ainda flutua, então true
		boolean barcoStatus = false;
		
		// Loop over the barcos
		for( int ship = 0; ship < barcos.length; ship++ ) {
			
			// Se nao achamos o barco, checa o status dele
			if( !barcoStatus ) {
				barcoStatus = barcos[ship].checkBarco();
			}
		}
		
		return barcoStatus;
	}
}
