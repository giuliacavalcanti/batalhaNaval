package start;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Cliente {

  static Socket servidor;
  static String ip;
  static Scanner in;
  static Jogador jogador;
  static int[] dados = new int[4];
  static Jogador[] jogadores = new Jogador[2];
  static Tabuleiro[] tabs = new Tabuleiro[2];

  static ObjectInputStream istream;
  static ObjectOutputStream ostream;
  static DataInputStream dis;
  static DataOutputStream dos;


  public static void print(String s){
    System.out.println(s);
  }

  public static void nullArray(Jogador[] j) {
    for (int i=0; i<j.length; i++)
      j[i] = null;
  }

  // cria o jogador para o jogo
  public static void createPlayer(int[] dados){
    System.out.print("Digite o seu nome para o jogo: ");
    String nome = in.next();
    jogador = new Jogador(nome, dados[0], dados[1], dados[2], dados[3]);
    System.out.println("Jogador criado!");
  }

  // configura o mapa do jogador posicionando os barcos na matriz
  public static void configurePlayerMap(){
    int x, y;
    System.out.println("Posicione as embarcações usando os índices das linhas e colunas.");
    while (jogador.getNumBarcos() < jogador.MAX_BARCOS){
      jogador.printTab();
			System.out.print("Linha: ");
			x = in.nextInt();
			System.out.print("Coluna: ");
			y = in.nextInt();
			jogador.posicionarBarco(x, y);
			System.out.printf("Adicionado! (%d/%d)\n", jogador.getNumBarcos(), jogador.MAX_BARCOS);
    }
    System.out.println("Meu mapa de embarcações:");
    jogador.printTab();
  }

  // imprimir os tabuleiros
  public static void mostrarTabs(int i){
    if (i == 0){
      print("Seu tabuleiro:");
      jogadores[0].printTab();
      print("Tabuleiro de "+ jogadores[1].getNome());
  		jogadores[1].printTabSecret();
    } else if (i == 1){
      print("Seu tabuleiro:");
      jogadores[1].printTab();
      print("Tabuleiro de "+ jogadores[0].getNome());
  		jogadores[0].printTabSecret();
    }
  }



  public static void main(String[] args) throws UnknownHostException {
    in = new Scanner(System.in);

    // conectar: digite a porta em que deseja se conectar
    String ip = Inet4Address.getLocalHost().getHostAddress();
    int porta = 4455;
    try {
      servidor = new Socket(ip, porta);
      print("Conectado a porta "+ porta);
      print("Aguardando outro jogador...");

      // ler dados do jogo
      istream = new ObjectInputStream(servidor.getInputStream());
      dados = (int[]) istream.readObject();

      createPlayer(dados); // criar o seu jogador
      configurePlayerMap(); // distribuir embarcações no mapa

      // enviar o obj jogador para o servidor
      ostream = new ObjectOutputStream(servidor.getOutputStream());
      ostream.writeObject(jogador); // escreve o objeto na stream
      ostream.flush();
      print("OK! Espere o outro jogador");

      // recebe índice do jogador no servidor
      int myIndex = istream.readInt();
      int oponentIndex;
      if (myIndex == 0) oponentIndex = 1;
      else oponentIndex = 0;
      print("Você é o jogador "+ (myIndex+1) +".");

      // recebe os vetores com os dois jogadores e os dois tabuleiros
      jogadores = (Jogador[]) istream.readObject();
      tabs = (Tabuleiro[]) istream.readObject();
      print("Você: "+ jogadores[myIndex].getNome());
      print("Adversário: "+ jogadores[oponentIndex].getNome());

      // iniciar a Batalha
      print("==============================:");
      print(" Iniciando a Batalha");
      print("==============================:");
      int sinal, x, y, indexVencedor;
      int[] coord = new int[2];
      String log, placar;
      boolean end = false;

      dos = new DataOutputStream(servidor.getOutputStream());
      dis = new DataInputStream(servidor.getInputStream());

      // aqui comeÃ§a o loop ===================================================================================//
      while (!end) {
        // receber o sinal para definir quem ataca
        sinal = (int) istream.readInt();
        //mostrarTabs(myIndex);


        /* o sinal enviado pelo servidor contém o índice do jogador que ataca nessa rodada
         * logo, o cliente deve verificar se esse sinal é o seu índice no jogo */
        if (sinal == myIndex){
        	placar = (String)istream.readObject();
            print(placar);
            print("Sua vez. Digite as coordenadas (linha e coluna) do tiro.");
           /* recebe os valores X e Y do teclado e em seguida envia para o servidor */
           coord = new int[2];
           System.out.print("Linha: ");
           coord[0] = in.nextInt(); // coordenada x
           System.out.print("Coluna: ");
           coord[1] = in.nextInt(); // coordenada y
           ostream.writeObject(coord); // envia as coordenadas

          // receber log do resultado do tiro
          log = (String)istream.readObject();
          print(log);

          /* se o sinal for igual ao índice do seu oponente significa que é a vez do mesmo */
        } else if (sinal == oponentIndex) {
            placar = (String)istream.readObject();
            print(placar);
          print("Vez de "+ jogadores[oponentIndex].getNome() +". Espere...");
          // receber log do resultado do tiro
          log = (String)istream.readObject();
          print(log);
        } 
        else {
          break;
        }
      }
      
      print("Todos os barcos foram destruidos");      
      // recebe o índice do jogador vencedor: 0 ou 1
      // em caso de empate receberÃ¡ -1.
      indexVencedor = istream.readInt();
      if (indexVencedor > 0){
        print("+-----------------------------+");
        print("| \tVencedor: "+ jogadores[indexVencedor].getNome());
        print("+-----------------------------+");
      } else if (indexVencedor == -1) {
        print("+-----------------------------+");
        print("| \tEmpate!");
        print("+-----------------------------+");
      }

      System.out.print("Digite qualquer coisa para desconectar... ");
      String fim = in.next();

      // fechando streams e socket
      istream.close();
      ostream.close();
      servidor.close();
      print("Conexão Encerrada.");

    } catch(Exception e) {
      System.err.println("Erro: "+ e.toString());
    }

  }

}
