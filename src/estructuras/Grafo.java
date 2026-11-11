package estructuras;

import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Grafo simple NO DIRIGIDO de similitud entre libros.
 * Tanto los vértices como las adyacencias se almacenan en listas enlazadas
 * implementadas con nodos propios. No usa mapas ni conjuntos.
 */
public class Grafo {
    private NodoVertice primero;
    private int cantidadVertices;
    private int cantidadAristas;

    /** Inserta un título nuevo. Retorna false si es inválido o ya existe. */
    public boolean insertarVertice(String titulo) {
        String limpio = normalizar(titulo);
        if (limpio == null || buscarVertice(limpio) != null) return false;
        NodoVertice nuevo = new NodoVertice(limpio, cantidadVertices);
        if (primero == null) {
            primero = nuevo;
        } else {
            NodoVertice actual = primero;
            while (actual.getSiguiente() != null) actual = actual.getSiguiente();
            actual.setSiguiente(nuevo);
        }
        cantidadVertices++;
        return true;
    }

    /** Busca sin distinguir mayúsculas, pero conserva el título original. */
    public NodoVertice buscarVertice(String titulo) {
        String limpio = normalizar(titulo);
        if (limpio == null) return null;
        for (NodoVertice actual = primero; actual != null; actual = actual.getSiguiente()) {
            if (actual.getTitulo().equalsIgnoreCase(limpio)) return actual;
        }
        return null;
    }

    /** Añade una única arista simétrica; no permite bucles ni duplicados. */
    public boolean crearConexion(String primeroTitulo, String segundoTitulo) {
        NodoVertice a = buscarVertice(primeroTitulo);
        NodoVertice b = buscarVertice(segundoTitulo);
        if (a == null || b == null || a == b || conectados(a, b)) return false;
        anexarAdyacencia(a, b);
        anexarAdyacencia(b, a);
        cantidadAristas++;
        return true;
    }

    /** Borra ambos extremos de la arista de manera consistente. */
    public boolean eliminarConexion(String primeroTitulo, String segundoTitulo) {
        NodoVertice a = buscarVertice(primeroTitulo);
        NodoVertice b = buscarVertice(segundoTitulo);
        if (a == null || b == null || a == b || !conectados(a, b)) return false;
        a.setPrimerAdyacente(quitarAdyacencia(a.getPrimerAdyacente(), b));
        b.setPrimerAdyacente(quitarAdyacencia(b.getPrimerAdyacente(), a));
        cantidadAristas--;
        return true;
    }

    /** Elimina el vértice, sus aristas incidentes y reindexa los restantes. */
    public boolean eliminarVertice(String titulo) {
        NodoVertice objetivo = buscarVertice(titulo);
        if (objetivo == null) return false;
        for (NodoAdyacencia arista = objetivo.getPrimerAdyacente(); arista != null;
             arista = arista.getSiguiente()) {
            NodoVertice vecino = arista.getDestino();
            vecino.setPrimerAdyacente(quitarAdyacencia(vecino.getPrimerAdyacente(), objetivo));
            cantidadAristas--;
        }
        if (primero == objetivo) {
            primero = objetivo.getSiguiente();
        } else {
            NodoVertice previo = primero;
            while (previo != null && previo.getSiguiente() != objetivo) previo = previo.getSiguiente();
            if (previo != null) previo.setSiguiente(objetivo.getSiguiente());
        }
        cantidadVertices--;
        int indice = 0;
        for (NodoVertice actual = primero; actual != null; actual = actual.getSiguiente()) {
            actual.setIndice(indice++);
        }
        return true;
    }

    public boolean existeConexion(String a, String b) {
        NodoVertice origen = buscarVertice(a);
        NodoVertice destino = buscarVertice(b);
        return origen != null && destino != null && conectados(origen, destino);
    }

    public ArrayList<String> obtenerVecinos(String titulo) {
        ArrayList<String> vecinos = new ArrayList<>();
        NodoVertice libro = buscarVertice(titulo);
        if (libro == null) return vecinos;
        for (NodoAdyacencia a = libro.getPrimerAdyacente(); a != null; a = a.getSiguiente()) {
            vecinos.add(a.getDestino().getTitulo());
        }
        return vecinos;
    }

    public String mostrarGrafo() {
        if (primero == null) return "Grafo vacío. No hay libros registrados.";
        StringBuilder salida = new StringBuilder();
        for (NodoVertice libro = primero; libro != null; libro = libro.getSiguiente()) {
            salida.append(libro.getTitulo()).append(" -> ");
            NodoAdyacencia ady = libro.getPrimerAdyacente();
            if (ady == null) salida.append("(sin conexiones)");
            while (ady != null) {
                salida.append(ady.getDestino().getTitulo());
                ady = ady.getSiguiente();
                if (ady != null) salida.append(" | ");
            }
            if (libro.getSiguiente() != null) salida.append(System.lineSeparator());
        }
        return salida.toString();
    }

