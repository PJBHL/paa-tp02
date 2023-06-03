package Components;
import java.util.*;

public class BranchAndBound {
    public static ArrayList<Integer> produtos;
    public static ArrayList<Integer> melhorSolucao = new ArrayList<>();
    public static double menorConsumo = Double.MAX_VALUE;
    public static int N;
    public static int CAPACIDADE_CAMINHAO = 2;
    private static final double CONSUMO_POR_PRODUTO = 0.5;
    public static ArrayList<Integer> produtosColetados = new ArrayList<>();
    public static int cargaAtual = 0;
    public static double rendimento = 10.0;
    
    public static void branchAndBound(ArrayList<Loja> listaDeLojas, ArrayList<Integer> solucao, int index, double consumoAtual, double lb) {
        if(index > N - 1) {
            if(consumoAtual < menorConsumo && !Consumo.restouProdutos(listaDeLojas)) {
                consumoAtual = menorConsumo;
                melhorSolucao = solucao;
            }
        } else {
            Loja currentStore = listaDeLojas.get(index);
            Loja nextStore = listaDeLojas.get(index + 1);
            int origemX = currentStore.x;
            int origemY = currentStore.y;
            int destinoX = nextStore.x;
            int destinoY = nextStore.y;
            if(!produtos.contains(currentStore.id)) {
                solucao.add(currentStore.id);

                if(currentStore.id != 0) {
                    cargaAtual = entregarProdutos(produtosColetados, currentStore, cargaAtual);
                    cargaAtual = coletarProdutos(produtosColetados, currentStore, cargaAtual);
                    rendimento = 10.0 - (cargaAtual * CONSUMO_POR_PRODUTO);
                }

                double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
                consumoAtual = distancia / rendimento;
                if(consumoAtual < menorConsumo && currentStore.destinos.isEmpty())
                    branchAndBound(listaDeLojas, solucao, index + 1, consumoAtual, lb);
                solucao.remove(index);
            }
            double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
            consumoAtual = distancia / rendimento;
            if(consumoAtual < menorConsumo && nextStore.destinos.isEmpty())
                branchAndBound(listaDeLojas, solucao, index + 1, consumoAtual, lb);
        }
        System.out.println(solucao);
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
            for (int i = 0; i < lojaDeColeta.destinos.size(); i++) {
                if(cargaAtual < CAPACIDADE_CAMINHAO) {
                    listaDeProdutos.add(lojaDeColeta.destinos.get(i));
                    lojaDeColeta.destinos.remove(i);
                    produtos.remove(i);
                    i--;
                    cargaAtual++;
                }
            }
        }
        return cargaAtual;
    }
}