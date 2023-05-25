package Components;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimacaoCaminhao extends JFrame {

    private ArrayList<Loja> listaLojas;
    private ArrayList<Integer> permutacao;

    private JPanel panel;
    private JLabel statusLabel;
    private int currentStoreIndex;
    private double currentConsumption;
    private int currentProductCount;

    public AnimacaoCaminhao(ArrayList<Loja> listaLojas, ArrayList<Integer> permutacao) {
        this.listaLojas = listaLojas;
        this.permutacao = permutacao;
        this.currentStoreIndex = -1;
        this.currentConsumption = 0.0;
        this.currentProductCount = 0;

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
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        statusLabel.setPreferredSize(new Dimension(200, 30));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);

        startAnimation();
    }

    private void drawStores(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        for (int i = 0; i < listaLojas.size(); i++) {
            Loja loja = listaLojas.get(i);
            int x = loja.x * 2;
            int y = loja.y * 2;

            if(loja.y == 0)
                loja.y = 50;

            g.setColor(Color.BLACK);
            g.fillOval(x - 10, y - 10, 20, 20);
            g.setColor(Color.WHITE);
            g.drawString(Integer.toString(i), x - 4, y + 5);
        }
    }

    private void drawPath(Graphics g) {
        g.setColor(Color.RED);

        for (int i = 0; i < currentStoreIndex; i++) {
            Loja currentStore = listaLojas.get(permutacao.get(i));
            Loja nextStore = listaLojas.get(permutacao.get(i + 1));

            g.drawLine(currentStore.x * 2, currentStore.y * 2, nextStore.x * 2, nextStore.y * 2);
        }
    }

    private void drawTruck(Graphics g) {
        if (currentStoreIndex == -1 || currentStoreIndex == listaLojas.size()) {
            // Não há caminhão para desenhar
            return;
        }

        Loja currentStore = listaLojas.get(permutacao.get(currentStoreIndex));
        int x = currentStore.x;
        int y = currentStore.y;

        g.setColor(Color.BLUE);
        g.fillRect((x * 2) - 5, (y * 2) - 5, 10, 10);
    }

    private void updateStatusLabel() {
        statusLabel.setText(String.format("Produtos: %d | Consumo: %.2f", currentProductCount, currentConsumption));
    }

    private void startAnimation() {
        Timer timer = new Timer(3000, e -> {
            if (currentStoreIndex < listaLojas.size() - 1) {
                currentStoreIndex++;
                Loja currentStore = listaLojas.get(permutacao.get(currentStoreIndex));

                currentProductCount += 1;
                ArrayList<Integer> novoCaminho = new ArrayList<Integer>(permutacao.subList(0, currentStoreIndex + 1));
                currentConsumption = Consumo.calcularConsumoAtual(listaLojas, novoCaminho);

                panel.repaint();
                updateStatusLabel();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setInitialDelay(1000);
        timer.start();
    }
}