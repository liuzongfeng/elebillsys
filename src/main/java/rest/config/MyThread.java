package rest.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbFoundsUseAmountMapper;
import rest.mybatis.dao.eBillDao.EbNetAmountMapper;
import rest.mybatis.dao.eBillDao.EbRebateTMapper;
import rest.mybatis.dao.eBillDao.EbSalesStatisticsTMapper;
import rest.mybatis.dao.eBillDao.EbUnrebateTMapper;
import rest.mybatis.dao.eBillDao.EleBillMapper;
import rest.mybatis.dao.misDao.TUserResourcesMapper;
import rest.mybatis.dao.misDao.UsertMapper;

/**
 * @TODO 自定义线程
 * @author 刘宗峰
 *
 */
@Component("myThread")
@Scope(value="prototype")
public class MyThread extends Thread{
	
	@Autowired
	private AdvancesReceivedMapper advancesReceivedMapper;            //预收表
	@Autowired
	private EbUnrebateTMapper ebUnrebateTMapper;                      //未核销的留返利
	@Autowired
	private EbRebateTMapper ebRebateTMapper;                          //留返利
	@Autowired
	private EbNetAmountMapper ebNetAmountMapper;                      //应收净额
	@Autowired
	public  EbSalesStatisticsTMapper ebSalesStatisticsTMapper;        //销售业务统计
	@Autowired
	private EbFoundsUseAmountMapper ebFoundsUseAmountMapper;          //资金占用
	@Autowired
	private UsertMapper usertMapper;
	@Autowired
	private TUserResourcesMapper tUserResourcesMapper;

	
	public AdvancesReceivedMapper getAdvancesReceivedMapper() {
		return advancesReceivedMapper;
	}

	public EbUnrebateTMapper getEbUnrebateTMapper() {
		return ebUnrebateTMapper;
	}

	public EbRebateTMapper getEbRebateTMapper() {
		return ebRebateTMapper;
	}

	public EbNetAmountMapper getEbNetAmountMapper() {
		return ebNetAmountMapper;
	}

	public EbSalesStatisticsTMapper getEbSalesStatisticsTMapper() {
		return ebSalesStatisticsTMapper;
	}

	public EbFoundsUseAmountMapper getEbFoundsUseAmountMapper() {
		return ebFoundsUseAmountMapper;
	}

	public UsertMapper getUsertMapper() {
		return usertMapper;
	}

	public TUserResourcesMapper gettUserResourcesMapper() {
		return tUserResourcesMapper;
	}

	public void startNewThreadForInsert(List list,EleBillMapper m){
		m.insertBatch(list);
		run();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("线程："+MyThread.currentThread().getName()+"运行中");
		
		super.run();
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}
}
