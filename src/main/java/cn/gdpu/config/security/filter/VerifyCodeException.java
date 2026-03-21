package cn.gdpu.config.security.filter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName VerifyCodeException
 * @Author ttaurus
 * @Date Create in 2020/6/28 19:23
 */

public class VerifyCodeException extends AuthenticationException{
    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public VerifyCodeException(String msg) {
        super(msg);
    }
}
