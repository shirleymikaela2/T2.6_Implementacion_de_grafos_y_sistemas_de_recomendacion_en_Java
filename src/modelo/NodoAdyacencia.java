
package modelo;

/**
 * Representa un nodo de una lista de adyacencia.
 * Responsable: Josue.
 */
public class NodoAdyacencia {

    private final NodoVertice destino;
    private NodoAdyacencia siguiente;

    public NodoAdyacencia(NodoVertice destino) {
        if (destino == null) {
            throw new IllegalArgumentException(
                "El vertice destino no puede ser nulo."
            );
        }

        this.destino = destino;
        this.siguiente = null;
    }

    public NodoVertice getDestino() {
        return destino;
    }

    public NodoAdyacencia getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoAdyacencia siguiente) {
        this.siguiente = siguiente;
    }
}

