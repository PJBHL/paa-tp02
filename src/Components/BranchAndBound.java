/**
 * Componentes do grupo:
 * João Pedro Lobato de Pinnho
 * Lucas Carvalho da Luz.
 */

package Components;
import java.util.*;

public class BranchAndBound {
    // Para ontrole do método.
    public static ArrayList<Integer> todosProdutos;
    public static ArrayList<Integer> melhorPermutacao;
    public static double menorConsumo;
    public static Caminhao caminhao;
    public static double rendimento = 10.0;

    /**
     * Método para encontrar o melhor caminho usando a técnica de branch and bound.
     * Inicializa variáveis e chama o método em si.
     * @param listaLojas - lista de lojas para encontrar o menor caminho.
     * @return - melhor caminho.
     */
    public static ArrayList<Integer> encontrarMelhorPermutacao(ArrayList<Loja> listaLojas) {
        ArrayList<Integer> permutacaoAtual = new ArrayList<>();
        // Lista de inteiros para controlar as lojas disponiveis e não precisar de excluir da lista original de lojas.
        ArrayList<Integer> lojasDisponiveis = new ArrayList<>();
        todosProdutos = Loja.getProductList(listaLojas);
        caminhao = new Caminhao();
        menorConsumo = Double.MAX_VALUE;

        // Inicializa lojas disponiveis começando em 1.
        for(int i = 1; i < listaLojas.size(); i++)
            lojasDisponiveis.add(i);

        // Pré adiciona 0 a permutação de resultado (partindo da matriz).
        permutacaoAtual.add(0, 0);

        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, 0.0, 0);

        return melhorPermutacao;
    }

    /**
     * Encontra o melhor caminho utilizando a técnica de branch and bound.
     * @param listaLojas - lista de lojas (objetos): lojas 0 ... n lidas do arquivo.
     * @param permutacaoAtual - permutação que será construída na solução.
     * @param lojasDisponiveis - lista de lojas disponíveis (inteiros).
     * @param consumoAtual - consumo construído durante uma solução.
     * @param index - index para controle de acesso a loja.
     * @return - melhor caminho encontrado.
     */
    public static ArrayList<Integer> branchNbound(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacaoAtual, ArrayList<Integer> lojasDisponiveis, double consumoAtual, int index) {
        // Lista de controle de entrega e coleta.
        ArrayList<Integer> produtosEntregues;
        ArrayList<Integer> produtosColetados;

        // Condição de parada: Não há mais lojas disponíveis, ou seja, um caminho foi encontrado.
        if(lojasDisponiveis.isEmpty()) {
            // Se encontrou um caminho é necessário retornar a matriz: distância do último item da permutação para a primeira loja (matriz).
            double distancia = Caminhao.calcularDistancia(listaLojas.get(permutacaoAtual.get(permutacaoAtual.size() - 1)), listaLojas.get(permutacaoAtual.get(0)));
            double consumoDeTrajeto = distancia / 10.0;
            consumoAtual += consumoDeTrajeto;
            // Se a solução atual é a melhor do que a melhor encontrada até agora, guarda solução atual.
            if(consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorPermutacao = new ArrayList<>(permutacaoAtual);
                return melhorPermutacao;
            }
        } else {
            for(int i = 0; i < lojasDisponiveis.size(); i++) {
                int lojaAtual = lojasDisponiveis.get(i);
                Loja currentStore = listaLojas.get(lojaAtual);
                Loja previousStore = listaLojas.get(permutacaoAtual.get(index));

                // Restrição do problema (critério de poda): Se a loja que estou caminhando tem um produto que ainda não coletei, o caminho é inválido.
                if(!todosProdutos.contains(currentStore.id)) {
                    produtosEntregues = Caminhao.entregarProdutos(currentStore.clone(), caminhao); // simulação de entrega para calcular a carga.
                    // Outra restrição do problema (critério de poda): Carga máxima do caminhão é violada.
                    if(consumoAtual < menorConsumo && ((currentStore.destinos.size() + caminhao.produtos.size()) <= Caminhao.CAPACIDADE_CAMINHAO)) {
                        Caminhao.adicionarProdutos(caminhao, produtosEntregues);
                        // É possível prosseguir, adicionar valores a solução e chamar novamente o método.
                        permutacaoAtual.add(lojaAtual);
                        lojasDisponiveis.remove(i);

                        double distancia = Caminhao.calcularDistancia(previousStore, currentStore);
                        rendimento = Caminhao.getRendimento();
                        double consumoDeTrajeto = distancia / rendimento;
                        consumoAtual += consumoDeTrajeto;

                        produtosEntregues = Caminhao.entregarProdutos(currentStore.clone(), caminhao);
                        produtosColetados = Caminhao.coletarProdutos(currentStore.clone(), caminhao);
                        todosProdutos.removeAll(produtosColetados);

                        branchNbound(listaLojas, permutacaoAtual, lojasDisponiveis, consumoAtual, index + 1);
                        // Tudo o que foi feito deve ser desfeito para o backtraking.

                        consumoAtual -= consumoDeTrajeto;
                        permutacaoAtual.remove(index + 1);
                        lojasDisponiveis.add(i, currentStore.id); // Adicionado na posição i, o 'for' faz o controle do próximo index aqui.

                        todosProdutos.addAll(produtosColetados); // Adiciona novamente os produtos coletados a lista de produtos para controle da primeira restrição.
                        caminhao.produtos.removeAll(produtosColetados); // Quando os produtos forem coletados, a loja será tirada da solução. Logo, tira-se do caminhão.
                        Caminhao.adicionarProdutos(caminhao, produtosEntregues); // devolve os produtos entregues para o caminhao.
                    } else {
                        Caminhao.adicionarProdutos(caminhao, produtosEntregues); // Não é possível prosseguir com essa loja, tentar para a próxima.
                    }
                }
            }
        }
        return melhorPermutacao;
    }
}
