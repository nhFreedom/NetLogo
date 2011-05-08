package org.nlogo.prim.etc ;

import org.nlogo.api.Agent;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;
import org.nlogo.api.Perspective;

public final strictfp class _subject
	extends Reporter
{
	@Override
	public Object report( final org.nlogo.nvm.Context context )
	{
		if( world.observer().perspective() == Perspective.OBSERVE )
		{
			return org.nlogo.api.Nobody.NOBODY ;
		}
		Agent subject = world.observer().targetAgent() ;
		// not actually sure if the null check here is necessary - ST 6/28/05
		if( subject == null || subject.id() == -1 )
		{
			return org.nlogo.api.Nobody.NOBODY ;
		}
		return subject ;
	}
	@Override
	public Syntax syntax()
	{
		return Syntax.reporterSyntax( Syntax.TYPE_AGENT ) ;
	}
}
