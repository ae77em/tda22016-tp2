package flujoDeRedes;

import java.util.ArrayList;
import java.util.Vector;

public class HalladorFlujoMaximo {
	FlujoRed redDeFlujo;
	
	private Vector<Boolean> visitados;
	
	private Vector<Arista> aristas; 
	
    private Vector<Integer> padres; 
        
    ArrayList<Integer> contenedorBFS; 
    
    private float flujoMaximo;         
  
    public void hallarFlujoMaximo(){
    	//la fuentes del grago siempre es el cero
    	int sumidero = redDeFlujo.obtenerSumidero();
    			
        System.out.println("Grafo inicial:");
        System.out.print("\n"+redDeFlujo);
        
        while (existeCaminoAug()) {
        	System.out.println("imprimiendo un aug path\n");
            
        	float cuelloDeBotella = obtenerCuelloDeBotella(sumidero);
            
            actualizarRedDeFlujos(cuelloDeBotella,sumidero);
        	
            flujoMaximo += cuelloDeBotella;

            System.out.println("\nGrafo luego de actualizar flujo con: " + cuelloDeBotella);
            
            System.out.print(redDeFlujo);
            //para la busqueda de un nuevo camino Aug
            limpiarVectores();
        }	
    }
    
    private void actualizarRedDeFlujos(float cuelloDeBotella,int sumidero) {
    	int nodo = sumidero;
    	
        boolean seLlegoALafuente = false; 
        
        System.out.println("\naumentando flujo de: "+ cuelloDeBotella +" flujo\n");
        
        while(!seLlegoALafuente){
        	Arista unaArista = aristas.elementAt(nodo);
        	
			unaArista.agregarFlujoResidual(cuelloDeBotella);
			
			//si es la primera vez q se agrega flujo debe crearse una arista al reves
			// con capacidad flujo igual al flujo agregado
			if(unaArista.obtenerCapacidad() != 0 && !unaArista.tieneInversaCreada()){
				AristaInversa b = new AristaInversa(
						unaArista.obtenerHacia(), 
						unaArista.obtenerDesde(),
						unaArista.obtenerCapacidad(), 
						unaArista.obtenerFlujo() );
				
				b.setAristaFront((AristaDirecta) unaArista);
				
				redDeFlujo.agregarAdyacencia(unaArista.obtenerHacia(),b);
			}       
			System.out.println(nodo + " " + aristas.elementAt(nodo));
			
			nodo = padres.elementAt(nodo);    
			
        	if (nodo == 0)
				seLlegoALafuente = true;
		}          
	}

	private float obtenerCuelloDeBotella(int sumidero) {
    	int nodoAnalizado = sumidero;
    	
        boolean seLlegoALafuente = false; 
        
        float cuelloDeBotella = Float.POSITIVE_INFINITY;

        while(!seLlegoALafuente){
			System.out.println(nodoAnalizado + " " + aristas.elementAt(nodoAnalizado));
			
			cuelloDeBotella = Math.min(cuelloDeBotella, aristas.elementAt(nodoAnalizado)
            		.obtenerCapacidadResidual(nodoAnalizado));
        			
			nodoAnalizado = padres.elementAt(nodoAnalizado);    
			
        	if (nodoAnalizado == 0)
				seLlegoALafuente = true;
		}
    
		return cuelloDeBotella;
	}

	public HalladorFlujoMaximo(FlujoRed redDeFlujo){
    	flujoMaximo = 0;
    	
    	this.redDeFlujo = redDeFlujo;
    	
    	int cantidadDeNodos = redDeFlujo.obtenerCantidadDeNodos();
    	
    	aristas = new Vector<Arista>();
        aristas.setSize(cantidadDeNodos);
    
    	visitados = new Vector<Boolean>();
    	padres = new Vector<Integer>();
        
        for (int i=0; i < cantidadDeNodos;i++){
        	visitados.add(false);
        	padres.add(0);
        }

        contenedorBFS = new ArrayList<Integer>();
    }

	// return flujoMaximo of max flow
    public float obtenerFlujoMaximo()  {
        return flujoMaximo;
    }

    // los nodos q quedaron marcos son los pertencientes al corte
    public boolean estaElNodoEnElCorte(int v)  {
        return visitados.elementAt(v);
    }

    //se implementa una busqueda BFS para encontrar una camino hacia el sumidero
    private boolean existeCaminoAug() {
    	int fuente = 0;
        //la busqueda siempre comienza por el nodo fuente
        contenedorBFS.add(fuente);
        
        visitados.set(fuente,true);
       
        while (!contenedorBFS.isEmpty()){
            int nodoVisitado = contenedorBFS.get(contenedorBFS.size()-1);
            
            contenedorBFS.remove(contenedorBFS.size()-1);
            //analizado las adyacencias del nodo visitado
            for (Arista unaArista : redDeFlujo.obtenerAdyacenciasDelNodo(nodoVisitado)) {
            	//voy analizando los nodos adyacentes al nodo quitado del contenedorBFS
                int nodoHacia = unaArista.obtenerHacia();
                
                if (unaArista.obtenerCapacidadResidual(nodoHacia) > 0) {
                    if (visitados.elementAt(nodoHacia) == false) {
                    	actualizarDatosBusqueda(nodoVisitado,nodoHacia,unaArista);
                    }
                }
            }            
        }
        System.out.println("ya encontre mi camino jajajajaj\n");

        //si no se llega a visitar el ultimo quiere decir que ya no hay camino aug
        return visitados.elementAt(redDeFlujo.obtenerSumidero());
    }

    private void actualizarDatosBusqueda(int nodoVisitado, int nodoHacia, Arista unaArista) {
    	padres.set(nodoHacia,nodoVisitado);

    	aristas.set(nodoHacia, unaArista);

        visitados.set(nodoHacia,true);
        
        contenedorBFS.add(nodoHacia);
	}

	private void limpiarVectores() {
    	//los tres vectores tienen el mismo tamaño
		for (int i = 0; i < aristas.size(); i++) {
			aristas.set(i,null);
			padres.set(i,0);
			visitados.set(i,false);
		}
		
	}

	public Boolean estaEnElCorte(int nodo)  {
        return visitados.elementAt(nodo);
    }

	public Vector<Integer> obtenerNodosDelCorte() {
		Vector<Integer> corte = new Vector<Integer>();
		
		for (int i = 0; i < visitados.size(); i++) {
			if(visitados.elementAt(i))
				corte.add(i);
		}	
		return corte;
	}
}
