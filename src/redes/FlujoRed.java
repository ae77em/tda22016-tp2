package flujoDeRedes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FlujoRed {
    private int cantidadNodos;
    private int cantidadAristas;
    
    private Vector<Vector<Arista>> adyacencias;
    
    BufferedReader lectorArchivo;
        
    public FlujoRed(String pathArchivo) {    	
    	abrirArchivo(pathArchivo);
    	
    	int cantCostos = leerCantCostos();
    	
    	int cantProyectos = lerrCantProyectos();
    	
    	crearRedDeFlujos(cantProyectos,cantCostos);
    	
        ArrayList<Integer> costosPorProyecto = leerCostos(cantCostos);
        
        ArrayList<String[]> gananciasDependencias = leerGanancias(cantProyectos);
        
        cargarDatosEnRedDeFlujos(gananciasDependencias,costosPorProyecto);
    }
    

    private void cargarDatosEnRedDeFlujos(ArrayList<String[]> gananciasDependencias,
			ArrayList<Integer> costosPorProyecto) {
		
        int cantProyectos = gananciasDependencias.size();
        
        conectarFuenteAProyectosYSusCostos(gananciasDependencias);

        conectarAristasAlSumidero(costosPorProyecto,cantProyectos);
	}

	private void conectarFuenteAProyectosYSusCostos(ArrayList<String[]> gananciasDependencias) {
		int cantProyectos = gananciasDependencias.size();
		
		for (int i = 1; i <= cantProyectos; i++) {
        	String[] unProyecto = gananciasDependencias.get(i-1);
        	
        	float capacidad = Integer.parseInt(unProyecto[0]);
        	
            agregarArista(new AristaDirecta(0, i, capacidad)); //agrego un proyecto
            
            //ya q n l primer valor esta el peso del beneficio del proyecto i
            for(int j = 1; j<= unProyecto.length-1; j++ ) 
            	agregarArista(new AristaDirecta(i, Integer.parseInt(unProyecto[j]) + cantProyectos, 
            														Float.POSITIVE_INFINITY));
		}
	}


	private ArrayList<String[]> leerGanancias(int cantProyectos) {
    	ArrayList<String[]> dataProyectos = new ArrayList<String[]>();
    	//agrego aristas a la fuente
        for (int i = 1; i <= cantProyectos; i++) {
        	String beneficioDependencias = "";
			try {
				beneficioDependencias = lectorArchivo.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	dataProyectos.add(beneficioDependencias.split(" "));
        }          
        return dataProyectos;
	}

	private void conectarAristasAlSumidero(ArrayList<Integer> costos,int cantProyectos){
		for (int j = 1; j <= costos.size(); j++) {
			agregarArista(new AristaDirecta(cantProyectos + j, cantidadNodos-1, costos.get(j-1)));		
		}
    }

	private ArrayList<Integer> leerCostos(int cantCostos) {
		ArrayList<Integer> costos = new ArrayList<Integer>();
		
		for (int i = 1; i <= cantCostos; i++) {
            int capacidad = 0;
			try {
				capacidad = Integer.parseInt(lectorArchivo.readLine());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			costos.add(capacidad);
        }
        return costos;
	}

	private void armarListaDeAdyacencias() {
    	adyacencias = new Vector<Vector<Arista>>();
        
    	adyacencias.setSize(cantidadNodos);
        
        for (int v = 0; v < cantidadNodos; v++)
        	adyacencias.set(v, new Vector<Arista>());		
	}

	private void crearRedDeFlujos(int cantProyectos, int cantCostos) {
    	//el 2 corresponde a los nodos fuente y sumidero
    	this.cantidadNodos = cantProyectos + cantCostos + 2; 
        
    	this.cantidadAristas = 0;
    	
    	armarListaDeAdyacencias();
	}

	private void abrirArchivo(String pathArchivo) {
    	try {
			lectorArchivo = new BufferedReader(new FileReader(pathArchivo));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	private int lerrCantProyectos() {
    	int cantProyectos = 0;
    	try {
			cantProyectos = Integer.parseInt(lectorArchivo.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //tomo los costos
    	return cantProyectos;
	}

	private int leerCantCostos() {
    	int cantCostos = 0;
		try {
			String nro = lectorArchivo.readLine();

			cantCostos = Integer.parseInt(nro);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return cantCostos;
	}

	public int obtenerCantidadDeNodos(){ 
    	return cantidadNodos; 
    }
    
    public int obtenerCantidadDeAristas(){ 
    	return cantidadAristas; 
    }

    public void agregarArista(Arista e) {
    	cantidadAristas++;
    	
        int v = e.obtenerDesde();
        
        int w = e.obtenerHacia();
        
        adyacencias.elementAt(v).add(e);
        
        adyacencias.elementAt(w).add(e);
    }

    // return list of edges incident to  v
    public Vector<Arista> obtenerAdyacenciasDelNodo(int v) {
        return adyacencias.elementAt(v);
    }

    // return list of all edges - excludes self loops
    public Vector<Arista> edges() {
        Vector<Arista> aristas = new Vector<Arista>();
        
        for (int v = 0; v < cantidadNodos; v++)
            for (Arista e : obtenerAdyacenciasDelNodo(v)) {
                if (e.obtenerHacia() != v)
                	aristas.add(e);
            }
        return aristas;
    }


    // string representation of Graph (excludes self loops) - takes quadratic time
    public String toString() {
        StringBuilder s = new StringBuilder();
        
        s.append("NODOS "+cantidadNodos + " aristas: " + cantidadAristas + "\n");
        
        for (int v = 0; v < cantidadNodos; v++) {
            s.append(v + ":  ");
            
            for (Arista e : adyacencias.elementAt(v)) {
                if (e.obtenerHacia() != v) s.append(e + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    public int obtenerSumidero(){
    	return cantidadNodos-1;
    }

	public void agregarAdyacencia(int nodo, Arista b) {
		adyacencias.elementAt(nodo).add(b);
	}
}
