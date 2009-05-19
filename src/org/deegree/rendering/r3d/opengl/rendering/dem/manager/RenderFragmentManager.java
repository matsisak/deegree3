//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/resources/eclipse/files_template.xml $
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

package org.deegree.rendering.r3d.opengl.rendering.dem.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.media.opengl.GL;

import org.deegree.rendering.r3d.multiresolution.MultiresolutionMesh;
import org.deegree.rendering.r3d.opengl.rendering.dem.RenderMeshFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the loading, unloading and caching of {@link RenderMeshFragment} data and the enabling/disabling in a certain
 * GL context.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: schneider $
 * 
 * @version $Revision: $, $Date: $
 */
public class RenderFragmentManager {

    private static final Logger LOG = LoggerFactory.getLogger( RenderFragmentManager.class );

    // contains the corresponding RenderableMeshFragment object for each fragment id
    final RenderMeshFragment[] renderFragments;

    private final Cache memoryCache;

    int inMemory;

    private int inGPU;

    private int maxInMemory, maxInGPU;

    private final MultiresolutionMesh mrModel;

    /**
     * Initialize the Manager with a multiresolution model.
     * 
     * @param mrModel
     * @param maxCached
     */
    public RenderFragmentManager( MultiresolutionMesh mrModel, int maxCached ) {

        LOG.debug( "Creating RenderFragmentManager." );
        this.mrModel = mrModel;
        renderFragments = new RenderMeshFragment[mrModel.fragments.length];
        for ( int fragmentId = 0; fragmentId < mrModel.fragments.length; fragmentId++ ) {
            renderFragments[fragmentId] = new RenderMeshFragment( mrModel.fragments[fragmentId] );
        }
        this.memoryCache = new Cache( maxCached );
    }

    /**
     * Ensures that the data of the specified fragments is available in main memory or in GPU memory.
     * 
     * @param fragments
     * @throws IOException
     */
    void require( Collection<RenderMeshFragment> fragments )
                            throws IOException {

        long begin = System.currentTimeMillis();
        SortedSet<RenderMeshFragment> sortedFragments = new TreeSet<RenderMeshFragment>( fragments );
        int loaded = 0;
        for ( RenderMeshFragment fragment : sortedFragments ) {
            if ( !( fragment.isLoaded() || fragment.isEnabled() ) ) {
                fragment.load();
                inMemory++;
                loaded++;

                if ( inMemory > maxInMemory ) {
                    maxInMemory++;
                }

                memoryCache.put( fragment.getId(), fragment );
            }
            // mark that fragment has been required
            memoryCache.get( fragment.getId() );
        }
        long elapsed = System.currentTimeMillis() - begin;
        LOG.debug( "Preparing of " + fragments.size() + " fragments (" + loaded + " new): " + elapsed + " ms" );
    }

    /**
     * Ensures that the data of the specified fragments is available in GPU memory (ready for rendering).
     * 
     * @param fragments
     * @param gl
     * @throws IOException
     */
    void requireOnGPU( Collection<RenderMeshFragment> fragments, GL gl )
                            throws IOException {

        long begin = System.currentTimeMillis();
        int loaded = 0;
        SortedSet<RenderMeshFragment> sortedFragments = new TreeSet<RenderMeshFragment>( fragments );
        for ( RenderMeshFragment fragment : sortedFragments ) {
            if ( !fragment.isEnabled() ) {
                fragment.enable( gl );
                inGPU++;
                loaded++;
                if ( inGPU > maxInGPU ) {
                    maxInGPU++;
                }
            }
        }
        long elapsed = System.currentTimeMillis() - begin;
        LOG.debug( "Preparing (GPU) of " + fragments.size() + " fragments (" + loaded + " new): " + elapsed + " ms" );
    }

    /**
     * Notifies the manager that the specified fragments are not needed for rendering anymore.
     * <p>
     * It's up to the manager to decide to unload them from GPU and/or memory or to keep them cached.
     * </p>
     * 
     * @param fragments
     * @param gl
     */
    void release( Collection<RenderMeshFragment> fragments, GL gl ) {
        long begin = System.currentTimeMillis();
        for ( RenderMeshFragment fragment : fragments ) {
            if ( fragment.isEnabled() ) {
                inGPU--;
                fragment.disable( gl );
            }
        }
        long elapsed = System.currentTimeMillis() - begin;
        LOG.debug( "Releasing (GPU) of " + fragments.size() + " fragments: " + elapsed + " ms" );
    }

    @Override
    public String toString() {
        String s = "Loaded fragments: memory: " + inMemory + " (max=" + maxInMemory + ") , GPU: " + inGPU + " (max="
                   + maxInGPU + ")";
        return s;
    }

    /**
     * @return the current mesh.
     */
    public MultiresolutionMesh getMultiresolutionMesh() {
        return mrModel;
    }

    /**
     * 
     * The <code>Cache</code> is a Map containing renderfragments mapped to their GLBuffer ids.
     * 
     * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
     * @author last edited by: $Author: rbezema $
     * @version $Revision: $, $Date: $
     * 
     */
    private class Cache extends LinkedHashMap<Integer, RenderMeshFragment> {

        /**
         * 
         */
        private static final long serialVersionUID = 9126150039966705148L;

        private final int maxEntries;

        Cache( int maxEntries ) {
            super( 16, 0.75f, true );
            this.maxEntries = maxEntries;
        }

        /**
         * Overrides to the needs of a cache.
         * 
         * @param eldest
         * @return true as defined by the contract in {@link LinkedHashMap}.
         */
        @Override
        protected boolean removeEldestEntry( Map.Entry<Integer, RenderMeshFragment> eldest ) {
            if ( size() > maxEntries ) {
                inMemory--;
                eldest.getValue().unload();
                return true;
            }
            return false;
        }
    }

}
