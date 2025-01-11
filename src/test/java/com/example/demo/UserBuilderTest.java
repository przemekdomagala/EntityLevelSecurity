package com.example.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import EntityLevelSecurity.Users.User;
import EntityLevelSecurity.Users.UserBuilder;
import EntityLevelSecurity.Roles.Role;
import EntityLevelSecurity.Roles.RoleBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class UserBuilderTest {

    @Mock
    private ApplicationContext contextMock;

    @Mock
    private User userMock;

    private UserBuilder userBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userBuilder = new UserBuilder();
        ReflectionTestUtils.setField(userBuilder, "context", contextMock);
    }

    @Test
    void testCreateUser_ShouldReturnUserInstanceAndLog() {
        when(contextMock.getBean(User.class)).thenReturn(userMock);
        User user = userBuilder.createUser();
        assertNotNull(user);
        assertEquals(userMock, user);
    }
}
