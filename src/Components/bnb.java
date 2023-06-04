package Components;
import java.util.*;

public class bnb {
    public static ArrayList<Integer> todosProdutos;
    public static ArrayList<Integer> melhorPermutacao;
    public static double menorConsumo;
    // Caminhão:
    public static ArrayList<Integer> produtosColetados;
    public static int capacidadeCaminhao = 2;
    public static double rendimento = 10.0;
    public static int cargaAtual;

    // Controle de entrega e coleta.
    // public static ArrayList<Integer> entregaIndex;
    // public static ArrayList<Integer> coletaIndex;
    
    public static ArrayList<Integer> encontrarMelhorPermutacao(ArrayList<Loja> listaLojas) {
        ArrayList<Integer> permutacaoAtual = new ArrayList<>();
        ArrayList<Integer> lojasDisponiveis = new ArrayList<>();
        todosProdutos = Loja.getProductList(listaLojas);
        produtosColetados = new ArrayList<>();
        menorConsumo = Double.MAX_VALUE;
        // entregaIndex = new ArrayList<>();
        // coletaIndex = new ArrayList<>();
        
        // permutacaoAtual.add(0);
        for(int i = 0; i < listaLojas.size(); i++)
            lojasDisponiveis.add(i);

        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, 0.0);

        return melhorPermutacao;
    }

    public static void branchNbound(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacaoAtual, ArrayList<Integer> lojasDisponiveis, double consumoAtual) {
        int lojaAtual = lojasDisponiveis.remove(0);
        permutacaoAtual.add(lojaAtual);
        Loja currentStore = listaLojas.get(lojaAtual);
        ArrayList<Integer> entregaIndex = entregarProdutos(produtosColetados, currentStore);
        ArrayList<Integer> coletaIndex = coletarProdutos(produtosColetados, currentStore);
        rendimento = 10.0 - (cargaAtual * 0.5);
        if(lojasDisponiveis.isEmpty()) {
            // permutacaoAtual.add(0);
            // rendimento = 10.0 - (cargaAtual * 0.5);
            // double distancia = Consumo.calcularDistancia(nextStore.x, nextStore.y, currentStore.x, currentStore.y);
            // double consumoDeViagemAtual = distancia / rendimento;
            // consumoAtual += consumoDeViagemAtual;
            if(consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorPermutacao = new ArrayList<>(permutacaoAtual);
                System.out.println(melhorPermutacao);
            }
        } else {
            for(int i = 0; i < lojasDisponiveis.size(); i++) {
                // permutacaoAtual.get(permutacaoAtual.size() - 1);
                // Verifica se a loja atual está na lista de destinos.
                if(!todosProdutos.contains(i)) {
                    Loja nextStore = listaLojas.get(lojasDisponiveis.get(i));
                    
                    double distancia = Consumo.calcularDistancia(nextStore.x, nextStore.y, currentStore.x, currentStore.y);
                    double consumoDeViagemAtual = distancia / rendimento;
                    consumoAtual += consumoDeViagemAtual;
                    
                    if(consumoAtual < menorConsumo && currentStore.destinos.size() == 0) {
                        // permutacaoAtual.add(lojasDisponiveis.get(i));
                        // lojasDisponiveis.remove(i);
                        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, consumoAtual);
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver entregaIndex loja anterior também.
                        devolverProdutos(currentStore, todosProdutos, i, coletaIndex, entregaIndex);
                    } else {
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver ao caminhão também.
                        devolverProdutos(currentStore, todosProdutos, i, coletaIndex, entregaIndex);
                        break;
                    }

                    // lojasDisponiveis.add(0 ,currentStore.id);
                    // permutacaoAtual.remove(currentStore.id);
                }
            }
            lojasDisponiveis.add(0 ,currentStore.id);
            permutacaoAtual.remove(currentStore.id);
        }
    }

    public static ArrayList<Integer> entregarProdutos(ArrayList<Integer> produtos, Loja lojaDeEntrega) {
        ArrayList<Integer> entregues = new ArrayList<>();
        if(produtos.contains(lojaDeEntrega.id)) {
            produtos.removeAll(Collections.singleton(lojaDeEntrega.id));
            entregues.add(lojaDeEntrega.id);
            cargaAtual = produtos.size();
        }
        return entregues;
    }

    public static ArrayList<Integer> coletarProdutos(ArrayList<Integer> produtos, Loja lojaDeColeta) {
        ArrayList<Integer> coletados = new ArrayList<>();
        if(!lojaDeColeta.destinos.isEmpty()) {
            for(int i = 0; i < lojaDeColeta.destinos.size(); i++) {
                if(cargaAtual < capacidadeCaminhao) {
                    produtos.add(lojaDeColeta.destinos.get(i));
                    coletados.add(lojaDeColeta.destinos.get(i));
                    todosProdutos.remove(lojaDeColeta.destinos.get(i));
                    lojaDeColeta.destinos.remove(i);
                    i--;
                    cargaAtual++;
                }
            }
        }
        return coletados;
    }

    public static void devolverProdutos(Loja devolverLoja, ArrayList<Integer> devolverProduto, int i, ArrayList<Integer> coleta, ArrayList<Integer> entrega) {
        // Mudar depois para devolver todos os produtos. (foda-se a ordem dos destinos).
        devolverLoja.destinos.addAll(coleta);
        devolverProduto.addAll(coleta);
        produtosColetados.removeAll(coleta);
        produtosColetados.addAll(entrega);
        cargaAtual = produtosColetados.size();
    }
}
