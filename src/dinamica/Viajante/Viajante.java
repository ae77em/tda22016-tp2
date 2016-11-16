package dinamica.Viajante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Viajante {
    private class Nodo {
        public int vertice;
        public Set<Integer> hijos;

        
        public Nodo(int vertice, Set<Integer> hijos) {
            this.vertice = vertice;
            this.hijos = hijos;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Nodo index = (Nodo) o;

            if (vertice != index.vertice) return false;
            return !(hijos != null ? !hijos.equals(index.hijos) : index.hijos != null);
        }

        @Override
        public int hashCode() {
            int result = vertice;
            result = 31 * result + (hijos != null ? hijos.hashCode() : 0);
            return result;
        }

    }


    private final int[][] matrizAdyacencia;
    private final List<Integer> vertices;
    private Deque<Integer> caminoSolucion;
    Map<Nodo, Integer> costoMinimoNodo;
    Map<Nodo, Integer> padres;





    public Viajante(List<Integer> vertices, int[][] matriz) {
        this.vertices = vertices;
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
    
    public void buscarCaminoMinimo() {
        List<Set<Integer>> setsHijos = crearSetsHijos();

        for(Set<Integer> set : setsHijos) {
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
        Set<Integer> set = new HashSet<>(vertices);
        
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

    private List<Set<Integer>> crearSetsHijos() {
        int entrada[] = new int[matrizAdyacencia.length -1];
        for(int i = 0; i < entrada.length; i++) {
            entrada[i] = i+1;
        }
        List<Set<Integer>> setsHijos = new ArrayList<>();
        int resultado[] = new int[entrada.length];
        crearSetHijo(entrada, 0, 0, setsHijos, resultado);
        Collections.sort(setsHijos, new ComparadorTamanio());
        return setsHijos;
    }

    private void crearSetHijo(int entrada[], int inicio, int pos, List<Set<Integer>> setsHijos, int resultado[]) {
        if(pos == entrada.length) {
            return;
        }
        Set<Integer> set = crearSet(resultado, pos);
        setsHijos.add(set);
        for(int i = inicio; i < entrada.length; i++) {
            resultado[pos] = entrada[i];
            crearSetHijo(entrada, i+1, pos+1, setsHijos, resultado);
        }
    }

    private static Set<Integer> crearSet(int input[], int pos) {
        if(pos == 0) {
            return new HashSet<>();
        }
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < pos; i++) {
            set.add(input[i]);
        }
        return set;
    }

    private static class ComparadorTamanio implements Comparator<Set<Integer>>{
        @Override
        public int compare(Set<Integer> o1, Set<Integer> o2) {
            return o1.size() - o2.size();
        }
    }


}
