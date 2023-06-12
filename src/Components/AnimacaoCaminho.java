package Components;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

/**
 * Classe para a animação do melhor caminho possível escolhido.
 * Mostra os produtos presentes no caminhão até o momento, consumo, distância, lista de produtos presentes na lista de lojas até o momento.
 */
public class AnimacaoCaminho extends JFrame {
    // Permutação (caminho) que será animado.
    private ArrayList<Loja> listaLojas;
    private ArrayList<Loja> listaLojasCopy;
    private ArrayList<Integer> permutacao;
    private ArrayList<Integer> allProductList;

    private JPanel panel;
    private JLabel statusLabel;
    private int currentStoreIndex;
    private double currentConsumption;
    private double currentDistance;
    private Caminhao caminhao;

    /**
     * Construtor público que inicializa a animação.
     * @param listaLojas - lista de lojas para a animação.
     * @param permutacao - permutação (caminho) que será animado.
     */
    public AnimacaoCaminho(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacao) {
        this.listaLojas = listaLojas;
        this.permutacao = permutacao;
        this.currentStoreIndex = 0;
        this.currentConsumption = 0.0;
        this.listaLojasCopy = Loja.clonarListaLoja(listaLojas);

        setTitle("Animação do Caminho");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawStores(g);
                drawPath(g);
                drawTruck(g);
            }
        };
        panel.setBackground(Color.WHITE);
        add(panel);

        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        statusLabel.setPreferredSize(new Dimension(200, 100));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);

        startAnimation();
    }

    /**
     * Método para desenhar nodos (lojas) no painel.
     * @param g - graphic.
     */
    private void drawStores(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        for (int i = 0; i < listaLojasCopy.size(); i++) {
            Loja loja = listaLojasCopy.get(i);
            // Somando 50 para melhorar a visualização.
            int x = (loja.x + 120);
            int y = (loja.y + 120);

            g.setColor(Color.BLACK);
            g.fillOval(x - 10, y - 10, 8, 8);
            g.setColor(Color.darkGray);
            g.drawString(Integer.toString(i), x - 15, y + 10);
        }
    }

    /**
     * Método para desenhar o caminho entre os nodos (lojas) no painel.
     * @param g - graphic.
     */
    private void drawPath(Graphics g) {
        g.setColor(Color.RED);

        for (int i = 0; i < currentStoreIndex; i++) {
            Loja currentStore = listaLojasCopy.get(permutacao.get(i));
            Loja nextStore = listaLojasCopy.get(permutacao.get(i + 1));

            g.drawLine(currentStore.x + 110, currentStore.y + 110, nextStore.x + 110, nextStore.y + 110);
        }
    }

    /**
     * Método para desenhar o caminhão passando de uma loja para a outra.
     * @param g - graphic.
     */
    private void drawTruck(Graphics g) {
        if (currentStoreIndex == -1 || currentStoreIndex == listaLojas.size())
            return; // Não há nada para desenhar.

        Loja currentStore = listaLojasCopy.get(permutacao.get(currentStoreIndex));
        int x = currentStore.x;
        int y = currentStore.y;

        g.setColor(Color.BLUE);
        g.fillRect((x + 110), (y + 110), 8, 8);
    }

    /**
     * Método para atualizar a legenda.
     */
    private void updateStatusLabel() {
        String caminhaoLabel = "Produtos no Caminhão: " + caminhao.produtos;
        String distanceLabel = String.format("Distância Percorrida: %.2f", currentDistance);
        String consumptionLabel = String.format(" | Consumo Atual: %.2f", currentConsumption);
        String productListLabel = " | Produtos Restantes: " + allProductList;
        statusLabel.setText("<html>" + distanceLabel + consumptionLabel + "<br>" + caminhaoLabel + productListLabel + "</html>");
    }

    /**
     * Método da animação em si. É similar ao cálculo de consumo, atualiza os valores globais na legenda.
     */
    private void startAnimation() {
        caminhao = new Caminhao();
        allProductList = Loja.getProductList(listaLojas);
        Timer timer = new Timer(500, e -> {
            if (currentStoreIndex < permutacao.size() - 1) {
                ArrayList<Integer> produtosColetados;
                double rendimento = 10.0;

                Loja currentStore = listaLojas.get(permutacao.get(currentStoreIndex));
                Loja nextStore = listaLojas.get(permutacao.get(currentStoreIndex + 1));
                
                // Para cálculo de rendimento:
                Caminhao.entregarProdutos(currentStore, caminhao);
                produtosColetados = Caminhao.coletarProdutos(currentStore, caminhao);
                rendimento = Caminhao.getRendimento();

                allProductList.removeAll(produtosColetados);

                double distancia = Caminhao.calcularDistancia(currentStore, nextStore);
                currentDistance += distancia;
                double consumoDeTrajeto = distancia / rendimento;
                currentConsumption += consumoDeTrajeto;

                currentStoreIndex++;
                panel.repaint();
                updateStatusLabel();
            } else
                ((Timer) e.getSource()).stop();
        });
        timer.setInitialDelay(5000);
        timer.start();
    }
}
