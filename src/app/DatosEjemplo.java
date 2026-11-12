package app;

import estructuras.Grafo;

/** 14 libros y 26 relaciones de similitud reproducibles para la exposición. */
public final class DatosEjemplo {
    private DatosEjemplo() { }

    public static void cargar(Grafo grafo) {
        String[] titulos = {
            "Algoritmos y Estructuras", "Java desde Cero", "Matemática Discreta",
            "Programación en Python", "Ingeniería de Software", "Clean Code",
            "Patrones de Diseño", "Bases de Datos", "Inteligencia Artificial",
            "Aprendizaje Automático", "Desarrollo Web", "Sistemas Operativos",
            "Redes de Computadoras", "Seguridad Informática"
        };
        for (String titulo : titulos) grafo.insertarVertice(titulo);
        String[][] conexiones = {
            {"Algoritmos y Estructuras", "Java desde Cero"},
            {"Algoritmos y Estructuras", "Matemática Discreta"},
            {"Algoritmos y Estructuras", "Programación en Python"},
            {"Algoritmos y Estructuras", "Ingeniería de Software"},
            {"Java desde Cero", "Clean Code"},
            {"Java desde Cero", "Patrones de Diseño"},
            {"Java desde Cero", "Desarrollo Web"},
            {"Java desde Cero", "Ingeniería de Software"},
            {"Programación en Python", "Clean Code"},
            {"Programación en Python", "Inteligencia Artificial"},
            {"Programación en Python", "Aprendizaje Automático"},
            {"Programación en Python", "Desarrollo Web"},
            {"Ingeniería de Software", "Clean Code"},
            {"Ingeniería de Software", "Patrones de Diseño"},
            {"Ingeniería de Software", "Bases de Datos"},
            {"Ingeniería de Software", "Sistemas Operativos"},
            {"Matemática Discreta", "Inteligencia Artificial"},
            {"Matemática Discreta", "Aprendizaje Automático"},
            {"Matemática Discreta", "Bases de Datos"},
            {"Matemática Discreta", "Sistemas Operativos"},
            {"Inteligencia Artificial", "Aprendizaje Automático"},
            {"Sistemas Operativos", "Redes de Computadoras"},
            {"Sistemas Operativos", "Seguridad Informática"},
            {"Redes de Computadoras", "Seguridad Informática"},
            {"Bases de Datos", "Desarrollo Web"},
            {"Patrones de Diseño", "Clean Code"}
        };
        for (String[] pareja : conexiones) grafo.crearConexion(pareja[0], pareja[1]);
    }
}
