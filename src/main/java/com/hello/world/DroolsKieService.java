package com.hello.world;

import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.command.Command;
import org.kie.api.KieServices;
import org.kie.api.command.KieCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
public class DroolsKieService {

    @Value("${kie.server.url}")
    private String kieServerUrl;

    @Value("${kie.server.user}")
    private String kieServerUser;

    @Value("${kie.server.password}")
    private String kieServerPassword;

    @Value("${kie.server.container-id}")
    private String containerId;

    public Claim executeRules(Claim claim) {
        // Create a set of classes to be recognized by the JaxbMarshaller
        Set<Class<?>> jaxbClasses = new HashSet<>();
        jaxbClasses.add(Claim.class); // Add the Claim class to the set

        // Set up the KieServices client configuration with JAXB marshaller
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(
                KieServicesFactory.newRestConfiguration(kieServerUrl, kieServerUser, kieServerPassword, 10000)
                        .setMarshallingFormat(org.kie.server.api.marshalling.MarshallingFormat.JAXB)
                        .setExtraJaxbClasses(jaxbClasses) // Register the Claim class set
        );

        // Get the rule services client
        RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);

        // Create a new KieCommands instance
        KieCommands commandsFactory = KieServices.Factory.get().getCommands();

        // Create a list of commands: insert the claim and fire all rules
        List<Command<?>> commands = new ArrayList<>();
        commands.add(commandsFactory.newInsert(claim, "claim")); // Add claim to the session
        commands.add(commandsFactory.newFireAllRules());

        // Execute the commands in the container and get the ServiceResponse
        ServiceResponse<ExecutionResults> response = rulesClient.executeCommandsWithResults(containerId, commandsFactory.newBatchExecution(commands));

        // Check if the response is successful
        if (response.getType() == ServiceResponse.ResponseType.SUCCESS) {
            ExecutionResults results = response.getResult();
            return (Claim) results.getValue("claim");
        } else {
            throw new RuntimeException("Error executing rules: " + response.getMsg());
        }
    }
}


