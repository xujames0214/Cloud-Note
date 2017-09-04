package cn.tedu.note.service;

import cn.tedu.note.entity.User;

public interface UserService {
	User login(String name, String password) throws UserNotFoundException, PasswordException;
	User regist(String name,String nick,String password,String confirm)throws UserNameException,PasswordException;
	
}
