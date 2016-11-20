package flujoDeRedes;

public class AristaDirecta extends Arista{
	
	AristaInversa b;
	
	public AristaDirecta(int desde, int a, float capacidad) {
		super(desde, a, capacidad);

		inversaCreada = false;
		
		this.b = null;
	}
	
	public void setAristaBack(AristaInversa b){
		this.b = b;
	}

	@Override
	public void agregarFlujoResidual(float unFlujo) {
		flujo += unFlujo;
		
		if(b != null)
			this.b.agregarFlujoResidual(unFlujo);
	}

	@Override
	public float obtenerCapacidadResidual(int nodo) {
		return capacidad - flujo;
	}
}
