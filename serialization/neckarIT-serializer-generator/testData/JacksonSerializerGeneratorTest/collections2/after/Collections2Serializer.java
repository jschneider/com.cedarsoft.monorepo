/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class Collections2Serializer extends it.neckar.open.serialization.jackson.AbstractJacksonSerializer<Collections2> {
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_ROLES = "roles";
    public static final String PROPERTY_EMAILS = "emails";
    public static final String PROPERTY_USER_DETAILS = "userDetails";
    public static final String PROPERTY_SINGLE_EMAIL = "singleEmail";

    @javax.inject.Inject
    public Collections2Serializer(@NotNull StringSerializer stringSerializer, @NotNull RoleSerializer roleSerializer, @NotNull EmailSerializer emailSerializer, @NotNull UserDetailsSerializer userDetailsSerializer) {
        super("collections-2", it.neckar.open.version.VersionRange.from(1, 0, 0).to());
        getDelegatesMappings().add(stringSerializer).responsibleFor(String.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        getDelegatesMappings().add(roleSerializer).responsibleFor(Role.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        getDelegatesMappings().add(emailSerializer).responsibleFor(Email.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        getDelegatesMappings().add(userDetailsSerializer).responsibleFor(UserDetails.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        assert getDelegatesMappings().verify();
    }

    @Override
    public void serialize(@NotNull com.fasterxml.jackson.core.JsonGenerator serializeTo, @NotNull Collections2 object, @NotNull it.neckar.open.version.Version formatVersion) throws IOException, it.neckar.open.version.VersionException, com.fasterxml.jackson.core.JsonProcessingException {
        verifyVersionWritable(formatVersion);
        serialize(object.getName(), String.class, PROPERTY_NAME, serializeTo, formatVersion);
        serializeArray(object.getRoles(), Role.class, PROPERTY_ROLES, serializeTo, formatVersion);
        serializeArray(object.getEmails(), Email.class, PROPERTY_EMAILS, serializeTo, formatVersion);
        serialize(object.getUserDetails(), UserDetails.class, PROPERTY_USER_DETAILS, serializeTo, formatVersion);
        serialize(object.getSingleEmail(), Email.class, PROPERTY_SINGLE_EMAIL, serializeTo, formatVersion);
    }

    @Override
    @NotNull
    public Collections2 deserialize(@NotNull com.fasterxml.jackson.core.JsonParser deserializeFrom, @NotNull it.neckar.open.version.Version formatVersion) throws IOException, it.neckar.open.version.VersionException, com.fasterxml.jackson.core.JsonProcessingException {
        verifyVersionWritable(formatVersion);

        String name = null;
        List<? extends Role> roles = null;
        List<? extends Email> emails = null;
        UserDetails userDetails = null;
        Email singleEmail = null;

        it.neckar.open.serialization.jackson.JacksonParserWrapper parser = new it.neckar.open.serialization.jackson.JacksonParserWrapper(deserializeFrom);
        while (parser.nextToken() == com.fasterxml.jackson.core.JsonToken.FIELD_NAME) {
            String currentName = parser.getCurrentName();

            if (currentName.equals(PROPERTY_NAME)) {
                parser.nextToken();
                name = deserialize(String.class, formatVersion, deserializeFrom);
                continue;
            }
            if (currentName.equals(PROPERTY_ROLES)) {
                parser.nextToken();
                roles = deserializeArray(Role.class, formatVersion, deserializeFrom);
                continue;
            }
            if (currentName.equals(PROPERTY_EMAILS)) {
                parser.nextToken();
                emails = deserializeArray(Email.class, formatVersion, deserializeFrom);
                continue;
            }
            if (currentName.equals(PROPERTY_USER_DETAILS)) {
                parser.nextToken();
                userDetails = deserialize(UserDetails.class, formatVersion, deserializeFrom);
                continue;
            }
            if (currentName.equals(PROPERTY_SINGLE_EMAIL)) {
                parser.nextToken();
                singleEmail = deserialize(Email.class, formatVersion, deserializeFrom);
                continue;
            }
            throw new IllegalStateException("Unexpected field reached <" + currentName + ">");
        }

        parser.verifyDeserialized(name, PROPERTY_NAME);
        assert name != null;
        parser.verifyDeserialized(roles, PROPERTY_ROLES);
        assert roles != null;
        parser.verifyDeserialized(emails, PROPERTY_EMAILS);
        assert emails != null;
        parser.verifyDeserialized(userDetails, PROPERTY_USER_DETAILS);
        assert userDetails != null;
        parser.verifyDeserialized(singleEmail, PROPERTY_SINGLE_EMAIL);
        assert singleEmail != null;

        parser.ensureObjectClosed();

        Collections2 object = new Collections2(name, singleEmail, userDetails);
        object.setRoles(roles);
        object.setEmails(emails);
        return object;
    }
}
