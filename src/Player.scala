package gameoflife

//Enum es una clase equivalente a un sealed trait y case object solo que mas simple de definir 
// Al ser cerrado, el compilador avisa si un match se deja algún caso.
enum Player:
  case Player1, Player2

// Devuelve el otro jugador.  
def otherPlayer(p: Player): Player = p match 
  case Player.Player1 => Player.Player2
  case Player.Player2 => Player.Player1


// Nombre que se muestra en la consola.
def playerName(p: Player): String = p match 
  case Player.Player1 => "Jugador 1"
  case Player.Player2 => "Jugador 2" 
