// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.headless
package lang

import org.scalatest.{FunSuite, Tag}
import org.nlogo.api.{AgentKind, SimpleJobOwner}
import java.io.File
import org.nlogo.util.SlowTest

// We parse the tests first, then run them.
// Parsing is separate so we can write tests for the parser itself.
abstract class TestLanguage(files: Iterable[File]) extends FunSuite with SlowTest {
  for(t:LanguageTest <- Parser.parseFiles(files); if(t.shouldRun))
    test(t.fullName, new Tag(t.suiteName){}, new Tag(t.fullName){}) {
      t.run()
    }
}

trait TestFinder extends Iterable[File]
case class TxtsInDir(dir:String) extends TestFinder {
  override def iterator =
    new File(dir).listFiles
      .filter(_.getName.endsWith(".txt"))
      .filterNot(_.getName.containsSlice("SDM"))
      .filterNot(_.getName.containsSlice("HubNet"))
      .iterator
}
case object ExtensionTestsDotTxt extends TestFinder {
  def iterator = {
    def filesInDir(parent:File): Iterable[File] =
      parent.listFiles.flatMap{f => if(f.isDirectory) filesInDir(f) else List(f)}
    filesInDir(new File("extensions")).filter(_.getName == "tests.txt").iterator
  }
}

class TestCommands extends TestLanguage(TxtsInDir("test/commands"))
class TestReporters extends TestLanguage(TxtsInDir("test/reporters"))
class TestExtensions extends TestLanguage(ExtensionTestsDotTxt)
class TestModels extends TestLanguage(
  TxtsInDir("models/test")
    .filterNot(_.getName.startsWith("checksums")))

// The output of the parser is lists of instances of these classes:

case class OpenModel(modelPath:String)
case class Proc(content: String)
case class Command(agentKind: String, command: String)
case class CommandWithError(agentKind: String, command: String, message: String)
case class CommandWithStackTrace(agentKind: String, command: String, stackTrace: String)
case class CommandWithCompilerError(agentKind: String, command: String, message: String)
case class ReporterWithResult(reporter: String, result: String)
case class ReporterWithError(reporter: String, error: String)
case class ReporterWithStackTrace(reporter: String, stackTrace: String)

// This is the code that runs each test:

case class LanguageTest(suiteName: String, testName: String, commands: List[String]) {
  val lineItems = commands map Parser.parse
  val (procs, nonProcs) = lineItems.partition(_.isInstanceOf[Proc])
  val proc = Proc(procs.map {_.asInstanceOf[Proc].content}.mkString("\n"))

  // on the core branch the _3D tests are gone, but extensions tests still have them since we
  // didn't branch the extensions, so we still need to filter those out - ST 1/13/12
  val shouldRun = !testName.endsWith("_3D") && {
    import org.nlogo.api.Version.useGenerator
    if (testName.startsWith("Generator"))
      useGenerator
    else if (testName.startsWith("NoGenerator"))
      !useGenerator
    else true
  }

  def fullName = suiteName + "::" + testName

  // run the test in both modes, Normal and Run
  def run() {
    import AbstractTestLanguage._
    def agentKind(a: String) = a match {
      case "O" => AgentKind.Observer
      case "T" => AgentKind.Turtle
      case "P" => AgentKind.Patch
      case "L" => AgentKind.Link
      case x => sys.error("unrecognized agent type: " + x)
    }
    class Tester(mode: TestMode) extends AbstractTestLanguage {
      // use a custom owner so we get fullName into the stack traces
      // we get on the JobThread - ST 1/26/11
      override def owner =
        new SimpleJobOwner(fullName, workspace.world.mainRNG)
      try {
        init()
        defineProcedures(proc.content)
        nonProcs.foreach {
          case OpenModel(modelPath) => workspace.open(modelPath)
          case Proc(content) =>
            defineProcedures(content)
          case Command(agent, command) =>
            testCommand(command, agentKind(agent), mode)
          case CommandWithError(agent, command, message) =>
            testCommandError(command, message, agentKind(agent), mode)
          case CommandWithCompilerError(agent, command, message) =>
            testCommandCompilerErrorMessage(command, message, agentKind(agent))
          case CommandWithStackTrace(agent, command, stackTrace) =>
            testCommandErrorStackTrace(command, stackTrace, agentKind(agent), mode)
          case ReporterWithResult(reporter, result) =>
            testReporter(reporter, result, mode)
          case ReporterWithError(reporter, error) =>
            testReporterError(reporter, error, mode)
          case ReporterWithStackTrace(reporter, error) =>
            testReporterErrorStackTrace(reporter, error, mode)
        }
      }
      finally workspace.dispose()
    }
    new Tester(NormalMode)
    if(!testName.startsWith("*")) new Tester(RunMode)
  }
}