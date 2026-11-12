# Casos de prueba reproducibles | Grupo 4

## Ejecución

```bash
# Dentro del proyecto y tras compilar
java -cp bin pruebas.PruebasGrafo
java -cp bin app.DemoEvidencias
java -cp bin app.Main
```

## Datos oficiales del caso de ejemplo del equipo

14 libros y 26 aristas bidireccionales (26 relaciones únicas, 52 referencias de
adyacencia). Se cargan desde `src/app/DatosEjemplo.java`.
El libro consultado para el ejemplo es **Algoritmos y Estructuras**.

## Matriz de cobertura

| ID | Escenario | Resultado esperado |
|---|---|---|
| CP01 | Insertar y buscar título | Se encuentra el nodo, título normalizado |
| CP02 | Insertar vacío, nulo y duplicado | Rechazados |
| CP03 | Crear A-B | A contiene B, B contiene A, una arista |
| CP04 | Intentar A-B repetida o A-A | Rechazado, conteo intacto |
| CP05 | Eliminar A-B | Desaparece en ambos extremos |
| CP06 | Eliminar libro conectado | Se eliminan todas sus conexiones |
| CP07 | Borrar cabeza, cola, aislados | Índices consecutivos e integridad correcta |
| CP08 | BFS desde A | A,B,C,D,E en el grafo de 5 nodos |
| CP09 | DFS desde A | A,B,D,C,E |
| CP10 | Componentes y vértice aislado | No atraviesa componentes distintos |
| CP11 | Inicio inexistente | Error controlado |
| CP12 | Carga del conjunto | 14 vértices y 26 aristas |
| CP13 | Vecinos de A y Clean Code | 3 títulos comunes verificables |
| CP14 | Top 3 | Clean Code (3), Aprendizaje Automático (2), Bases de Datos (2) |
| CP15 | Excluir origen y vecinos directos | Sin autorrecomendación ni conexión existente |
| CP16 | Agregar A-Clean Code | Clean Code deja de recomendarse |
| CP17 | Retirar A-Clean Code | Clean Code vuelve al ranking |
| CP18 | Sin candidatos / límites inválidos | Vacío o excepción controlada |

**Resultado real de esta copia (11-11-2026): 18 correctas, 0 fallidas.** Ver `pruebas/SalidaPruebas.txt` y `pruebas/SalidaMenu.txt`.
Es obligatorio repetir las pruebas cuando se integren cambios de otras ramas.

## Capturas

- `evidencias/grafo.png`: representación de la salida real de Java.
- `evidencias/bfs-dfs.png`: recorrido real de Java.
- `evidencias/recomendaciones.png`: clasificación real del recomendador.

Estas imágenes se generaron a partir de texto ejecutado, no son fotos de una sesión de VS Code.
Si la rúbrica requiere capturas del IDE, reemplazarlas por capturas propias con terminal,
fecha/entorno y programa visible.

## Pruebas manuales aconsejadas

1. Ejecutar menú, responder `s`, seleccionar 10 y 5: ver 14 libros, 26 relaciones y `CORRECTA`.
2. Opción 9: consultar `Algoritmos y Estructuras` y constatar el top 3.
3. Opción 2: crear relación con `Clean Code`, repetir opción 9 y comprobar exclusión.
4. Opción 3: eliminar la relación creada, repetir opción 9 y comprobar recuperación.
5. Opción 1: intentar un título repetido; verificar que la cantidad no cambia.
6. Opción 4: eliminar un libro con vecinos y confirmar integridad en opción 10.
7. Probar opción `99`, letras, cadena vacía y un título inexistente.
