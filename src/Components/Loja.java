package Components;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Loja {
    public int id; // Número de identificação da loja.
    public int x, y; // Coordenadas X e Y da loja.
    public Caminhao caminhao; // Classe controladora de rendimento e produtos.
    public Loja destino; // Caso tenha loja de destino.

    public Loja() {
        int id = this.id;
        int x = this.x;
        int y = this.y;
        Caminhao caminhao = this.caminhao;
        Loja destino = this.destino;
    }

    public Loja(int id, int x, int y, Caminhao caminhao, Loja destino) {
        id = 0;
        x = 0;
        y = 0;
        caminhao = null;
        destino = null;
    }

    public List<Loja> readLojas(String fileName) throws Exception {
        FileReader file = new FileReader(fileName);
        BufferedReader rd = new BufferedReader(file);
        List<Loja> lojas = new ArrayList<>();

        String line;

        while((line = rd.readLine()) != null) {
            Loja temp = new Loja();
            String[] campos = line.split("\\s+"); // Separando por espaços.
            if(campos.length < 3) {
                System.out.println("Erro no arquivo de entrada -> Menos de 3 campos na leitura de alguma loja");
                line = null;
            }
            else if(campos.length == 3) {
                temp.id = Integer.parseInt(campos[0]);
                temp.x = Integer.parseInt(campos[1]);
                temp.y = Integer.parseInt(campos[2]);
                lojas.add(temp);
            } else {}
        }


        rd.close();

        return lojas;
    }

    
}
