package Components;
import java.util.ArrayList;

public class BranchAndBound {
    private static ArrayList<Integer> melhorCaminho;
    public static double menorConsumo;

    public static ArrayList<Integer> encontrarMelhorPermutacao(ArrayList<Loja> listaDeLojas, int k) {
        melhorCaminho = null;
        menorConsumo = Double.MAX_VALUE;

        ArrayList<Integer> caminhoAtual = new ArrayList<>();
        caminhoAtual.add(0); // Adiciona a matriz como ponto de partida
        double consumoAtual = 0.0;
        int cargaAtual = 0;

        backtrack(listaDeLojas, k, caminhoAtual, consumoAtual, cargaAtual);

        return melhorCaminho;
    }

    private static void backtrack(ArrayList<Loja> listaDeLojas, int k, ArrayList<Integer> caminhoAtual,
                                  double consumoAtual, int cargaAtual) {
        if (caminhoAtual.size() == listaDeLojas.size() + 1) { // Verifica se todas as lojas foram visitadas
            if (consumoAtual < menorConsumo) {
                menorConsumo = consumoAtual;
                melhorCaminho = new ArrayList<>(caminhoAtual);
            }
            return;
        }

        int lojaAtual = caminhoAtual.get(caminhoAtual.size() - 1);
        Loja loja = listaDeLojas.get(lojaAtual);

        for (int i = 1; i < listaDeLojas.size(); i++) {
            if (!caminhoAtual.contains(i)) { // Verifica se a loja já foi visitada
                Loja proximaLoja = listaDeLojas.get(i);
                double distancia = calcularDistancia(loja, proximaLoja);
                double consumo = distancia / (10.0 - (cargaAtual * 0.5));

                boolean podeColetar = false;
                boolean podeEntregar = false;

                if (proximaLoja.destinos.contains(lojaAtual)) {
                    if (cargaAtual < k) {
                        cargaAtual++;
                        consumo -= 0.5;
                        podeColetar = true;
                    } else {
                        continue; // Não é possível coletar mais produtos, continue para a próxima loja
                    }
                }

                if (loja.destinos.contains(i)) {
                    if (cargaAtual > 0) {
                        cargaAtual--;
                        consumo -= 0.5;
                        podeEntregar = true;
                    } else {
                        continue; // Não há produtos para entregar, continue para a próxima loja
                    }
                }

                if (consumoAtual + consumo < menorConsumo) {
                    caminhoAtual.add(i);
                    double novoConsumo = consumoAtual + consumo;

                    backtrack(listaDeLojas, k, caminhoAtual, novoConsumo, cargaAtual);

                    caminhoAtual.remove(caminhoAtual.size() - 1); // Desfaz a escolha

                    if (podeEntregar) {
                        cargaAtual++;
                    }

                    if (podeColetar) {
                        cargaAtual--;
                    }
                }
            }
        }
    }

    private static double calcularDistancia(Loja origem, Loja destino) {
        double distancia = Math.sqrt(Math.pow(destino.x - origem.x, 2) + Math.pow(destino.y - origem.y, 2));
        return distancia;
    }
}