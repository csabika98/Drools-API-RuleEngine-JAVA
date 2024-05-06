package com.hello.world;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.kie.api.runtime.KieSession;



@Configuration
public class DroolsConfig {

    @Bean
    public KieServices kieServices() {
        return KieServices.Factory.get();
    }

    @Bean
    public KieContainer kieContainer(KieServices kieServices) {
        return kieServices.getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession("ksession-rules");
    }
}
