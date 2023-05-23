package Components;
import java.util.*;
import java.io.*;

public class Loja {
    int id; // Número de identificação da loja.
    int x, y; // Coordenadas.
    ArrayList<Integer> destinos; // Possiveis destinos de determinada loja.

    public Loja(int id, int x, int y, ArrayList<Integer> destinos) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.destinos = destinos;
    }

    public static ArrayList<Loja> lerLojas(String fileName) throws Exception {
        FileReader file = new FileReader(fileName);
        BufferedReader rd = new BufferedReader(file);
        ArrayList<Loja> listaDeLojas = new ArrayList<>();

        String line;

        while((line = rd.readLine()) != null) {
            String[] campos = line.split(" "); // Pegando os elementos de cada loja, i.g, id, x, y, destinos.

            int id = Integer.parseInt(campos[0]);
            int x = Integer.parseInt(campos[1]);
            int y = Integer.parseInt(campos[2]);

            ArrayList<Integer> destinos = new ArrayList<>();
            for(int i = 3; i < campos.length; i++)
                destinos.add(Integer.parseInt(campos[i]));

            Loja temp = new Loja(id, x, y, destinos);
            listaDeLojas.add(temp);
        }

        rd.close();

        return listaDeLojas;
    }

    public static void imprimirLojas(ArrayList<Loja> listaDeLojas) {
        for (Loja loja : listaDeLojas) {
            System.out.println(loja.id + " " + loja.x + " " + loja.y + " " + loja.destinos);
        }
        System.out.println("\n\n");
    }
}