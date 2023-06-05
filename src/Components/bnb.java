package Components;
import java.util.*;

public class bnb {
    public static ArrayList<Integer> todosProdutos;
    public static ArrayList<Integer> melhorPermutacao;
    public static double menorConsumo;
    // Caminhão:
    public static ArrayList<Integer> caminhao;
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
        caminhao = new ArrayList<>();
        menorConsumo = Double.MAX_VALUE;
        // entregaIndex = new ArrayList<>();
        // coletaIndex = new ArrayList<>();
        
        // permutacaoAtual.add(0);
        for(int i = 0; i < listaLojas.size(); i++)
            lojasDisponiveis.add(i);

        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, 0.0, 0);

        return melhorPermutacao;
    }

    public static void branchNbound(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacaoAtual, ArrayList<Integer> lojasDisponiveis, double consumoAtual, int contador) {
        int lojaAtual = lojasDisponiveis.remove(contador);
        permutacaoAtual.add(lojaAtual);
        Loja currentStore = listaLojas.get(lojaAtual);
        ArrayList<Integer> entregaIndex;
        ArrayList<Integer> coletaIndex;
        coletaIndex = coletarProdutos(caminhao, currentStore);
        rendimento = 10.0 - (cargaAtual * 0.5);
        if(lojasDisponiveis.isEmpty()) {
            permutacaoAtual.add(0);
            // 0, 1, 3, 2, 4, 5, 0
            rendimento = 10.0 - (cargaAtual * 0.5);
            double distancia = Consumo.calcularDistancia(listaLojas.get(permutacaoAtual.get(0)).x, listaLojas.get(permutacaoAtual.get(0)).y, currentStore.x, currentStore.y);
            double consumoDeViagemAtual = distancia / rendimento;
            consumoAtual += consumoDeViagemAtual;
            if(consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorPermutacao = new ArrayList<>(permutacaoAtual);
                System.out.println(melhorPermutacao);
            }
            permutacaoAtual.remove(permutacaoAtual.size() - 1);
        } else {
            for(int i = 0; i < lojasDisponiveis.size(); i++) {
                // permutacaoAtual.get(permutacaoAtual.size() - 1);
                // Verifica se a loja atual está na lista de destinos.
                Loja nextStore = listaLojas.get(lojasDisponiveis.get(i));
                if(!todosProdutos.contains(nextStore.id)) {
                    
                    double distancia = Consumo.calcularDistancia(nextStore.x, nextStore.y, currentStore.x, currentStore.y);
                    double consumoDeViagemAtual = distancia / rendimento;
                    consumoAtual += consumoDeViagemAtual;
                    
                    entregaIndex = entregarProdutos(caminhao, nextStore);
                    rendimento = 10.0 - (cargaAtual * 0.5);
                    if(consumoAtual < menorConsumo && nextStore.destinos.size() + caminhao.size() <= capacidadeCaminhao) {
                        // permutacaoAtual.add(lojasDisponiveis.get(i));
                        // lojasDisponiveis.remove(i);
                        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, consumoAtual, i);
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver entregaIndex loja anterior também.
                        devolverProdutos(currentStore, todosProdutos, i, coletaIndex, entregaIndex);
                        entregaIndex = entregarProdutos(caminhao, currentStore);
                        rendimento = 10.0 - (cargaAtual * 0.5);
                    } else {
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver ao caminhão também.
                        caminhao.addAll(entregaIndex);
                        continue;
                    }
                }
            }
        }
        lojasDisponiveis.add(0, currentStore.id);
        permutacaoAtual.remove(permutacaoAtual.indexOf(currentStore.id));
        todosProdutos.addAll(coletaIndex);
    }

    public static ArrayList<Integer> entregarProdutos(ArrayList<Integer> produtos, Loja lojaDeEntrega) {
        ArrayList<Integer> entregues = new ArrayList<>();
        if(produtos.contains(lojaDeEntrega.id)) {
            // remove do caminhão:
            produtos.removeAll(Collections.singleton(lojaDeEntrega.id));
            // adicionar todos os produtos na volta. (não na ida).
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
        // todosProdutos.addAll(entrega);
        caminhao.addAll(entrega);
        cargaAtual = caminhao.size();
    }
}