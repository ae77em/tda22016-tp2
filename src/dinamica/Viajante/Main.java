package dinamica.Viajante;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;

public class Main {
    
    private static int[][] crearMatriz(String archivoMatriz, int tamanio) throws IOException {
        int[][] matriz;
        BufferedReader lectorArchivo = null;
        lectorArchivo = new BufferedReader(new FileReader(archivoMatriz));
        String linea = lectorArchivo.readLine();

        if (linea == null) throw new IOException("Archivo con formato erroneo");
        
        String[] valoresLinea = linea.split(" ");
        int cantVertices = tamanio;
        matriz = new int[cantVertices][cantVertices];

        for (int i = 0; i < cantVertices; i++) {
            valoresLinea = linea.split(" ");
            
            for (int j = 0; j < cantVertices; j++) {
                matriz[i][j] = Integer.parseInt(valoresLinea[j]);
            }
            linea = lectorArchivo.readLine();
            if (linea == null && i < cantVertices -2) throw new IOException("Archivo con formato erroneo");
        }
        lectorArchivo.close();
        return matriz;
    }
    
    public static void main(String[] args) throws IOException {
        int[] tamaniosMatriz = {15, 17, 19, 21, 23};
        
        for (int tamanio : tamaniosMatriz) {
            int[][] matriz = crearMatriz(args[0], tamanio);
            Viajante viajante = new Viajante(matriz);
            int costo = viajante.calcular();
            Deque<Integer> camino = viajante.obtenerCamino();
            System.out.println("Tamanio: " + tamanio + ", Costo: " + costo + ", Camino:");
            System.out.println(camino);
        }
    }
    
}
