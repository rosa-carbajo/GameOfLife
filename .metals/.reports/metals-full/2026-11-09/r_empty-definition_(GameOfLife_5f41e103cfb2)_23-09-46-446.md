error id: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Player.scala:
file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Player.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -p.
	 -p#
	 -p().
	 -scala/Predef.p.
	 -scala/Predef.p#
	 -scala/Predef.p().
offset: 308
uri: file:///C:/Users/UAH/OneDrive%20-%20Universidad%20de%20Alcala/Documentos/GameOfLife/src/Player.scala
text:
```scala
package gameoflife

//Enum es una clase equivalente a un sealed trait y case object solo que mas simple de definir 
enum Player:
  case Player1, Player2

// Devuelve el otro jugador.
def otherPlayer(p: Player): Player = p match
  

// Nombre que se muestra en la consola.
def playerName(p: Player): String = @@p match
  case Player.Player1 => "Jugador 1"
  case Player.Player2 => "Jugador 2"
def otherPlayer(p: Player): Player = {
  case Player.Player1 => Player.Player2
  case Player.Player2 => Player.Player1
}

def playerName(p: Player): String = ??? //TODO

```


#### Short summary: 

empty definition using pc, found symbol in pc: 