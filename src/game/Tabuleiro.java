package game;

import java.io.Serializable;

public class Tabuleiro implements Serializable {

	int sizeX;
	int sizeY;
	char[][] tab;
	int n_barcos;

	// o tabuleiro recebe os valores de suas dimens�es
	public Tabuleiro(int x, int y){
		this.sizeX = x;
		this.sizeY = y;
		tab = new char[x][y];
		this.initTab();
	}

	public void initTab() {
		for (int i=0; i<sizeX; i++)
			for (int k=0; k<sizeY; k++)
				tab[i][k] = '~';
	}

	public void setTab(char[][] matriz){
		this.tab = matriz;
	}
	
	public char [][] getTab() {
		return this.tab;
	}

	/*
	 * Imprime o tabuleiro com os �ndices das linhas e colunas
	 */
	public void print() {
		System.out.println("");

		System.out.printf("     ");
		for (int j=0; j<sizeY; j++)
			System.out.printf("%d ", j); // imprime �ndices das colunas
		System.out.println("");

		for (int i=0; i<sizeX; i++) {
			System.out.printf("  %d  ", i); // imprime o numero da linha

			for (int k=0; k<sizeY; k++)
				System.out.printf("%c ", tab[i][k]); // imprime linha inteira

			System.out.println(""); // vai pra pr�ximas linha
		}
		System.out.println("");
	}



	public void printSecret() {
		System.out.println("");

		System.out.printf("     ");
		for (int j=0; j<sizeY; j++)
			System.out.printf("%d ", j); // imprime �ndices das colunas
		System.out.println("");

		for (int i=0; i<sizeX; i++) {
			System.out.printf("  %d  ", i); // imprime o numero da linha

			for (int k=0; k<sizeY; k++){
				if (tab[i][k] == '~' || tab[i][k] == 'B')
					if (tab[i][k] == '~' || tab[i][k] == 'B' || tab[i][k] == '*' || tab[i][k] == 'X'){
						System.out.printf("%c ", tab[i][k]);
					}
				} // imprime linha inteira

			System.out.println(""); // vai pra pr�ximas linha
		}
		System.out.println("");
	}

}
