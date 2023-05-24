package Components;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class TelaInicial extends JFrame {
    public JTextField txtArquivo;
    private JLabel lblArquivo;
    private JButton selecionarArquivo;

    public JTextField txtK;
    private JLabel lblK;
    private JButton iniciar;

    public TelaInicial() {
        setTitle("Força Bruta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(400, 150);
        setResizable(true);
        setLocationRelativeTo(null);

        lblArquivo = new JLabel("Arquivo: ");
        txtArquivo = new JTextField(20);
        selecionarArquivo = new JButton("Selecionar");

        lblK = new JLabel("Capacidade do Caminhão: ");
        txtK = new JTextField(5);
        iniciar = new JButton("Iniciar");

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

        iniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String arquivo = txtArquivo.getText();
                int k = Integer.parseInt(txtK.getText());
        
                ArrayList<Loja> listaDeLojas;
        
                try {
                    listaDeLojas = Loja.lerLojas(arquivo);
                    Consumo.CAPACIDADE_CAMINHAO = k;
        
                    BruteForce.forcaBruta(listaDeLojas);

                    ArrayList<Integer> melhorPermutacao = BruteForce.getMelhorPermutacao();
                    double menorConsumo = BruteForce.getMenorConsumo();
                    System.out.println("Melhor permutacao: " + melhorPermutacao + " Seu consumo: " + menorConsumo);

                    JFrame animacaoFrame = new JFrame("Animação do Caminhão");
                    animacaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    animacaoFrame.setSize(800, 600);
                    animacaoFrame.setResizable(false);
                    animacaoFrame.setLocationRelativeTo(null);

                    AnimacaoCaminhao animacaoCaminhao = new AnimacaoCaminhao(listaDeLojas, melhorPermutacao);
                    animacaoFrame.add(animacaoCaminhao);

                    animacaoFrame.setVisible(true);

                } catch (Exception ex) {
                    // Tratar a exceção de leitura do arquivo
                    System.out.println("Ocorreu um erro durante a leitura do arquivo: " + ex.getMessage());
                }
            }
        });
        

        add(lblArquivo);
        add(txtArquivo);
        add(selecionarArquivo);
        add(lblK);
        add(txtK);
        add(iniciar);

        setVisible(true);
    }
}
