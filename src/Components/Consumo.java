package Components;
import java.util.ArrayList;
import java.util.Collections;

public class Consumo {
    private static final int CAPACIDADE_CAMINHAO = 2;
    private static final double CONSUMO_POR_PRODUTO = 0.5;

    public static double calcularConsumoAtual(ArrayList<Loja> listaLoja, ArrayList<Integer> permutacao) {
        ArrayList<Loja> listaLojaCopy = Loja.clonarListaLoja(listaLoja);
        int cargaAtual = 0;
        double consumoDoCaminho = 0.0;
        double rendimento = 10.0;

        ArrayList<Integer> produtosColetados = new ArrayList<>();
        permutacao.add(0);

        int origemX = listaLoja.get(0).x;
        int origemY = listaLoja.get(0).y;

        for (int indexLoja : permutacao) {
            Loja lojaAtual = listaLojaCopy.get(indexLoja);
            int destinoX = lojaAtual.x;
            int destinoY = lojaAtual.y;

            cargaAtual = entregarProdutos(produtosColetados, lojaAtual, cargaAtual);
            cargaAtual = coletarProdutos(produtosColetados, lojaAtual, cargaAtual);

            double distancia = calcularDistancia(destinoX, destinoY, origemX, origemY);
            double consumoDeViagemAtual = distancia / rendimento;
            consumoDoCaminho += consumoDeViagemAtual;

            origemX = destinoX;
            origemY = destinoY;

            rendimento = 10.0 - (cargaAtual * CONSUMO_POR_PRODUTO);
            consumoDoCaminho += distancia;
        }

        if (produtosColetados.size() != 0 || restouProdutos(listaLojaCopy))
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
        if (cargaAtual < CAPACIDADE_CAMINHAO && !lojaDeColeta.destinos.isEmpty()) {
            for (int produtos : lojaDeColeta.destinos) {
                listaDeProdutos.add(produtos);
                cargaAtual++;
            }
            lojaDeColeta.destinos.clear();
        }
        return cargaAtual;
    }

    public static boolean restouProdutos(ArrayList<Loja> listaLoja) {
        for (Loja loja : listaLoja) {
            if (!loja.destinos.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static double calcularDistancia(int destinoX, int destinoY, int origemX, int origemY) {
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