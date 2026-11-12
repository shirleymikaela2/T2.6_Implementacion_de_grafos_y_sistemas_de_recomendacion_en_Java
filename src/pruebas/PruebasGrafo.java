package pruebas;

import java.util.ArrayList;
import algoritmos.Recorridos;
import algoritmos.Recomendador;
import estructuras.Grafo;
import app.DatosEjemplo;

/** Suite autónoma de pruebas sin dependencias externas (Java 17). */
public final class PruebasGrafo {
    private static int correctas;
    private static int fallidas;

    public static void main(String[] args) {
        probar("alta de vértices, búsqueda y títulos normalizados", () -> {
            Grafo g = new Grafo();
            ok(g.insertarVertice("  Libro A  "));
            ok(g.buscarVertice("libro a") != null);
            igual("Libro A", g.buscarVertice("Libro A").getTitulo());
            igual(1, g.cantidadVertices());
        });
        probar("rechazo de vértices vacíos, nulos y duplicados", () -> {
            Grafo g = new Grafo();
            ok(!g.insertarVertice(null));
            ok(!g.insertarVertice("  "));
            ok(g.insertarVertice("Libro A"));
            ok(!g.insertarVertice("libro a"));
            igual(1, g.cantidadVertices());
        });
        probar("arista bidireccional y conteo único", () -> {
            Grafo g = base("A", "B");
            ok(g.crearConexion("A", "B"));
            ok(g.existeConexion("A", "B") && g.existeConexion("B", "A"));
            igual(1, g.cantidadAristas());
            ok(g.validarIntegridad());
        });
        probar("aristas duplicadas, bucles y extremos inexistentes", () -> {
            Grafo g = base("A", "B");
            ok(g.crearConexion("A", "B"));
            ok(!g.crearConexion("B", "A"));
            ok(!g.crearConexion("A", "A"));
            ok(!g.crearConexion("A", "Z"));
            igual(1, g.cantidadAristas());
        });
        probar("eliminación simétrica de arista", () -> {
            Grafo g = base("A", "B");
            ok(g.crearConexion("A", "B"));
            ok(g.eliminarConexion("B", "A"));
            ok(!g.existeConexion("B", "A"));
            ok(!g.eliminarConexion("A", "B"));
            igual(0, g.cantidadAristas());
            ok(g.validarIntegridad());
        });
        probar("eliminar vértice limpia aristas incidentes", () -> {
            Grafo g = base("A", "B", "C", "D");
            g.crearConexion("A", "B");
            g.crearConexion("A", "C");
            g.crearConexion("B", "D");
            ok(g.eliminarVertice("A"));
            ok(g.buscarVertice("A") == null);
            igual(1, g.cantidadAristas());
            igual(3, g.cantidadVertices());
            ok(g.obtenerVecinos("C").isEmpty());
            ok(g.validarIntegridad());
        });
        probar("eliminar cabeza, cola, vértice aislado y grafo vacío", () -> {
            Grafo g = base("A", "B", "C");
            ok(g.eliminarVertice("A"));
            ok(g.eliminarVertice("C"));
            ok(g.eliminarVertice("B"));
            igual(0, g.cantidadVertices());
            ok(g.validarIntegridad());
            ok(!g.eliminarVertice("Z"));
        });
        probar("BFS respeta niveles y orden de inserción", () -> {
            Grafo g = recorrido();
            igual("A,B,C,D,E", String.join(",", Recorridos.bfs(g, "A")));
        });
        probar("DFS recursivo respeta profundidad y orden", () -> {
            Grafo g = recorrido();
            igual("A,B,D,C,E", String.join(",", Recorridos.dfs(g, "A")));
        });
        probar("aislados y componentes desconectados", () -> {
            Grafo g = base("A", "B", "C", "D");
            g.crearConexion("A", "B");
            g.crearConexion("C", "D");
            igual("A,B", String.join(",", Recorridos.bfs(g, "A")));
            igual("C,D", String.join(",", Recorridos.dfs(g, "C")));
            g.insertarVertice("E");
            igual("E", String.join(",", Recorridos.bfs(g, "E")));
        });
        probar("BFS y DFS rechazan un inicio inexistente", () -> {
            Grafo g = base("A");
            errorEsperado(() -> Recorridos.bfs(g, "Z"));
            errorEsperado(() -> Recorridos.dfs(g, " "));
        });
        probar("mínimo de 10 vértices y 15 aristas en la demo", () -> {
            Grafo g = demo();
            igual(14, g.cantidadVertices());
            igual(26, g.cantidadAristas());
            ok(g.validarIntegridad());
        });
        probar("vecinos comunes: tres puentes verificables", () -> {
            Grafo g = demo();
            ArrayList<String> comunes = Recomendador.vecinosComunes(g, "Algoritmos y Estructuras", "Clean Code");
            igual(3, comunes.size());
            ok(comunes.contains("Java desde Cero"));
            ok(comunes.contains("Programación en Python"));
            ok(comunes.contains("Ingeniería de Software"));
        });
        probar("top 3 ordenado y empate alfabético determinista", () -> {
            Grafo g = demo();
            ArrayList<Recomendador.Recomendacion> lista = Recomendador.recomendar(g, "Algoritmos y Estructuras", 3);
            igual(3, lista.size());
            igual("Clean Code", lista.get(0).getTitulo());
            igual(3, lista.get(0).getPuntuacion());
            igual("Aprendizaje Automático", lista.get(1).getTitulo());
            igual("Bases de Datos", lista.get(2).getTitulo());
            igual(2, lista.get(1).getPuntuacion());
            igual(2, lista.get(2).getPuntuacion());
        });
        probar("nunca recomendar origen ni conexión existente", () -> {
            Grafo g = demo();
            for (Recomendador.Recomendacion r : Recomendador.recomendar(g, "Algoritmos y Estructuras", 100)) {
                ok(!r.getTitulo().equals("Algoritmos y Estructuras"));
                ok(!g.existeConexion("Algoritmos y Estructuras", r.getTitulo()));
            }
        });
        probar("un candidato desaparece al crear conexión directa", () -> {
            Grafo g = demo();
            ok(g.crearConexion("Algoritmos y Estructuras", "Clean Code"));
            for (Recomendador.Recomendacion r : Recomendador.recomendar(g, "Algoritmos y Estructuras", 100)) {
                ok(!r.getTitulo().equals("Clean Code"));
            }
            ok(g.validarIntegridad());
        });
        probar("tras eliminar conexión se recalculan candidatos", () -> {
            Grafo g = demo();
            g.crearConexion("Algoritmos y Estructuras", "Clean Code");
            g.eliminarConexion("Algoritmos y Estructuras", "Clean Code");
            igual("Clean Code", Recomendador.recomendar(g, "Algoritmos y Estructuras", 3).get(0).getTitulo());
        });
        probar("sin candidatos, sin duplicados y entrada inválida", () -> {
            Grafo g = base("A", "B", "C");
            ok(Recomendador.recomendar(g, "A", 3).isEmpty());
            g.crearConexion("A", "B");
            g.crearConexion("B", "C");
            ArrayList<Recomendador.Recomendacion> lista = Recomendador.recomendar(g, "A", 3);
            igual(1, lista.size());
            igual("C", lista.get(0).getTitulo());
            errorEsperado(() -> Recomendador.recomendar(g, "A", 0));
            errorEsperado(() -> Recomendador.recomendar(g, "Z", 3));
            errorEsperado(() -> Recomendador.vecinosComunes(g, "A", "A"));
        });
        System.out.printf("%nRESULTADO FINAL: %d correctas, %d fallidas.%n", correctas, fallidas);
        if (fallidas > 0) System.exit(1);
    }

