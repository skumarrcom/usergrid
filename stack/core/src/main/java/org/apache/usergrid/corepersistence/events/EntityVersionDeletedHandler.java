/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.usergrid.corepersistence.events;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.usergrid.corepersistence.CpEntityManagerFactory;
import org.apache.usergrid.exception.NotImplementedException;
import org.apache.usergrid.persistence.EntityManagerFactory;
import org.apache.usergrid.persistence.collection.MvccLogEntry;
import org.apache.usergrid.persistence.collection.event.EntityVersionDeleted;
import org.apache.usergrid.persistence.core.scope.ApplicationScope;
import org.apache.usergrid.persistence.index.ApplicationEntityIndex;
import org.apache.usergrid.persistence.model.entity.Id;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import static org.apache.usergrid.corepersistence.CoreModule.EVENTS_DISABLED;


/**
 * Remove Entity index when specific version of Entity is deleted. TODO: do we need this? Don't our version-created and
 * entity-deleted handlers take care of this? If we do need it then it should be wired in via GuiceModule in the
 * corepersistence package.
 */
@Singleton
public class EntityVersionDeletedHandler implements EntityVersionDeleted {
    private static final Logger logger = LoggerFactory.getLogger( EntityVersionDeletedHandler.class );


    private final EntityManagerFactory emf;


    @Inject
    public EntityVersionDeletedHandler( final EntityManagerFactory emf ) {this.emf = emf;}


    @Override
    public void versionDeleted( final ApplicationScope scope, final Id entityId,
                                final List<MvccLogEntry> entityVersions ) {


        // This check is for testing purposes and for a test that to be able to dynamically turn
        // off and on delete previous versions so that it can test clean-up on read.
        if ( System.getProperty( EVENTS_DISABLED, "false" ).equals( "true" ) ) {
            return;
        }

        if ( logger.isDebugEnabled() ) {
            logger.debug( "Handling versionDeleted count={} event for entity {}:{} v {} "
                    + "  app: {}", new Object[] {
                    entityVersions.size(), entityId.getType(), entityId.getUuid(),
                    scope.getApplication()
                } );
        }

        CpEntityManagerFactory cpemf = ( CpEntityManagerFactory ) emf;

        final ApplicationEntityIndex ei = cpemf.getManagerCache().getEntityIndex( scope );


        throw new NotImplementedException( "Fix this" );


//        final IndexScope indexScope =
//            new IndexScopeImpl( new SimpleId( scope.getOwner().getUuid(), scope.getOwner().getType() ),
//                scope.getName() );
//
//        //create our batch, and then collect all of them into a single batch
//        Observable.from( entityVersions ).collect( () -> ei.createBatch(), ( entityIndexBatch, mvccLogEntry ) -> {
//            entityIndexBatch.deindex( indexScope, mvccLogEntry.getEntityId(), mvccLogEntry.getVersion() );
//        } )
//            //after our batch is collected, execute it
//            .doOnNext( entityIndexBatch -> {
//                entityIndexBatch.execute();
//            } ).toBlocking().last();
    }
}
