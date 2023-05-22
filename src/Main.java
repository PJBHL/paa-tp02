import Components.*;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Loja> listaDeLojas;
        listaDeLojas = Loja.lerLojas("entradas/lojas.txt");
        Loja.imprimirLojas(listaDeLojas);
    }
}