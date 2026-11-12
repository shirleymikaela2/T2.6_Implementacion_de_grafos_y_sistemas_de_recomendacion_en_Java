package app;

import algoritmos.Recorridos;
import algoritmos.Recomendador;
import estructuras.Grafo;

/** Salidas reales reproducibles, útiles para capturas y defensa. */
public final class DemoEvidencias {
    private DemoEvidencias() { }

    public static void main(String[] args) {
        Grafo g = new Grafo();
        DatosEjemplo.cargar(g);
        System.out.println("=== GRAFO ===");
        System.out.println(g.mostrarGrafo());
        System.out.println(g.estadisticas());
        System.out.println("=== BFS-DFS ===");
        System.out.println("Inicio: Algoritmos y Estructuras");
        System.out.println("BFS: " + String.join(" -> ", Recorridos.bfs(g, "Algoritmos y Estructuras")));
        System.out.println("DFS: " + String.join(" -> ", Recorridos.dfs(g, "Algoritmos y Estructuras")));
        System.out.println("=== RECOMENDACIONES ===");
        System.out.println("Libro consultado: Algoritmos y Estructuras");
        int n = 1;
        for (Recomendador.Recomendacion r : Recomendador.recomendar(g, "Algoritmos y Estructuras", 3)) {
            System.out.println(n++ + ". " + r);
        }
    }
}
