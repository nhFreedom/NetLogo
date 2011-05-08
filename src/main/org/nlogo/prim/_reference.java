package org.nlogo.prim;

import org.nlogo.agent.Agent;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.Reference;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;

public final strictfp class _reference
	extends Reporter
{
	public final Reference reference ;
	public _reference( Reference reference )
	{
		this.reference = reference ;
	}
	@Override
	public Syntax syntax()
	{
		return Syntax.reporterSyntax( Syntax.TYPE_REFERENCE ) ;
	}
	@Override
	public String toString()
	{
		return super.toString() + ":" + reference.agentClass().getSimpleName() + "," + reference.vn() ;
	}
	@Override
	public Object report( final Context context )
	{
		// we're always supposed to get compiled out of existence
		throw new UnsupportedOperationException() ;
	}
}
