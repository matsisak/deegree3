//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
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
package org.deegree.model.multiresolution;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.deegree.model.multiresolution.io.MeshFragmentDataReader;

/**
 * Encapsulates the bounding box and approximation error for a fragment of a {@link MultiresolutionMesh} and provides
 * access to the actual geometry data.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$
 */
public class MeshFragment {

    /**
     * Size of binary representation (in bytes).
     * 
     * NOTE: This is just the meta information of the patch, not the geometry data.
     */
    public static int SIZE = 40;

    public final int id;

    public final float[][] bbox = new float[2][3];

    public final float error;
    
    private long blobPosition;

    private int length;    
    
    private MeshFragmentDataReader patchReader;
    
    private MeshFragmentData data;
   
    public MeshFragment( int id, ByteBuffer buffer, MeshFragmentDataReader patchReader ) {
        this.id = id;
        this.patchReader = patchReader;
        this.bbox[0][0] = buffer.getFloat();
        this.bbox[0][1] = buffer.getFloat();
        this.bbox[0][2] = buffer.getFloat();
        this.bbox[1][0] = buffer.getFloat();
        this.bbox[1][1] = buffer.getFloat();
        this.bbox[1][2] = buffer.getFloat();
        this.error = buffer.getFloat();
        this.blobPosition = buffer.getLong();
        this.length = buffer.getInt();
    }

    public long getOffset() {
        return blobPosition;
    }

    public long getLastByteOffset() {
        return blobPosition + length - 1;
    }

    public MeshFragmentData getData() {
        return data;
    }    
    
    public void loadData() {
        if (data == null) {
            try {
                data = patchReader.read( id, blobPosition, length );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public void unloadData() {
        data = null;
    }

    public static void store( ByteBuffer target, float minX, float minY, float minZ, float maxX, float maxY,
                              float maxZ, float error, long blobPosition, int length ) {
        target.putFloat( minX );
        target.putFloat( minY );
        target.putFloat( minZ );
        target.putFloat( maxX );
        target.putFloat( maxY );
        target.putFloat( maxZ );
        target.putFloat( error );
        target.putLong( blobPosition );
        target.putInt( length );
    }

    public static void store( ByteBuffer target, float[][] bbox, float error, long blobPosition, int length ) {
        target.putFloat( bbox[0][0] );
        target.putFloat( bbox[0][1] );
        target.putFloat( bbox[0][2] );
        target.putFloat( bbox[1][0] );
        target.putFloat( bbox[1][1] );
        target.putFloat( bbox[1][2] );
        target.putFloat( error );
        target.putLong( blobPosition );
        target.putInt( length );
    }

    public boolean isLoaded() {
        return data != null;
    }

    @Override
    public String toString() {
        return "blobPosition: " + blobPosition + ", length: " + length;
    }
}