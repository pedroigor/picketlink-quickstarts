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
package org.jboss.as.quickstarts.picketlink.angularjs.security.model.entity;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.pki.model.CertificateType;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Pedro Igor
 */
@Entity
@IdentityManaged(CertificateType.class)
public class CertificateTypeEntity extends IdentityTypeEntity {

    @AttributeValue
    private String attributedTypeId;

    @AttributeValue
    @Unique
    private String distinguishedName;

    @AttributeValue
    @Column(columnDefinition = "TEXT")
    private String encoded;

    @AttributeValue
    private String keyAlgorithm;

    @AttributeValue
    private String signatureAlgorithm;

    @AttributeValue
    private Integer certificateValidity;

    @AttributeValue
    private Integer keyLength;

    @AttributeValue
    private String baseDN;

    public String getAttributedTypeId() {
        return attributedTypeId;
    }

    public void setAttributedTypeId(String attributedTypeId) {
        this.attributedTypeId = attributedTypeId;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getKeyAlgorithm() {
        return this.keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Integer getCertificateValidity() {
        return this.certificateValidity;
    }

    public void setCertificateValidity(Integer certificateValidity) {
        this.certificateValidity = certificateValidity;
    }

    public Integer getKeyLength() {
        return this.keyLength;
    }

    public void setKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
    }

    public String getBaseDN() {
        return this.baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }
}
