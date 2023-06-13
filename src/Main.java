/**
 * Componentes do grupo:
 * João Pedro Lobato de Pinnho
 * Lucas Carvalho da Luz.
 */

import Components.*;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaInicial();
            }
        });
    }
}