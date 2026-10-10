# Plan de trabajo: El Juego de la Vida (Programación Declarativa)

**Entrega:** miércoles 11 de noviembre de 2026 (fuera de plazo = suspenso; menos de 24 h tarde = −30 %).

Somos dos: **Rosa Carbajo Rodríguez** y **Rocío Muñoz del Olmo**.
El reparto está pensado para que cada uno toque las dos mitades del proyecto (matrices y lógica del juego), porque el enunciado avisa de que, si hay sospecha, individualmente nos tocará defender la implementación. Por eso cada persona debe trabajar en su rama propia (en vez del main) y cuando termines realizas un Pull request, es ahí cuando la otra persona revisa el codigo en la web, hace pruebas y finalmente se hace merge para que el código pase al main. 

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

## Calendario semanal

Si tenemos exámenes en una semana concreta, movemos esa semana entera hacia delante y recortamos primero las opcionales.

### Semana 0 · 9 al 11 de octubre: arranque (para las dos)
- Crear el repo en GitHub
- Crear y subir la estructura y la plantilla.
- Comprobar podemos ejecutar `MatrixTests` desde sbt(npm o maven de Scala), que es uno de los ficheros del profesor.
- Leer el enunciado completo y repasar recursión de cola y pattern matching sobre `List`.

### Semana 1 · 12 al 18 de octubre: Matrix (2 ptos)
- **Rosa:** constructores del companion object (`defaultValue` y `marked`), `inside`, `get`, `update` (tailrec).
- **Rocío:** `partition` (tailrec), `neighbors`, `filterNeighbors`.
- **Objetivo:** `MatrixTests` en verde, incluida la prueba de 300x300.
- Recordar: sin `map`, `filter`, `foldLeft`, `data(i)`... solo recursión y `head :: tail`.

### Semana 2 · 19 al 25 de octubre: tablero y estado
- **Rosa:** `GameOfLifeMatrix`: `empty`, `withAlive`, `update`, `countAliveNeighbors`, `aliveCells`, `aliveCount`, `nextGeneration`.
- **Rocío:** `Player` (`otherPlayer`, `playerName`), `State`(que ya está hecho), y en `Game`: `initialState`, `hasFinished`, `placeCell`.
- **Objetivo:** `GameOfLifeMatrixTests` en verde; probar `placeCell` con los ejemplos del enunciado (Game(4, 5, 3, 100)).

### Semana 3 · 26 de octubre al 1 de noviembre: lógica completa del juego
- **Rosa:** `simulate` (tailrec, con `Set` de estados vistos), `determineWinner`, `play`.
- **Rocío:** `placementLoop` (tailrec) y `ManualStrategy` (lectura recursiva con `StdIn.readLine`, caso `null`).
- **Objetivo:** ejemplo del enunciado funcionando: 5x5, 4 células, bloque (1, 4) contra esquinas (2, 0), gana Player2.

### Semana 4 · 2 al 8 de noviembre: programa principal y extras
- **Rocío:** `Main` (presupuesto 1 a 225 con reintento, elegir humano o IA, mostrar resultados, ofrecer `Animation.animate`).
- **Rosa:** opcional `AIStrategy` (búsqueda aleatoria con `game.simulate`).
- **si sobra tiempo:** `MatrixList` y `GameOfLifeMatrixList` (Rosa una y Rocío otra. Además los constructores de MatrixList ya vienen hechos).
- Repasar comentarios: cada función documentada, sin pasarse.
- **Objetivo:** partida completa jugable de principio a fin.

### Semana 5 · 9 y 10 de noviembre: memoria y entrega
- Lunes 9: cada uno escribe su parte de la memoria a partir de `docs/memoria/notas.md`.
- Lunes 10: generar el `.zip` (solo los `.scala` de `src/main`, sin pruebas) y el PDF. **Subir ese día.**
- Martes 11: margen para imprevistos.

## Cómo trabajar en GitHub
- `main` siempre compila. Nadie hace push directo a `main`.
- Una rama por tarea: `feature/matrix-constructores`, `feature/partition`, `feature/next-generation`, `feature/placement-loop`...
- Cada rama termina en una Pull Request que **revisa y aprueba la otra persona** antes de hacer merge (para que las dos lo entendamos)
- Opcional: crear un *issue* por cada fila del calendario y un *milestone* por semana para ver el avance.
- Apuntar en `docs/memoria/notas.md` las decisiones y problemas en el momento, no al final.
