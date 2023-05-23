package Components;
import java.util.*;

public class Consumo {
    public static int k = 2; // Capacidade de produtos do caminhão.
    public static final double CONSUMO_POR_PRODUTO = 0.5;

    public static double calcularConsumoAtual(ArrayList<Loja> listaLoja, ArrayList<Integer> permutacao) {
        int origemX = listaLoja.get(0).x;
        int origemY = listaLoja.get(0).y;
        int cargaAtual = 0;
        double consumoDeCarga = 0.0;
        double consumoDoCaminho = 0.0;
        // double rendimento = 10;

        ArrayList<Integer> produtosColetados = new ArrayList<>();
        permutacao.add(0);

        for (int indexLoja : permutacao) {
            Loja lojaAtual = listaLoja.get(indexLoja);
            int destinoX = lojaAtual.x;
            int destinoY = lojaAtual.y;

            cargaAtual = entregarProdutos(produtosColetados, lojaAtual, cargaAtual);
            consumoDeCarga = cargaAtual * CONSUMO_POR_PRODUTO;

            cargaAtual = coletarProdutos(produtosColetados, lojaAtual, cargaAtual);
            consumoDeCarga = cargaAtual * CONSUMO_POR_PRODUTO;

            double distancia = Math.sqrt(Math.pow(destinoX - origemX, 2) + Math.pow(destinoY - origemY, 2));
            // double consumoViagemAtual = distancia / rendimento;
            // consumoDoCaminho += consumoViagemAtual;
            origemX = destinoX;
            origemY = destinoY;
            // rendimento = 10 - consumoDeCarga
            consumoDoCaminho += distancia;
        }

        return consumoDoCaminho;
    }

    public static int coletarProdutos(ArrayList<Integer> listaDeProdutos, Loja lojaDeColeta, int cargaAtual) {
        if(cargaAtual < k && (lojaDeColeta.destinos.size() != 0)) {
            for (int produtos : lojaDeColeta.destinos) {
                listaDeProdutos.add(produtos);
                cargaAtual += 1;
            }
        }

        return cargaAtual;
    }

    public static int entregarProdutos(ArrayList<Integer> listaDeProdutos, Loja lojaDeEntrega, int cargaAtual) {
        if(listaDeProdutos.contains(lojaDeEntrega.id)) {
                listaDeProdutos.removeAll(Collections.singleton(lojaDeEntrega.id));
                cargaAtual = listaDeProdutos.size();
            }

        return cargaAtual;
    }

    public static boolean entradaInvalida(ArrayList<Loja> listaLoja) {
        boolean valido = true;
        for (Loja loja : listaLoja) {
            if (loja.destinos.size() > k) {
                valido = false;
                break;
            }
        }

        return valido;
    }
}