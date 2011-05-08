package org.nlogo.prim ;

import org.nlogo.agent.AgentSet;
import org.nlogo.agent.Turtle;
import org.nlogo.api.LogoException;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;

public final strictfp class _mylinks
	extends Reporter
{
	private final String breedName ;
	public _mylinks()
	{
		breedName = null ;
	}
	public _mylinks( String breedName )
	{
		this.breedName = breedName ;
	}
	@Override
	public Syntax syntax()
	{
		return Syntax.reporterSyntax
			( Syntax.TYPE_AGENTSET , "-T--" ) ;
	}
	@Override
	public String toString()
	{
		return super.toString() + ":" + breedName ;
	}
	@Override
	public Object report( final Context context ) throws LogoException
	{
		AgentSet breed = breedName == null ? world.links() : world.getLinkBreed( breedName ) ;
		mustNotBeDirected( breed , context ) ;
		return world.linkManager.findLinksWith
			( (Turtle) context.agent , breed ) ;
	}
}
