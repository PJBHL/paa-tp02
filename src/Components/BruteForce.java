/**
 * Componentes do grupo:
 * João Pedro Lobato de Pinnho
 * Lucas Carvalho da Luz.
 */

package Components;
import java.util.*;

/** 
 * Classe responsável por gerar as permutações possíveis (n - 1)!
 * A loja matriz é excluída da lista de permutações, já que é inevitávelmente de lá que o caminhão sai.
 */
public class BruteForce {
    // Guardar a melhor solução.
    public static ArrayList<Integer> melhorPermutacao;
    public static double menorConsumo;

    /**
     * Método para gerar todas as permutações (caminhos) possíveis da lista de lojas.
     * @param lojas - lista de lojas.
     * @return - uma lista onde cada index é uma permutação.
     */
    public static ArrayList<ArrayList<Integer>> gerarPermutacoes(ArrayList<Loja> lojas) {
        ArrayList<ArrayList<Integer>> permutacoes = new ArrayList<>();
        ArrayList<Integer> index = Loja.getStoreList(lojas);

        permutacoes(index, 0, permutacoes);

        return permutacoes;
    }

    /**
     * Método recursivo para geração das permutações.
     * Baseado na troca de elementos e index passado por parametro.
     * @param permutacao - permutacao i.
     * @param index - index para troca de elementos de forma recursiva.
     * @param todasPermutacoes - salva neste parâmetro a lista onde cada index é uma permutacao.
     */
    public static void permutacoes(ArrayList<Integer> permutacao, int index, ArrayList<ArrayList<Integer>> todasPermutacoes) {
        // Condição de parada. Chegar aqui significa que uma permutacao foi gerada.
        if(index == permutacao.size() - 1)
            todasPermutacoes.add(new ArrayList<>(permutacao));
        else {
            for(int i = index; i < permutacao.size(); i++) {
                Collections.swap(permutacao, index, i);
                permutacoes(permutacao, index + 1, todasPermutacoes);
                Collections.swap(permutacao, index, i);
            }
        }
    }

    /**
     * Método de força bruta para encontrar o melhor caminho (válido) dentre todas as permutações possíveis.
     * Não é inteligente, isto é, avalia permutações que também não são válidas por natureza.
     * Guarda o valor do consumo e da permutação caso sejam válidos e continua comparando até encontrar o melhor.
     * @param lojas - lista de lojas para encontrar o melhor caminho.
     */
    public static void forcaBruta(ArrayList<Loja> lojas) {
        melhorPermutacao = null;
        menorConsumo = Double.MAX_VALUE;

        ArrayList<ArrayList<Integer>> permutacoes = gerarPermutacoes(lojas);

        for (ArrayList<Integer> permutacao : permutacoes) {
            double consumo = calcularConsumoPermutacao(lojas, permutacao);
            if(consumo < menorConsumo) {
                menorConsumo = consumo;
                melhorPermutacao = permutacao;
            }
        }
    }

    /**
     * Calcula o consumo de uma permutacao específica e verifica se é válida ou não.
     * @param listaLoja - lista de lojas para calculos de distância.
     * @param caminho - permutacao de calculo.
     * @return - consumo cálculado. Infinito caso inválido.
     */
    public static double calcularConsumoPermutacao(ArrayList<Loja> listaLoja, ArrayList<Integer> caminho) {
        // Clonar para não danificar lista para cálculo de outras permutações.
        ArrayList<Loja> listaLojaCopy = Loja.clonarListaLoja(listaLoja);
        double consumoDaPermutacao = 0.0;
        Caminhao caminhao = new Caminhao();
        double rendimento = 10.0; // rendimento começa com 10.

        // Adicinando origem e destino na permutacao (matriz).
        caminho.add(0, 0);
        caminho.add(0);

        for(int indexLoja = 0; indexLoja < caminho.size() - 1; indexLoja++) {
            Loja currentStore = listaLojaCopy.get(caminho.get(indexLoja));
            Loja nextStore = listaLojaCopy.get(caminho.get(indexLoja + 1));
            
            // Tenta entregar primeiro para depois coletar possíveis produtos e atualiza rendimento.
            Caminhao.entregarProdutos(currentStore, caminhao);
            Caminhao.coletarProdutos(currentStore, caminhao);
            rendimento = Caminhao.getRendimento();

            // Calculo de distancia 
            double distancia = Caminhao.calcularDistancia(currentStore, nextStore);
            double consumoDeTrajeto = distancia / rendimento;
            consumoDaPermutacao += consumoDeTrajeto;
        }

        // Verifica a validade do caminho. Se restarem produtos no caminhão ou na lista de lojas, o caminho é inválido.
        if(Caminhao.getCarga() != 0 || Loja.restouProdutos(listaLojaCopy))
            consumoDaPermutacao = Double.MAX_VALUE;

        return consumoDaPermutacao;
    }

    public static ArrayList<Integer> getMelhorPermutacao() {
        return new ArrayList<>(melhorPermutacao);
    }
    
    public static double getMenorConsumo() {
        return menorConsumo;
    }
}
