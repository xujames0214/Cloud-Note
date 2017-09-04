package cn.tedu.note.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointcutAspect {
	@Before("within(cn.tedu.note.service.impl.NoteServiceImpl)")
	public void test(){
		System.out.println("cut point");
	}
}
