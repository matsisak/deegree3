//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.test.services.wms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;

import javax.imageio.ImageIO;

import org.deegree.commons.utils.PixelCounter;

/**
 * <code>SimilarityUpdater</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class SimilarityUpdater {

    /**
     * @param args
     * @throws IOException
     */
    public static void main( String[] args )
                            throws IOException {
        if ( args.length == 0 ) {
            return;
        }
        BufferedReader in = new BufferedReader( new FileReader( new File( args[1], "similaritytests.txt" ) ) );
        PrintWriter out = new PrintWriter( new FileWriter( new File( args[1], "footprints.txt" ) ) );
        String url;
        while ( ( url = in.readLine() ) != null ) {
            int len = -1;
            while ( len != url.length() ) {
                len = url.length();
                url = url.replace( "%20%20", "%20" );
            }
            url = args[0] + "?" + url;
            System.out.println( "Counting image from URL " + url );
            BigInteger[] vals = PixelCounter.countPixels( ImageIO.read( new URL( url ) ) );
            out.println( vals[0] + " " + vals[1] + " " + vals[2] + " " + vals[3] );
        }
        in.close();
        out.close();
    }

}