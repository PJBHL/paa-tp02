package Components;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimacaoCaminhao extends JFrame {
    private ArrayList<Loja> listaLojas;
    private ArrayList<Integer> permutacao;
    private ArrayList<Loja> listaLojasCopy;

    private JPanel panel;
    private JLabel statusLabel;
    private int currentStoreIndex;
    private double currentConsumption;
    private int currentProductCount;
    private ArrayList<Integer> produtosColetados;

    public AnimacaoCaminhao(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacao) {
        this.listaLojas = listaLojas;
        this.permutacao = permutacao;
        this.currentStoreIndex = 0;
        this.currentConsumption = 0.0;
        this.currentProductCount = 0;
        listaLojasCopy = Loja.clonarListaLoja(listaLojas);

        setTitle("Animação do Caminhão");
        setSize(800, 600);
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
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        statusLabel.setPreferredSize(new Dimension(200, 300));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);

        startAnimation();
    }

    private void drawStores(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        for (int i = 0; i < listaLojasCopy.size(); i++) {
            Loja loja = listaLojasCopy.get(i);
            int x = loja.x * 2;
            int y = loja.y * 2;

            // Caso a coordenada da loja seja y = 0, sua vizualização ficaria prejudicada. (Isso não afeta os cálculos).
            if(loja.y == 0) {
                loja.y = 50;
            }

            g.setColor(Color.BLACK);
            g.fillOval(x - 10, y - 10, 25, 25);
            g.setColor(Color.WHITE);
            g.drawString(Integer.toString(i), x - 4, y + 5);
        }
    }

    private void drawPath(Graphics g) {
        g.setColor(Color.RED);

        for (int i = 0; i < currentStoreIndex; i++) {
            Loja currentStore = listaLojasCopy.get(permutacao.get(i));
            Loja nextStore = listaLojasCopy.get(permutacao.get(i + 1));

            g.drawLine(currentStore.x * 2, currentStore.y * 2, nextStore.x * 2, nextStore.y * 2);
        }
    }

    private void drawTruck(Graphics g) {
        if (currentStoreIndex == -1 || currentStoreIndex == listaLojas.size()) {
            // Não há caminhão para desenhar
            return;
        }

        Loja currentStore = listaLojasCopy.get(permutacao.get(currentStoreIndex));
        int x = currentStore.x;
        int y = currentStore.y;

        g.setColor(Color.BLUE);
        g.fillRect((x * 2) - 5, (y * 2) - 5, 10, 10);
    }

    private void updateStatusLabel() {
        statusLabel.setText(String.format("Produtos: " + produtosColetados + " | Consumo: %.2f", currentConsumption));
    }

    private void startAnimation() {
        produtosColetados = new ArrayList<>();
        Timer timer = new Timer(3000, e -> {
            if (currentStoreIndex < listaLojas.size()) {
                double rendimento = 10.0;

                Loja currentStore = listaLojas.get(permutacao.get(currentStoreIndex));
                Loja nextStore = listaLojas.get(permutacao.get(currentStoreIndex + 1));
                int origemX = currentStore.x;
                int origemY = currentStore.y;
                int destinoX = nextStore.x;
                int destinoY = nextStore.y;
    
                if(currentStore.id != 0) {
                    currentProductCount = Consumo.entregarProdutos(produtosColetados, currentStore, currentProductCount);
                    currentProductCount = Consumo.coletarProdutos(produtosColetados, currentStore, currentProductCount);
                    rendimento = 10.0 - (currentProductCount * 0.5);
                }

                double distancia = Consumo.calcularDistancia(destinoX, destinoY, origemX, origemY);
                double consumoDeViagemAtual = distancia / rendimento;
                currentConsumption += consumoDeViagemAtual;
                
                currentStoreIndex++;
                panel.repaint();
                updateStatusLabel();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setInitialDelay(5000);
        timer.start();
    }
}