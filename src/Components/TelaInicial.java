package Components;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe responsável pela construção da tela inicial do programa.
 * Escolha de arquivo, técnica (Branch and Bound ou Força Bruta) e 'K'.
 */
public class TelaInicial extends JFrame {
    private JTextField txtArquivo;
    private JLabel lblArquivo;
    private JButton selecionarArquivo;
    public static long start, end; // Medição de tempo.

    private JTextField txtK;
    private JLabel lblK;
    private JButton bruteForceButton;
    private JButton branchAndBouButton;

    public TelaInicial() {
        setTitle("PAA - Trabalho de Implementação 02");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(700, 125);
        setResizable(true);
        setLocationRelativeTo(null);

        lblArquivo = new JLabel("Arquivo: ");
        txtArquivo = new JTextField(20);
        selecionarArquivo = new JButton("Selecionar");

        lblK = new JLabel("Capacidade do Caminhão: ");
        txtK = new JTextField(5);
        bruteForceButton = new JButton("Força Bruta");
        branchAndBouButton = new JButton("Branch and Bound");

        // Botão de seleção do arquivo.
        selecionarArquivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser file = new JFileChooser();
                int result = file.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = file.getSelectedFile();
                    txtArquivo.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        // Seleção de método - Força Bruta.
        bruteForceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String arquivo = txtArquivo.getText();
                int k = Integer.parseInt(txtK.getText());

                ArrayList<Loja> listaDeLojas;
                
                try {
                    listaDeLojas = Loja.lerLojas(arquivo);
                    // Defini a capacidade do caminhão como a lida da interface.
                    Caminhao.CAPACIDADE_CAMINHAO = k;

                    start = System.currentTimeMillis();
                    BruteForce.forcaBruta(listaDeLojas);
                    end = System.currentTimeMillis();

                    ArrayList<Integer> melhorPermutacao = BruteForce.getMelhorPermutacao();
                    double menorConsumo = BruteForce.getMenorConsumo();
                    Caminhao.logs(melhorPermutacao, menorConsumo);

                    JFrame animacaoFrame = new JFrame("Animação do Caminho - Força Bruta");
                    animacaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    animacaoFrame.setSize(800, 600);
                    animacaoFrame.setResizable(false);
                    animacaoFrame.setLocationRelativeTo(null);

                    AnimacaoCaminho animacaoCaminho = new AnimacaoCaminho(listaDeLojas, melhorPermutacao);
                    animacaoFrame.add(animacaoCaminho);
                    animacaoFrame.setVisible(true);
                } catch (Exception ex) {
                    // No Exceptions.
                }
            }
        });

        // Seleção de método - branch and bound.
        branchAndBouButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String arquivo = txtArquivo.getText();
                int k = Integer.parseInt(txtK.getText());

                ArrayList<Loja> listaDeLojas;

                try {
                    listaDeLojas = Loja.lerLojas(arquivo);
                    // Defini a capacidade do caminhão como a lida da interface.
                    Caminhao.CAPACIDADE_CAMINHAO = k;

                    start = System.currentTimeMillis();
                    BranchAndBound.encontrarMelhorPermutacao(listaDeLojas);
                    end = System.currentTimeMillis();

                    ArrayList<Integer> melhorPermutacao = BranchAndBound.melhorPermutacao;
                    double menorConsumo = BranchAndBound.menorConsumo;
                    melhorPermutacao.add(melhorPermutacao.size(), 0);
                    Caminhao.logs(melhorPermutacao, menorConsumo);
                    

                    JFrame animacaoFrame = new JFrame("Animação do Caminho - Branch and Bound");
                    animacaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    animacaoFrame.setSize(800, 600);
                    animacaoFrame.setResizable(false);
                    animacaoFrame.setLocationRelativeTo(null);

                    AnimacaoCaminho animacaoCaminho = new AnimacaoCaminho(listaDeLojas, melhorPermutacao);
                    animacaoFrame.add(animacaoCaminho);
                    animacaoFrame.setVisible(true);
                } catch (Exception ex) {
                    // No Exceptions.
                }
            }
        });

        add(lblArquivo);
        add(txtArquivo);
        add(selecionarArquivo);
        add(lblK);
        add(txtK);
        add(bruteForceButton);
        add(branchAndBouButton);

        setVisible(true);
    }
}
