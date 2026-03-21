package cn.gdpu.config.security;

import cn.gdpu.bean.User;
import cn.gdpu.config.security.filter.VerifyCodeFilter;
import cn.gdpu.config.security.handler.LoginFailureHandler;
import cn.gdpu.service.AdminService;
import cn.gdpu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private VerifyCodeFilter verifyCodeFilter;
    //访问授权。。 只有登录了 才能访问选课内容。 否则 只能停留在登录页。
    //定义授权的规则
    @Override
    protected void configure(HttpSecurity http) throws Exception{ //配置策略
        http.csrf().disable(); //关闭防跨域。。
        http.authorizeRequests().
                antMatchers("/static/**" ,"/getCode","/login","/logout", "resources").permitAll().anyRequest().authenticated(). //允许静态资源访问

                // 登录页面。
                        and().formLogin().loginPage("/login").failureForwardUrl("/login?error=true").permitAll().
                //成功登录时默认跳转页面
                        successForwardUrl("/success").defaultSuccessUrl("/success")
                        .successHandler(loginSuccessHandler())  //成功的处理器
                        //.failureHandler(loginFailureHandler) //失败的处理器

                .//登录页面 所有人都可访问
                and().logout().logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler()).
                and().rememberMe().
                //以下这句就可以控制单个用户只能创建一个session，也就只能在服务器登录一次
                and().sessionManagement().maximumSessions(1).expiredUrl("/login");
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class); //使每次验证之前必须走验证码过滤器
    }

    //认证。
    //密码编码 passwordEncoder 
    //springboot security 新增了很多加密方法 且默认是需要密码加密的。也可设置不需要加密。
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        //从数据库读去 用户及用户对应的权限。
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder()); //对密码加密
        auth.eraseCredentials(false);
        //auth.authenticationProvider(authenticationProvider());
    }


    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
        return new TokenBasedRememberMeServices("springRocks" , userDetailsService());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ //密码加密
        return new BCryptPasswordEncoder(4);
    }


    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){ //登出处理
        return new LogoutSuccessHandler(){
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse , Authentication authentication) throws IOException, ServletException{
                try{
                    SecurityUser user = (SecurityUser) authentication.getPrincipal();
                    logger.info("用户 : {} 登出成功 ! " , user.getUsername());
                }catch(Exception e){
                    logger.error("printStackTrace" , e);
                }
                httpServletResponse.sendRedirect("/login");
            }
        };
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler(){ //登入处理
        return new SavedRequestAwareAuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request , HttpServletResponse response , Authentication authentication) throws IOException, ServletException{
                User userDetails = (User) authentication.getPrincipal();
                logger.info("用户 : " + userDetails.getUsername() + " 登录成功!  ");
                super.onAuthenticationSuccess(request , response , authentication);
            }
        };
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService(){    //用户登录实现
        return new UserDetailsService(){
            @Autowired
            private UserService userService;
            @Autowired
            private AdminService adminService;

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
                User user = null;
                if("admin".equals(username)){
                    user = adminService.login(username);
                }else{
                    user = userService.login(username);
                }
                if(user == null) throw new UsernameNotFoundException("Username " + username + " not found");
                return new SecurityUser(user);
                //String role = user.getRole();
                //SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            }
        };
    }


}
