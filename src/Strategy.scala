package gameoflife

import scala.io.StdIn
import scala.util.{Failure, Random, Success, Try}

trait Strategy:
  // Metodo que deben implementar las sub estrategias 
  def choosePosition(state: State, player: Player, error: Option[String]): Position


// Error que indica que se ha cerrado la entrada (readLine devuelve null)
// No termina el programa porque Main lo recoge y termina la jugada con un mensaje
case object EntradaCerrada extends RuntimeException("Entrada cerrada")

// Toda la lectura de teclado se hace aqui
// Las lecturas devuelven (con Try) Success con el valor o Failure (EntradaCerrada)
// La conversion del texto se hace con funciones puras que devuelven Either
object Consola:
  // Lee una linea. Convierte el null de readLine en un Failure 
  def leerLinea(prompt: String): Try[String] =
    Try(StdIn.readLine(prompt)).flatMap( linea =>
      if linea == null then Failure(EntradaCerrada) 
      else Success(linea))

  // Lee hasta que parse acepte el texto(devuelva Right). Si lo rechaza, muestra el motivo y vuelve a leer
  // parse es una funcion que recibe un String (que todavía no le ha llegado)
  // y que nosotros completamos en el método
  @annotation.tailrec 
  def leerHasta[A](prompt: String, parse: String => Either[String, A]): Try[A] = leerLinea(prompt) match
      case Failure(e) => Failure(e) //En caso de que no se haya escrito nada
      case Success(texto) => // Y si tenemos texto evaluamos lo que devuelve 
        parse(texto) match   // La funcion parse es la que evalua si esta bien o no el texto
          case Right(valor) => Success(valor)
          case Left(mensaje) => // Si no es bueno se vuelve a llamar a la funcion 
            println(mensaje)
            leerHasta(prompt, parse)


//La estrategia que permite al jugador elegir la posicion (escribiendo la posicion por teclado)
//Se define como un objeto singleton, ya que no necesita almacenar ningun estado
case object ManualStrategy extends Strategy:
  def parsePosition(text: String): Either[String, Position] =
    // Le quita los espacios al principio y al final (trim) y separa la cadena por uno o más espacios
    // Y luego el toList lo convierte en una lista inmutable de cadenas 
    text.trim.split("\\s+").toList match
      // Y matcheamos a los casos, si tenemos 2 elementos 
      case List(fila, columna) =>
        //Comprobamos que ambos son enteros, si son devolvemos Some(valor correspondiente) y si no None
        (fila.toIntOption, columna.toIntOption) match
          // Si ambos son enteros, devolvemos la posicion 
          case (Some(f), Some(c)) => Right(Position(f, c)) // el right es para el constructor Either 
          //que por convencion indica el caso correcto

          // Y si no devolvemos un mensaje de error
          case _ => Left("Introduce dos números enteros separados por un espacio.")
      //Si no, otro mensaje de error    
      case _ => Left("Formato no válido: escribe fila y columna (por ejemplo: 2 3).")
  
  // Los fallos de formato se resuelven aquí (leerHasta) y los de casilla, en Game.placeCell
  // La firma obliga a devolver una Position, así que si la entrada se cierra el Failure
  // se relanza con .get y lo recoge Main, que es quien decide cómo terminar
  def choosePosition(state: State, player: Player, error: Option[String]): Position =
    error.foreach(println)
    println(state.boards(player).show(
      s"${playerName(player)} · te quedan ${state.remainingCells(player)} células"))
    Consola.leerHasta("Elige fila y columna: ", parsePosition).get


case class AIStrategy(chosenPattern: List[Position]) extends Strategy:
  // Sirve, por orden, la siguiente casilla del patron que aun no se ha colocado
  def choosePosition(state: State, player: Player, error: Option[String]): Position =
    val tablero = state.boards(player)
    chosenPattern.find(p => tablero.get(p) == Dead) match
      case Some(posicion) => posicion
      case None => throw new IllegalStateException("La IA ya ha colocado todo su patrón.")

object AIStrategy:
  // Busqueda aleatoria mixta. Se generan patrones al azar de dos tipos:
  //    dispersos: las celulas se reparten por todo el tablero
  //    compactos: las celulas se concentran en una ventana central pequeña 
  //               (para que asi interactuen entre si, esta estrategia suele hacer que se dure más)
  // Cada patron se puntua con game.simulate y se elige el mejor con el mismo criterio
  // que determineWinner (primero generaciones y despues celulas vivas)
  // La busqueda se hace una sola vez, antes de empezar a colocar
  def apply(game: Game, attempts: Int): AIStrategy =
    require(attempts >= 1, "La IA necesita al menos un intento")
    require(game.budget >= 0 && game.budget <= game.rows * game.columns,
      "El presupuesto debe caber en el tablero")

    def posicionesEn(filas: List[Int], columnas: List[Int]): List[Position] =
      filas.flatMap(f => columnas.map(c => Position(f, c)))

    val todas = posicionesEn(List.range(0, game.rows), List.range(0, game.columns))

    // Ventana central con unas dos casillas por célula, para dejar huecos entre ellas
    val lado = math.max(3, math.ceil(math.sqrt(2.0 * game.budget)).toInt)
    def centrado(tamano: Int, total: Int): List[Int] =
      val t = math.min(tamano, total)
      List.range((total - t) / 2, (total - t) / 2 + t)
    val ventana = posicionesEn(centrado(lado, game.rows), centrado(lado, game.columns))
    // Si la ventana no tiene sitio para todas las células, se usa el tablero entero
    val zonaCompacta = if ventana.length >= game.budget then ventana else todas

    // Los intentos pares son compactos y los impares dispersos
    def patron(i: Int): List[Position] =
      val zona = if i % 2 == 0 then zonaCompacta else todas
      Random.shuffle(zona).take(game.budget)

    val puntuados = List.tabulate(attempts)(patron).map(p =>
      (p, game.simulate(GameOfLifeMatrix.withAlive(game.rows, game.columns, p))))
    AIStrategy(puntuados.maxBy(_._2)._1)