    private static Grafo base(String... titulos) {
        Grafo g = new Grafo();
        for (String titulo : titulos) g.insertarVertice(titulo);
        return g;
    }

    private static Grafo recorrido() {
        Grafo g = base("A", "B", "C", "D", "E");
        g.crearConexion("A", "B");
        g.crearConexion("A", "C");
        g.crearConexion("B", "D");
        g.crearConexion("C", "E");
        return g;
    }

    private static Grafo demo() {
        Grafo g = new Grafo();
        DatosEjemplo.cargar(g);
        return g;
    }

    private static void probar(String nombre, Runnable accion) {
        try {
            accion.run();
            correctas++;
            System.out.println("[OK] " + nombre);
        } catch (Throwable error) {
            fallidas++;
            System.out.println("[FALLO] " + nombre + ": " + error.getMessage());
        }
    }

    private static void ok(boolean condicion) {
        if (!condicion) throw new AssertionError("Se esperaba una condición verdadera.");
    }

    private static void igual(Object esperado, Object recibido) {
        if (!esperado.equals(recibido)) {
            throw new AssertionError("Esperado=" + esperado + " / recibido=" + recibido);
        }
    }

    private static void errorEsperado(Runnable accion) {
        try {
            accion.run();
        } catch (IllegalArgumentException esperado) {
            return;
        }
        throw new AssertionError("Se esperaba IllegalArgumentException.");
    }
}
