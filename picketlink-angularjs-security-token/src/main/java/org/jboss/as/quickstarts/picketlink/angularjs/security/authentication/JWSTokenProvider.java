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
package org.jboss.as.quickstarts.picketlink.angularjs.security.authentication;

import org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.User;
import org.picketlink.json.jose.JWSBuilder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.UUID;

/**
 * <p>This is a simple {@link org.picketlink.idm.credential.Token.Provider} that manages a specific token type. In this case the
 * type is {@link org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSToken}.</p>
 *
 * @author Pedro Igor
 *
 * @see org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSToken
 */
@Stateless
public class JWSTokenProvider implements Token.Provider<JWSToken> {

    @Inject
    private PartitionManager partitionManager;

    @Override
    public JWSToken issue(Account account) {
        User user = (User) account;
        JWSBuilder builder = new JWSBuilder();

        // here we construct a JWS signed with the private key provided by the partition.
        builder
            .id(UUID.randomUUID().toString())
            .issuer("picketlink-angularjs-security-token")
            .issuedAt(getCurrentTime())
            .subject(user.getLoginName())
            .expiration(getCurrentTime() + (5 * 60))
            .notBefore(getCurrentTime());

        builder.claim("roles", loadRoles(account));
        builder.claim("permissions", "read", "write");

        return new JWSToken(builder.build().encode());
    }

    /**
     * <p>You must access a specific repository in order to load roles for the given {@link org.picketlink.idm.model.Account}.</p>
     *
     * @param account
     * @return
     */
    private String[] loadRoles(Account account) {
        return new String[]{ApplicationRole.ADMINISTRATOR};
    }

    @Override
    public JWSToken renew(Account account, JWSToken renewToken) {
        return issue(account);
    }

    @Override
    public void invalidate(Account account) {
        getIdentityManager().removeCredential(account, TokenCredentialStorage.class);
    }

    @Override
    public Class<JWSToken> getTokenType() {
        return JWSToken.class;
    }

    private int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private Realm getPartition() {
        return partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);
    }

    private IdentityManager getIdentityManager() {
        return this.partitionManager.createIdentityManager(getPartition());
    }
}
