package start;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import game.Jogador;
import game.Tabuleiro;

public class Servidor {

  static ServerSocket servidor;
  static Socket[] clientes = new Socket[2];
  static Jogador[] jogadores = new Jogador[2];
  static Tabuleiro[] tabuleiro = new Tabuleiro[2];

  // streams de entrada e são do para objetos e dados primitivos
  static Scanner in = new Scanner(System.in);
  static ObjectInputStream[] istream = new ObjectInputStream[2];
  static ObjectOutputStream[] ostream = new ObjectOutputStream[2];
  static DataInputStream[] dis = new DataInputStream[2];
  static DataOutputStream[] dos = new DataOutputStream[2];

  // constantes string:
  private static final String BARCO_DESTRUIDO = "------------------------------\nBarco destruição!\n------------------------------";
  private static final String TIRO_NA_AGUA = "------------------------------\nTiro na água!\n------------------------------";
  private static final String ALVO_JA_ATACADO = "------------------------------\nAlvo ja foi atacado antes!\n------------------------------";


  public static void print(String s){
    System.out.println(s);
  }

  public static int [][] partesBarcos(){
	  int [][] partes = new int [2][2];
	  int jogador=-1;
	  	for (Tabuleiro tabs : tabuleiro){
	  		jogador++;
	  		char [][] jogadorTabuleiro = tabs.getTab();
		  	for(int linha = 0; linha < jogadorTabuleiro.length; linha++){
		  		for(int coluna =0; coluna < jogadorTabuleiro[linha].length; coluna++){
		  			if(jogadorTabuleiro[linha][coluna] == 'B'){
		  				partes[jogador][jogador] = partes[jogador][jogador] + 1;
		  			}
		  		}
		  	}
	  	}
	  	return partes;
  }
  
  public static String placar(){
    String nome1 = jogadores[0].getNome();
    String pontos1 = ""+jogadores[0].getPontos();
    String nome2 = jogadores[1].getNome();
    String pontos2 = ""+jogadores[1].getPontos();
    return "------------Placar------------\n"+ nome1 + ": "+ pontos1 +"\n"+ nome2 +": "+ pontos2 +"\n------------------------------";
  }

  // pegar o índices do jogador com mais pontos
  public static int indexJogadorComMaisPontos() {
    int p1 = jogadores[0].getPontos();
    int p2 = jogadores[1].getPontos();
    // verifica a maior pontuação para retornar o índices
    if (p1 > p2)
      return 0;
    else if (p2 > p1)
      return 1;
    // se não houver um maior que o outro,
    // significa ue serÃ£o iguais, logo, retorne -1
    return -1;
  }


  public static void main(String[] args) {
    int n=0, porta;
    int[] dados = new int[4];

    // configurar jogo
    print("Configure o servidor.");
    System.out.print("Número de linhas do tabuleiro: ");
    dados[0] = in.nextInt();
    System.out.print("Número de colunas do tabuleiro: ");
    dados[1] = in.nextInt();
    System.out.print("Número de barcos para cada jogador: ");
    dados[2] = in.nextInt();
//    System.out.print("Número de tiros: ");
//    dados[3] = in.nextInt();
    System.out.print("Digite a porta para a conexão: ");
    porta = in.nextInt();
    print("----------------------------------------");

    try {
      String ip = Inet4Address.getLocalHost().getHostAddress();
      servidor = new ServerSocket(porta);
      // aguardar duas conexão e envia os dados do jogo para os clientes

      print("Aguardando conexão na porta "+ porta +". IP ["+ ip +"]");
      for (n=0; n<2; n++){
        clientes[n] = servidor.accept();
        print("Host conectado: "+ clientes[n].getInetAddress().getHostName()+" ["+ clientes[n].getInetAddress().getHostAddress()+ "]");
      }
      print("----------------------------------------");

      // enviar dados do jogo para os clientes
      print("Aguardando jogadores");
      for (int i=0; i<2; i++){
        ostream[i] = new ObjectOutputStream(clientes[i].getOutputStream());
        ostream[i].writeObject(dados);
        ostream[i].flush();
      }

      // receber jogadores dos clientes e enviar o índices para o respecctivo clientes
      for (int i=0; i<2; i++){
        istream[i] = new ObjectInputStream(clientes[i].getInputStream());
        jogadores[i] = (Jogador) istream[i].readObject();
        tabuleiro[i] = jogadores[i].getTabuleiro();
        print("Jogador "+(i+1)+": "+ jogadores[i].getNome());
        ostream[i].writeInt(i); // envia o índices para o respectivo cliente para identificação
        ostream[i].flush();
      }
      print("----------------------------------------");

      // enviar o vetor de jogadores e tabuleiros para os clientes
      for (int i=0; i<2; i++){
        ostream[i].writeObject(jogadores);
        ostream[i].writeObject(tabuleiro);
        ostream[i].flush();
      }

      // iniciar a Batalha
      int i, def, x, y, iteration=0;
      int[] coord = new int[2];
      char alvo;
      String result = "";

      // LOOP ==================================================================================================//
      int [][] partesBarcosExistentes = partesBarcos();
      while (partesBarcosExistentes[0][0] >= 0 && partesBarcosExistentes[1][1] >= 0) {
    	  coord = null; // reseta coordenadas
          i = iteration % 2; // indice do jogador que ataca
          if (i==0){
            def = 1;
          } // indice do jogador que � atacado
          else{
            def = 0;
          } 
        // mandar um sinal para os clientes com o índices de quem atacante
        // e a string com o placar
        for (int k=0; k<2; k++){
            ostream[k].writeInt(i);
            ostream[k].writeObject( placar() );
            ostream[k].flush();
          }
        
        if ((partesBarcosExistentes[0][0] > 0) && (partesBarcosExistentes[1][1] > 0)) {
          // esperar coordenadas do tiro
          coord = (int[]) istream[i].readObject();

          // efetuar ataque
          alvo = jogadores[i].atacar(jogadores[def], coord[0], coord[1]);
          if (alvo == 'B'){
              result = BARCO_DESTRUIDO;
              partesBarcosExistentes[def][def]--;

            } 
            else if (alvo == '~'){
              result = TIRO_NA_AGUA;
            } 
            else{
              result = ALVO_JA_ATACADO;
            } 

            // enviar log do resultado do tiro para os clientes
            for (int k=0; k<2; k++){
              ostream[k].writeObject(coord[0] +","+ coord[1]);
              ostream[k].flush();
            }

            iteration++;

          } else {
            // se partesBarcos == 0
            break;
          }

        }// fim do while

      // enviar o índices do vencedor
      // em caso de empate, indexVencedor = -1
      int indexVencedor = indexJogadorComMaisPontos();
      for (int k=0; k<2; k++) {
        ostream[k].writeInt(indexVencedor);
        ostream[k].flush();
      }

      // aguarda os clientes encerrarem a conexÃ£o
      String[] str = new String[2];
      for (int k=0; k<2; k++)
        str[k] = (String) istream[k].readObject();

      // fechando streams e socket
      for (i=0; i<2; i++) {
        istream[i].close();
        ostream[i].close();
        clientes[i].close();
      }
      print("Conexões encerradas.");

    } catch(Exception e){
      System.out.println("Erro: "+ e.toString());
    }
  }
}
