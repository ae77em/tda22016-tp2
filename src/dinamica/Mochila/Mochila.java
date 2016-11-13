import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Mochila {
	private BufferedReader lectorArchivo;
	
	private int cantItems;
	
	private int capacidadMochila;
	
	private int[] valores;
	private int[] pesos;
	private int[][] cargardorMochila;
	
	
	public Mochila(String rutaArchivo){
		try {
		lectorArchivo = new BufferedReader(new FileReader(rutaArchivo));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		cantItems = 0;
		
		capacidadMochila = 0;
	}

	public void cargarDatos() {		
		try {
			lectorArchivo.readLine();//salteo titulo del archivo
			cantItems = Integer.parseInt(lectorArchivo.readLine().split(" ")[1]);
			capacidadMochila = Integer.parseInt(lectorArchivo.readLine().split(" ")[1]);
			lectorArchivo.readLine(); //salteo optimo
			lectorArchivo.readLine(); //salteo tiempo
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		valores = new int[cantItems+1];
		pesos = new int[cantItems+1];
		cargardorMochila = new int[cantItems+1][capacidadMochila+1];
		
		//leo todos los valores
		for (int i = 1; i <= cantItems ; i++) {
			String[] linea = null;
			try {
				linea = lectorArchivo.readLine().split(",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			valores[i] = Integer.parseInt(linea[1]);
			pesos[i] = Integer.parseInt(linea[2]);
		}		// TODO Auto-generated method stub
	}

	public void cargarMochila() {
		for(int i=0;i<=cantItems;i++){
			for(int j=0;j<=capacidadMochila;j++){
				if(i==0 || j==0){
					cargardorMochila[i][j] = 0;
				}
				else if(j < pesos[i]){
					cargardorMochila[i][j] = cargardorMochila[i-1][j];
				}
				else{			
					cargardorMochila[i][j] = Math.max(cargardorMochila[i-1][j],cargardorMochila[i-1][j-pesos[i]]+valores[i]);
				}
			}
		}
	}
	
	public void mostrarDatos(){
		System.out.println("Optimo: "+ cargardorMochila[cantItems][capacidadMochila]);
		
		int i = cantItems;
		int j = capacidadMochila;
		int itemsCargados = 0;
		int pesoAcumulado = 0;
		int valorAcumulado = 0;
		
		//antes de q recorra todas las filas o el peso se hace cero
		while(i > 0 && j > 0){
			//arranco desde la ultima fila y columna y voy retrocediendo
			// si la fila anterior cambia quiere decir que hubo un incrmento
			// y que el elemento i fue ingresado en la mochila
			if(cargardorMochila[i][j] != cargardorMochila[i-1][j]){
				itemsCargados++;
				
				valorAcumulado += valores[i];
				
				pesoAcumulado += pesos[i];
				
				j = j - pesos[i];
			}
			i--;
		}
		
		System.out.println("Items : "+ itemsCargados + " valorTotal: " + valorAcumulado + "  pesoTotal: "+pesoAcumulado);        
	}
}
