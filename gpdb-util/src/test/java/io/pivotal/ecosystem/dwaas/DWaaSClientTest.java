/*
 * Copyright (C) 2016-Present Pivotal Software, Inc. All rights reserved.
 * <p>
 * This program and the accompanying materials are made available under
 * the terms of the under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.ecosystem.dwaas;

import io.pivotal.ecosystem.dwaas.connector.DWaaSServiceInfo;
import io.pivotal.ecosystem.servicebroker.model.ServiceBinding;
import io.pivotal.ecosystem.servicebroker.model.ServiceInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DWaaSClientTest {

    private static final Logger log = LoggerFactory.getLogger(DWaaSClientTest.class);

    @Autowired
    private DWaaSClient client;

    @Autowired
    private ServiceBinding serviceBindingWithParms;

    @Autowired
    private ServiceBinding serviceBindingNoParms;

    @Autowired
    private ServiceInstance serviceInstanceWithParams;

    @Autowired
    private ServiceInstance serviceInstanceNoParams;

    @Autowired
    DataSource dataSource;


    @Test
    public void testCreateCredentialsProvided() throws Exception {

        Map<String, Object> userCredentials = client.createUserCreds(serviceBindingWithParms);

        String uid = userCredentials.get(DWaaSServiceInfo.USERNAME).toString();
        assertNotNull(uid);

        String pw = userCredentials.get(DWaaSServiceInfo.PASSWORD).toString();
        assertNotNull(pw);

        assertEquals("testUser", userCredentials.get(DWaaSServiceInfo.USERNAME));
        assertEquals("testPassw0rd", userCredentials.get(DWaaSServiceInfo.PASSWORD));
        assertEquals("testDb", userCredentials.get(DWaaSServiceInfo.DATABASE));
        client.deleteUserCreds(userCredentials);
    }

    @Test
    public void testCreateCredentialsGenerated() throws Exception {

<<<<<<< HEAD
        Map<String, Object> userCredentials = client.createUserCreds(serviceBindingNoParms);

        String uid = userCredentials.get(DWaaSServiceInfo.USERNAME).toString();
        assertNotNull(uid);

        String pw = userCredentials.get(DWaaSServiceInfo.PASSWORD).toString();
        assertNotNull(pw);

=======
        Map<String, String> userCredentials = client.createUserCreds(serviceBindingNoParms);
>>>>>>> 7b640b25e86752162a30dbbae9107f634e415131
        assertNotNull(userCredentials.get(DWaaSServiceInfo.USERNAME));
        assertNotNull(userCredentials.get(DWaaSServiceInfo.PASSWORD));
        assertNotNull(userCredentials.get(DWaaSServiceInfo.DATABASE));
        assertEquals(true, client.checkUserExists(userCredentials.get(DWaaSServiceInfo.USERNAME)));
        client.deleteUserCreds(userCredentials);
    }


    @Test
    public void testDbExists() {
        assertTrue(client.checkDatabaseExists("template1"));
        assertFalse(client.checkDatabaseExists("kjfhskfjd"));
    }

    @Test
    public void testDBUrl() throws Exception {
        //"jdbc:pivotal:greenplum://104.198.46.128:5432;DatabaseName=gpadmin;"
<<<<<<< HEAD
        Map<String, Object> userCredentials = client.createUserCreds(serviceBindingWithParms);
        client.setDbUrl("jdbc:pivotal:greenplum://104.198.46.128:5432;DatabaseName=gpadmin;");
=======
        Map<String, String> userCredentials = client.createUserCreds(serviceBindingWithParms);

>>>>>>> 7b640b25e86752162a30dbbae9107f634e415131

        String uid = userCredentials.get(DWaaSServiceInfo.USERNAME).toString();
        assertNotNull(uid);

        String pw = userCredentials.get(DWaaSServiceInfo.PASSWORD).toString();
        assertNotNull(pw);

        String db = userCredentials.get(DWaaSServiceInfo.DATABASE).toString();
        assertNotNull(db);

        String connectionString = client.getDbUrl(uid, db, pw);

        log.info("ConnectionString [{}]", connectionString);

        assertEquals(connectionString, "jdbc:pivotal:greenplum://104.198.46.128:5432;DatabaseName=testDb;;User=testUser;Password=testPassw0rd;");
    }


    @Test
    public void testCreate() throws SQLException {
        //DataSource ds = new DWaaSConnectionCreator().create(sqlServerServiceInfo, null);
        assertNotNull(dataSource);
        Connection c = dataSource.getConnection();
        assertNotNull(c);
        c.close();
    }

    @Test
    public void testClean() {
        assertEquals("dsfuwe98fy2Yd9y2", client.clean("dsfuwe98fy2Yd9y2"));
        assertEquals("dsfuwe98fy2Yd9y2", client.clean("dsfuw  e98f& ()$%^$ <> y2Yd9y2"));
        assertEquals("", client.clean(""));
        assertEquals("", client.clean("        "));
        assertEquals("", client.clean(null));
        assertEquals("dfjhgkjhfdgtjhowefiTT", client.clean("dfjhgkjhfd<>&gt;// \\ jhowefi TT "));
    }
}