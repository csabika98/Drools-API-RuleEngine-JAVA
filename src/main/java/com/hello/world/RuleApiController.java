package com.hello.world;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RuleApiController {

    @Autowired
    private KieContainer kieContainer;

    @PostMapping("/process")
    public Claim processClaim(@RequestBody Claim claim) {
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
        kieSession.insert(claim);
        kieSession.fireAllRules();
        kieSession.dispose();
        return claim;
    }

}