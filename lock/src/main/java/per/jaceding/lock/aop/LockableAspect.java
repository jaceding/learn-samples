package per.jaceding.lock.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁AOP实现
 *
 * @author jaceding
 * @date 2020/8/25
 */
@Slf4j
@Aspect
@Component
public class LockableAspect {

    private final RedissonClient redissonClient;

    public LockableAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut(value = "@annotation(per.jaceding.lock.aop.Lockable)")
    public void lockablePointCut() {
    }


    @Around("lockablePointCut()")
    public Object invokeFlowLimiter(ProceedingJoinPoint point) throws Throwable {
        Method method = resolveMethod(point);
        Lockable lockable = method.getAnnotation(Lockable.class);
        String key = String.join(":", lockable.keys());
        log.info("key=" + key);
        RLock rLock = redissonClient.getLock(key);
        if (Long.MAX_VALUE == lockable.waitTime()) {
            if (Long.MAX_VALUE == lockable.leaseTime()) {
                rLock.lock();
            } else {
                rLock.lock(lockable.leaseTime(), lockable.unit());
            }
        } else {
            if (Long.MAX_VALUE == lockable.leaseTime()) {
                rLock.tryLock(lockable.waitTime(), lockable.unit());
            } else {
                rLock.tryLock(lockable.waitTime(), lockable.leaseTime(), lockable.unit());
            }
        }
        Object result = point.proceed();
        rLock.unlock();
        return result;
    }

    protected Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = getDeclaredMethodFor(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }

    /**
     * Get declared method with provided name and parameterTypes in given class and its super classes.
     * All parameters should be valid.
     *
     * @param clazz          class where the method is located
     * @param name           method name
     * @param parameterTypes method parameter type list
     * @return resolved method, null if not found
     */
    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}
