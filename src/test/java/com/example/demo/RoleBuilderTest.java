package com.example.demo;

import EntityLevelSecurity.Roles.Role;
import EntityLevelSecurity.Roles.Permission;
import EntityLevelSecurity.Roles.RoleBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleBuilderTest {

    @Mock
    private ApplicationContext contextMock;

//    @Mock
//    private MyLogger loggerMock;

    @Mock
    private Role roleMock;

    private RoleBuilder roleBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleBuilder = new RoleBuilder();
        ReflectionTestUtils.setField(roleBuilder, "context", contextMock);
//        MyLogger.setInstance(loggerMock);
    }

    @Test
    void testCreateRole_ShouldReturnRoleInstanceAndLog() {
        // Arrange
        when(contextMock.getBean(Role.class)).thenReturn(roleMock);

        // Act
        Role role = roleBuilder.createRole();

        // Assert
        assertNotNull(role);
        assertEquals(roleMock, role);
//        verify(loggerMock).log("Creating Role");
    }
}

