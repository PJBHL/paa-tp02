/**
 * Componentes do grupo:
 * João Pedro Lobato de Pinnho
 * Lucas Carvalho da Luz.
 */

package Components;
import java.util.*;
import java.io.*;

/** 
 * Classe responsável pela estrutura de loja e leitura de arquivo.
 */
public class Loja {
    int id; // Número de identificação de loja.
    int x, y; // Coordenadas de uma loja.
    ArrayList<Integer> destinos; // Destinos de determinada loja.

    public Loja(int id, int x, int y, ArrayList<Integer> destinos) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.destinos = destinos;
    }

    /** 
     * Método de clone
     * @param other - clone.
     */
    public Loja(Loja other) {
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;
        this.destinos = new ArrayList<>(other.destinos);
    }

    public Loja clone() {
        return new Loja(this);
    }

    /**
     * Método para clonar uma lista de lojas.
     * Útil para não usar a referência original durante operações de cálculo de rendimento e consumo.
     * @param listaLoja - lista de lojas para clonar.
     * @return - lista clonada de lojas.
     */
    public static ArrayList<Loja> clonarListaLoja(ArrayList<Loja> listaLoja) {
        ArrayList<Loja> listaLojaCopy = new ArrayList<>();
        
        for(Loja loja : listaLoja) 
            listaLojaCopy.add(new Loja(loja));

        return listaLojaCopy;
    }

    /**
     * Método para gerar uma lista com todos os produtos de todas as lojas.
     * @param listaDeLojas - lista de loja para extrair lista de produtos.
     * @return - lista com produtos das lojas.
     */
    public static ArrayList<Integer> getProductList(ArrayList<Loja> listaDeLojas) {
        ArrayList<Integer> todosProdutos = new ArrayList<>();

        for(Loja loja : listaDeLojas)
            todosProdutos.addAll(loja.destinos);

        return todosProdutos;
    }

    /**
     * Método para verificar se há produtos (destinos) restantes em uma lista de Lojas.
     * Útil para operações quando há entrega e coleta de produtos.
     * Se sobrar produto em uma loja significa que o caminho é inválido.
     */
    public static boolean restouProdutos(ArrayList<Loja> listaLoja) {
        for (Loja loja : listaLoja) 
            if(!loja.destinos.isEmpty())
                return true;
        
        return false;
    }
    
    /**
     * Método para retornar uma lista de inteiros de IDs das lojas. Excluindo a matriz.
     * Útil para a geração de permutações.
     * @param lojas - lista de lojas.
     * @return - lista com lojas 1 ... n.
     */
    public static ArrayList<Integer> getStoreList(ArrayList<Loja> lojas) {
        ArrayList<Integer> indexList = new ArrayList<>();

        for(int i = 1; i < lojas.size(); i++)
            indexList.add(i);

        return indexList;
    }

    /**
     * Método para a leitura de um arquivo de entrada para criar lista de lojas.
     * @param fileName - nome do arquivo de leitura.
     * @return - lista de lojas contida no arquivo.
     */
    public static ArrayList<Loja> lerLojas(String fileName) throws Exception {
        FileReader file = new FileReader(fileName);
        BufferedReader rd = new BufferedReader(file);
        ArrayList<Loja> listaDeLojas = new ArrayList<>();
        String line;

        while((line = rd.readLine()) != null) {
            String[] campos = line.split(" ");

            int id = Integer.parseInt(campos[0]);
            int x  = Integer.parseInt(campos[1]);
            int y  = Integer.parseInt(campos[2]);

            ArrayList<Integer> destinos = new ArrayList<>();
            for(int i = 3; i < campos.length; i++)
                destinos.add(Integer.parseInt(campos[i]));

            Loja lojaI = new Loja(id, x, y, destinos);
            listaDeLojas.add(lojaI);
        }

        rd.close();

        return listaDeLojas;
    }

    /**
     * Método para testar leitura da entrada.
     */
    public static void imprimirLojas(ArrayList<Loja> listaDeLojas) {
        for (Loja loja : listaDeLojas)
            System.out.println(loja.id + " " + loja.x + " " + loja.y + " " + loja.destinos);
        
        System.out.println("\n\n");
    }
}
