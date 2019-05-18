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
	
	// Posi��o do tamanho do barco no array
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
			
		//Valida a dire��o que est� se construindo
		if( linhaColuna != null ) {
			
			// Salva as mudan�as na linha e coluna
			int mudancaLinha = linhaColuna[0];
			int mudancaColuna = linhaColuna[1];
			
			// Controla quantas posi��es de tamanhoBarcos checamos
			int spot = 0;
			
			// se a frota est� construida nos limites do tabuleiro
			boolean validaDentroTabuleiro = true;
	
			while( spot < tamanhosBarcos && validaDentroTabuleiro ) {
				// True se est� dentro dos limites
				validaDentroTabuleiro = tabuleiro.checaValidadeLinhaColuna( linha, coluna );
				
				//Colocar as posi��es um array para compara��o com os locais dos barcos que j� est�o no tabuleiro.
				int[] tiroXbarcos = { linha, coluna };
				
				// Verifica que aquele lugar no tabuleiro j� est� ocupado
				// Loop sobre o array de barcos ande a posi��o destes barcos.
				// E ao mesmo tempo, checa se o spot est� no tabuleiro.
				int numeroBarco = 0;
				while( barcos[numeroBarco] != null && numeroBarco < 4 && validaDentroTabuleiro ) {
					
					// Fa�a um loop sobre as posi��es tomadas pelo navio selecionado
					for( int position = 0; position < barcos[numeroBarco].tamanhoBarco; position++ ) {
						
						// Se ainda n�o encontramos uma posi��o fora do tabuleiro ou 
						// tomada por outro navio, compare a posi��o atual
						if( validaDentroTabuleiro ) {
							
							// Se a posi��o atual j� tiver sido tomada, 
							// ent�o validaDentroTabuleiro ser� falso e n�s sairemos de todos os loops.
							validaDentroTabuleiro = !Arrays.equals( barcos[numeroBarco].positions[position], tiroXbarcos );
						}
					}
					
					// Olhe para o pr�ximo navio j� no tabuleiro
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
		
		// Se a dire��o nao for valida 
		else {
			return false;
		}		
	}
	
	/**
	 * Esse metodo faz duas coisas:
	 * Primeiro, determina qual linha e coluna 
	 * devem ser alteradas para dada uma dire��o. 
	 * Segundo, determina se a dire��o � valida
	 * 
	 * @param direcao
	 * @return se a dire��o � valida ou n�o
	 */
	private int[] direcao( String direcao ) {
		
		// Depende da dire��o
		int mudancaLinha = 0;
		int mudancaColuna = 0;
		
		// Determinar a dire��oq ue o barco foi construido
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
			
			System.out.println( "Nao � uma dire��o valida. As dire��o validas sao" +
					"Cima, Baixo, Esquerda, Direita.");
			return null;
		}

		int[] dirValue = { mudancaLinha, mudancaColuna };
		
		return dirValue;
	}
	
	/**
	 * O objetivo deste m�todo � chamar validacaoConstrucaoFrota para ver se podemos 
	 * construir um navio no local determinado, dos tamanhos descritosBarcos
	 * e na dire��o dada. Se pudermos, criamos um navio de acordo com esses par�metros.
	 * @param   linha         
	 * @param   coluna     
	 * @param   tamanhosBarcos     
	 * @param   direcao   
	 * @return  boolean    
	 */
	public boolean construcaoBarco(int linha, int coluna, String direcao ) {
		
		// pega a altera��o de linha e coluna dada uma dire��o
		int[] linhaColuna = direcao( direcao );
		
		// Se true, construa
		if( validacaoConstrucaoFrota( linha, coluna, tamanhosBarcos[posicaoTamanhoBarcoNoArray], linhaColuna ) ) {
			
			// Indica onde colocaremos o navio
			int index = 0;
			
			// Encontre o primeiro valor nulo no array de barcos
			while( barcos[index] != null && index < barcos.length ) {
				index++;
			}
			
			// Posi��o do barco
			int[][] positions = new int[tamanhosBarcos[posicaoTamanhoBarcoNoArray]][];
			
			// Calcula as diferentes posi��es 
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
			System.out.println( "Nao � uma posicao ou direcao valida!" );
			return false;
		}
	}
	
	/**
	 * O objetivo deste m�todo � verificar se o ataque atinge 
	 * qualquer um dos barcos da frota
	 * @param   linha     
	 * @param   coluna   
	 * @return  boolean  
	 */
	public boolean checkHit( int linha, int coluna, char tiro ) {
		
		// Inicialize como falso, j� que n�o sabemos 
		// quando todos os barcos s�o verificados.
		boolean hit = false;
		
		// Posi��o na matriz de barcos do navio atual que estamos verificando
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
	 * O objetivo deste m�todo � verificar 
	 * todos os nossos barcos para ver se eles ainda est�o flutuando .
	 * @return   boolean  
	 */
	public boolean checkShips() {
		
		//Se no minimo um navio ainda flutua, ent�o true
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
