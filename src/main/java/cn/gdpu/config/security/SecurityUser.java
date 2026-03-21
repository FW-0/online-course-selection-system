package cn.gdpu.config.security;

import cn.gdpu.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUser extends User implements UserDetails {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(SecurityUser.class);

    public SecurityUser(User user) {
        if (user != null) {
            this.setStudentName(user.getStudentName());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setRole(user.getRole());
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String role = this.getRole();
        if(role != null){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);
        }
        log.info("获取登录用户已具有的权限：{}", authorities.toString());
        return authorities;
    }

    @Override
    public boolean equals(Object rhs) {
        return this.toString().equals(rhs.toString());
    }

    /**
     * 重写方法用于sessionRegistry
     * @return
     */
    @Override
    public int hashCode() {
        return super.getUsername().hashCode();
    }


    //账户是否未过期,过期无法验证
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //指定用户是否解锁,锁定的用户无法进行身份验证
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用 ,禁用的用户不能身份验证
    @Override
    public boolean isEnabled() {
        return true;
    }
}
