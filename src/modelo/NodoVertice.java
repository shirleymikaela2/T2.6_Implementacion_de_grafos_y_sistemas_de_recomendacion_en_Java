
package modelo;

/**
 * Representa un libro como vertice de un grafo.
 * Responsable: Josue.
 */
public class NodoVertice {

    private final String titulo;
    private int indice;
    private NodoVertice siguiente;
    private NodoAdyacencia primerAdyacente;

    public NodoVertice(String titulo, int indice) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El titulo del libro no puede estar vacio."
            );
        }

        if (indice < 0) {
            throw new IllegalArgumentException(
                "El indice no puede ser negativo."
            );
        }

        this.titulo = titulo.trim();
        this.indice = indice;
        this.siguiente = null;
        this.primerAdyacente = null;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        if (indice < 0) {
            throw new IllegalArgumentException(
                "El indice no puede ser negativo."
            );
        }
        this.indice = indice;
    }

    public NodoVertice getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoVertice siguiente) {
        this.siguiente = siguiente;
    }

    public NodoAdyacencia getPrimerAdyacente() {
        return primerAdyacente;
    }

    public void setPrimerAdyacente(
            NodoAdyacencia primerAdyacente) {
        this.primerAdyacente = primerAdyacente;
    }
}
