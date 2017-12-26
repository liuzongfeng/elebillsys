package rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rest.config.ApplicationContextProvider;
import rest.config.LZFDS;
import rest.config.MyThread;
import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbFoundsUseAmountMapper;
import rest.mybatis.dao.eBillDao.EbNetAmountMapper;
import rest.mybatis.dao.eBillDao.EbRebateTMapper;
import rest.mybatis.dao.eBillDao.EbSalesStatisticsTMapper;
import rest.mybatis.dao.eBillDao.EbUnrebateTMapper;
import rest.mybatis.dao.eBillDao.EleBillMapper;
import rest.mybatis.dao.misDao.TUserResourcesMapper;
import rest.mybatis.dao.misDao.UsertMapper;
import rest.mybatis.model.eBillModel.AdvancesReceived;
import rest.mybatis.model.eBillModel.EbFoundsUseAmount;
import rest.mybatis.model.eBillModel.EbNetAmount;
import rest.mybatis.model.eBillModel.EbRebateT;
import rest.mybatis.model.eBillModel.EbSalesStatisticsT;
import rest.mybatis.model.eBillModel.EbUnrebateT;
import rest.mybatis.model.misModel.TUserResources;
import rest.mybatis.model.misModel.Usert;
import rest.utils.MyException;

@Service
@EnableCaching
@CacheConfig(cacheNames = "cache1")
public class HandleTransactionalService {

	@Value("${self.lastIndex}")
	private int lastIndex;                                 //sublist的lastIndex值
	@Autowired
	private AdvancesReceivedMapper advancesReceivedMapper;            //预收表
	@Autowired
	private EbUnrebateTMapper ebUnrebateTMapper;                      //未核销的留返利
	@Autowired
	private EbRebateTMapper ebRebateTMapper;                          //留返利
	@Autowired
	private EbNetAmountMapper ebNetAmountMapper;                      //应收净额
	@Autowired
	private EbSalesStatisticsTMapper ebSalesStatisticsTMapper;        //销售业务统计
	@Autowired
	private EbFoundsUseAmountMapper ebFoundsUseAmountMapper;          //资金占用
	@Autowired
	private UsertMapper usertMapper;
	@Autowired
	private TUserResourcesMapper tUserResourcesMapper;
	@Autowired
	private HandleExectuorService handleExectuorService;
	
	@Cacheable(key ="#p0")
	@LZFDS("misDS")
	public List<Map> searchDZObj(String param_s){
		List<Usert> userTs =  usertMapper.searchDZObj(param_s);
		List<Map> l = new ArrayList<Map>();
		if(null != userTs && userTs.size() >0){
			for(Usert u : userTs){
				Map<String,String> m = new HashMap<String,String>();
				m.put("id", u.getUserId().toString());
				m.put("text", u.getUserName());
				m.put("value", u.getUserId().toString());
				l.add(m);
			}
		}
		
		return l;
	}
	
	@Cacheable(key ="#p0")
	@LZFDS("misDS")
	public Usert selectU(@Param("uId") Integer uId){
		return usertMapper.selectByPrimaryKey(uId);
	}
	
