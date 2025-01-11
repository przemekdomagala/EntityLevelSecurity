package com.example.demo;

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

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "EntityLevelSecurity.Logger", "EntityLevelSecurity.Roles", "EntityLevelSecurity"})
@EnableAspectJAutoProxy
public class DemoApplication {

    @Autowired
    private RoleBuilder roleBuilder;

    @Autowired
    private UserBuilder userBuilder;

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
    }

}
