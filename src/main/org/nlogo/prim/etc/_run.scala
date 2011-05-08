package org.nlogo.prim.etc

import org.nlogo.api.{ CompilerException, LogoException }
import org.nlogo.nvm.{ Activation, ArgumentTypeException, Command, CommandLambda, Context, EngineException, Syntax }

class _run extends Command {

  override def syntax =
    Syntax.commandSyntax(
      Array(Syntax.TYPE_STRING | Syntax.TYPE_COMMAND_LAMBDA,
            Syntax.TYPE_REPEATABLE | Syntax.TYPE_WILDCARD),
      1)

  override def perform(context: Context) {
    args(0).report(context) match {
      case s: String =>
        if(args.size > 1)
          throw new EngineException(context, this,
            token.name + " doesn't accept further inputs if the first is a string")
        try {
          val procedure = workspace.compileForRun(
            argEvalString(context, 0), context, false)
          // the procedure returned by compileForRun is executed without switching Contexts, only
          // activations.  so we create a new activation...
          context.activation = new Activation(procedure, context.activation, next)
          context.activation.setUpArgsForRunOrRunresult()
          // put the instruction pointer at the beginning of the new procedure.  note that when we made
          // the new Activation above, we passed the proper return address to the constructor so the
          // flow execution will resume in the right place.
          context.ip = 0
        } catch {
          case error: CompilerException =>
            throw new EngineException(context, this, error.getMessage)
        }
      case lambda: CommandLambda =>
        val n = args.size - 1
        if(n < lambda.formals.size)
          throw new EngineException(
            context, this, lambda.missingInputs(n))
        val actuals = new Array[AnyRef](n)
        var i = 0
        while(i < n) {
          actuals(i) = args(i + 1).report(context)
          i += 1
        }
        lambda.perform(context, actuals)
        context.ip = next
      case obj =>
        throw new ArgumentTypeException(
          context, this, 0, Syntax.TYPE_COMMAND_LAMBDA | Syntax.TYPE_STRING, obj)
    }
  }

}
