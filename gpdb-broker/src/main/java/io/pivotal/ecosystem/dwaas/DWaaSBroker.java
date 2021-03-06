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

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import io.pivotal.ecosystem.dwaas.aws.AWSCloudFormationDeployer;
import io.pivotal.ecosystem.dwaas.connector.DWaaSServiceInfo;
import io.pivotal.ecosystem.servicebroker.model.ServiceBinding;
import io.pivotal.ecosystem.servicebroker.model.ServiceInstance;
import io.pivotal.ecosystem.servicebroker.service.DefaultServiceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Example service broker. Can be used as a template for creating custom service brokers
 * by adding your code in the appropriate methods. For more information on the CF service broker
 * lifecycle and API, please see See <a href="https://docs.cloudfoundry.org/services/api.html">here.</a>
 * <p>
 * This class extends DefaultServiceImpl, which has no-op implementations of the methods. This means
 * that if, for instance, your broker does not support binding you can just delete the binding methods below
 * (in other words, you do not need to implement your own no-op implementations).
 */

@Service
class DWaaSBroker extends DefaultServiceImpl {

    @Autowired
    private DWaaSClient client;

    private Environment env;

    private static final Logger log = LoggerFactory.getLogger(DWaaSBroker.class);

    @Autowired
    AmazonCloudFormation cloudformationClient;

    @Autowired
    AWSCloudFormationDeployer deployer;



    public DWaaSBroker(Environment env, DWaaSClient client) {
        super();
        this.env = env;
        this.client = client;
    }

    /**
     * Add code here and it will be run during the create-service process. This might include
     * calling back to your underlying service to create users, schemas, fire up environments, etc.
     *
     * @param instance service instance data passed in by the cloud connector. Clients can pass additional json
     *                 as part of the create-service request, which will show up as key value pairs in instance.parameters.
     */
    @Override
    public void createInstance(ServiceInstance instance) throws ServiceBrokerException {
        log.info("creating database...");

        String templateUrl = "https://s3.amazonaws.com/awsmp-fulfillment-cf-templates-prod/a11bec62-52d1-4fce-9a09-ea8e38ddcd3b.5b93ff4f-cc7f-49e2-a568-e6e9881a0fc8.template";

        //instance.getParameters().get(DWaaSServiceInfo.CLOUD_PROVIDER).toString()          // string: AWS, Azure [, or vSphere, GCP]
        //instance.getParameters().get(DWaaSServiceInfo.CLOUD_CREDENTIALS).toString()       // object: specific to each cloud
        //instance.getParameters().get(DWaaSServiceInfo.CLOUD_INSTANCE_TYPE).toString()     // string: specific to each cloud
        //instance.getParameters().get(DWaaSServiceInfo.CLOUD_REGION).toString()            // string

        //instance.getParameters().get(DWaaSServiceInfo.PIVNET_APIKEY).toString()
        //instance.getParameters().get(DWaaSServiceInfo.CLUSTER_SIZE).toString()
        //instance.getParameters().get(DWaaSServiceInfo.NETWORK_ACCESS_MASK).toString()

        //instance.getParameters().get(DWaaSServiceInfo.SQL_BOOTSTRAP_URL).toString()


        this.deployer.setDeploymentOptions("sb-gpdb","1", "us-west-2a", "G3vyGKeP-yz9YxJUizGd", "cognition-testing", "d2.4xlarge-Ephemeral-24TB", "0.0.0.0/0");


        //this.stackName           = "java-greenplum-cloudformation";
        String logicalResourceName = "SampleNotificationTopic";

        try {
            deployer.makeStack(cloudformationClient);
        }
        catch (Exception e) {

        }


        //user can optionally specify a db name
        log.info("instance created.");
    }

    /**
     * Code here will be called during the delete-service instance process. You can use this to de-allocate resources
     * on your underlying service, delete user accounts, destroy environments, etc.
     *
     * @param instance service instance data passed in by the cloud connector.
     */
    @Override
    public void deleteInstance(ServiceInstance instance) {
        String db = instance.getParameters().get(DWaaSServiceInfo.DATABASE).toString();
        log.info("deleting database: " + db);
        //client.deleteDatabase(db);

        try {
            deployer.deleteStack(cloudformationClient);
        }
        catch (Exception e) {

        }
    }

    /**
     * Code here will be called during the update-service process. You can use this to modify
     * your service instance.
     *
     * @param instance service instance data passed in by the cloud connector.
     */
    @Override
    public void updateInstance(ServiceInstance instance) {
        log.info("update not yet implemented");
    }

