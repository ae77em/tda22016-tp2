package flujoDeRedes;

public class Arista {
    private  int desde;             
    private  int a;             
    private float flujo;         
    private  float capacidad;   
   
    public Arista(int desde, int a, float capacidad) {
        this.desde = desde;
        this.a = a;  
        this.capacidad = capacidad;
        this.flujo = 0;
    }

    public Arista(Arista e) {
        this.desde = e.desde;
        this.a = e.a;  
        this.capacidad = e.capacidad;
        this.flujo = e.flujo;
    }

    public int obtenerDesde(){ 
    	return desde;        
    }  
    
    public int obtenerA(){ 
    	return a;        
    }  
    
    public float obtenerCapacidad(){ 
    	return capacidad; 
    }
    
    public float obtenerFlujo(){ 
    	return flujo;     
    }

    public int devolverNodoOrigenODestino(int nodo) {
        if(nodo == desde) 
        	return a;
        else if (nodo == a) 
        	return desde;
        else throw new RuntimeException("Illegal endpoint");
    }

    public float obtenerCapacidadResidual(int nodo) {
        if(nodo == desde) 
        	return flujo;
        else
        	return capacidad - flujo;
    }

    public void agregarFlujoResidual(int nodo, float diferencia) {
        if (nodo == desde) 
        	flujo -= diferencia;
        else if (nodo == a) 
        	flujo += diferencia;
    }

    public String toString() {
        return desde + "->" + a + " " + capacidad + " ("+flujo+") ";
    }
}
