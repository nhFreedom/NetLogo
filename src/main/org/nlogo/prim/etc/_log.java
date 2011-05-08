package org.nlogo.prim.etc ;

import org.nlogo.api.LogoException;
import org.nlogo.nvm.Reporter;
import org.nlogo.nvm.EngineException;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.Syntax;
import org.nlogo.nvm.Pure;

public final strictfp class _log extends Reporter implements Pure
{
	@Override public Syntax syntax()
	{
		return Syntax.reporterSyntax
			( new int[] { Syntax.TYPE_NUMBER , Syntax.TYPE_NUMBER } ,
			  Syntax.TYPE_NUMBER ) ;
	}
	@Override public Object report( Context context )
		throws LogoException
	{
		return report_1( context ,
						 argEvalDoubleValue( context , 0 ) ,
						 argEvalDoubleValue( context , 1 ) ) ;
	}
	public double report_1( Context context , double n , double base ) throws LogoException
	{
		if( n <= 0 )
		{
			throw new EngineException
				( context , this , "can't take the logarithm of " + n ) ;
		}
		if( base <= 0 )
		{
			throw new EngineException
				( context , this , base + " isn't a valid base for a logarithm" ) ;
		}
		return validDouble( StrictMath.log( n ) / StrictMath.log( base ) ) ;
	}
}
