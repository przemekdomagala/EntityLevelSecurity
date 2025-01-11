package com.example.demo;

import EntityLevelSecurity.Database;
import EntityLevelSecurity.ProxyDatabase;
import EntityLevelSecurity.Logger.Logger;
import EntityLevelSecurity.Users.User;
import EntityLevelSecurity.Users.UserBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import EntityLevelSecurity.Roles.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import EntityLevelSecurity.Command.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "EntityLevelSecurity.Logger", "EntityLevelSecurity.Roles", "EntityLevelSecurity"})
@EnableAspectJAutoProxy
public class DemoApplication {

    @Autowired
    private RoleBuilder roleBuilder;

    @Autowired
    private UserBuilder userBuilder;

    @Autowired
    private Database database;

    @PostConstruct
    public void connectToDatabase() {
        database.connect("jdbc:postgresql://localhost:5432/test_hib");

        database.getTableNames().forEach(System.out::println);

        System.out.println("Connected to database successfully!");
    }

    private ProxyDatabase proxyDatabase;

    private CommandInvoker commandInvoker;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Role rola = roleBuilder.createRole().withName("sekretarka").withPermission(Permission.READ, "minions");
        Role rola2 = roleBuilder.createRole().withName("HR Manager").withPermission(Permission.MODIFY, "polacy");


        User barbara = userBuilder.createUser().withRole(rola).withName("barbara");
        User engineers_lay_offs = userBuilder.createUser().withRole(rola).withName("engineers_lay_offs");
        Logger logger = Logger.getInstance();

//        System.out.println(rola.getName());
//        System.out.println(rola2.getName());
//        System.out.println(engineers_lay_offs.equals(barbara));
        System.out.println(rola.getName());
        System.out.println(rola2.getName());
        System.out.println(engineers_lay_offs.equals(barbara));


        Role engineerRole = roleBuilder.createRole().withName("Engineer").withPermission(Permission.MODIFY, "manhattan.minions");
        User engineer = userBuilder.createUser().withRole(engineerRole).withName("John");

        proxyDatabase = new ProxyDatabase(database, engineer);

        proxyDatabase.connect("jdbc:postgresql://localhost:5432/test_hib");

        System.out.println("Testing select operation...");
        Map<String, Object> whereConditions = Map.of("name", "Bob");
        List<Map<String, Object>> results = proxyDatabase.select("manhattan.minions", whereConditions);

        System.out.println("Query results: " + results);

        proxyDatabase.insert("manhattan.minions", Map.of("name", "KAJEK", "job", "best"));

        System.out.println("Res" + proxyDatabase.select("manhattan.minions", Map.of("name", "KAJEK")));

        proxyDatabase.update("manhattan.minions", Map.of("name", "Kajek", "job", "even_better"), Map.of("name", "KAJEK"));

        System.out.println("Res" + proxyDatabase.select("manhattan.minions", Map.of("name", "KAJEK")));


        proxyDatabase.delete("manhattan.minions", Map.of("name", "KAJEK"));

        System.out.println("Res" + proxyDatabase.select("manhattan.minions", Map.of("name", "KAJEK")));


        commandInvoker = new CommandInvoker();

        SelectCommand selectBob = new SelectCommand(proxyDatabase,"manhattan.minions", Map.of("name", "Bob"));
        InsertCommand insertWegiel = new InsertCommand(proxyDatabase,"manhattan.minions", Map.of("name", "Wegiel", "job", "pasterz traszek"));
        SelectCommand selectWegiel = new SelectCommand(proxyDatabase,"manhattan.minions", Map.of("name", "Wegiel"));
        UpdateCommand updateWegiel = new UpdateCommand(proxyDatabase,"manhattan.minions", Map.of("name", "Wegiel", "job", "pan i wladca imperator jowisza"), Map.of("name", "Wegiel"));
        DeleteCommand deleteWegiel = new DeleteCommand(proxyDatabase,"manhattan.minions", Map.of("name", "Wegiel"));


        commandInvoker.addCommand(selectBob);

        commandInvoker.addCommand(insertWegiel);
        commandInvoker.addCommand(selectWegiel);

        commandInvoker.addCommand(updateWegiel);
        commandInvoker.addCommand(selectWegiel);

        commandInvoker.addCommand(deleteWegiel);
        commandInvoker.addCommand(selectWegiel);

        commandInvoker.executeCommands();
    }
}
