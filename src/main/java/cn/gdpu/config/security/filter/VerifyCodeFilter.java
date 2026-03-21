package cn.gdpu.config.security.filter;

import cn.gdpu.config.security.handler.LoginFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @ClassName VerifyCodeFilter
 * @Author ttaurus
 * @Date Create in 2020/6/28 19:23
 */
@Component
public class VerifyCodeFilter extends OncePerRequestFilter{


    private static final Logger log = LoggerFactory.getLogger(VerifyCodeFilter.class);

    @Autowired
    private LoginFailureHandler loginFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //A、设置服务器端的编码
        response.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        //B、通知浏览器服务器发送的数据格式
        response.setContentType("text/html;charset=utf-8");
        if(request.getRequestURI().equals("/login")&&request.getMethod().equalsIgnoreCase("post")){
            try {
                validate(request);
            } catch (VerifyCodeException e) {
                log.error("登录失败 [{}] ",e.getMessage());
                request.getSession().setAttribute("login_msg",e.getMessage());
                //request.getRequestDispatcher("/login?error=true");
                response.sendRedirect("/login");
                return;
            }
        }
        // 3. 校验通过，就放行
        filterChain.doFilter(request, response);
        //清除session里的信息 且必须先准备好session对象 因为放行之后找不到对应的sessionid了
        session.removeAttribute("login_msg"); 
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException{
        String captcha = ServletRequestUtils.getStringParameter(request, "captcha"); //拿到表单传进来的验证码数值
        String code = (String) request.getSession().getAttribute(request.getParameter("uuid")); //拿到存放在session的验证码数值
        if(!code.equalsIgnoreCase(captcha)){
            request.getSession().removeAttribute(request.getParameter("uuid")); //清除session里的信息
            throw new VerifyCodeException("验证码不正确！");
        }
    }
}