    /**
     * Called during the bind-service process. This is a good time to set up anything on your underlying service specifically
     * needed by an application, such as user accounts, rights and permissions, application-specific environments and connections, etc.
     * <p>
     * Services that do not support binding should set '"bindable": false,' within their catalog.json file. In this case this method
     * can be safely deleted in your implementation.
     *
     * @param instance service instance data passed in by the cloud connector.
     * @param binding  binding data passed in by the cloud connector. Clients can pass additional json
     *                 as part of the bind-service request, which will show up as key value pairs in binding.parameters. Brokers
     *                 can, as part of this method, store any information needed for credentials and unbinding operations as key/value
     *                 pairs in binding.properties
     */
    @Override
    public void createBinding(ServiceInstance instance, ServiceBinding binding) {
        log.debug("Binding a new Service Message For Testing");

        if (client == null) {
            log.debug("CLIENT IS NULL***************");
            return;
        } else {
<<<<<<< HEAD
            Map<String, Object> userCredentials = client.createUserCreds(binding);
            log.info("USER CREDENTIALS CREATED");
            binding.getParameters().put(DWaaSServiceInfo.USERNAME, userCredentials.get(DWaaSServiceInfo.USERNAME));
            binding.getParameters().put(DWaaSServiceInfo.PASSWORD, userCredentials.get(DWaaSServiceInfo.PASSWORD));
            binding.getParameters().put(DWaaSServiceInfo.DATABASE, userCredentials.get(DWaaSServiceInfo.DATABASE));
=======
            try {
                Map<String, String> userCredentials = client.createUserCreds(binding);
                log.debug("USER CREDENTIALS CREATED");
                binding.getParameters().put(DWaaSServiceInfo.USERNAME, userCredentials.get(DWaaSServiceInfo.USERNAME));
                binding.getParameters().put(DWaaSServiceInfo.PASSWORD, userCredentials.get(DWaaSServiceInfo.PASSWORD));
                binding.getParameters().put(DWaaSServiceInfo.DATABASE, userCredentials.get(DWaaSServiceInfo.DATABASE));
            } catch (Exception e) {
                log.error("BindingException: {}", e.getMessage());
            }
>>>>>>> 7b640b25e86752162a30dbbae9107f634e415131
        }
        log.info("bound app: " + binding.getAppGuid());
    }

    /**
     * Called during the unbind-service process. This is a good time to destroy any resources, users, connections set up during the bind process.
     *
     * @param instance service instance data passed in by the cloud connector.
     * @param binding  binding data passed in by the cloud connector.
     */
    @Override
    public void deleteBinding(ServiceInstance instance, ServiceBinding binding) {
        log.debug("DELETE Binding ");
        Map<String, Object> userCredentials = getCredentials(instance, binding);

        client.deleteUserCreds(userCredentials);
    }

    /**
     * Bind credentials that will be returned as the result of a create-binding process. The format and values of these credentials will
     * depend on the nature of the underlying service. For more information and some examples, see
     * <a href=https://docs.cloudfoundry.org/services/binding-credentials.html>here.</a>
     * <p>
     * This method is called after the create-binding method: any information stored in binding.properties in the createBinding call
     * will be available here, along with any custom data passed in as json parameters as part of the create-binding process by the client.
     *
     * @param instance service instance data passed in by the cloud connector.
     * @param binding  binding data passed in by the cloud connector.
     * @return credentials, as a series of key/value pairs
     */
    @Override
    public Map<String, Object> getCredentials(ServiceInstance instance, ServiceBinding binding) {
<<<<<<< HEAD
        log.info("returning credentials.");
        String theUser = binding.getParameters().get(DWaaSServiceInfo.USERNAME).toString();
        String thePasswd = binding.getParameters().get(DWaaSServiceInfo.PASSWORD).toString();
        String theDB = binding.getParameters().get(DWaaSServiceInfo.DATABASE).toString();


        Map<String, Object> m = new HashMap<String, Object>();
        m.put(DWaaSServiceInfo.URI, client.getDbUrl(theUser, theDB, thePasswd));

=======
        String theUser = (String) binding.getParameters().get(DWaaSServiceInfo.USERNAME);
        String thePasswd = (String) binding.getParameters().get(DWaaSServiceInfo.PASSWORD);
        String theDB = (String) binding.getParameters().get(DWaaSServiceInfo.DATABASE);
        String uri = client.getDbUrl(theUser, theDB, thePasswd);

        Map<String, Object> m = new HashMap<>();
        m.put(DWaaSServiceInfo.URI, uri);
>>>>>>> 7b640b25e86752162a30dbbae9107f634e415131
        m.put(DWaaSServiceInfo.USERNAME, theUser);
        m.put(DWaaSServiceInfo.PASSWORD, thePasswd);

        return m;
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}