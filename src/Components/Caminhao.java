package Components;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe responsável pela coleta, entrega, carga e restrições do problema.
 * Um caminhão pode entregar e coletar produtos em 'n' lojas.
 * Ele tem uma carga máxima, definida na interface do programa.
 * Tem um rendimento correspondente há fórmula:
 * rendimento = 10.0 - (cargaAtual * 0.5).
 */
public class Caminhao {
    public static int CAPACIDADE_CAMINHAO;
    private static final double CONSUMO_POR_PRODUTO = 0.5;
    private static int cargaAtual;
    public ArrayList<Integer> produtos;

    public Caminhao() {
        this.produtos = new ArrayList<>();
        cargaAtual = 0;
    }
    
    /**
     * Método para entregar produtos em uma loja.
     * Uma entrega remove o produto de uma loja designada do caminhão, 
     * atualiza a carga atual do caminhão e consequentemente altera seu rendimento.
     * @param lojaDeEntrega - loja de controle.
     * @return - lista com os produtos entregues (útil para backtracking no método de branch and bound).
     */
    public static ArrayList<Integer> entregarProdutos(Loja lojaDeEntrega, Caminhao caminhao) {
        ArrayList<Integer> produtosEntregues = new ArrayList<>();
        // Se o caminhao possui a loja, uma entrega deve ser feita (remover do caminhao).
        if(caminhao.produtos.contains(lojaDeEntrega.id)) {
            caminhao.produtos.removeAll(Collections.singleton(lojaDeEntrega.id));
            produtosEntregues.add(lojaDeEntrega.id);
            cargaAtual = caminhao.produtos.size();
        }
        
        return produtosEntregues;
    }

    /**
     * Método para coletar produtos de uma loja.
     * Uma coleta remove os destinos da loja de coleta e os adiciona no caminhão,
     * atualiza a carga atual do caminhão e consequentemente altera seu rendimento.
     * Entretanto, uma coleta é restringida pela capacidade máxima do caminhão.
     * @param lojaDeColeta - loja de controle.
     * @return - lista com os produtos coletados (útil para backtracking no método de branch and bound).
     */
    public static ArrayList<Integer> coletarProdutos(Loja lojaDeColeta, Caminhao caminhao) {
        ArrayList<Integer> produtosColetados = new ArrayList<>();

        if(!lojaDeColeta.destinos.isEmpty()) {
            for(int i = 0; i < lojaDeColeta.destinos.size(); i++) {
                // Essa condição é apenas para o força bruta. O branch and bound já verifica se poderia entrar na loja antes.
                if(cargaAtual < CAPACIDADE_CAMINHAO) {
                    caminhao.produtos.add(lojaDeColeta.destinos.get(i));
                    produtosColetados.add(lojaDeColeta.destinos.get(i));
                    lojaDeColeta.destinos.remove(i);
                    i--;
                    cargaAtual++;
                }
            }
        }

        return produtosColetados;
    }

    /**
     * Método para devolver os produtos do caminhão para uma loja.
     * Consequentemente, os produtos são removidos do caminhão e sua carga é atualizada.
     * Útil no backtracking.
     * @param lojaDevolucao - loja que terá os produtos devolvidos.
     * @param produtos - lista de produtos que serão devolvidos.
     */
    public static void devolverProdutos(Loja lojaDevolucao, ArrayList<Integer> produtos, Caminhao caminhao) {
        lojaDevolucao.destinos.addAll(produtos);
        caminhao.produtos.removeAll(produtos);
        cargaAtual = caminhao.produtos.size();
    }

    /**
     * Método para adicionar produtos no caminhão fora de uma entrega.
     * @param caminhao - caminhão utilizado.
     * @param produtos - produtos a serem adicionados.
     */
    public static void adicionarProdutos(Caminhao caminhao, ArrayList<Integer> produtos) {
        caminhao.produtos.addAll(produtos);
        cargaAtual = caminhao.produtos.size();
    }

    /**
     * Método para calcular a distância entre duas lojas.
     */
    public static double calcularDistancia(Loja loja1, Loja loja2) {
        return Math.sqrt(Math.pow(loja2.x - loja1.x, 2) + Math.pow(loja2.y - loja1.y, 2));
    }

    /**
     * Método para atualizar o rendimento do caminhão.
     * Seu rendimento é atualizado após uma entrega, coleta ou devolução. Ou seja, quando sua carga é alterada.
     */
    public static double getRendimento() {
        return (10.0 - (cargaAtual * CONSUMO_POR_PRODUTO));
    }

    public static int getCarga() {
        return cargaAtual;
    }

    /**
     * Método para disponibilizar logs da execução de uma das técnicas.
     * @param melhorPermutacao - melhor permutação que força bruta ou branch and bound encontrou.
     * @param menorConsumo - menor consumo dessa melhor permutação.
     */
    public static void logs(ArrayList<Integer> melhorPermutacao, double menorConsumo) {
        System.out.println("--Logs de Execução--");
        System.out.println("Melhor permutação encontrada: " + melhorPermutacao);
        System.out.println("Consumo da permutação: " + menorConsumo);
        System.out.println("Tempo em ms: " + (TelaInicial.end - TelaInicial.start));
        System.out.println("-------------------------------");
    }
}
