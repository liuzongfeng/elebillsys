package rest.MultiDataSourceTest;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import rest.config.LZFDS;
import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.dao.misDao.UsertMapper;
import rest.mybatis.model.misModel.Usert;
import rest.service.HandleTransactionalService;

@RunWith(SpringRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！ 
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
public class MultiDataSourceTest {

	@Autowired
	private UsertMapper usertMapper;
	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	@Autowired
	private HandleTransactionalService handleTransactionalService;
	
	/**
	 * @author 刘宗峰
	 * @TODE 测试动态数据源
	 */
	@Test
	public void testMisDb(){
		Usert u = handleTransactionalService.selectU(3255);
		Assert.assertEquals("902884", u.getComNum());
		
	}
}
