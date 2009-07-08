//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/resources/eclipse/files_template.xml $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 and
 lat/lon GmbH

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
package org.deegree.geometry.standard.points;

import java.util.Iterator;
import java.util.List;

import org.deegree.geometry.points.Points;
import org.deegree.geometry.primitive.Point;

/**
 * {@link Points} implementation based on a <code>List</code> that allows to hold identifiable {@link Point} objects
 * (with id or that are references to point objects}.
 * <p>
 * This implementation is rather expensive, as every contained {@link Point} is represented as an object.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: schneider $
 * 
 * @version $Revision: $, $Date: $
 */
public class PointsList implements Points {

    protected List<Point> points;

    /**
     * Creates a new {@link PointsList} instance based on the given list.
     * 
     * @param points
     */
    public PointsList( List<Point> points ) {
        this.points = points;
    }

    @Override
    public int getCoordinateDimension() {
        return points.get( 0 ).getCoordinateDimension();
    }

    @Override
    public int size() {
        return points.size();
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    @Override
    public Point get( int i ) {
        return points.get( i );
    }

    @Override
    public double[] getAsArray() {
        double [] coords = new double [getCoordinateDimension() * size()];
        int i = 0;
        for ( Point p : this ) {
            for (double coord : p.getAsArray()) {
                coords [i++] = coord;
            }
        }
        return coords;
    }   
}
