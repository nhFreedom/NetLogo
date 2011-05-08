package org.nlogo.prim.etc

import org.nlogo.nvm.{Command, Context, Syntax}

class _thunkdidfinish extends Command {
  override def syntax = Syntax.commandSyntax
  override def perform(context: Context) {
    workspace.completedActivations.put(context.activation, true)
    context.ip = next
  }
}