    public String estadisticas() {
        int aislados = 0;
        int gradoMaximo = 0;
        String libroMayorGrado = "(ninguno)";
        for (NodoVertice libro = primero; libro != null; libro = libro.getSiguiente()) {
            int grado = contarAdyacencias(libro);
            if (grado == 0) aislados++;
            if (grado > gradoMaximo) {
                gradoMaximo = grado;
                libroMayorGrado = libro.getTitulo();
            }
        }
        double gradoPromedio = cantidadVertices == 0 ? 0 : (2.0 * cantidadAristas) / cantidadVertices;
        return String.format(java.util.Locale.ROOT,
                "Libros: %d%nRelaciones de similitud: %d%nLibros aislados: %d%n" +
                "Grado promedio: %.2f%nGrado máximo: %d (%s)%nIntegridad: %s",
                cantidadVertices, cantidadAristas, aislados, gradoPromedio,
                gradoMaximo, libroMayorGrado, validarIntegridad() ? "CORRECTA" : "ERROR");
    }

    /**
     * Comprueba índices, conteos, simetría, ausencia de bucles, vértices
     * huérfanos y referencias repetidas. Puede ejecutarse tras cada cambio.
     */
    public boolean validarIntegridad() {
        int verticesObservados = 0;
        int sumaGrados = 0;
        for (NodoVertice libro = primero; libro != null; libro = libro.getSiguiente()) {
            if (libro.getIndice() != verticesObservados || normalizar(libro.getTitulo()) == null)
                return false;
            for (NodoVertice siguiente = libro.getSiguiente(); siguiente != null;
                 siguiente = siguiente.getSiguiente()) {
                if (libro.getTitulo().equalsIgnoreCase(siguiente.getTitulo())) return false;
            }
            for (NodoAdyacencia arista = libro.getPrimerAdyacente(); arista != null;
                 arista = arista.getSiguiente()) {
                NodoVertice vecino = arista.getDestino();
                if (vecino == null || vecino == libro || !contieneReferencia(vecino)) return false;
                if (contarReferencias(libro, vecino) != 1) return false;
                if (contarReferencias(vecino, libro) != 1) return false;
                sumaGrados++;
            }
            verticesObservados++;
        }
        return verticesObservados == cantidadVertices && sumaGrados == 2 * cantidadAristas;
    }

    public int cantidadVertices() { return cantidadVertices; }
    public int cantidadAristas() { return cantidadAristas; }
    public NodoVertice getPrimero() { return primero; }

    private static String normalizar(String titulo) {
        if (titulo == null) return null;
        String limpio = titulo.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private static void anexarAdyacencia(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia nuevo = new NodoAdyacencia(destino);
        NodoAdyacencia actual = origen.getPrimerAdyacente();
        if (actual == null) {
            origen.setPrimerAdyacente(nuevo);
        } else {
            while (actual.getSiguiente() != null) actual = actual.getSiguiente();
            actual.setSiguiente(nuevo);
        }
    }

    private static NodoAdyacencia quitarAdyacencia(NodoAdyacencia cabeza, NodoVertice destino) {
        if (cabeza == null) return null;
        if (cabeza.getDestino() == destino) return cabeza.getSiguiente();
        NodoAdyacencia previo = cabeza;
        while (previo.getSiguiente() != null && previo.getSiguiente().getDestino() != destino) {
            previo = previo.getSiguiente();
        }
        if (previo.getSiguiente() != null) previo.setSiguiente(previo.getSiguiente().getSiguiente());
        return cabeza;
    }

    private static boolean conectados(NodoVertice a, NodoVertice b) {
        for (NodoAdyacencia actual = a.getPrimerAdyacente(); actual != null;
             actual = actual.getSiguiente()) {
            if (actual.getDestino() == b) return true;
        }
        return false;
    }

    private boolean contieneReferencia(NodoVertice buscado) {
        for (NodoVertice actual = primero; actual != null; actual = actual.getSiguiente()) {
            if (actual == buscado) return true;
        }
        return false;
    }

    private static int contarReferencias(NodoVertice origen, NodoVertice destino) {
        int total = 0;
        for (NodoAdyacencia actual = origen.getPrimerAdyacente(); actual != null;
             actual = actual.getSiguiente()) {
            if (actual.getDestino() == destino) total++;
        }
        return total;
    }

    private static int contarAdyacencias(NodoVertice libro) {
        int total = 0;
        for (NodoAdyacencia actual = libro.getPrimerAdyacente(); actual != null;
             actual = actual.getSiguiente()) total++;
        return total;
    }
}
