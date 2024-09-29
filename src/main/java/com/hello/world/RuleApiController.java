package com.hello.world;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RuleApiController {

    @Autowired
    private DroolsKieService droolsKieService;

    @PostMapping("/process")
    public Claim processClaim(@RequestBody Claim claim) {
        return droolsKieService.executeRules(claim);
    }
}
