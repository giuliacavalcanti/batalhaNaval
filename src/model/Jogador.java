package model;

/**
 * @author Falcao
 *
 */
public class Jogador {
	// Nome do jogador
	private String nomeJogador;

	// Indica os barcos do jogador
	private char barcoMarcador;
		
		// Indica as jogadas do jogador
	private char hitMarcador;
		
		
	/**
	 * @param playerName
	 * @param barcoMarcador
	 * @param hitMarcador
	 */
	public Jogador( String playerName, char barcoMarcador, char hitMarcador ) {
			this.nomeJogador = playerName;
			this.barcoMarcador = barcoMarcador;
			this.hitMarcador = hitMarcador;
	}
	
	public char getBarcoMarcador() {
		return barcoMarcador;
	}

	public void setBarcoMarcador(char barcoMarcador) {
		this.barcoMarcador = barcoMarcador;
	}

	public char getHitMarcador() {
		return hitMarcador;
	}

	public void setHitMarcador(char hitMarcador) {
		this.hitMarcador = hitMarcador;
	}

	public void setNomeJogador(String nomeJogador) {
		this.nomeJogador = nomeJogador;
	}

	public String getNomeJogador() {
		return nomeJogador;
	}	
}