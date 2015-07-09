/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.picketlink.quickstart.zanata.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.jpa.internal.JPAIdentityStore;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.spi.ContextInitializer;
import org.picketlink.idm.spi.IdentityContext;
import org.picketlink.idm.spi.IdentityStore;
import org.picketlink.quickstart.zanata.User;
import org.picketlink.quickstart.zanata.entity.HAccount;
import org.picketlink.quickstart.zanata.entity.HPerson;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class HAAccountTestCase {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Before
    public void onBefore() throws Exception {
        initializeEntityManager();
    }

    @After
    public void onAfter() throws Exception {
        closeEntityManager();
    }

    @Test
    public void testAccount() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder.named("default.config")
                .stores()
                    .jpa()
                        .supportType(User.class)
                        .supportCredentials(false)
                        .mappedEntity(HAccount.class)
                        .addContextInitializer(new ContextInitializer() {
                            @Override
                            public void initContextForStore(IdentityContext context, IdentityStore<?> store) {
                                if (store instanceof JPAIdentityStore) {
                                    if (!context.isParameterSet(JPAIdentityStore.INVOCATION_CTX_ENTITY_MANAGER)) {
                                        context.setParameter(JPAIdentityStore.INVOCATION_CTX_ENTITY_MANAGER, entityManager);
                                    }
                                }
                            }
                        });

        PartitionManager partitionManager = new DefaultPartitionManager(builder.buildAll());

        IdentityManager identityManager = partitionManager.createIdentityManager();

        User user = new User("zanata");

        HPerson person = new HPerson();

        person.setName("Zanata Person Name");

        user.setPerson(person);

        identityManager.add(user);

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        IdentityQuery<User> identityQuery = queryBuilder
                .createIdentityQuery(User.class)
                    .where(queryBuilder.equal(User.USER_NAME, "zanata"));

        List<User> result = identityQuery.getResultList();

        assertFalse(result.isEmpty());
    }

    private void initializeEntityManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("picketlink-custom-identity-model-pu");
        this.entityManager = entityManagerFactory.createEntityManager();
        this.entityManager.getTransaction().begin();
    }

    private void closeEntityManager() {
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }

}
