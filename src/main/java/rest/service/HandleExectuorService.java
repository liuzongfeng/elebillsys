package rest.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import rest.mybatis.dao.eBillDao.EleBillMapper;
/**
 * @TODO 异步多线程
 * @author 刘宗峰
 *
 */
@Service
public class HandleExectuorService {

	protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * @author 刘宗峰
	 * @TODO 异步启动多线程
	 * @param list
	 * @param m
	 */
	@Async("myExecutor2")
	public void startNewThreadForInsert(List list,EleBillMapper m){
		logger.info("=============第次批量插入开始："+System.currentTimeMillis());
		m.insertBatch(list);
		logger.info("=============第次批量插入结束："+System.currentTimeMillis());
	}
}
