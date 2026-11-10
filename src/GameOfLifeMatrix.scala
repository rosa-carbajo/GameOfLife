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
      def aux(lista: List[Position], contador: Int):Int = lista match {
        case Nil => contador
        case head :: cola => 
          //como le pasamos la lista de celdas vivas no es necesario comprobar nada
          //Simplemente contamos y vamos sumando
          aux(cola, contador +1)
      }
      //Le meto la lista de celdas vivas 
      aux(this.aliveCells, 0)
  
    }

    def countAliveNeighbors(p: Position): Int = {
      val vecinos: List[Position] = super.filterNeighbors(p, (c: Cell) => c == Alive)
      
      //Este contador es casi igual que aux de aliveCount, en espiritu lo es 
      def contar(lista: List[Position], cont: Int):Int = lista match {
        case Nil => cont
        case head :: cola => 
          contar(cola, cont +1)
      }
      contar(vecinos, 0)
    }

    def nextGeneration: GameOfLifeMatrix = {
    @annotation.tailrec
    def avanzar(resto: List[Cell], i: Int, acumulado: List[Cell]): List[Cell] = resto match {
      case Nil => reverso(acumulado, Nil)
      case celda :: cola =>
        //defino la celula que estoy mirando 
        val p = Position(i / columns, i % columns)
        //cuento sus vecinos vivos
        val vecinas = countAliveNeighbors(p)
        //Y veo si la celula va a vivir o morir en la siguiente generacion
        val siguiente: Cell = celda match
          //Vive si tiene 2 o 3 vecinos vivos y muere si no 
          case Alive => if vecinas == 2 || vecinas == 3 then Alive else Dead
          case Dead  => if vecinas == 3 then Alive else Dead
        avanzar(cola, i + 1, siguiente :: acumulado)
      }
      // Y devolvemos la matriz siguiente (o siguiente gen de células)
    GameOfLifeMatrix(rows, columns, avanzar(data, 0, Nil))
    }
    
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
      GameOfLifeMatrix(rows, columns, Matrix(rows, columns, Dead: Cell, alive, Alive: Cell).data)
    } 
