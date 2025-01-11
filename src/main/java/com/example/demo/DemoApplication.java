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
        database.connect("jdbc:postgresql://localhost:5432/sampledb");

        database.getTableNames().forEach(System.out::println);

        System.out.println("Connected to database successfully!");
    }

    private ProxyDatabase proxyDatabase;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Role rola = roleBuilder.createRole().withName("sekretarka").withPermission(Permission.READ, "paragony");
        Role rola2 = roleBuilder.createRole().withName("HR Manager").withPermission(Permission.MODIFY, "pracownicy");

        User barbara = userBuilder.createUser().withRole(rola).withName("barbara");
        User engineers_lay_offs = userBuilder.createUser().withRole(rola).withName("engineers_lay_offs");
        Logger logger = Logger.getInstance();

//        System.out.println(rola.getName());
//        System.out.println(rola2.getName());
//        System.out.println(engineers_lay_offs.equals(barbara));
        System.out.println(rola.getName());
        System.out.println(rola2.getName());
        System.out.println(engineers_lay_offs.equals(barbara));


        Role engineerRole = roleBuilder.createRole().withName("Engineer").withPermission(Permission.MODIFY, "users");
        User engineer = userBuilder.createUser().withRole(engineerRole).withName("John");

        proxyDatabase = new ProxyDatabase(database, engineer);

        proxyDatabase.connect("jdbc:postgresql://localhost:5432/sampledb");

        System.out.println("Testing select operation...");
        Map<String, Object> whereConditions = Map.of("name", "Jane Smith");
        List<Map<String, Object>> results = proxyDatabase.select("users", whereConditions);

        System.out.println("Query results: " + results);

//        proxyDatabase.insert("users", Map.of("name", "KAJEK", "email", "kajesob@o2.pl"));

        System.out.println("Res" + proxyDatabase.select("users", Map.of("name", "KAJEK")));

        proxyDatabase.update("users", Map.of("name", "Kajek", "email", "EMAIL"), Map.of("name", "KAJEK"));

        System.out.println("Res" + proxyDatabase.select("users", Map.of("name", "KAJEK")));


        proxyDatabase.delete("users", Map.of("name", "KAJEK"));

        System.out.println("Res" + proxyDatabase.select("users", Map.of("name", "KAJEK")));


    }
}
