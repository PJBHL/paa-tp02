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
        ArrayList<Loja> listaDeLojas = Loja.lerLojas("entradas/15.txt");

        ArrayList<Integer> melhorCaminho = bnb.encontrarMelhorPermutacao(listaDeLojas);

        if (melhorCaminho != null) {
            System.out.println("Melhor caminho encontrado: " + melhorCaminho);
            System.out.println("Menor consumo: " + bnb.menorConsumo);
        } else {
            System.out.println("Nenhum caminho válido encontrado.");
        }
        // ArrayList<Integer> testPermu = new ArrayList<>();
        // testPermu.add(1);
        // testPermu.add(3);
        // testPermu.add(2);
        // testPermu.add(5);
        // testPermu.add(4);
        // System.out.println(Consumo.calcularConsumoCaminho(listaDeLojas, testPermu));
    }
}