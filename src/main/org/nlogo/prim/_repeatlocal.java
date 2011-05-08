package org.nlogo.prim;

import org.nlogo.api.LogoException;
import org.nlogo.nvm.Command;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.MutableLong;
import org.nlogo.nvm.Syntax;

public final strictfp class _repeatlocal
	extends Command
	implements org.nlogo.nvm.CustomAssembled
{
	final int vn ;
	public _repeatlocal( int vn )
	{
		this.vn = vn ;
	}
	@Override
	public Syntax syntax()
	{
		int[] right = { Syntax.TYPE_NUMBER , Syntax.TYPE_COMMAND_BLOCK } ;
		return Syntax.commandSyntax( right );
	}
	@Override
	public String toString()
	{
		return super.toString() + ":" + vn + ",+" + offset ;
	}
	@Override
	public void perform( final Context context )
		throws LogoException
	{
		context.activation.args[ vn ] =
			new MutableLong( validLong( argEvalDoubleValue( context , 0 ) ) ) ;
		context.ip = offset ;
	}
	public void perform_1( final Context context , double arg0 )
		throws LogoException
	{
		context.activation.args[ vn ] = new MutableLong( validLong( arg0 ) ) ;
		context.ip = offset ;
	}
	public void assemble( org.nlogo.nvm.AssemblerAssistant a )
	{
		a.add( this ) ;
		a.block() ;
		a.resume() ;
		a.add( new _repeatlocalinternal( vn , 1 - a.offset() ) ) ;
	}
}
