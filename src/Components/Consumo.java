package Components;
import java.util.ArrayList;
import java.util.Collections;

public class Consumo {
    public static int CAPACIDADE_CAMINHAO;
    private static final double CONSUMO_POR_PRODUTO = 0.5;
    public static int cargaAtual = 0;

    public static double calcularConsumoCaminho(ArrayList<Loja> listaLoja, ArrayList<Integer> permutacao) {
        ArrayList<Loja> listaLojaCopy = Loja.clonarListaLoja(listaLoja);
        double consumoDoCaminho = 0.0;
        double rendimento = 10.0;
        cargaAtual = 0;

        ArrayList<Integer> produtosColetados = new ArrayList<>();
        // Adicionando origem e destino na permutação (matriz).
        permutacao.add(0, 0);
        permutacao.add(0);

        for(int indexLoja = 0; indexLoja < permutacao.size() - 1; indexLoja ++) {
            Loja currentStore = listaLojaCopy.get(permutacao.get(indexLoja));
            Loja nextStore = listaLojaCopy.get(permutacao.get(indexLoja + 1));
            int origemX = currentStore.x;
            int origemY = currentStore.y;
            int destinoX = nextStore.x;
            int destinoY = nextStore.y;

            if(currentStore.id != 0) {
                cargaAtual = entregarProdutos(produtosColetados, currentStore, cargaAtual);
                cargaAtual = coletarProdutos(produtosColetados, currentStore, cargaAtual);
                rendimento = 10.0 - (cargaAtual * CONSUMO_POR_PRODUTO);
            }
            
            double distancia = calcularDistancia(destinoX, destinoY, origemX, origemY);
            double consumoDeViagemAtual = distancia / rendimento;
            consumoDoCaminho += consumoDeViagemAtual;
        }

        if (produtosColetados.size() != 0 || Loja.restouProdutos(listaLojaCopy))
            consumoDoCaminho = Double.MAX_VALUE;

        return consumoDoCaminho;
    }

    public static int entregarProdutos(ArrayList<Integer> listaDeProdutos, Loja lojaDeEntrega, int cargaAtual) {
        if (listaDeProdutos.contains(lojaDeEntrega.id)) {
            listaDeProdutos.removeAll(Collections.singleton(lojaDeEntrega.id));
            cargaAtual = listaDeProdutos.size();
        }
        return cargaAtual;
    }

    public static int coletarProdutos(ArrayList<Integer> listaDeProdutos, Loja lojaDeColeta, int cargaAtual) {
        if (!lojaDeColeta.destinos.isEmpty()) {
            for (int produtos = 0; produtos < lojaDeColeta.destinos.size(); produtos++) {
                if(cargaAtual < CAPACIDADE_CAMINHAO) {
                    listaDeProdutos.add(lojaDeColeta.destinos.get(produtos));
                    lojaDeColeta.destinos.remove(produtos);
                    produtos--;
                    cargaAtual++;
                }
            }
        }
        return cargaAtual;
    }

    public static double calcularDistancia(int destinoX, int destinoY, int origemX, int origemY) {
        return Math.sqrt(Math.pow(destinoX - origemX, 2) + Math.pow(destinoY - origemY, 2));
    }

    public static boolean entradaInvalida(ArrayList<Loja> listaLoja) {
        for (Loja loja : listaLoja) {
            if (loja.destinos.size() > CAPACIDADE_CAMINHAO) {
                return false;
            }
        }
        return true;
    }
}