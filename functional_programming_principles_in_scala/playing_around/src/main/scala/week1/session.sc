println("welcome to the Scala worksheet")

1 + 2


def sqrtIter(guess: Double, x: Double): Double =
  if (isGoodEnough(guess, x)) x
  else sqrtIter(improve(guess, x), x)

def isGoodEnough(guess: Double, x: Double) = true

def improve(guess: Double, x: Double) = x/2

sqrtIter(3, 1)


