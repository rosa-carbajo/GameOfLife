package gameoflife

class MatrixList[T](val rows: Int, val columns: Int, val data: List[T]):
  require(rows >= 0 && columns >= 0 && data.length == rows * columns,
    s"Una matriz $rows x $columns necesita ${rows * columns} datos (hay ${data.length})")

  private def index(p: Position): Int = p.row * columns + p.column
  
  private def positionOf(i: Int): Position = Position(i / columns, i % columns)

    def inside(p: Position): Boolean = 
      p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns

    def get(p: Position): T = 
      if !inside(p) then throw new IndexOutOfBoundsException(s"Posicion fuera de la matriz: $p")
      data(index(p))

    def update(p: Position, value: T): MatrixList[T] = 
      if !inside(p) then throw new IndexOutOfBoundsException(s"Posicion fuera de la matriz: $p")
      new MatrixList(rows, columns, data.updated(index(p), value))

    def neighbors(p: Position): List[Position] = 
      val desplazamientos = List(
      (-1, -1), (-1, 0), (-1, 1),
      (0, -1),           (0, 1),
      (1, -1),  (1, 0),  (1, 1)
      )
      desplazamientos.map { case (df, dc) => Position(p.row + df, p.column + dc) }.filter(inside)

    def partition(f: T => Boolean): (List[Position], List[Position]) = 
      val (cumplen, noCumplen) = cells.partition(par => f(par._2))
      (cumplen.map(_._1), noCumplen.map(_._1))

    def filterNeighbors(p: Position, f: T => Boolean): List[Position] = 
      neighbors(p).filter(vecina => f(get(vecina)))

    def cells: List[(Position, T)] = 
      data.zipWithIndex.map { case (valor, indice) => (positionOf(indice), valor) }

object MatrixList:
  def apply[T](rows: Int, columns: Int, data: List[T]): MatrixList[T] =
    new MatrixList(rows, columns, data)
  def apply[T](rows: Int, columns: Int, defaultValue: T): MatrixList[T] =
    new MatrixList(rows, columns, List.fill(rows * columns)(defaultValue))
  def apply[T](rows: Int, columns: Int, defaultValue: T, marked: List[Position], markedValue: T): MatrixList[T] =
    new MatrixList(rows, columns,
      List.tabulate(rows * columns)(i =>
        if marked.contains(Position(i / columns, i % columns)) then markedValue else defaultValue))
