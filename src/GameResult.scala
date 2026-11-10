package gameoflife

case class GameResult(
    finalState: State, // Clase de fuera
    //Map sirve para aplicar una funcion a cada uno de los elementos de esta coleccion y devolver una nueva con los resultados
    results: Map[Player, (Int, Int)], // Guarda para cada jugador el nº de generaciones sobrevividas y de celulas vivas
    winner: Option[Player] // es decir Player.Player1, Player.Player2 o None (empate)
)
