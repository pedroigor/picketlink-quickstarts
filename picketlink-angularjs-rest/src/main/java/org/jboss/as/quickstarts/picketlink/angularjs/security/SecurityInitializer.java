/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.picketlink.angularjs.security;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Person;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.picketlink.PartitionManagerCreateEvent;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.Role;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

import static org.jboss.as.quickstarts.picketlink.angularjs.security.model.IdentityModelUtils.findByLoginName;
import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static org.picketlink.idm.model.basic.BasicModel.grantRole;

/**
 * @author Pedro Igor
 */
@Stateless
public class SecurityInitializer {

    public void configureDefaultPartition(@Observes PartitionManagerCreateEvent event) {
        PartitionManager partitionManager = event.getPartitionManager();

        createDefaultPartition(partitionManager);
        createDefaultRoles(partitionManager);
        createAdminAccount(partitionManager);
    }

    private void createDefaultRoles(PartitionManager partitionManager) {
        IdentityManager identityManager = partitionManager.createIdentityManager();

        createRole(ApplicationRole.ADMINISTRATOR, identityManager);
        createRole(ApplicationRole.USER, identityManager);
    }

    private void createDefaultPartition(PartitionManager partitionManager) {
        Realm partition = partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);

        if (partition == null) {
            try {
                partition = new Realm(Realm.DEFAULT_REALM);
                partitionManager.add(partition);
            } catch (Exception e) {
                throw new SecurityConfigurationException("Could not create default partition.", e);
            }
        }
    }

    public static Role createRole(String roleName, IdentityManager identityManager) {
        Role role = getRole(identityManager, roleName);

        if (role == null) {
            role = new Role(roleName);
            identityManager.add(role);
        }

        return role;
    }

    public void createAdminAccount(PartitionManager partitionManager) {
        IdentityManager identityManager = partitionManager.createIdentityManager();
        String email = "admin@picketlink.org";

        // if admin exists dont create again
        if(findByLoginName(email, identityManager) != null) {
            return;
        }

        Person person = new Person();

        person.setFirstName("Almight");
        person.setLastName("Administrator");
        person.setEmail(email);

        MyUser admin = new MyUser(person.getEmail());

        admin.setPerson(person);

        identityManager.add(admin);

        identityManager.updateCredential(admin, new Password("admin"));

        Role adminRole = getRole(identityManager, ApplicationRole.ADMINISTRATOR);

        grantRole(partitionManager.createRelationshipManager(), admin, adminRole);
    }
}
