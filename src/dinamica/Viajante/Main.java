package dinamica.Viajante;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;

public class Main {
    public static void main(String[] args) throws IOException {
        int[][] matriz;
        BufferedReader lectorArchivo = null;
        lectorArchivo = new BufferedReader(new FileReader(args[0]));
        String linea = lectorArchivo.readLine();

        if (linea == null) throw new IOException("Archivo con formato erroneo");
        
        String[] valoresLinea = linea.split(" ");
        int cantVertices = valoresLinea.length;
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
        lectorArchivo = new BufferedReader(new FileReader(args[1]));
        valoresLinea = lectorArchivo.readLine().split(" ");        
        
        Viajante viajante = new Viajante(matriz);
        int costo = viajante.calcular();
        Deque<Integer> camino = viajante.obtenerCamino();
        for (int i = 0; i < cantVertices; i++) {
            if (Integer.parseInt(valoresLinea[i]) != camino.poll()) {
                System.out.println("Camino incorrecto");
                break;
            }
        }
        lectorArchivo.close();
    }
    
}
