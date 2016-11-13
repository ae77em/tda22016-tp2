package flujoDeRedes;

public class Main{

    public static void main(String[] args){
    	MaximizadorRecursos m = new MaximizadorRecursos("file2.txt");

    	m.buscarProyectoQueMaximicenLosBeneficios();
    	
    	m.seleccionarProyectos();
    	
    	m.recolectarBeneficiosCostos();
    	
    	m.mostrarDatos();
   }
}
