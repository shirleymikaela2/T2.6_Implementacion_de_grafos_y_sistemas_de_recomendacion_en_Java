package app;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import algoritmos.Recorridos;
import algoritmos.Recomendador;
import estructuras.Grafo;

/** Aplicación de consola: T3 de grafos, caso especial Grupo 4 (libros). */
public final class Main {
    private final Grafo grafo = new Grafo();
    private final Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) { new Main().iniciar(); }

    private void iniciar() {
        System.out.println("=== T3 | GRUPO 4 | RECOMENDACIÓN DE LIBROS ===");
        System.out.println("Una conexión indica similitud entre dos libros.");
        String respuesta = pedir("¿Cargar 14 libros y 26 relaciones de prueba? (s/n): ");
        if (respuesta == null) return;
        if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") ||
            respuesta.equalsIgnoreCase("sí")) {
            DatosEjemplo.cargar(grafo);
            System.out.println("Datos cargados. Integridad: " + grafo.validarIntegridad());
        }
        while (true) {
            mostrarMenu();
            String entrada = pedir("Opción: ");
            if (entrada == null) return;
            int opcion;
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException ex) {
                System.out.println("Error: ingrese un número entero de 0 a 10.");
                continue;
            }
            if (opcion == 0) {
                System.out.println("Fin del programa.");
                return;
            }
            if (opcion < 0 || opcion > 10) {
                System.out.println("Opción fuera de rango. Use 0 a 10.");
                continue;
            }
            try { procesarOpcion(opcion); }
            catch (IllegalArgumentException ex) { System.out.println("Error: " + ex.getMessage()); }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n---------- MENÚ ----------");
        System.out.println("1. Registrar libro");
        System.out.println("2. Crear relación de similitud");
        System.out.println("3. Eliminar relación");
        System.out.println("4. Eliminar libro");
        System.out.println("5. Visualizar grafo");
        System.out.println("6. Recorrer mediante BFS");
        System.out.println("7. Recorrer mediante DFS");
        System.out.println("8. Mostrar vecinos comunes");
        System.out.println("9. Recomendar libros (top 3)");
        System.out.println("10. Mostrar estadísticas e integridad");
        System.out.println("0. Salir");
    }

    private void procesarOpcion(int opcion) {
        String a;
        String b;
        switch (opcion) {
            case 1:
                a = pedirTitulo("Título del nuevo libro: ");
                if (a != null) informar(grafo.insertarVertice(a), "Libro registrado.", "Título inválido o repetido.");
                break;
            case 2:
                a = pedirTitulo("Primer libro: ");
                b = pedirTitulo("Segundo libro: ");
                if (a != null && b != null) informar(grafo.crearConexion(a, b),
                    "Relación bidireccional creada.", "No se pudo crear: libros inexistentes, relación repetida o mismo libro.");
                break;
            case 3:
                a = pedirTitulo("Primer libro: ");
                b = pedirTitulo("Segundo libro: ");
                if (a != null && b != null) informar(grafo.eliminarConexion(a, b),
                    "Relación eliminada en ambos extremos.", "No existe esa relación.");
                break;
            case 4:
                a = pedirTitulo("Libro que desea eliminar: ");
                if (a != null) informar(grafo.eliminarVertice(a),
                    "Libro y relaciones asociadas eliminados.", "El libro no existe.");
                break;
            case 5:
                System.out.println(grafo.mostrarGrafo());
                break;
            case 6:
                a = pedirTitulo("Libro inicial BFS: ");
                if (a != null) mostrarRecorrido("BFS", Recorridos.bfs(grafo, a));
                break;
            case 7:
                a = pedirTitulo("Libro inicial DFS: ");
                if (a != null) mostrarRecorrido("DFS", Recorridos.dfs(grafo, a));
                break;
            case 8:
                a = pedirTitulo("Primer libro: ");
                b = pedirTitulo("Segundo libro: ");
                if (a != null && b != null) {
                    validarExistencia(a);
                    validarExistencia(b);
                    ArrayList<String> comunes = Recomendador.vecinosComunes(grafo, a, b);
                    System.out.println("Vecinos comunes (" + comunes.size() + "): " +
                        (comunes.isEmpty() ? "ninguno" : String.join(", ", comunes)));
                }
                break;
            case 9:
                a = pedirTitulo("Libro para recomendar similares indirectos: ");
                if (a != null) {
                    ArrayList<Recomendador.Recomendacion> lista = Recomendador.recomendar(grafo, a, 3);
                    System.out.println("TOP 3 de recomendaciones para " + grafo.buscarVertice(a).getTitulo() + ":");
                    if (lista.isEmpty()) System.out.println("No existen candidatos indirectos válidos.");
                    for (int i = 0; i < lista.size(); i++) {
                        System.out.println((i + 1) + ". " + lista.get(i));
                    }
                }
                break;
            case 10:
                System.out.println(grafo.estadisticas());
                break;
            default:
                throw new IllegalArgumentException("Opción no reconocida.");
        }
    }

    private void validarExistencia(String titulo) {
        if (grafo.buscarVertice(titulo) == null) throw new IllegalArgumentException("Libro no encontrado: " + titulo);
    }

    private void mostrarRecorrido(String tipo, ArrayList<String> orden) {
        System.out.println(tipo + " (" + orden.size() + " libro(s)): " + String.join(" -> ", orden));
    }

    private String pedir(String mensaje) {
        System.out.print(mensaje);
        return teclado.hasNextLine() ? teclado.nextLine().trim() : null;
    }

    private String pedirTitulo(String mensaje) {
        String titulo = pedir(mensaje);
        if (titulo != null && titulo.isBlank()) {
            System.out.println("Error: el título no puede estar vacío.");
            return null;
        }
        return titulo;
    }

    private void informar(boolean exito, String mensajeBien, String mensajeError) {
        System.out.println(exito ? mensajeBien : mensajeError);
        if (!grafo.validarIntegridad()) {
            throw new IllegalStateException("Se detectó una inconsistencia en la estructura.");
        }
    }
}
