error id: 6C9F283AFB9F4262FB06BF5BD4344D59
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Matrix.scala
### java.lang.StringIndexOutOfBoundsException: String index out of range: 5586

occurred in the presentation compiler.



action parameters:
offset: 5587
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
    def update_aux(pos: Int, lista: List[T], lista_updt: List[T]): List[T] = lista match {
      //La idea es que se recorra toda lista para pasarle todos los valores a lista_updt, pero 
      //quedarán del revés asique metemos una funcion de reverse y ya 
      case Nil => lista_updt
      case head :: tail =>
        if (pos == 0) {update_aux(-1, tail, value :: lista_updt)}
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

    def aux(lista: List[Position], la_nueva: List[Position]): List[Position] = lista match{
      case Nil => la_nueva
      case head :: tail =>
        if(this.inside(head)) {aux(tail, head :: la_nueva)}
        else {aux(tail, la_nueva)}
    }
    aux(vecinos, Nil)
  } 

  def partition(f: T => Boolean): (List[Position], List[Position]) = {
    val vecinos = neighbors(this.)

    def aux(lista_original: List[Position], cumple: List[Position], no_cumple: List[Position]): (List[Position], List[Position]) = lista_original match{
      case Nil => (cumple, no_cumple)
      //Dato importante: la lista que devolvemos sale revertida
      // Pero el profesor dice que no importa el orden asique entiendo que no pasa nada
      case head :: tail =>
        if(f(this.get(head))) {aux(tail, head :: cumple, no_cumple)}
        else {aux(tail, cumple, head :: no_cumple)}
    }
    aux(vecinos, Nil, Nil)
  } 

  //f es un una funcion que devuelve un booleano y recibe un T 
  def filterNeighbors(p: Position, f: T => Boolean): List[Position] = {
    if(!this.inside(p)) throw new IndexOutOfBoundsException
    val vecinos = neighbors(p)
    neighbors(p).filter(f)
    /*  
    Descubrí que la funcion filter de List ya hace lo que queriamos hacer, 
    pero me daba pena eliminar el codigo asique lo dejo aquí
    def aux(lista_original: List[Position], loq_cumplen: List[Position]): List[Position] = lista_original match{
      case Nil => loq_cumplen
      //Dato importante: la lista que devolvemos sale revertida
      // Pero el profesor dice que no importa el orden asique entiendo que no pasa nada
      case head :: tail =>
        if(f(this.get(head))) {aux(tail, head :: loq_cumplen)}
        else {aux(tail, loq_cumplen)}
    }
    aux(vecinos, Nil)
    */
  }

object Matrix:

  def apply[T](rows: Int, columns: Int, data: List[T]): Matrix[T] =
    new Matrix(rows, columns, data)

  def apply[T](rows: Int, columns: Int, defaultValue: T): Matrix[T] = {
    
    val longitud: Int = rows * columns
    def aux (restantes: Int, lista: List[T]): List[T] = {
     if (restantes == 0) lista
     else aux(restantes -1, defaultValue :: lista)
    }
    Matrix(rows, columns, aux(longitud, Nil))
  }

  def apply[T](rows: Int, columns: Int, defaultValue: T, marked: List[Position], markedValue: T): Matrix[T] = {
    longitud = rows * columns
    def conversion(posiciones: List[T], indices: List[Int]): List[Int] = posiciones match {
        case Nil => indices
        case head :: tail => conversion(tail, index(head) :: indices)
    }
    
    def metiendo(restantes: Int, indexes: List[Int], finalista: List[T]): List[T] = {
     // Lo dejo en -1 porque podría haber una posicion 0
     if (restantes == -1) finalista
     else if (restantes in indexes){
      metiendo(restantes -1, indexes, markedValue :: finalista)
      }
      else {metiendo(restantes -1, indexes, defaultValue :: finalista)}
    
    }
    //Pongo longitud -1 porque empezamos por 0 
    Matrix(rows, columns, metiendo(longitud -1, conversion(marked, Nil), Nil))

  } 
@@
```


presentation compiler configuration:
Scala version: 3.8.4-bin-nonbootstrapped
Classpath:
<WORKSPACE>\.scala-build\GameOfLife_41e103cfb2\classes\main [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.8.4\scala3-library_3-3.8.4.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\3.8.4\scala-library-3.8.4.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\sourcegraph\semanticdb-javac\0.12.3\semanticdb-javac-0.12.3.jar [exists ], <WORKSPACE>\.scala-build\GameOfLife_41e103cfb2\classes\main\META-INF\best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -Ywith-best-effort-tasty




#### Error stacktrace:

```
java.base/java.lang.StringLatin1.charAt(StringLatin1.java:48)
	java.base/java.lang.String.charAt(String.java:1517)
	scala.collection.StringOps$.apply$extension(StringOps.scala:191)
	dotty.tools.dotc.interactive.Completion$.naiveCompletionPrefix(Completion.scala:141)
	dotty.tools.dotc.interactive.Completion$.completionPrefix(Completion.scala:170)
	dotty.tools.dotc.interactive.Completion$.scopeContext(Completion.scala:60)
	dotty.tools.pc.IndexedContext$LazyWrapper.<init>(IndexedContext.scala:91)
	dotty.tools.pc.IndexedContext$.apply(IndexedContext.scala:80)
	dotty.tools.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:49)
	dotty.tools.pc.ScalaPresentationCompiler.autoImports$$anonfun$1(ScalaPresentationCompiler.scala:338)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: String index out of range: 5586