import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class servidor {
    public static void main(String[] args) throws IOException {
        int port = 5000 ;
        List<Integer> jogadas = new ArrayList<Integer>();
        int contador = 0;
        int vitoriasCliente = 0;
        int vitoriasServidor = 0;

        ServerSocket servidor = new ServerSocket(port);

        Socket cliente = servidor.accept();

        System.out.println("Cliente Conectado: "+cliente.getInetAddress().getHostAddress());


        while(contador<15){
            DataInputStream input = new DataInputStream(cliente.getInputStream());
            String msgCliente = input.readUTF();
            String jogVencedor;

            int jogadaCliente = Integer.parseInt(msgCliente);
            int jogadaServidor = calcularJogada(jogadas, vitoriasCliente, vitoriasServidor);

            jogadas.add(jogadaCliente);

            if(jogadaServidor == jogadaCliente) jogVencedor = "empate";
            else{
                boolean vencedor = avaliarJogada(jogadaServidor,jogadaCliente);
                if(vencedor){
                    jogVencedor = "Servidor";
                    vitoriasServidor++;
                } else{
                    jogVencedor = "Cliente";
                    vitoriasCliente++;
                }
            }
            OutputStream output = cliente.getOutputStream();
            output.write(String.valueOf(jogadaServidor).getBytes(StandardCharsets.UTF_8));
            String resultado =
                    "Partida: " + (contador+1) + " - " +
                    "Servidor: "+ nomeEnum(jogadaServidor) + " / " +
                    "Cliente: "+ nomeEnum(jogadaCliente) + " - " +
                    "Vencedor " + jogVencedor;
            System.out.println(resultado);
            contador++;
        }
        String jogVencedor;

        if(vitoriasServidor > vitoriasCliente) jogVencedor = "Servidor com " + vitoriasServidor + " vitorias";
        else jogVencedor = "Cliente com " + vitoriasCliente + " vitorias";

        String resultado = "Final: " + jogVencedor;
        System.out.println(resultado);

    }

    static int calcularJogada(List<Integer> jogadas, int vitoriasCliente,int vitoriasServidor){
        int tamanho = jogadas.size();

        if(tamanho == 0) return (int) ((Math.random()*(4))+1);
        if(vitoriasCliente > vitoriasServidor) return (int) ((Math.random()*(4))+1);

        int escolha = (int) ((Math.random()*tamanho));
        return jogadas.get(escolha);
    }

    static boolean avaliarJogada(int jogada1, int jogada2){
        if(jogada1 == Jogada.pedra.ordinal())
            return jogada2 == Jogada.tesoura.ordinal() || jogada2 == Jogada.lagarto.ordinal();
        if(jogada1 == Jogada.tesoura.ordinal())
            return jogada2 == Jogada.papel.ordinal() ||  jogada2 == Jogada.lagarto.ordinal();
        if(jogada1 == Jogada.spock.ordinal())
            return jogada2 == Jogada.tesoura.ordinal() ||  jogada2 == Jogada.pedra.ordinal();
        if(jogada1 == Jogada.lagarto.ordinal())
            return jogada2 == Jogada.papel.ordinal() || jogada2 == Jogada.spock.ordinal();
        if(jogada1 == Jogada.papel.ordinal())
            return jogada2 == Jogada.pedra.ordinal() || jogada2 == Jogada.spock.ordinal();
        return false;
    }

    static String nomeEnum(int valor){
        if(valor == Jogada.pedra.ordinal()) return "pedra";
        if(valor == Jogada.spock.ordinal()) return "spock";
        if(valor == Jogada.papel.ordinal()) return "papel";
        if(valor == Jogada.tesoura.ordinal()) return "tesoura";
        if(valor == Jogada.lagarto.ordinal()) return "lagarto";

        return "desconhecido";
    }

    enum Jogada{
        pedra,
        spock,
        papel,
        tesoura,
        lagarto
    }
}
