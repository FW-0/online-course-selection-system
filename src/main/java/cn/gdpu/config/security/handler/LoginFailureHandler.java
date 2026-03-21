package cn.gdpu.config.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginFailureHandler
 * @Author ttaurus
 * @Date Create in 2020/6/28 19:23
 */

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
        LOGGER.error("登录失败 [{}] ",exception.getMessage());
        //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        //response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //response.setCharacterEncoding("utf-8");
        response.sendRedirect("/login");
    }
}
