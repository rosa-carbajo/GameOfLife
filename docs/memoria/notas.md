**1. Decisiones y dificultades**

> **Matrices recursivas.** La decisión central fue usar recursión de cola con acumulador en todas las funciones que recorren la lista, para que funcionen con 90.000 elementos sin desbordar la pila. Como el acumulador construye la lista al revés, escribimos un único método genérico para invertirla y lo reutilizamos con distintos tipos. Para `partition` evitamos llamar a `get` en cada posición, porque cada llamada recorre la lista desde el principio y el coste sería cuadrático. En su lugar hicimos un único recorrido con un contador. En el constructor con posiciones marcadas convertimos primero las posiciones en índices y rellenamos la lista desde el final, así sale ya en orden sin tener que invertirla. Las principales dificultades fueron un primer `update` que devolvía la misma matriz en vez de una nueva, una condición de límites invertida y darnos cuenta de que en el objeto compañero no existe todavía una matriz sobre la que comprobar los límites. En el tablero del juego, lo más delicado fue calcular cada generación siempre a partir del tablero anterior completo, nunca de uno a medio actualizar. También nos costó la sintaxis `Dead: Cell`, necesaria para que la matriz se cree como `Matrix[Cell]`.

> **Lógica del juego.** Mantuvimos la clase `Game` libre de entrada/salida, de modo que toda la partida se puede probar con estrategias fijas, sin teclado. Los errores de colocación se devuelven con `Either` en lugar de excepciones: así el bucle de colocación puede volver a preguntar al mismo jugador pasándole el motivo, sin saber si es humano o IA. Para detectar cuándo se estabiliza un tablero guardamos los estados ya vistos en un `Set`. Como el tablero es una case class, dos tableros iguales son `==`, y una sola comprobación cubre tanto los patrones estables como los ciclos de cualquier longitud.

> **Strategy y Main: gestión de efectos.** En las primeras versiones, `ManualStrategy` y `Main` leían del teclado cada uno por su cuenta y, si se cerraba la entrada, cortaban el programa con `sys.exit`. Al revisarlo, con ayuda de una herramienta de IA, vimos que cada lectura mezclaba dos tareas: leer el texto y comprobar si era válido. Las separamos. Para validar están las funciones puras `parsePosition`, `parseIntEnRango`, `parseSiNo` y `parseHumanoIA`, que devuelven `Either`. Para leer está el objeto `Consola`: `leerLinea` convierte el `null` de `readLine` en un `Failure`, y `leerHasta` sigue preguntando hasta que el texto es válido. Además dividimos `Main` en funciones pequeñas (`elegirEstrategia`, `mostrarResultado`, `partida`). La partida se ejecuta dentro de un `Try`, que es el único punto donde se trata el cierre de la entrada. Esto nos obligó a estudiar partes de Scala que no conocíamos, como pasar funciones como parámetro y usar `Try` y `Either` para tratar errores sin excepciones.

**2. Heurística de la IA**
> Al principio generábamos patrones totalmente al azar, pero las células quedaban tan separadas que casi siempre morían en la primera generación. La cambiamos por una búsqueda aleatoria mixta: la mitad de los intentos agrupan las células en una zona central y la otra mitad las reparten por todo el tablero. Cada patrón se simula con `game.simulate` y se elige el mejor.
> - *Qué mide:* las generaciones que tarda en estabilizarse y, si hay empate, las células vivas. Es el mismo criterio que decide el ganador.
> - *Cómo lo aproxima:* probar todas las combinaciones es imposible, así que evalúa 40 patrones. Los compactos favorecen que las células interactúen, que es lo que alarga la partida.
> - *Cuándo puede fallar:* con pocos intentos depende de la suerte, no conoce patrones longevos famosos y, como cada intento se simula hasta 1000 generaciones, aumentar mucho los intentos la hace lenta.

**3. Extras**

> **Matrices con funciones de orden superior.** La segunda versión, con los métodos de `List`, fue mucho más corta y fácil de razonar: casi todos los métodos se quedan en una línea, sin acumuladores ni inversiones. Aun así, la versión recursiva nos ayudó a entender qué hacen esas funciones por dentro. Además, el tablero es prácticamente idéntico en las dos versiones, porque solo cambia la clase de la que hereda: al apoyarse en los métodos heredados, no depende de cómo esté implementada la matriz.

**4. Coordinación y reparto**

> Antes de empezar preparamos un plan semanal que repartía las tareas para que las dos trabajáramos tanto en las matrices como en la lógica del juego. Trabajamos cada una en su propia rama e integramos los cambios mediante Pull Requests revisadas por la otra. Rosa se encargó además de unir todas las partes en la versión final. El plan con el reparto detallado y el historial del trabajo están en el repositorio: [https://github.com/rosa-carbajo/GameOfLife]. Al terminar revisamos juntas el código, los comentarios y esta memoria.


---




