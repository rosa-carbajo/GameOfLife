package gameoflife

//Sealed trait X, define que solo se puede definir las clases que heredan de X en este mismo fichero 
sealed trait Cell 


//  
//Object se utiliza para crear una clase sin datos y unico e irrepetible 
//(en el sentido de que no hay mas instancias, esta es la unica)
case object Alive extends Cell
case object Dead extends Cell


//Ademas no se puede heredar de case object porque son finales, es decir, 
//final class X estamos definiendo
//una clase de la que no se puede heredar y ademas no se puede instanciar

