package dinamica.Viajante;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Viajante {
    private class Nodo {
        public int vertice;
        public int hashCodeSetHijos;
        public boolean hijosNoNulo;

        
        public Nodo(int vertice, Set<Integer> hijos) {
            this.vertice = vertice;
            hijosNoNulo = (hijos == null);
            if (hijosNoNulo) hashCodeSetHijos = hijos.hashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Nodo index = (Nodo) o;

            if (vertice != index.vertice) return false;
            return !(hijosNoNulo ? ! (hashCodeSetHijos == index.hashCodeSetHijos) : index.hijosNoNulo);
        }

        @Override
        public int hashCode() {
            int result = vertice;
            result = 31 * result + (hijosNoNulo ? hashCodeSetHijos : 0);
            return result;
        }

    }


    private final int[][] matrizAdyacencia;
    private Deque<Integer> caminoSolucion;
    Map<Nodo, Integer> costoMinimoNodo;
    Map<Nodo, Integer> padres;





    public Viajante(int[][] matriz) {
        matrizAdyacencia = matriz;
        costoMinimoNodo = new HashMap<>();
        padres = new HashMap<>();
    }

    public int calcular() {
        buscarCaminoMinimo();
        
        Set<Integer> set = new HashSet<>();
        for(int i=1; i < matrizAdyacencia.length; i++) {
            set.add(i);
        }
        int min = buscarPadreCostoMinimo(new Nodo(0, set), set);
        
        guardarCamino();
        return min;
    }
    
    private void buscarCaminoMinimo() {
        SetFactory factory = new SetFactory(1, matrizAdyacencia.length-1);
        
        Set<Integer> set = factory.crearSet();
        while (set != null) {
            for(int verticeActual = 1; verticeActual < matrizAdyacencia.length; verticeActual++) {
                if(set.contains(verticeActual)) {
                    continue;
                }
                Nodo nodoActual = new Nodo(verticeActual, set);
                int costoMinimo = buscarPadreCostoMinimo(nodoActual, set);
                if(set.isEmpty()) {
                    costoMinimo = matrizAdyacencia[0][verticeActual];
                }
                costoMinimoNodo.put(nodoActual, costoMinimo);
            }
            set = factory.crearSet();
        }
    }
    
    private int buscarPadreCostoMinimo(Nodo nodoActual, Set<Integer> set) {
        int verticeActual = nodoActual.vertice;
        int costoMinimo = Integer.MAX_VALUE;
        int padreCostoMinimo = 0;

        Set<Integer> set2 = new HashSet<>(set);
        for(int padre : set) {
            int costo = matrizAdyacencia[padre][verticeActual] + obtenerCosto(set2, padre);
            if(costo < costoMinimo) {
                costoMinimo = costo;
                padreCostoMinimo = padre;
            }
        }
        padres.put(nodoActual, padreCostoMinimo);
        return costoMinimo;
    }

    public Deque<Integer> obtenerCamino() {
        return caminoSolucion;
    }
    
    private void guardarCamino() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < matrizAdyacencia.length; i++) {
            set.add(i);
        }
        
        Integer vertice = 0;
        caminoSolucion = new LinkedList<>();
        while(true) {
            caminoSolucion.push(vertice);
            set.remove(vertice);
            vertice = padres.get(new Nodo(vertice, set));
            if(vertice == null) {
                break;
            }
        }
    }

    private int obtenerCosto(Set<Integer> set, int verticePadre) {
        set.remove(verticePadre);
        Nodo nodo = new Nodo(verticePadre, set);
        int costo = costoMinimoNodo.get(nodo);
        set.add(verticePadre);
        return costo;
    }

    private class SetFactory {
        final int inicio;
        final int fin;
        int len;
        int ultimoBase;
        Set<Integer> setBase;
        private boolean terminar;
        
        public SetFactory(int inicio, int fin) {
            setBase = new HashSet<>();
            this.inicio = inicio;
            this.fin = fin;
            len = 0;
            ultimoBase = 0;
            terminar = false;
        }
        
        public Set<Integer> crearSet() {
            if (terminar) return null;
            if (len == 0) {
                len++;
                return new HashSet<>();
            }
            
            modificarBase();
            return (setBase.size() >= fin) ? null : new HashSet<>(setBase);
        }
        
        private void modificarBase() {
            if (ultimoBase != 0) setBase.remove(ultimoBase);
            if (ultimoBase != fin) {
                setBase.add(++ultimoBase);
                return;
            }
            if (ultimoBase == 0) {
                setBase.add(inicio);
                ultimoBase = inicio;
                return;
            }
            
            
            boolean modificado = false;
            int ultimoRemovido = ultimoBase, ultimoAgregado = 0,
                    elemento = fin -1, cantRemovidos = 1;
            
            
                    
            while (! modificado && ! setBase.isEmpty() && elemento > 0) {
                if (setBase.contains(elemento)) {
                    if (ultimoRemovido != elemento+1) {
                        modificado = true;
                        setBase.remove(elemento);
                        setBase.add(elemento + 1);
                        ultimoAgregado = elemento + 1;
                        if (ultimoBase < ultimoAgregado) ultimoBase = ultimoAgregado;
                    } else {
                        setBase.remove(elemento);
                        ultimoRemovido = elemento;
                        cantRemovidos++;
                    }
                    
                }
                elemento--;
                
            }
            if (ultimoBase == fin && setBase.isEmpty()) cantRemovidos++;
            
            for (int i = 1; i <= cantRemovidos; i++) {
                setBase.add(ultimoAgregado + i);
                ultimoBase = ultimoAgregado + i;
            }
            if (setBase.size() >= fin) {
                
                terminar = true;
            }
        }
    }

}
