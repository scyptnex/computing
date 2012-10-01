/*
 * [TestJCE.java]
 *
 * Summary: Test JCE, find providers and algorithms supported.
 *
 * Copyright: (c) 2009-2011 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.6+
 *
 * Created with: JetBrains IntelliJ IDEA IDE http://www.jetbrains.com/idea/
 *
 * Version History:
 *  1.0 2009-01-01 initial version
 */

import java.security.Provider;
import java.security.Security;

import static java.lang.System.out;

/**
 * Test JCE, find providers and algorithms supported.
 * <p/>
 * .
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.0 2009-01-01 initial version
 * @since 2009-01-01
 */
public final class TestJCE
{
	// --------------------------- main() method ---------------------------

	/**
	 * Prepare a list of providers and services.
	 *
	 * @param args not used
	 */
	public static void main( String[] args )
	{
		out.println( "Algorithms Supported in Java "
				+ System.getProperty( "java.version" )
				+ " JCE." );
		out.println( "====================" );
		// heading
		out.println( "Provider: type.algorithm -> className"
				+ "\n  aliases:"
				+ "\n  attributes:\n" );
		// discover providers
		Provider[] providers = Security.getProviders();
		for ( Provider provider : providers )
		{
			out.println( "<><><>" + provider + "<><><>\n" );
			// discover services of each provider
			for ( Provider.Service service : provider.getServices() )
			{
				out.println( service );
			}
			out.println();
		}
	}
}