package algoritmos;

import java.util.ArrayList;
import estructuras.Grafo;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/** BFS con cola enlazada propia y DFS recursivo en orden de inserción. */
public final class Recorridos {
    private Recorridos() { }

    public static ArrayList<String> bfs(Grafo grafo, String inicio) {
        NodoVertice origen = exigirVertice(grafo, inicio);
        boolean[] visitado = new boolean[grafo.cantidadVertices()];
        ArrayList<String> orden = new ArrayList<>();
        Cola cola = new Cola();
        visitado[origen.getIndice()] = true;
        cola.encolar(origen);
        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();
            orden.add(actual.getTitulo());
            for (NodoAdyacencia a = actual.getPrimerAdyacente(); a != null;
                 a = a.getSiguiente()) {
                NodoVertice vecino = a.getDestino();
                if (!visitado[vecino.getIndice()]) {
                    visitado[vecino.getIndice()] = true;
                    cola.encolar(vecino);
                }
            }
        }
        return orden;
    }

    public static ArrayList<String> dfs(Grafo grafo, String inicio) {
        NodoVertice origen = exigirVertice(grafo, inicio);
        boolean[] visitado = new boolean[grafo.cantidadVertices()];
        ArrayList<String> orden = new ArrayList<>();
        visitarProfundidad(origen, visitado, orden);
        return orden;
    }

    private static void visitarProfundidad(NodoVertice actual, boolean[] visitado,
                                           ArrayList<String> orden) {
        visitado[actual.getIndice()] = true;
        orden.add(actual.getTitulo());
        for (NodoAdyacencia a = actual.getPrimerAdyacente(); a != null;
             a = a.getSiguiente()) {
            NodoVertice vecino = a.getDestino();
            if (!visitado[vecino.getIndice()]) visitarProfundidad(vecino, visitado, orden);
        }
    }

    private static NodoVertice exigirVertice(Grafo grafo, String nombre) {
        if (grafo == null) throw new IllegalArgumentException("Grafo inexistente.");
        NodoVertice vertice = grafo.buscarVertice(nombre);
        if (vertice == null) throw new IllegalArgumentException("Libro no encontrado: " + nombre);
        return vertice;
    }

    private static final class ElementoCola {
        private final NodoVertice valor;
        private ElementoCola siguiente;
        private ElementoCola(NodoVertice valor) { this.valor = valor; }
    }

    private static final class Cola {
        private ElementoCola frente;
        private ElementoCola fin;

        void encolar(NodoVertice valor) {
            ElementoCola nuevo = new ElementoCola(valor);
            if (fin == null) frente = nuevo;
            else fin.siguiente = nuevo;
            fin = nuevo;
        }

        NodoVertice desencolar() {
            if (frente == null) throw new IllegalStateException("Cola vacía.");
            NodoVertice valor = frente.valor;
            frente = frente.siguiente;
            if (frente == null) fin = null;
            return valor;
        }

        boolean estaVacia() { return frente == null; }
    }
}
