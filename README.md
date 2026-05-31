# APE 4 — Grafos: Mapa del Campus UTA

## Estructura de Datos — Universidad Técnica de Ambato

---

## Descripción

Implementación de un grafo mediante lista de adyacencia para representar
las rutas entre ubicaciones del Campus Huachi de la UTA. El proyecto
aplica dos algoritmos de búsqueda de caminos, BFS y Dijkstra, para
encontrar rutas entre nodos del grafo y comparar sus resultados.

---

## Autor

Acosta Gavilanes Emmanuel Alejandro  
Carrera de Software — Nivel 3B  
Estructura de Datos  
Universidad Técnica de Ambato  
Ciclo académico: Enero 2026 – Julio 2026

---

## Objetivo

Implementar un grafo con lista de adyacencia que represente el Campus
Huachi de la UTA y comparar el comportamiento de BFS y Dijkstra al
encontrar rutas entre dos ubicaciones.

---

## Topología del grafo
Universidad --50-- FISEI --40-- Idiomas --30-- Biblioteca --70-- Estadio
|
20
|
Comedor -----------------200------------------- Estadio

Los pesos representan distancias aproximadas en metros entre cada
ubicación del campus.

---

## Nodos

| ID | Nombre |
|:--:|:------:|
| uta | Universidad |
| fisei | FISEI |
| idiomas | Idiomas |
| biblioteca | Biblioteca |
| estadio | Estadio |
| comedor | Comedor |

---

## Aristas

| Origen | Destino | Distancia (m) |
|:------:|:-------:|:-------------:|
| Universidad | FISEI | 50 |
| FISEI | Idiomas | 40 |
| Idiomas | Biblioteca | 30 |
| Biblioteca | Estadio | 70 |
| Universidad | Comedor | 20 |
| Comedor | Estadio | 200 |

---

## Métodos implementados

### agregarNodo()
Registra una nueva ubicación en el grafo. Crea el objeto Nodo con su
identificador y nombre, lo inserta en el mapa de nodos e inicializa
su lista de aristas vacía en el mapa de adyacencia.

### agregarArista()
Conecta dos nodos con un peso dado. Debido a que el grafo es no
dirigido, la arista se agrega en ambas direcciones dentro del mapa
de adyacencia.

### bfs()
Implementa la búsqueda en anchura. Recorre el grafo por niveles
usando una cola donde cada elemento es un camino completo. Retorna
la ruta con menor número de paradas entre el origen y el destino,
sin considerar los pesos de las aristas.

### dijkstra()
Implementa el algoritmo de Dijkstra. Usa una cola de prioridad para
procesar siempre el nodo con menor distancia acumulada. Retorna la
ruta con menor distancia total considerando el peso de cada arista.
Reconstruye el camino siguiendo el mapa de nodos anteriores desde
el destino hacia el origen.

---

## Comparación de resultados

| Algoritmo | Ruta encontrada | Paradas | Distancia total |
|:---------:|:---------------:|:-------:|:---------------:|
| BFS | Universidad → Comedor → Estadio | 2 | 220 m |
| Dijkstra | Universidad → FISEI → Idiomas → Biblioteca → Estadio | 4 | 190 m |

BFS seleccionó la ruta con menos paradas sin considerar la distancia.
Dijkstra descartó esa ruta porque su costo acumulado es mayor y
encontró una ruta de menor distancia total, aunque con más paradas.

---

## Estructura del proyecto
Proyecto_APE4/
│
├── src/
│   └── APE4_Grafos.java
│
├── captura/
│   └── captura1.png
│
└── README.md

---

## Compilación y ejecución

Compilar:

```bash
javac APE4_Grafos.java
```

Ejecutar:

```bash
java APE4_Grafos
```

---

## Conceptos aplicados

**Lista de adyacencia.** Estructura que representa para cada nodo la
lista de sus vecinos con el peso de cada conexión. Es eficiente para
grafos dispersos donde cada nodo tiene pocos vecinos.

**BFS.** Busca la ruta con menor número de paradas expandiendo el
grafo nivel por nivel desde el origen.

**Dijkstra.** Busca la ruta con menor distancia total considerando
el peso de las aristas. Usa una cola de prioridad para garantizar
que siempre se procese primero el nodo más cercano al origen.

---

## Resultado esperado en consola
===== BFS — Ruta con menos paradas =====
Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)
===== DIJKSTRA — Ruta con menor distancia =====
Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
===== COMPARACION =====
BFS      : 3 nodos (menos paradas)
Dijkstra : 5 nodos (menor distancia)
