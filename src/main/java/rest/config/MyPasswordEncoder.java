package rest.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import rest.utils.MD5Utils;
/**
 * @TODO 处理密码加密问题
 * @author 刘宗峰
 *
 */
public class MyPasswordEncoder extends BCryptPasswordEncoder {

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if(null == rawPassword && null == encodedPassword){
			return false;
		}else{
			MD5Utils md5 = new MD5Utils();
			String decodedPassword = md5.getMD5ofStr(rawPassword.toString());
			
			if(encodedPassword.equals(decodedPassword)){
				return true;
			}else{
				return false;
			}
		}
	}
}
