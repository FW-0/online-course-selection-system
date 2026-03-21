package cn.gdpu.config.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 一种成功的身份验证策略，通过ExceptionTranslationFilter，默认将DefaultSavedRequest
 * 存储在session中
 *
 * 当这样的请求被截获并需要身份验证时，在身份验证过程开始之前存储请求数据以记录原始目的地（url）
 * 并允许在重定向到相同的URL时重键请求，如果合适的话，这个类负责执行到原始URL的重定向。
 *
 * 在成功进行验证之后，它根据以下场景确定重定向目标：
 *
 * 1. 如果alwaysUseDefaultTargetUrl属性设置为true，则defaultTargetUrl将用于目标.
 * 会话中存储的任何defaultSavedRequest将被删除。
 * 2. 如果在请求中设置了targetUrlParameter,则该值将用作目标。defaultTargetUrl将再次被删除
 * 3. 如果在RequestCache中找到SavedRequest(由ExceptionTranslationFilter所设置，在验证过程
 * 开始之前记录原始目的地)，将对原始目的地的Url执行重定向。当收到重定向请求时，SavedRequest对象
 * 将保持缓存状态并被拾取（请参阅SavedRequestAwareWrapper）
 * 4.如果找不到SavedRequest它将委托给基类（父类）。
 *
 * @author xiyang.ycj
 * @since Jun 25, 2019 01:41:57 AM
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {


        // 会帮我们跳转到上一次请求的页面上
        //super.onAuthenticationSuccess(request, response, authentication);
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //PrintWriter writer = response.getWriter();
        //writer.write("登录成功");
        //writer.flush();
        //writer.close();
    }


}
