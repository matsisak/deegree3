//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2009 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth  
 lat/lon GmbH 
 Aennchenstr. 19
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de


 ---------------------------------------------------------------------------*/
package org.deegree.commons.filter.spatial;

import org.deegree.commons.filter.FilterEvaluationException;
import org.deegree.commons.filter.MatchableObject;
import org.deegree.commons.filter.expression.PropertyName;
import org.deegree.geometry.Geometry;

/**
 * TODO add documentation here
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class Intersects extends SpatialOperator {

    private final PropertyName param1;
    
    private final Geometry param2;
    
    public Intersects( PropertyName param1, Geometry param2 ) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public SubType getSubType() {
        return SubType.INTERSECTS;
    }

    @Override
    public boolean evaluate( MatchableObject object )
                            throws FilterEvaluationException {
        Geometry param1Value = checkGeometryOrNull( param1.evaluate( object ));
        return param2.intersects( param1Value );
    }

    public String toString( String indent ) {
        // TODO Auto-generated method stub
        return null;
    }

}
