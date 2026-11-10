# Plan de trabajo: El Juego de la Vida (Programación Declarativa)

**Entrega:** Viernes 13 de noviembre de 2026 (fuera de plazo = suspenso; menos de 24 h tarde = −30 %).

Somos dos: **Rosa Carbajo Rodríguez** y **Rocío Muñoz del Olmo**.
El reparto está pensado para que cada uno toque las dos mitades del proyecto (matrices y lógica del juego), porque el enunciado avisa de que, si hay sospecha, individualmente nos tocará defender la implementación. Por eso cada persona debe trabajar en su rama propia (en vez del `main`) y cuando termines realizas un Pull request, es ahí cuando la otra persona revisa el codigo en la web, hace pruebas y finalmente se hace merge para que el código pase al `main`. 

## Puntuación (para priorizar)

| Parte | Puntos | Obligatoria |
|---|---|---|
| Matrix[T] (recursiva, con tailrec) | 2 | Sí |
| GameOfLifeMatrix | 1 | Sí |
| Player y State | 0,5 | Sí |
| Estrategias (ManualStrategy) | 1 | Sí |
| Game: fase de colocación | 2 | Sí |
| Game: simulación, ganador y partida | 1 | Sí |
| Programa principal (Main) | 0,5 | Sí |
| AIStrategy | 1 | Opcional |
| MatrixList / GameOfLifeMatrixList | 0,5 | Opcional |
| Gestión de efectos | 0,5 | Opcional |

Lo obligatorio suma 8. Las opcionales solo si vamos bien de tiempo. 

## Tareas comunes (las dos)

- Crear el repositorio en GitHub y subir la estructura y la plantilla del profesor.
- Comprobar que las dos podemos ejecutar las pruebas del profesor (`MatrixTests`, etc.) desde IntelliJ o sbt.
- Leer el enunciado completo y repasar la recursión de cola y el pattern matching sobre `List`.
- Revisar las Pull Requests de la otra.
- Repasar los comentarios: cada función documentada, sin pasarse.
- Escribir cada una su parte de la memoria a partir de `docs/memoria/notas.md`.

## Reparto del trabajo

### Rosa Carbajo Rodríguez

| Parte | Qué incluye |
|---|---|
| **Matrix** (completa) | Constructores del objeto compañero (`defaultValue` y `marked`), `inside`, `get`, `update`, `partition`, `neighbors`, `filterNeighbors`. Todo recursivo y de cola, sin `map`, `filter`, `foldLeft` ni `data(i)`. |
| **GameOfLifeMatrix** | `empty`, `withAlive`, `update`, `countAliveNeighbors`, `aliveCells`, `aliveCount`, `nextGeneration`. |
| **Game: simulación y partida** | `simulate` (tailrec, con un `Set` de estados vistos), `determineWinner`, `play`. |
| **AIStrategy** (opcional) | Búsqueda aleatoria mixta (patrones compactos y dispersos) evaluada con `game.simulate`. |
| **Gestión de efectos** (opcional) | Objeto `Consola` (`Try`) y funciones de validación puras (`Either`) usadas en `ManualStrategy` y `Main`. |
| **MatrixList** (opcional) | Versión con funciones de orden superior. |
| **Integración** | Unir todas las partes en la versión final y generar la entrega. |

### Rocío Muñoz del Olmo

| Parte | Qué incluye |
|---|---|
| **Player** | `otherPlayer`, `playerName` (`State` ya viene hecho). |
| **Game: fase de colocación** | `initialState`, `hasFinished`, `placeCell`, `placementLoop` (tailrec). |
| **ManualStrategy** | Lectura recursiva de la posición con `StdIn.readLine` y caso `null`. |
| **Main** | Presupuesto de 1 a 225 con reintento, elegir humano o IA, mostrar resultados y ofrecer `Animation.animate`. |
| **GameOfLifeMatrixList** (opcional) | Versión del tablero sobre `MatrixList`. |

## Objetivos de control

Cada objetivo se comprueba antes de pasar al siguiente bloque:

1. `MatrixTests` en verde, incluida la prueba de 300x300.
2. `GameOfLifeMatrixTests` en verde, y `placeCell` probado con los ejemplos del enunciado (`Game(4, 5, 3, 100)`).
3. Ejemplo del enunciado funcionando: 5x5, 4 células, bloque (1, 4) contra esquinas (2, 0), gana Player2.
4. Partida completa jugable de principio a fin, con humano y con IA.
5. Entrega: `.zip` con solo los `.scala` de `src/`, sin pruebas, y la memoria en PDF.

## Cómo trabajar en GitHub
- `main` siempre compila. Nadie hace push directo a `main`.
- Una rama por tarea: `feature/matrix-constructores`, `feature/partition`, `feature/next-generation`, `feature/placement-loop`...
- Cada rama termina en una Pull Request que **revisa y aprueba la otra persona** antes de hacer merge (para que las dos lo entendamos)
- Opcional: crear un *issue* por cada fila del calendario y un *milestone* por semana para ver el avance.
- Apuntar en `docs/memoria/notas.md` las decisiones y problemas en el momento, no al final.
