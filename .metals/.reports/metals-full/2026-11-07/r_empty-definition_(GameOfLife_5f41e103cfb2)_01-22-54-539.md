error id: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala:
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -meter.
	 -meter#
	 -meter().
	 -scala/Predef.meter.
	 -scala/Predef.meter#
	 -scala/Predef.meter().
offset: 1871
uri: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala
text:
```scala
package gameoflife

import scala.annotation.tailrec

//Constructor pp de la clase matriz
class Matrix[T](val rows: Int, val columns: Int, val data: List[T]):
  require(rows >= 0 && columns >= 0 && data.length == rows * columns,
    s"Una matriz $rows x $columns necesita ${rows * columns} datos (hay ${data.length})")

  private def index(p: Position): Int = p.row * columns + p.column

  private def positionOf(i: Int): Position = Position(i / columns, i % columns)

  def inside(p: Position): Boolean = {
    (0 <= p.row) && (p.row < this.rows) && (0 <= p.column) && (p.column < this.columns)
    // Al final lo uqe queremos es 4 columnas y si ponemos <= tendriamos 5 columnas 
    //(porque empezamos por 0)
  } 


  def get(p: Position): T = {
    if(!this.inside(p)) throw new IndexOutOfBoundsException
    @tailrec
    def kEsimo(lista: List[T], k: Int): T = {
      if (k == 0) {lista.head} 
      else {kEsimo(lista.tail, k-1)} 
  } 
    kEsimo(this.data, this.index(p))
  }
   
  //Creo esta funcion adicional porque la utilizo en update, en partition y en los constructores
  @tailrec
  private def reverso(lista: List[T], lista_inver: List[T]): List[T] = lista match {
    case Nil => lista_inver
    case head :: tail => reverso(tail, head :: lista_inver)
  }

  def update(p: Position, value: T): Matrix[T] = {
    if (!this.inside(p)) throw new IndexOutOfBoundsException
    
    //No se necesita pasar value porque ya lo tenemos de fuera
    @tailrec
    def update_aux(indice:Int, lista: List[T], lista_updt: List[T]): List[T] = lista match{
      //La idea es que se recorra toda lista para pasarle todos los valores a lista_updt, pero 
      //quedarán del revés asique metemos una funcion de reverse y ya 
      case Nil => lista_updt
      case head :: tail =>
        if (indice == 0) {update_aux(indice-1, tail, value :: lista_updt)}
        else {m@@eter(indice-1, tail, head :: lista_updt)}
    }
    
    val indice:Int = this.index(p)
    val finalista:List[T] = reverso(update_aux(indice, value, this.data, Nil), Nil)

    Matrix(this.rows, this.columns, finalista)
  } 

  def neighbors(p: Position): List[Position] = ??? //TODO

  def partition(f: T => Boolean): (List[Position], List[Position]) = ??? //TODO

  def filterNeighbors(p: Position, f: T => Boolean): List[Position] = ??? //TODO

object Matrix:

  def apply[T](rows: Int, columns: Int, data: List[T]): Matrix[T] =
    new Matrix(rows, columns, data)

  def apply[T](rows: Int, columns: Int, defaultValue: T): Matrix[T] = ??? //TODO

  def apply[T](rows: Int, columns: Int, defaultValue: T, marked: List[Position], markedValue: T): Matrix[T] = ??? //TODO

```


#### Short summary: 

empty definition using pc, found symbol in pc: 