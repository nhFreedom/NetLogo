package org.nlogo.prim.hubnet ;

import org.nlogo.api.LogoException;
import org.nlogo.nvm.Syntax;

public final strictfp class _hubnetplot
	extends org.nlogo.nvm.Command
{
	@Override
	public void perform( final org.nlogo.nvm.Context context ) throws LogoException
	{	
		final String name  = argEvalString( context , 0 ) ;
		final double n = argEvalDoubleValue( context , 1 ) ;
		workspace.waitFor
			( new org.nlogo.api.CommandRunnable() {
					public void run() {
						workspace.getHubNetManager().plot( name , n ) ;
					} } ) ;
		context.ip = next ;
	}
	@Override
	public Syntax syntax()
	{
		int[] right = { Syntax.TYPE_STRING , Syntax.TYPE_NUMBER } ;
		return Syntax.commandSyntax( right ) ;
	}
}
