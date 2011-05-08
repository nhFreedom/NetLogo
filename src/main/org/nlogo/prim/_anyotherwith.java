package org.nlogo.prim ;

import org.nlogo.agent.Agent;
import org.nlogo.agent.AgentSet;
import org.nlogo.api.Dump;
import org.nlogo.api.LogoException;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.EngineException;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;

public final strictfp class _anyotherwith extends Reporter
{
	@Override public Syntax syntax()
	{
		return Syntax.reporterSyntax
			( new int[] { Syntax.TYPE_AGENTSET , Syntax.TYPE_BOOLEAN_BLOCK } ,
			  Syntax.TYPE_BOOLEAN , "-TPL" ,
			  "?" ) ;  // takes reporter block of unknown agent type
	}
	@Override public Object report( Context context ) throws LogoException
	{
		return report_1
			( context , argEvalAgentSet( context , 0 ) , args[ 1 ] ) ;
	}
	public boolean report_1( Context context , AgentSet sourceSet , Reporter reporterBlock )
		throws LogoException
	{
		Context freshContext = new Context( context , sourceSet ) ;
		reporterBlock.checkAgentSetClass( sourceSet , context ) ;
		for( AgentSet.Iterator iter = sourceSet.iterator() ; iter.hasNext() ; )
		{
			Agent tester = iter.next() ;
			if( tester == context.agent )
			{
				continue ;
			}
			Object value = freshContext.evaluateReporter( tester , reporterBlock ) ;
			if( ! ( value instanceof Boolean ) )
			{
				throw new EngineException
					( context , this , displayName() + " expected a true/false value from " +
					  Dump.logoObject( tester ) +
					  ", but got "+ Dump.logoObject( value ) + " instead" ) ;
			}
			if( ( ( Boolean ) value ).booleanValue() )
			{
				return true ;
			}
		}
		return false ;
	}
}
