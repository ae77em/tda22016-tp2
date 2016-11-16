public class Main {

	public static void main(String[] args){
		Mochila m = new Mochila(args[0]);
		
		m.cargarDatos();
		
		m.cargarMochila();
		
		m.mostrarDatos();
	}
}