// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise

import java.io.{ BufferedReader, InputStreamReader }
import
  org.nlogo.headless,
  headless.Shell.{ input, isQuit },
  headless.Main.setHeadlessProperty

object Shell {

  def main(argv: Array[String]) {
    setHeadlessProperty()
    Rhino.eval(Compiler.compileProcedures(
      "", 3, 3, 3, 3))
    System.err.println("Tortoise Shell 1.0")
    input.takeWhile(!isQuit(_))
      .foreach(run)
  }

  def run(s: String) {
    try {
      val (output, json) =
        Rhino.run(
          Compiler.compileCommands(s))
      Seq(output) // , json)
        .filter(_.nonEmpty)
        .foreach(x => println(x.trim))
    }
    catch { case e: Exception => println(e) }
  }

}
