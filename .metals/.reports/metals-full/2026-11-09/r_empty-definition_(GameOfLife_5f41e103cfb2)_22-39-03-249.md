error id: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/GameOfLifeMatrix.scala:
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/GameOfLifeMatrix.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -aux.
	 -aux#
	 -aux().
	 -scala/Predef.aux.
	 -scala/Predef.aux#
	 -scala/Predef.aux().
offset: 1164
uri: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/GameOfLifeMatrix.scala
text:
```scala
package gameoflife

case class GameOfLifeMatrix(override val rows: Int, override val columns: Int, override val data: List[Cell])
    extends Matrix[Cell](rows, columns, data):

    override def update(p: Position, value: Cell): GameOfLifeMatrix = {
      val aux = super.update(p, value)
      GameOfLifeMatrix(aux.rows, aux.columns, aux.data)
    } 

    def aliveCells: List[Position] = {
      //Tenemos que recorrer la lista de celdas 
      partition((c:Cell) => c == Alive)._1
    } 

    def aliveCount: Int = {
      //Creamos un auxiliar que cuente las celdas vivas
      def aux(lista: List[Cell], contador: Int):Int = lista match {
        case Nil => contador
        case head :: cola => 
          if (head == Alive) aux(cola, contador +1)
          else aux(cola, contador)
      }
      //Le meto la lista de celdas vivas 
      aux(this.aliveCells, 0)

    }

    def countAliveNeighbors(p: Position): Int = {
      val vecinos: List[Position] = super.filterNeighbors(p, (c: Cell) => c == Alive)
      
      def contador(lista: List[Cell], contador: Int):Int = lista match {
        case Nil => contador
        case head :: cola => 
          c@@ontador(cola, contador +1)
      }
      contador(vecinos, 0)
    }

    def nextGeneration: GameOfLifeMatrix = ???

    //¡No tocar!
    def show(title: String = ""): String =
      val reset  = "\u001b[0m"
      val green  = "\u001b[92m"
      val dark   = "\u001b[38;5;237m"
      val dim    = "\u001b[90m"
      val frame  = "\u001b[38;5;67m"
      val yellow = "\u001b[93m"
      def square(c: Cell): String = c match
        case Alive => s"$green██$reset"
        case Dead  => s"$dark██$reset"
      val titleLine = if title.isEmpty then Nil else List(s"  $yellow$title$reset")
      val header    = s"$dim      " + List.range(0, columns).map(c => f"$c%2d").mkString(" ") + reset
      val top       = s"$frame    ┌" + "─" * (3 * columns + 1) + s"┐$reset"
      val bottom    = s"$frame    └" + "─" * (3 * columns + 1) + s"┘$reset"
      val lines     = data.grouped(columns).toList.zipWithIndex.map: (row, r) =>
        s"$dim ${f"$r%2d"} $reset$frame│$reset " + row.map(c => square(c) + " ").mkString + s"$frame│$reset"
      (titleLine ++ List(header, top) ++ lines ++ List(bottom)).mkString("\n")

    override def toString: String = show()

object GameOfLifeMatrix:
    def empty(rows: Int, columns: Int): GameOfLifeMatrix = {
      GameOfLifeMatrix(rows, columns, Matrix(rows, columns, Dead).data)
    } 

    def withAlive(rows: Int, columns: Int, alive: List[Position]): GameOfLifeMatrix = {
      Matrix(rows, columns, Dead, alive, Alive)

    } 

```


#### Short summary: 

empty definition using pc, found symbol in pc: 