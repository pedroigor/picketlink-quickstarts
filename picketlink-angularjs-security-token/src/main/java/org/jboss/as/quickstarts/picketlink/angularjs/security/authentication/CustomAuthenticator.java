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

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.TokenCredential;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * @author Pedro Igor
 */
@RequestScoped
@PicketLink
public class CustomAuthenticator extends BaseAuthenticator {

    @Inject
    private DefaultLoginCredentials credentials;

    @Inject
    private JWSTokenConsumer consumer;

    @Override
    public void authenticate() {
        Object credential = this.credentials.getCredential();

        if (Password.class.isInstance(credential)) {
            Password password = (Password) credential;

            if (veriryPassword(password)) {
                performSuccessfulAuthentication(credentials.getUserId());
            }
        } else if (TokenCredential.class.isInstance(credential)) {
            TokenCredential tokenCredential = (TokenCredential) credential;

            if (this.consumer.validate((JWSToken) tokenCredential.getToken())) {
                performSuccessfulAuthentication(tokenCredential.getToken().getSubject());
            }
        }
    }

    private void performSuccessfulAuthentication(String userId) {
        setStatus(AuthenticationStatus.SUCCESS);
        setAccount(loadAccount(userId));
    }

    private boolean veriryPassword(Password password) {
        return "changeme".equals(String.valueOf(password.getValue()));
    }

    private User loadAccount(String loginName) {
        return new User(loginName);
    }
}
