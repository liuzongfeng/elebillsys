package rest;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@RequestMapping("/testCache")
	public void testCacheEnable(){
		
		
		Set keys = redisTemplate.keys("*");
		for(Object s : keys){
			System.out.println(s.toString());
		}
		
		Set keys1 = redisTemplate.keys("*session*");
		for(Object o : keys1){
			System.out.println(o.toString());
		}
		//清空缓存
		redisCacheManager.getCache("cache1").clear();
	}
}

