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
        ArrayList<Loja> listaDeLojas = new ArrayList<>();
        listaDeLojas = Loja.lerLojas("entradas/teste1.txt");
        System.out.println(Loja.getProductList(listaDeLojas));
        BranchAndBound.N = listaDeLojas.size();
        BranchAndBound.produtos = Loja.getProductList(listaDeLojas);
        ArrayList<Integer> solucao = new ArrayList<>();
        BranchAndBound.branchAndBound(listaDeLojas, solucao, 0, 0, 0);
    }
}