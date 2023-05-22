import Components.*;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Loja> listaDeLojas;
        listaDeLojas = Loja.lerLojas("entradas/testePermu.txt");
        Loja.imprimirLojas(listaDeLojas);
        ArrayList<ArrayList<Integer>> permutacoes = BruteForce.gerarPermutacoes(listaDeLojas);
        BruteForce.imprimirPermutacoes(permutacoes);
    }
}