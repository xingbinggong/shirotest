package com.xingbg;

import com.xingbg.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Testor {
    public static void main(String[] args) {
        User zhang3 = new User();
        zhang3.setName("zhang3");
        zhang3.setPassword("12345");

        boolean login = login(zhang3);

        boolean admin = hasRole("admin");

        boolean addProduct = isPermitted("addProduct");
    }

    private static Subject getSubject() {
         //加载配置文件，并获取工厂
         Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-database.ini");
         //获取安全管理者实例
          SecurityManager sm = factory.getInstance();
          //将安全管理者放入全局对象
          SecurityUtils.setSecurityManager(sm);
          //全局对象通过安全管理者生成Subject对象
          Subject subject = SecurityUtils.getSubject();

          return subject;
    }

    private static boolean login(User user) {
       Subject subject= getSubject();
       //如果已经登录过了，退出
       if(subject.isAuthenticated()) {
           subject.logout();
       }

       //封装用户的数据
       UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
       try {
               //将用户的数据token 最终传递到Realm中进行对比
               subject.login(token);
           } catch (AuthenticationException e) {
               //验证错误
               return false;
           }

       return subject.isAuthenticated();
   }

    private static boolean hasRole( String role) {
         Subject subject = getSubject();
         return subject.hasRole(role);
     }

     private static boolean isPermitted(String permit) {
         Subject subject = getSubject();
         return subject.isPermitted(permit);
     }
}