	@LZFDS("misDS")
	public List<TUserResources> selectListByComNum(@Param("username") String username){
		
		return tUserResourcesMapper.selectListByComNum(username);
	}
	/**
	 * @author 刘宗峰
	 * @TODO 查询人员
	 * @param comNum
	 * @return
	 */
	@Cacheable(key ="#p0")
	@LZFDS("misDS")
	public Usert selectUByComNum(String comNum){
		Usert u = usertMapper.selectByComNum(comNum);
		return u;
	}
	/**
	 * 
	 * @param list : 要保存的集合
	 * @param issueId ： 期数id
	 * @param fileType : 文件类型
	 * @throws Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void handleTransactional(List list,String issueId,String fileType) throws MyException{
		
		if(null != fileType && fileType.equals("file_yushou")){
			//1.先根据期数查询是否已经存在上传的数据
			//List<AdvancesReceived> forUpdateList = new ArrayList<AdvancesReceived>();
			List<AdvancesReceived> arList = advancesReceivedMapper.searchListByIssueId(issueId);
			try {
				//2.插入新数据
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					advancesReceivedMapper.insertBatch(insertList.get(i));
				}
				//3.执行更改，逻辑删除
				if(null != arList && arList.size() >0){
					/*for(AdvancesReceived ar : arList){
						ar.setAdvancesReceivedOperator("3148");
						ar.setAdvancesReceivedOperateDate(new Date());
						ar.setAdvancesReceivedIsLive(0);
						forUpdateList.add(ar);
					}*/
					List<List> updateList = obtainSubList(arList);
					for(int i=0; i<updateList.size(); i++){
						//advancesReceivedMapper.updateBatch(updateList.get(i));
						advancesReceivedMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new MyException("数据有误，上传预收表数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_rebate")){ //留返利往来清单
			//1.先根据期数查询是否已经存在上传的数据
			//List<EbRebateT> forUpdateList = new ArrayList<EbRebateT>();
			List<EbRebateT> reList = ebRebateTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					ebRebateTMapper.insertBatch(insertList.get(i));
				}
				if(null != reList && reList.size() >0){
					/*for(EbRebateT re : reList){
						re.setRebateOperator(3148);
						re.setRebateOperatDate(new Date());
						re.setRebateIsLive(0);
						forUpdateList.add(re);
					}*/
					List<List> updateList = obtainSubList(reList);
					for(int i=0; i<updateList.size(); i++){
						//ebRebateTMapper.updateBatch(updateList.get(i));
						ebRebateTMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new MyException("数据有误，上传留返利往来清单数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_unrebate")){//未核销的留返利
			//1.先根据期数查询是否已经存在上传的数据
			//List<EbUnrebateT> forUpdateList = new ArrayList<EbUnrebateT>();
			List<EbUnrebateT> unreList = ebUnrebateTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					ebUnrebateTMapper.insertBatch(insertList.get(i));
				}
				if(null != unreList && unreList.size() >0){
					/*for(EbUnrebateT unre : unreList){
						unre.setUnrebateOperator(3148);
						unre.setUnrebateOperateDate(new Date());
						unre.setUnrebateIsLive(0);
						forUpdateList.add(unre);
					}*/
					List<List> updateList = obtainSubList(unreList);
					for(int i=0; i<updateList.size(); i++){
						//ebUnrebateTMapper.updateBatch(updateList.get(i));
						ebUnrebateTMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传未核销的留返利数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_netsum")){ //应收净额
			//1.先根据期数查询是否已经存在上传的数据
			//List<EbNetAmount> forUpdateList = new ArrayList<EbNetAmount>();
			List<EbNetAmount> netList = ebNetAmountMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					ebNetAmountMapper.insertBatch(insertList.get(i));
				}
				if(null != netList && netList.size() > 0){
					/*for(EbNetAmount net : netList){
						net.setNetAmountOperator(3148);
						net.setNetAmountOperateDate(new Date());
						net.setNetAmountIsLive(0);
						forUpdateList.add(net);
					}*/
					List<List> updateList = obtainSubList(netList);
					for(int i=0; i<updateList.size(); i++){
						//ebNetAmountMapper.updateBatch(updateList.get(i));
						ebNetAmountMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传应收净额数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_statistic")){ //销售业务统计总表
			//1.先根据期数查询是否已经存在上传的数据
			//List<EbSalesStatisticsT> forUpdateList = new ArrayList<EbSalesStatisticsT>();
			List<EbSalesStatisticsT> ssList = ebSalesStatisticsTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					ebSalesStatisticsTMapper.insertBatch(insertList.get(i));
				}
				
				if(null != ssList && ssList.size() >0){
					/*for(EbSalesStatisticsT ss : ssList){
						ss.setOperator(3148);
						ss.setOperateDate(new Date());
						ss.setIsLive(0);
						forUpdateList.add(ss);
					}*/
					List<List> updateList = obtainSubList(ssList);
					for(int i=0; i<updateList.size(); i++){
						//ebSalesStatisticsTMapper.updateBatch(updateList.get(i));
						ebSalesStatisticsTMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传销售业务统计总表数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_useamount")){//资金占用费计算表
			//1.先根据期数查询是否已经存在上传的数据
			//List<EbFoundsUseAmount> forUpdateList = new ArrayList<EbFoundsUseAmount>();
			List<EbFoundsUseAmount> fuList = ebFoundsUseAmountMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				List<List> insertList = obtainSubList(list);
				for(int i=0; i<insertList.size(); i++){
					ebFoundsUseAmountMapper.insertBatch(insertList.get(i));
				}
				if(null != fuList && fuList.size() >0){
					/*for(EbFoundsUseAmount fu : fuList){
						fu.setOperator("3148");
						fu.setOperateDate(new Date());
						fu.setIsLive(0);
						forUpdateList.add(fu);
					}*/
					List<List> updateList = obtainSubList(fuList);
					for(int i=0; i<updateList.size(); i++){
						//ebFoundsUseAmountMapper.updateBatch(updateList.get(i));
						ebFoundsUseAmountMapper.deleteBatch(updateList.get(i));
					}
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传资金占用费计算表数据失败");
			}
			
		}
		
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 拆分list数据，每500一组
	 * @param list
	 * @return
	 */
	private List<List> obtainSubList(List list){
		List ll = new LinkedList();
		if(null != list && list.size() > lastIndex){
			int l = list.size()/lastIndex;
			//处理前l-1条数据
			for(int i=1; i<= l; i++){
				if(i == l){
					List subList_end = list.subList(lastIndex*(l-1), list.size()-1);
					ll.add(subList_end);
					break;
				}else{
					List subList_pre = list.subList(lastIndex*(i-1), (lastIndex*i)-1);
					ll.add(subList_pre);
				}
			}
			return ll;
		}else{
			ll.add(list);
			return ll;
		}
	}
}
