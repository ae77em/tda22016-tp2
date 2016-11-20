package flujoDeRedes;

public class AristaInversa extends Arista {
	
	AristaDirecta f;

	public AristaInversa(int desde, int a, float capacidad,float flujo) {
		super(desde, a, capacidad);
		// TODO Auto-generated constructor stub
		inversaCreada = true;
		
		this.flujo = flujo;
		
	}
	
	public void setAristaFront(AristaDirecta f){
		this.f = f;
		
		this.f.notificarInversaCreada();
	}

	@Override
	public void agregarFlujoResidual(float unFlujo) {
		flujo -= unFlujo;	
		
		this.f.agregarFlujoResidual(unFlujo);
		
		System.out.println("ESTOY ARAL REVES ______________________------");
		System.out.println("ESTOY ARAL REVES ______________________------");
		System.out.println("ESTOY ARAL REVES ______________________------\n");
	}
 
	@Override
	public float obtenerCapacidadResidual(int nodo) {
		return flujo;
	}
}
