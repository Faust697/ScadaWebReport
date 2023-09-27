package com.example.ScadaWebReport.DbConfigurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;

@Configuration
@Profile("init-mongodb")
public class MongoInitializationConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoInitializationConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void initializeMongoDB() {
      //  Resource scriptResource = "";
        String scriptContent =  "";
        NamedMongoScript initScript = new NamedMongoScript("initScript", new ExecutableMongoScript(scriptContent));
      //  mongoTemplate.scriptOps().execute(initScript, Collections.emptyList());
    }
}