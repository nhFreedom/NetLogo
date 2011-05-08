package org.nlogo.prim ;

import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;
import org.nlogo.nvm.Context;
import org.nlogo.agent.Turtle;
import org.nlogo.agent.Patch;
import org.nlogo.api.Nobody;

public final strictfp class _patchnorth extends Reporter
{
	@Override public Syntax syntax()
	{
		return Syntax.reporterSyntax
			( Syntax.TYPE_PATCH , "-TP-" ) ;
	}
	@Override public Object report( Context context )
	{
		return report_1( context ) ;
	}
	public Object report_1( Context context )
	{
		Patch patch ;
		if( context.agent instanceof Patch )
		{
			patch = ( (Patch) context.agent ).getPatchNorth() ;
		}
		else if( context.agent instanceof Turtle )
		{
			patch = ( (Turtle) context.agent ).getPatchHere().getPatchNorth() ;
		}
		else
		{
			patch = world.fastGetPatchAt( 0 , 1 ) ;
		}
		if ( patch == null )
		{
			return Nobody.NOBODY ; 
		}
		return patch ;
	}
}
