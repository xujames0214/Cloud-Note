package cn.tedu.note.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.util.JsonResult;

/**
 * 创建一个切面组件，就是一个普通的JavaBean
 * @author Administrator
 *
 */
@Component
@Aspect
public class DemoAspect {
	//声明test方法将在userService的全部方法之前运行
	@Before("bean(userService)")
	public void test(){
		System.out.println("Before");
	}
	
	@After("bean(userService)")
	public void test2(){
		System.out.println("After");
	}
	
	@AfterThrowing("bean(userService)")
	public void test3(){
		System.out.println("AfterThrowing");
	}
	
	@AfterReturning("bean(userService)")
	public void test4(){
		System.out.println("AfterReturning");
	}
	/**
	 * 1.
	 * @param jp
	 * @return
	 */
	@Around("bean(userService)")
	public Object test5(ProceedingJoinPoint jp)
		throws Throwable{
		Object val = jp.proceed();
		return val;
	}
	
	
}
