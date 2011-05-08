package org.nlogo.prim.etc ;

import org.nlogo.agent.Agent;
import org.nlogo.agent.Link;
import org.nlogo.agent.Patch;
import org.nlogo.agent.Turtle;
import org.nlogo.api.LogoException;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.EngineException;
import org.nlogo.nvm.Pure;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.Syntax;

public final strictfp class _lessorequal
	extends Reporter implements Pure
{
	@Override
	public Syntax syntax()
	{
		int left = Syntax.TYPE_NUMBER | Syntax.TYPE_STRING | Syntax.TYPE_AGENT ;
		int[] right = { Syntax.TYPE_NUMBER | Syntax.TYPE_STRING | Syntax.TYPE_AGENT } ;
		int ret = Syntax.TYPE_BOOLEAN ;
		return Syntax.reporterSyntax( left , right , ret , Syntax.NORMAL_PRECEDENCE - 4 ) ;
	}
	@Override
	public Object report( Context context ) throws LogoException
	{
		return report_1( context ,
						 args[ 0 ].report( context ) ,
						 args[ 1 ].report( context ) )
			? Boolean.TRUE
			: Boolean.FALSE ;
	}
	public boolean report_1( Context context , Object o1 , Object o2 ) 
		throws LogoException
	{
		if( o1 instanceof Double && o2 instanceof Double )
		{
			return ( (Double) o1 ).doubleValue() <= ( (Double) o2 ).doubleValue() ;
		}
		if( o1 instanceof String && o2 instanceof String )
		{
			return ( ( (String) o1 ).compareTo( (String) o2 ) <= 0 );
		}
		if( o1 instanceof Agent && o2 instanceof Agent )
		{
			Agent a1 = (Agent) o1 ;
			Agent a2 = (Agent) o2 ;
			if( a1.getAgentBit() == a2.getAgentBit() )
			{
				return a1.compareTo( a2 ) <= 0 ;
			}
		}
		throw new EngineException
			( context , this , 
			  "the <= operator can only be used on two numbers, " 
			  + "two strings, or two agents of the same type, " +
			  "but not on " + Syntax.aTypeName( o1 ) + " and " + Syntax.aTypeName( o2 ) ) ;
	}
	public boolean report_2( Context context , String arg0 , String arg1 )
	{
		return arg0.compareTo( arg1 ) <= 0 ;
	}
	public boolean report_3( Context context , double arg0 , double arg1 )
	{
		return arg0 <= arg1 ;
	}
	public boolean report_4( Context context , Turtle arg0 , Turtle arg1 )
	{
		return arg0.compareTo( arg1 ) <= 0 ;
	}
	public boolean report_5( Context context , Patch arg0 , Patch arg1 )
	{
		return arg0.compareTo( arg1 ) <= 0 ;
	}
	public boolean report_6( Context context , Link arg0 , Link arg1 )
	{
		return arg0.compareTo( arg1 ) <= 0 ;
	}
	public boolean report_7( Context context , double arg0 , Object arg1 ) throws LogoException
	{
		if( arg1 instanceof Double )
		{
			return arg0 <= ( (Double) arg1 ).doubleValue() ;
		}
		throw new EngineException
			( context , this , 
			  "the <= operator can only be used on two numbers, " 
			  + "two strings, or two agents of the same type, " +
			  "but not on a " + Syntax.aTypeName( arg0 ) + " and " +
			  Syntax.aTypeName( arg1 ) ) ;
	}
	public boolean report_8( Context context , Object arg0 , double arg1 ) throws LogoException
	{
		if( arg0 instanceof Double )
		{
			return ( (Double) arg0 ).doubleValue() <= arg1 ;
		}
		throw new EngineException
			( context , this , 
			  "the <= operator can only be used on two numbers, " 
			  + "two strings, or two agents of the same type, " +
			  "but not on a " + Syntax.aTypeName( arg0 ) + " and " +
			  Syntax.aTypeName( arg1 ) ) ;
	}
}
