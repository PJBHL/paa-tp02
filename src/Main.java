import Components.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Loja> listaDeLojas;
        listaDeLojas = Loja.lerLojas("entradas/lojas.txt");
        Loja.imprimirLojas(listaDeLojas);
        BruteForce.forcaBruta(listaDeLojas);
        ArrayList<Integer> melhorPermutacao = BruteForce.getMelhorPermutacao();
        double menorConsumo = BruteForce.getMenorConsumo();

        // // Cria a janela
        // JFrame frame = new JFrame("Animação do Caminhão");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // // Configura a animação
        // int larguraTela = 800; // Defina a largura desejada
        // int alturaTela = 600; // Defina a altura desejada
        // AnimacaoCaminhao animacao = new AnimacaoCaminhao();

        // // Adiciona a animação à janela
        // frame.getContentPane().add(animacao);
        // frame.pack();
        // frame.setLocationRelativeTo(null); // Centraliza a janela na tela
        // frame.setVisible(true);

    }
}