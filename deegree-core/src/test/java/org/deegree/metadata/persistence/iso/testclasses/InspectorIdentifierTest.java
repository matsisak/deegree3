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
package org.deegree.metadata.persistence.iso.testclasses;

import java.sql.SQLException;
import java.util.List;

import org.deegree.metadata.persistence.MetadataInspectorException;
import org.deegree.metadata.persistence.MetadataStoreException;
import org.deegree.metadata.persistence.MetadataStoreTransaction;
import org.deegree.metadata.persistence.iso.ISOMetadataStore;
import org.deegree.metadata.persistence.iso.ISOMetadataStoreProvider;
import org.deegree.metadata.persistence.iso.helper.AbstractISOTest;
import org.deegree.metadata.persistence.iso.helper.TstConstants;
import org.deegree.metadata.persistence.iso.helper.TstUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class InspectorIdentifierTest extends AbstractISOTest {
    private static Logger LOG = LoggerFactory.getLogger( InspectorIdentifierTest.class );

    /**
     * If the fileIdentifier shouldn't be generated automaticaly if not set.
     * <p>
     * 1.xml has no fileIdentifier and no ResourceIdentifier -> reject<br>
     * 3.xml has a fileIdentifier -> insert <br>
     * Output should be a MetadataStoreException
     * 
     * @throws MetadataStoreException
     * 
     * @throws MetadataInspectorException
     * @throws MetadataStoreException
     * @throws MetadataInspectorException
     * @throws MetadataInspectorException
     * @throws SQLException
     */
    @Test(expected = MetadataInspectorException.class)
    public void testIdentifierRejectTrue2()
                            throws MetadataStoreException, MetadataInspectorException {
        LOG.info( "START Test: test if the configuration rejects the insert of the missing identifier. (Reject TRUE)" );
        MetadataStoreTransaction ta = null;
        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {

            store = (ISOMetadataStore) new ISOMetadataStoreProvider().getMetadataStore( TstConstants.configURL_REJECT_FI_TRUE );

        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            throw new MetadataInspectorException( "skipping test (needs configuration)" );
        }

        TstUtils.insertMetadata( store, TstConstants.tst_1, TstConstants.tst_3 );

    }

    /**
     * If the fileIdentifier should be generated automaticaly if not set.
     * <p>
     * 1.xml has no fileIdentifier<br>
     * 2.xml has a fileIdentifier
     * 
     * @throws MetadataStoreException
     * @throws MetadataInspectorException
     */

    @Test
    public void testIdentifierRejectFalse()
                            throws MetadataStoreException, MetadataInspectorException {
        LOG.info( "START Test: test if the configuration generates the identifier automaticaly. (Reject FALSE)" );
        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {
            store = (ISOMetadataStore) new ISOMetadataStoreProvider().getMetadataStore( TstConstants.configURL_REJECT_FI_FALSE );
        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            return;
        }
        List<String> ids = TstUtils.insertMetadata( store, TstConstants.tst_1, TstConstants.tst_2 );

        resultSet = store.getRecordById( ids );
        int size = 0;
        while ( resultSet.next() ) {
            size++;
        }

        Assert.assertEquals( 2, size );

    }

    /**
     * If the fileIdentifier shouldn't be generated automaticaly if not set.
     * <p>
     * 1.xml has no fileIdentifier but with one ResourceIdentifier -> insert<br>
     * 2.xml has a fileIdentifier -> insert Output: 2 because 1.xml has a resourceIdentifier which can be taken
     * 
     * @throws MetadataStoreException
     * @throws MetadataInspectorException
     */

    @Test
    public void testIdentifierRejectTrue()
                            throws MetadataStoreException, MetadataInspectorException {
        LOG.info( "START Test: test if the configuration rejects the insert of the missing identifier. (Reject TRUE)" );

        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {
            store = (ISOMetadataStore) new ISOMetadataStoreProvider().getMetadataStore( TstConstants.configURL_REJECT_FI_TRUE );
        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            return;
        }
        List<String> ids = TstUtils.insertMetadata( store, TstConstants.tst_1, TstConstants.tst_2 );

        resultSet = store.getRecordById( ids );
        int size = 0;
        while ( resultSet.next() ) {
            size++;
        }

        Assert.assertEquals( 2, size );

    }

}
