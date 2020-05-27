package com.itheima.ssm.controller;


import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ISysLogService iSysLogService;


    private Date visitTime;//开始时间
    private Class calzz;  //访问的类
    private Method method; //访问的方法

    //前置通知 主要获取开启时间，执行类是哪一个，执行哪个方法
    @Before("execution(* com.itheima.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime=new Date();//当前时间就是开始访问时间
        calzz=jp.getTarget().getClass();//具体访问的类
        String methodName=jp.getSignature().getName();//获取方法的名称
        Object[] args = jp.getArgs();//获取访问的方法参数


        //获取到了具体执行的方法的method对象
        if (args == null || args.length==0 ){
            method=calzz.getMethod(methodName);//只能获取无参数的方法
        }else {
            Class[] classArgs =new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classArgs[i] =args[i].getClass();
            }
            calzz.getMethod(methodName,classArgs);
        }

    }
    //后置通知
    @After("execution(* com.itheima.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {
        Long time =new Date().getTime()-visitTime.getTime();//获取访问时长

        String url ="";
        //获取URL
        if (calzz != null && method != null &&calzz != LogAop.class){
            //1.获取类上的@RequestMapping("/orders")
            RequestMapping classAnnotation = (RequestMapping) calzz.getAnnotation(RequestMapping.class);
            if (classAnnotation != null){
                String[] classValue = classAnnotation.value();

                //2.获取方法上的@RequestMapping（xxx）
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null){
                    String[] methodValue = methodAnnotation.value();

                    url=classValue[0] +methodValue[0];

                    //获取访问的ip地址
                    String ip =request.getRemoteAddr();

                    //获取当前操作用户               可以通过SecurityContext获取，也可以从request.getSession中获取
                    SecurityContext context = SecurityContextHolder.getContext();//从上下文中获取了当前登陆的用户
                    User user = (User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();


                    //将日志相关信息封装到SysLog对象
                    SysLog sysLog =new SysLog();
                    sysLog.setIp(ip);
                    sysLog.setUrl(url);
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(visitTime);
                    sysLog.setExecutionTime(time);
                    sysLog.setMethod("[类名]"+calzz.getName()+"[方法名]"+method.getName());

                    //调用service完成
                    iSysLogService.save(sysLog);
                }
            }
        }

    }
}


