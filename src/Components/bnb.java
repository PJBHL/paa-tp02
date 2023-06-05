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
        
        for(int i = 1; i < listaLojas.size(); i++)
            lojasDisponiveis.add(i);
        permutacaoAtual.add(0, 0);

        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, 0.0, 0);

        return melhorPermutacao;
    }

    public static ArrayList<Integer> branchNbound(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacaoAtual, ArrayList<Integer> lojasDisponiveis, double consumoAtual, int contador) {
        ArrayList<Integer> entregaIndex;
        ArrayList<Integer> coletaIndex;
        if(lojasDisponiveis.isEmpty()) {
            // 0, 1, 3, 2, 5, 4, 0
            int origemX = listaLojas.get(permutacaoAtual.get(permutacaoAtual.size() - 1)).x;
            int origemY = listaLojas.get(permutacaoAtual.get(permutacaoAtual.size() - 1)).y;
            int destinoX = listaLojas.get(permutacaoAtual.get(0)).x;
            int destinoY = listaLojas.get(permutacaoAtual.get(0)).y;
            rendimento = 10.0 - (cargaAtual * 0.5);
            double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
            double consumoDeViagemAtual = distancia / rendimento;
            consumoAtual += consumoDeViagemAtual;
            if(consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorPermutacao = new ArrayList<>(permutacaoAtual);
                // System.out.println(melhorPermutacao);
                System.out.println("distancia: " + distancia + " | ConsumoDeViagem: " + consumoDeViagemAtual + " | Rendimento: " + rendimento + " Consumo do caminho: " + consumoAtual);
                return melhorPermutacao;
            }
        } else {
            for(int i = 0; i < lojasDisponiveis.size(); i++) {
                int lojaAtual = lojasDisponiveis.get(i);
                // permutacaoAtual.add(lojaAtual);
                Loja currentStore = listaLojas.get(lojaAtual);
                // permutacaoAtual.get(permutacaoAtual.size() - 1);
                // Verifica se a loja atual está na lista de destinos.
                Loja previousStore = listaLojas.get(permutacaoAtual.get(contador));
                if(!todosProdutos.contains(currentStore.id)) {
                    rendimento = 10.0 - (cargaAtual * 0.5);
                    double distancia = Consumo.calcularDistancia(previousStore.x, previousStore.y, currentStore.x, currentStore.y);
                    double consumoDeViagemAtual = distancia / rendimento;
                    consumoAtual += consumoDeViagemAtual;
                    System.out.println("distancia: " + distancia + " | ConsumoDeViagem: " + consumoDeViagemAtual + " | Rendimento: " + rendimento + " Consumo do caminho: " + consumoAtual);
                    
                    entregaIndex = entregarProdutos(caminhao, currentStore);
                    if(consumoAtual < menorConsumo && (currentStore.destinos.size() + caminhao.size()) <= capacidadeCaminhao) {
                        coletaIndex = coletarProdutos(caminhao, currentStore);
                        permutacaoAtual.add(lojaAtual);
                        lojasDisponiveis.remove(i);
                        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, consumoAtual, contador + 1);
                        // Iniciar back tracking.
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver entregaIndex loja anterior também.
                        permutacaoAtual.remove(contador + 1);
                        lojasDisponiveis.add(0, currentStore.id);
                        // permutacaoAtual.remove(permutacaoAtual.indexOf(currentStore.id));
                        // todosProdutos.addAll(entregaIndex);
                        // retornar produtos para loja / remover do caminhão.
                        
                        caminhao.addAll(entregaIndex);
                        cargaAtual = caminhao.size();
                        
                        // devolverProdutos(previousStore, todosProdutos, i, entregaIndex, entregaIndex);
                        // entregaIndex = entregarProdutos(caminhao, currentStore);
                        rendimento = 10.0 - (cargaAtual * 0.5);
                    } else {
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver ao caminhão também.
                        caminhao.addAll(entregaIndex);
                        cargaAtual = caminhao.size();
                        rendimento = 10.0 - (cargaAtual * 0.5);
                    }
                }
            }
        }
        return melhorPermutacao;
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
        // devolverLoja.destinos.addAll(coleta);
        // todosProdutos.addAll(entrega);
        caminhao.addAll(entrega);
        // caminhao.removeAll(coleta);
        cargaAtual = caminhao.size();
    }
}