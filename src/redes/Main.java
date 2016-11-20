package flujoDeRedes;

public class Main{

    public static void main(String[] args){
    	MaximizadorRecursos m = new MaximizadorRecursos(args[0]);

    	m.buscarProyectoQueMaximicenLosBeneficios();
    	
    	m.seleccionarProyectos();
    	
    	m.recolectarBeneficiosCostos();
    	
    	m.mostrarDatos();
   }
}
