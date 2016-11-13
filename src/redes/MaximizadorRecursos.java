package flujoDeRedes;

import java.util.Vector;

public class MaximizadorRecursos {
	FlujoRed redDeFlujos;
	HalladorFlujoMaximo maxflow;
	
	Vector<Proyecto> proyectos;
	Vector<Experto> expertos;
	
	//ayudador
	private class Proyecto{
		public Proyecto(int p){
			proyecto = p;
			beneficio = 0;
			
			expertos = new Vector<Experto>();
		}
		
		Vector<Experto> expertos;
		
		public int proyecto;
		public float beneficio;
	}
	
	private class Experto{
		public Experto(int e){
			experto = e;
			costo = 0;
		}
		
		public int experto;
		public float costo;
	}
	
	public MaximizadorRecursos(String pathArchivo){
		redDeFlujos = new FlujoRed(pathArchivo);
		
		proyectos = new Vector<Proyecto>();

		expertos = new Vector<Experto>();
	}

	public void buscarProyectoQueMaximicenLosBeneficios() {
		maxflow = new HalladorFlujoMaximo(redDeFlujos);
   }
	
	public void seleccionarProyectos(){
		Vector<Integer> corte = maxflow.obtenerNodosDelCorte();
		
		corte.remove(0); //quito la fuente
	
		int cantidadProyectos = obtenerCantidadDeProyectos();
				
        for (int v = 0; v < cantidadProyectos; v++){
        	if(corte.elementAt(v) <= cantidadProyectos){
        		proyectos.add(new Proyecto(corte.elementAt(v)));
        	}
        }
	}

	private int obtenerCantidadDeProyectos() {
		//el tamanio de las adyacencias que salen de la fuente son la cantidad de proyectos
		return redDeFlujos.obtenerAdyacenciasDelNodo(0).size();
	}

	public void recolectarBeneficiosCostos(){
		for (int v = 0; v < proyectos.size(); v++){
			int unProyecto = proyectos.elementAt(v).proyecto;
			
			proyectos.elementAt(v).beneficio = obtenerBeneficioDelProyecto(unProyecto);
			//System.out.println(proyectos.elementAt(v).proyecto +" "+proyectos.elementAt(v).beneficio);
			
			proyectos.elementAt(v).expertos = obtenerExpertosNecDelProyecto(unProyecto);
		}
	}

	private Vector<Experto> obtenerExpertosNecDelProyecto(int unProyecto) {
		Vector<Experto> expertos = new Vector<Experto>();
		
		for(Arista unaArista : redDeFlujos.obtenerAdyacenciasDelNodo(unProyecto)){
			if(unaArista.obtenerDesde() == unProyecto){
				int nodoExperto = unaArista.obtenerA();
				Experto unExperto = new Experto(nodoExperto);
				
				unExperto.costo = obtenerCostoDelExperto(nodoExperto);
				
				expertos.add(unExperto);
			}
		}
		return expertos;
	}

	private float obtenerCostoDelExperto(int nodoExperto) {
		for(Arista unaArista : redDeFlujos.obtenerAdyacenciasDelNodo(nodoExperto)){
			if(unaArista.obtenerDesde() == nodoExperto){
				return unaArista.obtenerCapacidad();
			}
		}
		return 0;
	}

	private float obtenerBeneficioDelProyecto(int unProyecto){
		//el proyecto es el nodo A de la arista conectada con la fuente
		for(Arista unaArista : redDeFlujos.obtenerAdyacenciasDelNodo(0)){
			if(unaArista.obtenerA() == unProyecto){
				return unaArista.obtenerCapacidad(); 
			}
		}
		return 0;
	}

	public void mostrarDatos() {
		int beneficioTotal = 0;
		int costoTotal = 0;
		
		for (int v = 0; v < proyectos.size(); v++){
			int unProyecto = proyectos.elementAt(v).proyecto;
			
			System.out.println("Proyecto: "+unProyecto+"  Beneficio: "+proyectos.elementAt(v).beneficio);
			
			beneficioTotal += proyectos.elementAt(v).beneficio; 
			
			System.out.println("Costos: ");
			
			for (Experto unExperto: proyectos.elementAt(v).expertos){
				System.out.println("Experto: "+ unExperto.experto+" Costo: "+ unExperto.costo);
				costoTotal += unExperto.costo;
			}
			System.out.println();
		}
		System.out.println("BeneficioTotal: "+beneficioTotal+" CostoTotal: "+costoTotal+" Rentabilidad: "+ (beneficioTotal-costoTotal));
	}
}
