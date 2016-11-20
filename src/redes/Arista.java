package flujoDeRedes;

public abstract class Arista {
	protected int desde;             
    protected  int hacia;             
    protected float flujo;         
    protected  float capacidad;   
    protected boolean inversaCreada;
	
    
    public Arista(Arista e) {
        this.desde = e.desde;
        this.hacia = e.hacia;  
        this.capacidad = e.capacidad;
        this.flujo = e.flujo;
    }

    public int obtenerDesde(){ 
    	return desde;        
    }  
    
    
    public Arista(int desde, int a, float capacidad) {
        this.desde = desde;
        this.hacia = a;  
        this.capacidad = capacidad;
        this.flujo = 0;
    }

    public int obtenerHacia(){ 
    	return hacia;        
    }  
    
    public float obtenerCapacidad(){ 
    	return capacidad; 
    }
    
    public float obtenerFlujo(){ 
    	return flujo;     
    }
    
     
    public void notificarInversaCreada(){
		inversaCreada = true;
	}
	    
    public float flujo(){
    	return flujo;
    }
    
    public boolean tieneInversaCreada() {
		return inversaCreada;
	}    
    public String toString() {
        return desde + "->" + hacia + " " + " ("+flujo+") de " + capacidad ;
    }
    
    public abstract float obtenerCapacidadResidual(int nodo);
    
    public abstract void agregarFlujoResidual(float unFlujo);
}
