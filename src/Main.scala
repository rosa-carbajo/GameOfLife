package gameoflife

import scala.util.{Failure, Success, Try}


// La entrada/salida se hace entre las estrategias y aqui
// Gestión de efectos: cada respuesta se valida con una función pura (Either) y se lee
// con Consola.leerHasta (Try). Si la entrada se cierra, el Failure sube hasta el final
// del programa, que lo recoge en un solo sitio y termina con un mensaje
object Main extends App:
  val BoardSize      = 15
  val MaxGenerations = 1000
  val AIAttempts     = 40 // Número de patrones aleatorios por IA

  // La idea es que si le pasamos todos los datos, devuelva el valor que sea como una funcion normal
  // Pero si solo pones los dos primeros(min y max) que significatia 
  // que el usuario todavia no ha escrito nada, pues no puede devolver nada, 
  // asique se devuelve una funcion que espera un string, vamos que de esta forma 
  // se nos permite dejar la funcion a medias
  def parseIntEnRango(min: Int, max: Int)(texto: String): Either[String, Int] =
    texto.trim.toIntOption match
      case Some(n) if n >= min && n <= max => Right(n)
      case _ => Left(s"Introduce un número entero entre $min y $max.")

  // true significa jugador humano y false significa inteligencia artificial
  def parseHumanoIA(texto: String): Either[String, Boolean] =
    texto.trim.toLowerCase match
      case "h" | "humano" => Right(true)
      case "i" | "ia"     => Right(false)
      case _              => Left("Escribe H para humano o I para IA.")

  def parseSiNo(texto: String): Either[String, Boolean] =
    texto.trim.toLowerCase match
      case "s" | "si" | "sí" => Right(true)
      case "n" | "no"        => Right(false)
      case _                 => Left("Responde S o N.")

  // La que se encarga de que el jugador haya escrito las posiciones
  def readInt(prompt: String, min: Int, max: Int): Int = 
    Consola.leerHasta(prompt, parseIntEnRango(min, max)).get
    // Consola es un objeto definido en Strategy 

  // Evalua si quien juega es una IA o un jugador
  def readHumanOrAI(prompt: String): Boolean = 
    Consola.leerHasta(prompt, parseHumanoIA).get

  // Evalua si se ha escrito si o no por teclado
  def readYesNo(prompt: String): Boolean = 
    Consola.leerHasta(prompt, parseSiNo).get

  // Explica el ganador: generaciones, células o empate total
  def describeWinner(result: GameResult): String =
    result.winner match // Como winner es un tipo Option: 
      //Que no haya nada es que no ha ganado ninguno y por tanto empate
      case None => "Empate: ambos tableros tienen las mismas generaciones y células vivas finales."
      //Si hay algo es que hay un jugador que ha ganado
      case Some(ganador) =>
        val resultadoGanador = result.results(ganador) 
        val resultadoRival = result.results(otherPlayer(ganador))
        if resultadoGanador._1 > resultadoRival._1 then // Y aqui comparamos el numero de generaciones sobrevividas
          s"Gana ${playerName(ganador)} por durar más generaciones."
        else // Y si no pues es que tiene mas celulas vivas (sabemos que ganar a ganado asique solo tiene 2 opciones)
          s"Gana ${playerName(ganador)} por tener más células vivas al empatar a generaciones."
  
  // Construye la estrategia(o contra quien juega) de un jugador según lo que se elija
  def elegirEstrategia(jugador: Player, juego: Game): Strategy =
    if readHumanOrAI(s"${playerName(jugador)}: H (humano) / I (IA): ") then ManualStrategy
    else AIStrategy(juego, AIAttempts)

  // Muestra los patrones iniciales, los resultados y el ganador
  def mostrarResultado(resultado: GameResult): Unit =
    val tablero1 = resultado.finalState.boards(Player.Player1)
    val tablero2 = resultado.finalState.boards(Player.Player2)
    println("\nPATRONES INICIALES")
    println(Animation.sideBySide(tablero1.show("Jugador 1"), tablero2.show("Jugador 2")))
    println("\nRESULTADOS")
    List(Player.Player1, Player.Player2).foreach: jugador =>
      val (generaciones, vivas) = resultado.results(jugador)
      println(s"${playerName(jugador)}: $generaciones generaciones, $vivas células vivas finales.")
    println(describeWinner(resultado))
  
  // Una partida completa, de la configuracion a la animacion
  def partida(): Unit =
    val presupuesto = readInt("Células por jugador (1-225): ", 1, BoardSize * BoardSize)
    val juego = new Game(BoardSize, BoardSize, presupuesto, MaxGenerations)
    val estrategias = List(Player.Player1, Player.Player2)
      .map(j => j -> elegirEstrategia(j, juego)).toMap
    val resultado = juego.play(estrategias)
    mostrarResultado(resultado)
    if readYesNo("¿Ver la simulación animada? (S/N): ") then
      Animation.animate(resultado)
      println("\n" + describeWinner(resultado))

  println("EL JUEGO DE LA VIDA: la batalla de los patrones")
  println("Cada jugador coloca células en su propio tablero de 15 x 15.")

  // Unico punto donde se tratan los fallos de entrada/salida
  Try(partida()) match
    case Success(_)              => ()
    case Failure(EntradaCerrada) => println("\nEntrada cerrada. Fin de la partida.")
    case Failure(e)              => println(s"\nError inesperado: ${e.getMessage}")
