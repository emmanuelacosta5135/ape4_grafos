import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo — representa una ubicación del campus
    // Cada nodo tiene un identificador corto (id)
    // y un nombre legible para mostrar al usuario
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Arista — representa una conexión entre dos nodos
    // Guarda el destino y el peso (distancia en metros)
    // Solo almacena el extremo de llegada porque el
    // origen ya se conoce por la lista de adyacencia
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Grafo — estructura principal
    // Usa dos mapas:
    //   nodos      → id del nodo -> objeto Nodo
    //   adyacencia → id del nodo -> lista de aristas
    // Este enfoque se llama Lista de Adyacencia y es
    // eficiente para grafos dispersos (pocos vecinos)
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // agregarNodo — registra una nueva ubicación
        //
        // Se crea el objeto Nodo y se guarda en el mapa
        // de nodos usando su id como clave única.
        // También se inicializa su lista de aristas vacía
        // para que luego agregarArista() pueda usarla
        // sin riesgo de NullPointerException.
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {

            // Crear el nodo con su identificador y nombre
            Nodo nodo = new Nodo(id, nombre);

            // Registrar el nodo en el mapa principal
            nodos.put(id, nodo);

            // Preparar la lista de vecinos vacía para este nodo
            adyacencia.put(id, new ArrayList<>());
        }

        // ═══════════════════════════════════
        // agregarArista — conecta dos nodos con un peso
        //
        // El grafo es NO DIRIGIDO: si hay camino de A a B,
        // también lo hay de B a A. Por eso se añade la
        // arista en ambas direcciones dentro del mapa
        // de adyacencia. El peso representa la distancia
        // en metros entre las dos ubicaciones del campus.
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {

            // Agregar conexión de origen hacia destino
            adyacencia.get(origen).add(new Arista(destino, peso));

            // Agregar conexión de vuelta (grafo no dirigido)
            adyacencia.get(destino).add(new Arista(origen, peso));
        }

        // ═══════════════════════════════════
        // bfs — Búsqueda en Anchura (Breadth-First Search)
        //
        // Encuentra la ruta con MENOS PARADAS entre
        // dos nodos, sin importar el peso de las aristas.
        // Funciona por niveles: primero explora todos los
        // vecinos directos, luego los vecinos de vecinos, etc.
        //
        // En lugar de guardar solo el nodo actual, se guarda
        // el camino completo en cada elemento de la cola.
        // Así, cuando se llega al destino, ya se tiene la
        // ruta completa lista para retornar.
        //
        // Complejidad: O(V + E) donde V = nodos, E = aristas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola donde cada elemento es un camino completo
            // Se usa LinkedList porque permite agregar y quitar
            // elementos de ambos extremos eficientemente
            Queue<List<String>> cola = new LinkedList<>();

            // Set de visitados para no procesar el mismo nodo
            // dos veces y evitar ciclos infinitos
            Set<String> visitados = new HashSet<>();

            // El primer camino solo contiene el nodo inicial
            List<String> caminoInicial = new ArrayList<>();

            // Agregar el nodo de inicio como punto de partida
            caminoInicial.add(inicio);

            // Encolar el camino inicial para comenzar la búsqueda
            cola.add(caminoInicial);

            // Marcar el inicio como visitado para no revisitarlo
            visitados.add(inicio);

            // Mientras haya caminos por explorar en la cola
            while (!cola.isEmpty()) {

                // Sacar el primer camino pendiente de procesar
                List<String> camino = cola.poll();

                // El nodo actual es el último del camino extraído
                String actual = camino.get(camino.size() - 1);

                // Si el nodo actual es el destino, encontramos la ruta
                // BFS garantiza que esta es la de menos paradas
                if (actual.equals(fin)) {
                    return camino;
                }

                // Explorar todos los vecinos del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Solo procesar vecinos que aún no fueron visitados
                    // para evitar ciclos y rutas redundantes
                    if (!visitados.contains(arista.destino)) {

                        // Marcar como visitado antes de encolar
                        // para que otros caminos no lo vuelvan a agregar
                        visitados.add(arista.destino);

                        // Copiar el camino actual y añadir el vecino
                        // Se crea una copia para no alterar el camino original
                        List<String> nuevoCamino = new ArrayList<>(camino);

                        // Extender el camino con el vecino encontrado
                        nuevoCamino.add(arista.destino);

                        // Encolar el nuevo camino para seguir explorando
                        cola.add(nuevoCamino);
                    }
                }
            }

            // Si se vació la cola sin encontrar el destino,
            // no existe ruta entre los dos nodos dados
            return null;
        }

        // ═══════════════════════════════════
        // dijkstra — Algoritmo de Dijkstra
        //
        // Encuentra la ruta con MENOR DISTANCIA TOTAL
        // considerando el peso de cada arista.
        // A diferencia de BFS, sí toma en cuenta los metros
        // entre ubicaciones, por lo que puede preferir rutas
        // con más paradas si la distancia acumulada es menor.
        //
        // Usa una cola de prioridad (min-heap) para procesar
        // siempre primero el nodo con menor distancia conocida.
        // El mapa "anteriores" permite reconstruir el camino
        // completo al finalizar la búsqueda.
        //
        // Complejidad: O((V + E) log V) con cola de prioridad
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {

            // Mapa que guarda la distancia mínima conocida
            // desde el inicio hasta cada nodo del grafo
            Map<String, Integer> distancias = new HashMap<>();

            // Mapa que guarda de dónde se llegó a cada nodo
            // con la distancia mínima, para reconstruir la ruta
            Map<String, String> anteriores = new HashMap<>();

            // Cola de prioridad ordenada por distancia acumulada
            // Siempre extrae primero el nodo más cercano al inicio
            PriorityQueue<String> cola = new PriorityQueue<>(
                    Comparator.comparingInt(distancias::get)
            );

            // Inicializar todas las distancias como infinito
            // porque aún no se sabe cómo llegar a ningún nodo
            for (String nodo : nodos.keySet()) {
                distancias.put(nodo, Integer.MAX_VALUE);
            }

            // La distancia desde el inicio hasta sí mismo es 0
            distancias.put(inicio, 0);

            // Encolar el nodo de inicio para comenzar la exploración
            cola.add(inicio);

            while (!cola.isEmpty()) {

                // Extraer el nodo con menor distancia acumulada
                // La cola de prioridad garantiza este orden automáticamente
                String actual = cola.poll();

                // Si llegamos al destino podemos detener la búsqueda
                // ya tenemos la distancia mínima garantizada
                if (actual.equals(fin)) break;

                // Revisar todos los vecinos del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Calcular la distancia si llegáramos al vecino
                    // pasando por el nodo actual
                    int nuevaDistancia = distancias.get(actual) + arista.peso;

                    // Si esta nueva distancia es menor a la registrada,
                    // encontramos un camino más corto hacia ese vecino
                    if (nuevaDistancia < distancias.get(arista.destino)) {

                        // Actualizar con la distancia mínima encontrada
                        distancias.put(arista.destino, nuevaDistancia);

                        // Registrar que llegamos al vecino desde el nodo actual
                        // esto permite reconstruir el camino al final
                        anteriores.put(arista.destino, actual);

                        // Encolar el vecino para que sea procesado
                        // con su nueva distancia actualizada
                        cola.add(arista.destino);
                    }
                }
            }

            // Reconstruir el camino desde el destino hacia el inicio
            // siguiendo los nodos anteriores registrados en el mapa
            List<String> camino = new ArrayList<>();

            String actual = fin;

            // Retroceder desde el destino hasta el inicio
            // insertando cada nodo al principio de la lista
            while (actual != null) {
                camino.add(0, actual);
                actual = anteriores.get(actual);
            }

            return camino;
        }

        // ═══════════════════════════════════
        // mostrarRuta — imprime la ruta encontrada
        //
        // Recibe la lista de ids y traduce cada uno
        // a su nombre legible usando el mapa de nodos.
        // Muestra el formato: Nombre (id) -> Nombre (id)
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {

            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }

            for (int i = 0; i < ruta.size(); i++) {

                String idNodo = ruta.get(i);
                Nodo nodo = nodos.get(idNodo);

                System.out.print(
                    nodo.nombre + " (" + nodo.id + ")"
                );

                // Agregar flecha entre nodos, excepto al final
                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN — punto de entrada del programa
    //
    // Construye el grafo del Campus Huachi UTA,
    // define las conexiones con sus distancias
    // y ejecuta ambos algoritmos para comparar
    // sus resultados entre el mismo par de nodos.
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        // ── Nodos: ubicaciones del campus ──
        grafo.agregarNodo("uta",        "Universidad");
        grafo.agregarNodo("fisei",      "FISEI");
        grafo.agregarNodo("idiomas",    "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio",    "Estadio");
        grafo.agregarNodo("comedor",    "Comedor");

        // ── Aristas: caminos con distancia en metros ──
        // Ruta larga pero con más paradas
        grafo.agregarArista("uta",        "fisei",      50);
        grafo.agregarArista("fisei",      "idiomas",    40);
        grafo.agregarArista("idiomas",    "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio",    70);

        // Ruta corta en paradas pero mayor distancia total
        // BFS la preferirá, Dijkstra no
        grafo.agregarArista("uta",     "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);

        // ── Prueba: ruta de Universidad a Estadio ──

        System.out.println("===== BFS — Ruta con menos paradas =====");
        // BFS ignora pesos, elige: uta -> comedor -> estadio (2 saltos)
        List<String> rutaBFS = grafo.bfs("uta", "estadio");
        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA — Ruta con menor distancia =====");
        // Dijkstra suma pesos, elige: uta -> fisei -> idiomas -> biblioteca -> estadio (190 m)
        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");
        grafo.mostrarRuta(rutaDijkstra);

        System.out.println("\n===== COMPARACION =====");
        System.out.println("BFS      : " + rutaBFS.size()      + " nodos (menos paradas)");
        System.out.println("Dijkstra : " + rutaDijkstra.size() + " nodos (menor distancia)");
    }
}
