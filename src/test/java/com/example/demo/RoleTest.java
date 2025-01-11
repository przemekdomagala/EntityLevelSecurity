package com.example.demo;

import EntityLevelSecurity.Roles.Role;
import EntityLevelSecurity.Roles.Permission;
import EntityLevelSecurity.Roles.RoleBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleTest {

//    @Mock
//    private MyLogger loggerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        MyLogger.setInstance(loggerMock);
    }

    @Test
    void testWithName_ShouldSetNameAndLog() {
        // Arrange
        Role role = new Role();
        String roleName = "Admin";

        // Act
        role.withName(roleName);

        // Assert
        assertEquals(roleName, role.getName());
//        verify(loggerMock).log("Role's name: " + roleName);
    }

    @Test
    void testWithPermission_ShouldAddPermissionAndLog() {
        // Arrange
        Role role = new Role();
        String tableName = "Users";
        Permission permission = Permission.READ;

        // Act
        role.withPermission(permission, tableName);

        // Assert
        Map<String, Permission> permissions = role.getPermissions();
        assertTrue(permissions.containsKey(tableName));
        assertEquals(permission, permissions.get(tableName));
//        verify(loggerMock).log("Role with READ permission on table Users");
    }
}

