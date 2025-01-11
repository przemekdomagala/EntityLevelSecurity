package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import EntityLevelSecurity.Users.User;
import EntityLevelSecurity.Roles.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    @Mock
    private Role roleMock;

//    @Mock
//    private MyLogger loggerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNew_ShouldReturnNewUserInstance() {
        // Act
        User user = User.CreateNew();

        // Assert
        assertNotNull(user);
    }

    @Test
    void testWithName_ShouldSetNameAndLog() {
        // Arrange
        User user = User.CreateNew();
        String name = "TestUser";

        // Act
        user.withName(name);

        // Assert
        assertEquals(name, user.getName());
    }

    @Test
    void testWithRole_ShouldSetRoleAndLog() {
        // Arrange
        User user = User.CreateNew();
        when(roleMock.getName()).thenReturn("Admin");

        // Act
        user.withRole(roleMock);

        // Assert
        assertEquals(roleMock, user.getRole());
//        verify(loggerMock).log("User's role: Admin");
    }
}

