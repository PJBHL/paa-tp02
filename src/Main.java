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
        ArrayList<Integer> solucao = new ArrayList<>();
        BranchAndBound.listaDeLojas = Loja.lerLojas("entradas/teste1.txt");
        BranchAndBound.produtos = Loja.getProductList(BranchAndBound.listaDeLojas);
        BranchAndBound.N = BranchAndBound.listaDeLojas.size();
        BranchAndBound.listaLojaCopy = Loja.clonarListaLoja(BranchAndBound.listaDeLojas);
        BranchAndBound.branchAndBound(solucao, 0, 0, 0);
    }
}