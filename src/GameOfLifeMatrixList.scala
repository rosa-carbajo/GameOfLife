package gameoflife

case class GameOfLifeMatrixList(override val rows: Int, override val columns: Int, override val data: List[Cell])
    extends MatrixList[Cell](rows, columns, data):

    override def update(p: Position, value: Cell): GameOfLifeMatrixList = 
        GameOfLifeMatrixList(rows, columns, super.update(p, value).data)

    def aliveCells: List[Position] =  partition(_ == Alive)._1

    def aliveCount: Int = aliveCells.length

    def countAliveNeighbors(p: Position): Int = filterNeighbors(p, _ == Alive).length

    def nextGeneration: GameOfLifeMatrixList = 
        val nuevosDatos: List[Cell] = cells.map { case (posicion, celda) =>
        val vecinas = countAliveNeighbors(posicion)
        celda match
            case Alive => if vecinas == 2 || vecinas == 3 then Alive else Dead
            case Dead  => if vecinas == 3 then Alive else Dead
        }
        GameOfLifeMatrixList(rows, columns, nuevosDatos)


    def toMatrix: GameOfLifeMatrix = GameOfLifeMatrix(rows, columns, data)
    override def toString: String = toMatrix.show()

object GameOfLifeMatrixList:
    def empty(rows: Int, columns: Int): GameOfLifeMatrixList = 
        GameOfLifeMatrixList(rows, columns, MatrixList(rows, columns, Dead: Cell).data)

    def withAlive(rows: Int, columns: Int, alive: List[Position]): GameOfLifeMatrixList = 
         GameOfLifeMatrixList(rows, columns, MatrixList(rows, columns, Dead: Cell, alive, Alive: Cell).data)
