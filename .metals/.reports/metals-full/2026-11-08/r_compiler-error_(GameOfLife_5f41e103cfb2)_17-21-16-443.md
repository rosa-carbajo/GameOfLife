error id: E1C0D5C28160ACD963B7FEC53510FBC3
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/GameOfLifeMatrix.scala
### java.lang.StringIndexOutOfBoundsException: index 2041, length 2032

occurred in the presentation compiler.



action parameters:
offset: 2042
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
      //Tenemos que revcorrer la lista de celdas 
      def aux(lista: List[Cell], contador: Int, posiciones: List[Position]):List[Position] = lista match {}

    } 

    def aliveCount: Int = ??? //TODO

    def countAliveNeighbors(p: Position): Int = ??? //TODO

    def nextGeneration: GameOfLifeMatrix = ??? //TODO

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
      super.apply(rows, columns, Dead)

    } 

    def withAlive(rows: Int, columns: Int, alive: List[Position]): GameOfLifeMatrix = {
      super.apply(rows, columns, Dead, alive, Alive)

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
java.base/java.lang.String.checkIndex(String.java:4565)
	java.base/java.lang.StringUTF16.checkIndex(StringUTF16.java:1624)
	java.base/java.lang.StringUTF16.charAt(StringUTF16.java:1421)
	java.base/java.lang.String.charAt(String.java:1519)
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

java.lang.StringIndexOutOfBoundsException: index 2041, length 2032