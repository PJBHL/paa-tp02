package Components;
import java.util.*;

public class bnb {
    public static ArrayList<Integer> todosProdutos;
    public static ArrayList<Integer> melhorPermutacao;
    public static double menorConsumo;
    // Caminhão:
    public static ArrayList<Integer> caminhao;
    public static int capacidadeCaminhao = 3;
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
            // rendimento = 10.0 - (cargaAtual * 0.5);
            double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
            double consumoDeViagemAtual = distancia / rendimento;
            consumoAtual += consumoDeViagemAtual;
            if(consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorPermutacao = new ArrayList<>(permutacaoAtual);
                // System.out.println(melhorPermutacao);
                // System.out.printf("Truck r: %.4f", consumoAtual);
                // System.out.println(" " + melhorPermutacao);
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
                if(!todosProdutos.contains(previousStore.id)) {
                    // rendimento = 10.0 - (cargaAtual * 0.5);
                    int origemX = currentStore.x;
                    int destinoX = previousStore.x;
                    int origemY = currentStore.y;
                    int destinoY = previousStore.y;
                    double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
                    double consumoDeViagemAtual = distancia / rendimento;
                    consumoAtual += consumoDeViagemAtual;
                    // System.out.println("distancia: " + distancia + " | ConsumoDeViagem: " + consumoDeViagemAtual + " | Rendimento: " + rendimento + " Consumo do caminho: " + consumoAtual);
                    
                    entregaIndex = entregarProdutos(caminhao, currentStore);
                    rendimento = 10.0 - (cargaAtual * 0.5);
                    if(consumoAtual < menorConsumo && ((currentStore.destinos.size() + caminhao.size()) <= capacidadeCaminhao)) {
                        coletaIndex = coletarProdutos(caminhao, currentStore);
                        rendimento = 10.0 - (cargaAtual * 0.5);
                        permutacaoAtual.add(lojaAtual);
                        lojasDisponiveis.remove(i);
                        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, consumoAtual, contador + 1);
                        // Iniciar back tracking.
                        // Diminuir consumo calculado. (consumoAtual += consumoDeViagemAtual;).
                        
                        consumoAtual -= consumoDeViagemAtual;
                        // Adiciona produtos ao caminhao. (entregaIndex = entregarProdutos(caminhao, currentStore);).
                        // Remove do caminhao, adiciona a todos produtos, coloca de volta na loja de coleta (coletaIndex = coletarProdutos(caminhao, currentStore);).
                        // Remove da permutacaoAtual (permutacaoAtual.add(lojaAtual);).
                        permutacaoAtual.remove(contador + 1);
                        lojasDisponiveis.add(0, currentStore.id);
                        // Adiciona as lojasDisponiveis novamente. (lojasDisponiveis.remove(i);).
                        // 0 1 3 2 5 4
                        caminhao.addAll(entregaIndex);
                        devolverProdutos(currentStore, coletaIndex);
                        // permutacaoAtual.remove(permutacaoAtual.indexOf(currentStore.id));
                        // todosProdutos.addAll(entregaIndex);
                        // retornar produtos para loja / remover do caminhão.
                        
                        
                        
                        // devolverProdutos(previousStore, todosProdutos, i, entregaIndex, entregaIndex);
                        // entregaIndex = entregarProdutos(caminhao, currentStore);
                        // Calculo de rendimento Inutil.
                        rendimento = 10.0 - (cargaAtual * 0.5);
                    } else {
                        consumoAtual -= consumoDeViagemAtual;
                        // Devolver ao caminhão também.
                        caminhao.addAll(entregaIndex);
                        cargaAtual = caminhao.size();
                        // rendimento = 10.0 - (cargaAtual * 0.5);
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

    public static void devolverProdutos(Loja devolverLoja, ArrayList<Integer> produtos) {
        // Mudar depois para devolver todos os produtos. (foda-se a ordem dos destinos)
        devolverLoja.destinos.addAll(produtos);
        todosProdutos.addAll(produtos);
        caminhao.removeAll(produtos);
        cargaAtual = caminhao.size();
    }
}