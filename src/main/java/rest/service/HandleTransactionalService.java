package rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbFoundsUseAmountMapper;
import rest.mybatis.dao.eBillDao.EbNetAmountMapper;
import rest.mybatis.dao.eBillDao.EbRebateTMapper;
import rest.mybatis.dao.eBillDao.EbSalesStatisticsTMapper;
import rest.mybatis.dao.eBillDao.EbUnrebateTMapper;
import rest.mybatis.model.eBillModel.AdvancesReceived;
import rest.mybatis.model.eBillModel.EbFoundsUseAmount;
import rest.mybatis.model.eBillModel.EbNetAmount;
import rest.mybatis.model.eBillModel.EbRebateT;
import rest.mybatis.model.eBillModel.EbSalesStatisticsT;
import rest.mybatis.model.eBillModel.EbUnrebateT;
import rest.utils.MyException;

@Service
public class HandleTransactionalService {

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
			List<AdvancesReceived> forUpdateList = new ArrayList<AdvancesReceived>();
			List<AdvancesReceived> arList = advancesReceivedMapper.searchListByIssueId(issueId);
			try {
				//2.插入新数据
				advancesReceivedMapper.insertBatch(list);
				//3.执行更改，逻辑删除
				if(null != arList && arList.size() >0){
					for(AdvancesReceived ar : arList){
						ar.setAdvancesReceivedOperator("3148");
						ar.setAdvancesReceivedOperateDate(new Date());
						ar.setAdvancesReceivedIsLive(0);
						forUpdateList.add(ar);
					}
					advancesReceivedMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new MyException("数据有误，上传预收表数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_rebate")){ //留返利往来清单
			//1.先根据期数查询是否已经存在上传的数据
			List<EbRebateT> forUpdateList = new ArrayList<EbRebateT>();
			List<EbRebateT> reList = ebRebateTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				ebRebateTMapper.insertBatch(list);
				if(null != reList && reList.size() >0){
					for(EbRebateT re : reList){
						re.setRebateOperator(3148);
						re.setRebateOperatDate(new Date());
						re.setRebateIsLive(0);
						forUpdateList.add(re);
					}
					ebRebateTMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new MyException("数据有误，上传留返利往来清单数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_unrebate")){//未核销的留返利
			//1.先根据期数查询是否已经存在上传的数据
			List<EbUnrebateT> forUpdateList = new ArrayList<EbUnrebateT>();
			List<EbUnrebateT> unreList = ebUnrebateTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				ebUnrebateTMapper.insertBatch(list);
				if(null != unreList && unreList.size() >0){
					for(EbUnrebateT unre : unreList){
						unre.setUnrebateOperator(3148);
						unre.setUnrebateOperateDate(new Date());
						unre.setUnrebateIsLive(0);
						forUpdateList.add(unre);
					}
					ebUnrebateTMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传未核销的留返利数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_netsum")){ //应收净额
			//1.先根据期数查询是否已经存在上传的数据
			List<EbNetAmount> forUpdateList = new ArrayList<EbNetAmount>();
			List<EbNetAmount> netList = ebNetAmountMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				ebNetAmountMapper.insertBatch(list);
				if(null != netList && netList.size() > 0){
					for(EbNetAmount net : netList){
						net.setNetAmountOperator(3148);
						net.setNetAmountOperateDate(new Date());
						net.setNetAmountIsLive(0);
						forUpdateList.add(net);
					}
					ebNetAmountMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传应收净额数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_statistic")){ //销售业务统计总表
			//1.先根据期数查询是否已经存在上传的数据
			List<EbSalesStatisticsT> forUpdateList = new ArrayList<EbSalesStatisticsT>();
			List<EbSalesStatisticsT> ssList = ebSalesStatisticsTMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				ebSalesStatisticsTMapper.insertBatch(list);
				if(null != ssList && ssList.size() >0){
					for(EbSalesStatisticsT ss : ssList){
						ss.setOperator(3148);
						ss.setOperateDate(new Date());
						ss.setIsLive(0);
						forUpdateList.add(ss);
					}
					ebSalesStatisticsTMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传销售业务统计总表数据失败");
			}
			
		}else if(null != fileType && fileType.equals("file_useamount")){//资金占用费计算表
			//1.先根据期数查询是否已经存在上传的数据
			List<EbFoundsUseAmount> forUpdateList = new ArrayList<EbFoundsUseAmount>();
			List<EbFoundsUseAmount> fuList = ebFoundsUseAmountMapper.searchListByIssueId(issueId);
			//2.插入新数据
			try {
				ebFoundsUseAmountMapper.insertBatch(list);
				if(null != fuList && fuList.size() >0){
					for(EbFoundsUseAmount fu : fuList){
						fu.setOperator("3148");
						fu.setOperateDate(new Date());
						fu.setIsLive(0);
						forUpdateList.add(fu);
					}
					ebFoundsUseAmountMapper.updateBatch(forUpdateList);
				}
			} catch (Exception e) {

				e.printStackTrace();
				throw new MyException("数据有误，上传资金占用费计算表数据失败");
			}
			
		}
		
	}
}
