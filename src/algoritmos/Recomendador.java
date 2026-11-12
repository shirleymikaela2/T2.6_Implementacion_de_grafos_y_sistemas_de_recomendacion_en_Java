package algoritmos;

import java.util.ArrayList;
import estructuras.Grafo;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Cada libro NO conectado directamente obtiene un punto por cada vecino común.
 * Los candidatos se ordenan por puntuación descendente y título ascendente.
 */
public final class Recomendador {
    private Recomendador() { }

    /** Lista inmutable en concepto (se devuelve una copia propia por llamada). */
    public static final class Recomendacion {
        private final String titulo;
        private final ArrayList<String> vecinosComunes;

        private Recomendacion(String titulo, ArrayList<String> vecinosComunes) {
            this.titulo = titulo;
            this.vecinosComunes = vecinosComunes;
        }

        public String getTitulo() { return titulo; }
        public int getPuntuacion() { return vecinosComunes.size(); }
        public ArrayList<String> getVecinosComunes() { return new ArrayList<>(vecinosComunes); }
        @Override
        public String toString() {
            return titulo + " | " + getPuntuacion() + " punto(s) | En común: " +
                    String.join(", ", vecinosComunes);
        }
    }

    /** Devuelve hasta limite candidatos con al menos un vecino común. */
    public static ArrayList<Recomendacion> recomendar(Grafo grafo, String titulo, int limite) {
        if (limite < 1) throw new IllegalArgumentException("El límite debe ser positivo.");
        NodoVertice origen = exigirVertice(grafo, titulo);
        ArrayList<Recomendacion> resultados = new ArrayList<>();
        for (NodoVertice candidato = grafo.getPrimero(); candidato != null;
             candidato = candidato.getSiguiente()) {
            if (candidato == origen || sonVecinos(origen, candidato)) continue;
            ArrayList<String> compartidos = comunes(origen, candidato);
            if (!compartidos.isEmpty()) {
                resultados.add(new Recomendacion(candidato.getTitulo(), compartidos));
            }
        }
        ordenar(resultados);
        while (resultados.size() > limite) resultados.remove(resultados.size() - 1);
        return resultados;
    }

    public static ArrayList<String> vecinosComunes(Grafo grafo, String tituloA, String tituloB) {
        NodoVertice a = exigirVertice(grafo, tituloA);
        NodoVertice b = exigirVertice(grafo, tituloB);
        if (a == b) throw new IllegalArgumentException("Seleccione dos libros diferentes.");
        return comunes(a, b);
    }

    private static ArrayList<String> comunes(NodoVertice a, NodoVertice b) {
        ArrayList<String> resultado = new ArrayList<>();
        for (NodoAdyacencia actual = a.getPrimerAdyacente(); actual != null;
             actual = actual.getSiguiente()) {
            NodoVertice vecino = actual.getDestino();
            if (sonVecinos(b, vecino)) resultado.add(vecino.getTitulo());
        }
        return resultado;
    }

    private static boolean sonVecinos(NodoVertice origen, NodoVertice buscado) {
        for (NodoAdyacencia arista = origen.getPrimerAdyacente(); arista != null;
             arista = arista.getSiguiente()) {
            if (arista.getDestino() == buscado) return true;
        }
        return false;
    }

    /** Inserción estable sin listas ordenadas ni estructuras prohibidas. */
    private static void ordenar(ArrayList<Recomendacion> lista) {
        for (int i = 1; i < lista.size(); i++) {
            Recomendacion actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && comparar(actual, lista.get(j)) < 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }

    private static int comparar(Recomendacion a, Recomendacion b) {
        int puntos = Integer.compare(b.getPuntuacion(), a.getPuntuacion());
        if (puntos != 0) return puntos;
        int titulo = a.getTitulo().compareToIgnoreCase(b.getTitulo());
        return titulo != 0 ? titulo : a.getTitulo().compareTo(b.getTitulo());
    }

    private static NodoVertice exigirVertice(Grafo grafo, String titulo) {
        if (grafo == null) throw new IllegalArgumentException("Grafo inexistente.");
        NodoVertice vertice = grafo.buscarVertice(titulo);
        if (vertice == null) throw new IllegalArgumentException("Libro no encontrado: " + titulo);
        return vertice;
    }
}
