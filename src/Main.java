import Components.*;
import java.util.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // SwingUtilities.invokeLater(new Runnable() {
        //     @Override
        //     public void run() {
        //         new TelaInicial();
        //     }
        // });
        ArrayList<Loja> listaDeLojas = Loja.lerLojas("entradas/teste1.txt");

        ArrayList<Integer> melhorCaminho = bnb.encontrarMelhorPermutacao(listaDeLojas);

        if (melhorCaminho != null) {
            System.out.println("Melhor caminho encontrado: " + melhorCaminho);
            System.out.println("Menor consumo: " + BranchAndBound.menorConsumo);
        } else {
            System.out.println("Nenhum caminho válido encontrado.");
        }
    }
}