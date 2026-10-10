# El Juego de la Vida: la batalla de los patrones

Práctica de Programación Funcional (Programación Declarativa) en Scala 3.
Variante competitiva para dos jugadores del Juego de la Vida de Conway: cada jugador coloca un número
limitado de células en un tablero de 15x15 y gana quien tarde más generaciones en estabilizarse (máximo 1000).

Entrega: **11 de noviembre de 2026**. Plan y reparto en [PLAN.md](PLAN.md).

## Estructura del repositorio

.
├── README.md
├── PLAN.md
├── .gitignore
├── src/                        código que SE ENTREGA (estructura de la plantilla)
│   ├── Cell.scala              ya hecho
│   ├── Position.scala          ya hecho
│   ├── Matrix.scala
│   ├── GameOfLifeMatrix.scala  (show ya hecho)
│   ├── Player.scala
│   ├── State.scala             ya hecho
│   ├── GameResult.scala        ya hecho
│   ├── Strategy.scala
│   ├── Game.scala
│   ├── Main.scala
│   ├── Animation.scala         ya hecho
│   ├── MatrixList.scala        opcional (constructores ya hechos)
│   └── GameOfLifeMatrixList.scala  opcional
├── pruebas/                    pruebas del profe, NO se entregan
│   ├── TestKit.scala
│   ├── MatrixTests.scala
│   ├── GameOfLifeMatrixTests.scala
│   └── MatrixListTests.scala
└── docs/
    ├── enunciado.pdf
    └── memoria/
        └── notas.md            borrador de la memoria, se va llenando
```

Es la misma estructura que la plantilla del profesor; no cambiar, porque el zip tiene que ir igual.

## Ejecutar las pruebas

Cada fichero de pruebas/ es un object ... extends App: se ejecuta con el botón de ejecutar sobre el objeto (IntelliJ o VS Code con Metals). Muestra [OK] o [XX] por cada prueba


```

## Generar la entrega

El zip lleva solo los `.scala` del código, sin las pruebas:

```
mkdir -p entrega && zip -r entrega/practica.zip src/main
```

La carpeta `entrega/` está en `.gitignore`.
