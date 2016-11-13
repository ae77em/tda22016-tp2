package flujoDeRedes;

import java.util.ArrayList;
import java.util.Vector;

public class HalladorFlujoMaximo {
    private Vector<Boolean> visitados;     // visitados[v] = true iff s->v path in residual graph
        
    private float flujoMaximo;         // current flujoMaximo of max flow
  
    private Vector<Arista> aristas; // aristas[v] = last edge on shortest residual s->v path
    
    public HalladorFlujoMaximo(FlujoRed redDeFlujo) {
    	int fuente = 0;
    	int sumidero = redDeFlujo.obtenerCantidadDeNodos()-1;
    			
        flujoMaximo = flujoSobrante(redDeFlujo, sumidero);
        
        while (existeCaminoAug(redDeFlujo, fuente, sumidero)) {

            float cuelloDeBotella = Float.POSITIVE_INFINITY;
            
            int nodo = sumidero;
            
            while(nodo != fuente){
            	cuelloDeBotella = Math.min(cuelloDeBotella, aristas.elementAt(nodo)
                		.obtenerCapacidadResidual(nodo));
            	
            	nodo = aristas.elementAt(nodo).devolverNodoOrigenODestino(nodo);
            }
            // augment flow
            nodo = sumidero;
            
            while(nodo != fuente){
            	aristas.elementAt(nodo).agregarFlujoResidual(nodo, cuelloDeBotella);
                
            	nodo = aristas.elementAt(nodo).devolverNodoOrigenODestino(nodo);
            }
            flujoMaximo += cuelloDeBotella;
        }
    }

    // return flujoMaximo of max flow
    public float obtenerFlujoMaximo()  {
        return flujoMaximo;
    }

    // is v in the s side of the min s-t cut?
    public boolean estaElNodoEnElCorte(int v)  {
        return visitados.elementAt(v);
    }


    // if so, upon termination aristas[] will 
    //contain a parent-link representation of such a path
    private boolean existeCaminoAug(FlujoRed redDeFlujos, int fuente, int sumidero) {
        aristas = new Vector<Arista>();
        aristas.setSize(redDeFlujos.obtenerCantidadDeNodos());
        
        visitados = new Vector<Boolean>();
        for (int i=0; i < redDeFlujos.obtenerCantidadDeNodos();i++)
        	visitados.add(false);
        
        // breadth-first search
        ArrayList<Integer> q = new ArrayList<Integer>();
        
        q.add(fuente);
        
        visitados.set(fuente,true);
       
        while (!q.isEmpty()){
            int nodoAnalizado = q.get(q.size()-1);
            
            q.remove(q.size()-1);

            for (Arista unaArista : redDeFlujos.obtenerAdyacenciasDelNodo(nodoAnalizado)) {
                int w = unaArista.devolverNodoOrigenODestino(nodoAnalizado);

                // if residual capacidad desde nodoAnalizado to w
                if (unaArista.obtenerCapacidadResidual(w) > 0) {
                    if (visitados.elementAt(w) == false) {
                    	aristas.set(w, unaArista);
                        
                        visitados.set(w,true);
                        
                        q.add(w);
                    }
                }
            }
        }
        // is there an augmenting path?
        return visitados.elementAt(sumidero);
    }

    // devuelve el flujo sobrante en el nodo
    private float flujoSobrante(FlujoRed G, int nodo) {
        float flujoSobrante = 0;
        
        for (Arista e : G.obtenerAdyacenciasDelNodo(nodo)) {
            if (nodo == e.obtenerDesde()) 
            	flujoSobrante -= e.obtenerFlujo();
            else               
            	flujoSobrante += e.obtenerFlujo();
        }
        return flujoSobrante;
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
