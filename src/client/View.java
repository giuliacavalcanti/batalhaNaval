package client;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Esta classe é usada para exibir informações sobre o jogo para o 
 * jogador e para adquirir informações do usuário.
 * @author  Falcao
 *
 */
@SuppressWarnings("serial")
public class View implements Serializable{
	
	// Input do jogador
	private Scanner scan = new Scanner( System.in );
	
	// Nome dos barcos
	private final String[] nomesBarcos = { "Patrol", "Destroyer", "Battleship", "Carrier" };
	
	/**
	 * Esse método verifica a entrada do usuário para tipos válidos 
	 * (exemplo: int para posições e String para direção).
	 * @param   nomeJogador   nome do jogador
	 * @param   index        tipo do barco
	 * @return  Object[]    
	 */
	public Object[] getPositions( String nomeJogador, int index ) {
		
		// Matriz do tipo objeto: manter as coordenadas e a direção
		Object[] inputs = new Object[3];
		
		// Se o input nao é valido, set falso
		boolean inputValido = false;
		
		// posição no inputs
		int spot = 0;
		
		// Pergunta pro usuario dizer uma localização e direção de um barco
		System.out.println( "Os inputs devem ser (Numero [0-9], Numero [0-9], Direção [Cima, Baixo, Esquerda, Direita]");
		System.out.println( nomeJogador + ", escolha uma posição inicial de linha e coluna para o " +
				nomesBarcos[index] + " navio de comprimento e em qual direção sera construido: " );
	
		// Continua perguntando até todos os tipos serem validos
		// ( integer, integer, and String).
		while( !inputValido ) {
			
			// Valida coluna e linha
			if( spot != 2 && scan.hasNextInt() ) {
				System.out.println( "Input  registrado" );
				inputs[spot] = scan.nextInt();
				spot++;
			}
			
			// Valida direção
			else if ( spot == 2 && scan.hasNext() ) {
				System.out.println( "Input Registrado." );
				inputs[spot] = scan.next();
				inputValido = true;
			}
			else {
				System.out.println( "Input invalido. Os inputs devem ser (inteiro, inteiro, String)" );
				spot = 0;
				scan.next();
			}
		}
		return inputs;
	}
	
	/**
	 * Este método recebe entrada do usuário para a coordenada desejada
	 * @param    nomeJogador   Nome do jogador a vez
	 * @return   int[]        Coordenadas que o jogador esta atacando
	 */
	public int[] getInput( String nomeJogador ) {
		int[] alvo = new int[2];
		
		// Pergunta ao usuario o alvo
		System.out.println( nomeJogador + ", escolha a posicao do alvo! (ex. linha, coluna)" );
		
		//Se o input nao é valido, set falso
		boolean inputValido = false;
		
		// Poiscao no array alvo
		int index = 0;
		
		// Enquanto o input nao for valido, loop
		while ( !inputValido ) {
			
			// Valida linha
			if( index == 0 && scan.hasNextInt() ) {
				alvo[index] = scan.nextInt();
				index++;
			}
			
			// Valida coluna
			else if( index == 1 && scan.hasNextInt() ) {
				alvo[index] = scan.nextInt();
				inputValido = true;
			}
			
			//
			else {
				System.out.println( "Não é um input valido! Must be a pair of numbers!" );
				index = 0;
			}
		}
		return alvo;
	}
	
	/**
	 * Print console
	 * @param   mensagem
	 */
	public void mensagem( String mensagem ) {
		System.out.println( mensagem );
	}
}