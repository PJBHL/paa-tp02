package Components;
import java.util.*;

public class BruteForce {
    /**
     * Metodo para pegar somente a lista de inteiros de uma lista de loja. Ou seja, os indices.
     * Útil para gerar as permutações começa em 1 porque a matriz é desconsiderada.
     * @param lojas
     * @return
     */
    public static ArrayList<Integer> getList(ArrayList<Loja> lojas) {
        ArrayList<Integer> indexList = new ArrayList<>();
        
        for(int i = 1; i < lojas.size(); i++)
            indexList.add(i);

        return indexList;
    }

    /**
     * Metodo para gerar a lista de permutações possíveis das lojas.
     */
    public static ArrayList<ArrayList<Integer>> gerarPermutacoes(ArrayList<Loja> lojas) {
        ArrayList<ArrayList<Integer>> permutacoes = new ArrayList<>();
        ArrayList<Integer> getIndex = getList(lojas);

        permutacoes(getIndex, 0, permutacoes);

        return permutacoes;
    }

    public static void permutacoes(ArrayList<Integer> elemento, int index, ArrayList<ArrayList<Integer>> permutacoes) {
        if(index == elemento.size() - 1)
            permutacoes.add(new ArrayList<>(elemento));
        else {
            for(int i = index; i < elemento.size(); i++) {
                Collections.swap(elemento, index, i);
                permutacoes(elemento, index + 1, permutacoes);
                Collections.swap(elemento, index, i);
            }
        }
    }

    public static void imprimirPermutacoes(ArrayList<ArrayList<Integer>> permutacoes) {
        for (ArrayList<Integer> arrayList : permutacoes) {
            System.out.println(arrayList);
        }
    }
}
