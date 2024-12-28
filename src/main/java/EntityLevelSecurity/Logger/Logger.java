package EntityLevelSecurity.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {

    @Pointcut("execution(public * EntityLevelSecurity.Roles.*.*(..))")
    public void allRoleMethods() {
        // Pointcut to match all public methods in Role class
    }

    @Pointcut("execution(public * EntityLevelSecurity.Users.*.*(..))")
    public void allUserMethods() {}

    @Before("allRoleMethods()")
    public void logAllRoleMethods(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    @After("allRoleMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After method: " + joinPoint.getSignature().getName());
    }

    @Before("allUserMethods()")
    public void logBeforeUser(JoinPoint joinPoint) {
        System.out.println("Before User method: " + joinPoint.getSignature().getName());
    }

    @After("allUserMethods()")
    public void logAfterUser(JoinPoint joinPoint) {
        System.out.println("After User method: " + joinPoint.getSignature().getName());
    }
}