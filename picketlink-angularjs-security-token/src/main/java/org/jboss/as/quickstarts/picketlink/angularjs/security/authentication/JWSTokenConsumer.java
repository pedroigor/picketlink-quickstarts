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

import org.picketlink.idm.credential.Token;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

/**
 * @author Pedro Igor
 */
public class JWSTokenConsumer implements Token.Consumer<JWSToken> {

    @Override
    public <I extends IdentityType> I extractIdentity(JWSToken token, Class<I> identityType, StereotypeProperty.Property stereotypeProperty, Object identifier) {
        if (User.class.isAssignableFrom(identityType)) {
            return (I) new User(identifier.toString());
        } else if (Role.class.isAssignableFrom(identityType)) {
            return (I) new Role(identifier.toString());
        }

        return null;
    }

    @Override
    public boolean validate(JWSToken token) {
        return isSignatureValid(token);
    }

    /**
     * <p>You must validate the signature to properly validate the token. You may also want to access some storage to
     * check authenticity.</p>
     *
     * @param token
     * @return
     */
    private boolean isSignatureValid(JWSToken token) {
        return true;
    }

    @Override
    public Class<JWSToken> getTokenType() {
        return JWSToken.class;
    }
}
