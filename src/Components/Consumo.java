package Components;
import java.util.ArrayList;

public class Consumo {
    public static int k = 2; // Capacidade de produtos do caminhão.
    public static final double CONSUMO_POR_PRODUTO = 0.5;

    public static double calcularConsumoAtual(ArrayList<Loja> index, ArrayList<Integer> permutacao) {
        return Double.MAX_VALUE;
    }

    public static boolean entradaInvalida(ArrayList<Loja> listaLoja) {
        boolean valido = true;
        for (Loja loja : listaLoja) {
            if (loja.destinos.size() > k) {
                valido = false;
                break;
            }
        }

        return valido;
    }
}
