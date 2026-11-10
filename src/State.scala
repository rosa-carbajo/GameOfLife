package gameoflife

//Definimos el estado de la partida acutual
case class State(
    boards: Map[Player, GameOfLifeMatrix],
    remainingCells: Map[Player, Int],
    turn: Player
)
