import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo
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
    // Arista
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
    // Grafo
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1 — COMPLETADO
        // Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {

            // Crear un nuevo nodo con el id y nombre dados
            Nodo nodo = new Nodo(id, nombre);

            // Insertar el nodo en el mapa de nodos usando su id como clave
            nodos.put(id, nodo);

            // Inicializar la lista de aristas vacía para este nodo
            adyacencia.put(id, new ArrayList<>());
        }

        // ═══════════════════════════════════
        // TODO 2 — COMPLETADO
        // Agregar arista no dirigida
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {

            // Agregar arista de origen hacia destino con el peso dado
            adyacencia.get(origen).add(new Arista(destino, peso));

            // Agregar arista de destino hacia origen (grafo no dirigido)
            adyacencia.get(destino).add(new Arista(origen, peso));
        }

        // ═══════════════════════════════════
        // TODO 3 — COMPLETADO
        // BFS — Ruta con menos paradas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola para recorrer niveles (cada elemento es un camino completo)
            Queue<List<String>> cola = new LinkedList<>();

            // Nodos visitados para evitar ciclos
            Set<String> visitados = new HashSet<>();

            // Camino inicial que solo contiene el nodo de inicio
            List<String> caminoInicial = new ArrayList<>();

            // Agregar nodo inicio al camino inicial
            caminoInicial.add(inicio);

            // Agregar caminoInicial a la cola
            cola.add(caminoInicial);

            // Marcar inicio como visitado
            visitados.add(inicio);

            while (!cola.isEmpty()) {

                // Obtener el primer camino de la cola
                List<String> camino = cola.poll();

                // Nodo actual es el último del camino
                String actual = camino.get(camino.size() - 1);

                // Si llegamos al destino, retornar el camino encontrado
                if (actual.equals(fin)) {
                    return camino;
                }

                // Recorrer todos los vecinos del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Verificar si el vecino NO fue visitado
                    if (!visitados.contains(arista.destino)) {

                        // Marcar vecino como visitado
                        visitados.add(arista.destino);

                        // Crear nuevo camino copiando el camino actual
                        List<String> nuevoCamino = new ArrayList<>(camino);

                        // Agregar vecino al nuevo camino
                        nuevoCamino.add(arista.destino);

                        // Agregar nuevoCamino a la cola para procesar
                        cola.add(nuevoCamino);
                    }
                }
            }

            // Si no se encontró ruta, retornar null
            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — COMPLETADO
        // Dijkstra — Ruta con menor distancia
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {

            // Mapa de distancias mínimas desde el inicio a cada nodo
            Map<String, Integer> distancias = new HashMap<>();

            // Mapa de nodos anteriores para reconstruir el camino
            Map<String, String> anteriores = new HashMap<>();

            // Cola de prioridad: procesa primero el nodo con menor distancia
            PriorityQueue<String> cola = new PriorityQueue<>(
                    Comparator.comparingInt(distancias::get)
            );

            // Inicializar todas las distancias como infinito
            for (String nodo : nodos.keySet()) {
                // Distancia inicial infinita para todos los nodos
                distancias.put(nodo, Integer.MAX_VALUE);
            }

            // Distancia del nodo inicio es 0
            distancias.put(inicio, 0);

            // Agregar inicio a la cola de prioridad
            cola.add(inicio);

            while (!cola.isEmpty()) {

                // Obtener el nodo con menor distancia acumulada
                String actual = cola.poll();

                // Si llegamos al destino, detener la búsqueda
                if (actual.equals(fin)) break;

                // Recorrer todos los vecinos del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Calcular nueva distancia: distancia actual + peso de la arista
                    int nuevaDistancia = distancias.get(actual) + arista.peso;

                    // Verificar si la nueva distancia es menor a la registrada
                    if (nuevaDistancia < distancias.get(arista.destino)) {

                        // Actualizar con la nueva distancia mínima
                        distancias.put(arista.destino, nuevaDistancia);

                        // Guardar nodo anterior para reconstruir el camino
                        anteriores.put(arista.destino, actual);

                        // Agregar vecino a la cola para ser procesado
                        cola.add(arista.destino);
                    }
                }
            }

            // Reconstruir el camino desde el destino hacia el inicio
            List<String> camino = new ArrayList<>();

            String actual = fin;

            // Seguir los nodos anteriores hasta llegar al inicio (null)
            while (actual != null) {
                camino.add(0, actual);
                actual = anteriores.get(actual);
            }

            return camino;
        }

        // ═══════════════════════════════════
        // Mostrar resultado
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

                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        // NODOS — Ubicaciones del Campus UTA
        grafo.agregarNodo("uta",        "Universidad");
        grafo.agregarNodo("fisei",      "FISEI");
        grafo.agregarNodo("idiomas",    "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio",    "Estadio");
        grafo.agregarNodo("comedor",    "Comedor");

        // ARISTAS — Conexiones con pesos (metros aprox.)
        grafo.agregarArista("uta",       "fisei",      50);
        grafo.agregarArista("fisei",     "idiomas",    40);
        grafo.agregarArista("idiomas",   "biblioteca", 30);
        grafo.agregarArista("biblioteca","estadio",    70);

        // Ruta con menos paradas pero más distancia total
        grafo.agregarArista("uta",       "comedor",   20);
        grafo.agregarArista("comedor",   "estadio",  200);

        // ═══════════════════════════════════
        // PRUEBAS — Ruta de Universidad a Estadio
        // ═══════════════════════════════════

        System.out.println("===== BFS — Ruta con menos paradas =====");
        List<String> rutaBFS = grafo.bfs("uta", "estadio");
        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA — Ruta con menor distancia =====");
        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");
        grafo.mostrarRuta(rutaDijkstra);

        System.out.println("\n===== COMPARACION =====");
        System.out.println("BFS       : " + rutaBFS.size()      + " paradas");
        System.out.println("Dijkstra  : " + rutaDijkstra.size() + " paradas (ruta optima en distancia)");
    }
}
