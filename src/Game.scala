package gameoflife
import scala.math.Ordering.Implicits.infixOrderingOps
import scala.annotation.tailrec

class Game(val rows: Int, val columns: Int, val budget: Int, val maxGenerations: Int):

    // Se define el estado inicial de los dos jugadores (humanos o IA) con el tablero vacío y el mismo presupuesto (numero de celulas que pueden colocar)
    // empieza Player1
    def initialState: State = 
        val vacio = GameOfLifeMatrix.empty(rows, columns)
        State(
        Map(Player.Player1 -> vacio, Player.Player2 -> vacio),
        Map(Player.Player1 -> budget, Player.Player2 -> budget),
        Player.Player1
        )

    //Termina cuando ya no tenga mas celulas que colocar
    def hasFinished(state: State, player: Player): Boolean = 
        state.remainingCells(player) <= 0
        

    // Devuelve Left con el error o Right con un nuevo estado valido
    def placeCell(state: State, p: Position): Either[String, State] = 
        val jugador = state.turn
        val tablero = state.boards(jugador)

        if hasFinished(state, jugador) then
            Left(s"${playerName(jugador)} ya no tiene celulas para colocar.")
        else if !tablero.inside(p) then
            Left(s"La posicion (${p.row}, ${p.column}) esta fuera del tablero.")
        else if tablero.get(p) == Alive then
            Left(s"La posicion (${p.row}, ${p.column}) ya esta ocupada.")
        else
            val nuevosTableros = state.boards.updated(jugador, tablero.update(p, Alive))
            val nuevosRestantes = state.remainingCells.updated(jugador, state.remainingCells(jugador) - 1)
            val rival = otherPlayer(jugador)
            val turnoSiguiente = if nuevosRestantes(rival) > 0 then rival else jugador
            Right(state.copy(boards = nuevosTableros, remainingCells = nuevosRestantes,
                turn = turnoSiguiente))

    // Pide posiciones a las estrategias y reintenta si placeCell las rechaza
    def placementLoop(state: State, strategies: Map[Player, Strategy]): State = 
        @tailrec //Es recursiva de cola ya que la llamada a colocar es lo último que se hace 
        def colocar(actual: State, error: Option[String]): State =
            val jugador = actual.turn
            val rival = otherPlayer(jugador)
            if hasFinished(actual, jugador) && hasFinished(actual, rival) then actual
            else if hasFinished(actual, jugador) then
                colocar(actual.copy(turn = rival), None)
            else
                val posicion = strategies(jugador).choosePosition(actual, jugador, error)
                placeCell(actual, posicion) match
                    case Left(mensaje) => colocar(actual, Some(mensaje))
                    case Right(nuevo)  => colocar(nuevo, None)

        colocar(state, None)


    // La generacion 0 es el tablero inicial. Se detiene ante cualquier
    // repeticion anterior o al alcanzar el limite de generaciones
    def simulate(board: GameOfLifeMatrix): (Int, Int) = 
        @tailrec 
        def simular(actual: GameOfLifeMatrix, vistos: Set[GameOfLifeMatrix], generacion: Int): (Int, Int) =
            if generacion >= maxGenerations || vistos.contains(actual) then
                (generacion, actual.aliveCount)
            else
                simular(actual.nextGeneration, vistos + actual, generacion + 1)

        simular(board, Set.empty[GameOfLifeMatrix], 0)

    // Compara primero generaciones y, si empatan comparamos las celulas vivas
    def determineWinner(resultPlayer1: (Int, Int), resultPlayer2: (Int, Int)): Option[Player] =
        if resultPlayer1._1 > resultPlayer2._1 then Some(Player.Player1)
        else if resultPlayer2._1 > resultPlayer1._1 then Some(Player.Player2)
        else if resultPlayer1._2 > resultPlayer2._2 then Some(Player.Player1)
        else if resultPlayer2._2 > resultPlayer1._2 then Some(Player.Player2)
        else None 

    // Encadena colocacion, simulacion y calculo del ganador
    def play(strategies: Map[Player, Strategy]): GameResult =
        val colocado = placementLoop(initialState, strategies)
        val resultado1 = simulate(colocado.boards(Player.Player1))
        val resultado2 = simulate(colocado.boards(Player.Player2))
        GameResult(
            colocado,
            Map(Player.Player1 -> resultado1, Player.Player2 -> resultado2),
            determineWinner(resultado1, resultado2)
        )

