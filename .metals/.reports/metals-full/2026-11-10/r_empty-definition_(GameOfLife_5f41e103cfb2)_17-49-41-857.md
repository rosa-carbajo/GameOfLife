error id: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala:local7
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -lista_updt.
	 -lista_updt#
	 -lista_updt().
	 -scala/Predef.lista_updt.
	 -scala/Predef.lista_updt#
	 -scala/Predef.lista_updt().
offset: 2063
uri: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala
text:
```scala
package gameoflife

import scala.annotation.tailrec

class Matrix[T](val rows: Int, val columns: Int, val data: List[T]):
  require(rows >= 0 && columns >= 0 && data.length == rows * columns,
    s"Una matriz $rows x $columns necesita ${rows * columns} datos (hay ${data.length})")

  private def index(p: Position): Int = p.row * columns + p.column

  private def positionOf(i: Int): Position = Position(i / columns, i % columns)

  // Al final lo que queremos es 4 columnas y si ponemos <= tendriamos 5 columnas 
    //(porque empezamos por 0)
  def inside(p: Position): Boolean = {
    (0 <= p.row) && (p.row < this.rows) && (0 <= p.column) && (p.column < this.columns)
  } 


  def get(p: Position): T = {
    if(!this.inside(p)) throw new IndexOutOfBoundsException
    @tailrec
    def kEsimo(lista: List[T], k: Int): T = lista match {
      case Nil => throw new IndexOutOfBoundsException
      case head :: tail => if (k == 0) head else kEsimo(tail, k - 1)
    }
    kEsimo(this.data, this.index(p))
  }
   
  // Creo esta funcion adicional porque la utilizo en update y partition
  // Y más tarde la utilizo en GameOfLifeMatrix para aliveCells
  // He tenido que definirlo como final porque se exige que el metodo no se pueda sobreescribir en una subclase y que así podamos hacer el bucle
  @tailrec
  protected final def reverso[A](lista: List[A], lista_inver: List[A]): List[A] = lista match {
    case Nil => lista_inver
    case head :: tail => reverso(tail, head :: lista_inver)
  }

  def update(p: Position, value: T): Matrix[T] = {
    if (!this.inside(p)) throw new IndexOutOfBoundsException
    
    //No se necesita pasar value porque ya lo tenemos de fuera
    @tailrec
    def update_aux(pos: Int, lista: List[T], listaNueva: List[T]): List[T] = lista match {
      //La idea es que se recorra toda lista para pasarle todos los valores a lista_updt, pero 
      //quedarán del revés asique metemos una funcion de reverse y ya 
      case Nil => listaNueva
      case head :: tail =>
        if (pos == 0) {update_aux(-1, tail, value :: listaN@@ueva)}
        else {update_aux(pos-1, tail, head :: lista_updt)}
    }
    
    val revertida: List[T] = update_aux(this.index(p), this.data, Nil)
    val finalista: List[T] = reverso(revertida, Nil)
    Matrix(this.rows, this.columns, finalista)
  } 

  def neighbors(p: Position): List[Position] = {
    if(!this.inside(p)) throw new IndexOutOfBoundsException
    val vecinos: List[Position] = List(Position(p.row-1, p.column-1), Position(p.row-1, p.column), Position(p.row-1, p.column+1),
      Position(p.row, p.column-1), Position(p.row, p.column+1),
      Position(p.row+1, p.column-1), Position(p.row+1, p.column), Position(p.row+1, p.column+1))

    @tailrec
    def existen(lista: List[Position], la_nueva: List[Position]): List[Position] = lista match{
      case Nil => la_nueva
      case head :: tail =>
        if(this.inside(head)) {existen(tail, head :: la_nueva)}
        else {existen(tail, la_nueva)}
    }
    existen(vecinos, Nil)
  } 

  def partition(f: T => Boolean): (List[Position], List[Position]) = {
    @tailrec
    def aux(contador: Int, lista_original: List[T], cumple: List[Position], no_cumple: List[Position]): (List[Position], List[Position]) = lista_original match{
      //Como terminemos de recorrer la lista_original tendremos las listas que cumplen y las que no del revés, 
      //asique las devolvemos revertidas
      case Nil => (reverso(cumple, Nil), reverso(no_cumple, Nil))
      case head :: tail =>
        if(f(head)) {aux(contador+1, tail, positionOf(contador) :: cumple, no_cumple)}
        else {aux(contador+1, tail, cumple, positionOf(contador) :: no_cumple)}
    }
    aux(0, this.data, Nil, Nil)
  } 

  //f es un una funcion que devuelve un booleano y recibe un T 
  def filterNeighbors(p: Position, f: T => Boolean): List[Position] = {
    if(!this.inside(p)) throw new IndexOutOfBoundsException
    val vecinos = neighbors(p)
    
    @tailrec
    def aux(lista_original: List[Position], los_que_cumplen: List[Position]): List[Position] = lista_original match{
      case Nil => los_que_cumplen
      // Dato importante: la lista que devolvemos sale revertida
      // Pero el orden del resultado no importa (lo indica así el enunciado)
      case head :: tail =>
        if(f(this.get(head))) {aux(tail, head :: los_que_cumplen)}
        else {aux(tail, los_que_cumplen)}
    }
    aux(vecinos, Nil)
  }


// Este es un objeto companion, se usa para implementar el comportamiento estático
// de una clase
object Matrix:

  def apply[T](rows: Int, columns: Int, data: List[T]): Matrix[T] =
    new Matrix(rows, columns, data)

  def apply[T](rows: Int, columns: Int, defaultValue: T): Matrix[T] = {
    val longitud: Int = rows * columns
    @tailrec
    def creador(restantes: Int, lista: List[T]): List[T] = {
     if (restantes <= 0) lista
     else creador(restantes -1, defaultValue :: lista)
    }
    Matrix(rows, columns, creador(longitud, Nil))
  }

  def apply[T](rows: Int, columns: Int, defaultValue: T, marked: List[Position], markedValue: T): Matrix[T] = {
    val longitud: Int = rows * columns
     
    @tailrec
    def conversion(posiciones: List[Position], indices: List[Int]): List[Int] = posiciones match {
        case Nil => indices
        case head :: tail => 
          //Comprobamos que la posicion está dentro de la matriz 
          if (!((0 <= head.row) && (head.row < rows) && (0 <= head.column) && (head.column < columns))) throw new IndexOutOfBoundsException
          //Si lo está pues la metemos en la lista
          else conversion(tail, head.row * columns + head.column :: indices)
    }
  
    @tailrec
    def contiene(indice: Int , lista: List[Int]):Boolean = lista match{
      case Nil => false
      case head :: tail =>
        if(indice == head) true
        else{contiene(indice, tail)}
    }
    @tailrec
    def metiendo(restantes: Int, indexes: List[Int], finalista: List[T]): List[T] = {
     // Lo dejo en -1 porque podría haber una posicion 0
     if (restantes < 0) finalista
     else if (contiene(restantes, indexes)){ metiendo(restantes -1, indexes, markedValue :: finalista)}
     else {metiendo(restantes -1, indexes, defaultValue :: finalista)}
    }
    //Pongo longitud -1 porque empezamos por 0 
    Matrix(rows, columns, metiendo(longitud -1, conversion(marked, Nil), Nil))

  } 

```


#### Short summary: 

empty definition using pc, found symbol in pc: 