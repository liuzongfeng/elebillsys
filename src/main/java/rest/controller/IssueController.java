package rest.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.utils.CommonUtils;
import rest.utils.MyException;
/**
 * 
 * @author 刘宗峰
 *
 */
@Controller
public class IssueController {

	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	
	
	@RequestMapping(value = "/searchIssues", method = RequestMethod.GET)
	@ResponseBody
	public List<EbIssueT> searchIssues(){
		
		List<EbIssueT> es = ebIssueTMapper.selectIssueList();
		if(null != es && es.size()>0){
			return es;
		}else{
			
		}
		return null;
	}
	/**
	 * @author 刘宗峰
	 * TODO 分页查询
	 * @return
	 */
	@RequestMapping(value = "/queryListByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryListByPage(@RequestBody Map<String,T> map){
		
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, ebIssueTMapper);
	}
	@RequestMapping(value = "/delIssueById", method = RequestMethod.GET)
	@ResponseBody
	public int delIssueById(String issueId){
		if(null != issueId){
			//1.根据期数id 查询期数
			EbIssueT ebissue = ebIssueTMapper.selectByPrimaryKey(issueId);
			if(null != ebissue){
				
				//2.进行更新原内容
				ebissue.setIssueIsLive(0);
				ebissue.setIssueOperateDate(new Date());  //期数操作时间
				ebissue.setIssueOperater(3255); //期数操作人 ： 读取session中用户信息
				int d = ebIssueTMapper.updateByPrimaryKeySelective(ebissue);
				return d;
			}else{
				throw new MyException("不存在该账单期数，删除失败");
			}
		}else{
			throw new MyException("传参异常，删除期数失败");
		}
	}
	
	@RequestMapping(value = "/updateIssue", method = RequestMethod.GET)
	@ResponseBody
	public EbIssueT updateIssue(String issueId){
		
		if(null != issueId){
			
			//1.根据期数id 查询期数
			EbIssueT ebissue = ebIssueTMapper.selectByPrimaryKey(issueId);
			if(null != ebissue){
				
				//2.进行更新原内容
				ebissue.setIssueState(ebissue.getIssueState()+1 > 2 ? ebissue.getIssueState() : ebissue.getIssueState()+1 );  //期数业务状态 ：未发布
				ebissue.setIssueOperateDate(new Date());  //期数操作时间
				ebissue.setIssueOperater(3255); //期数操作人 ： 读取session中用户信息
				ebIssueTMapper.updateByPrimaryKeySelective(ebissue);
				
				//3.返回更新后的对象
				return ebissue;
			}else{
				throw new MyException("不存在该账单期数，更新失败");
			}
		}else{
			throw new MyException("传参异常，更新失败");
		}
		
	}
	/**
	 * @author 刘宗峰
	 * @TODO 查询是否存在未关闭的期数存在
	 * @return
	 */
	@RequestMapping(value = "/searchUnClosed", method = RequestMethod.GET)
	@ResponseBody
	public int searchUnClosed(){
		
		//1.查询是否存在未关闭的期数
		List<EbIssueT> ui = ebIssueTMapper.searchUnClosed();
		if(null != ui && ui.size() >0){
			return ui.size();
		}else{
			return 0;
		}
	}
	
	/**
	 * TODO 创建期数
	 * @param ebIssueT
	 * @return
	 */
	@RequestMapping(value = "/saveIssue", method = RequestMethod.GET)
	@ResponseBody
	public EbIssueT saveIssue2(EbIssueT ebIssueT){
		
		if(null !=ebIssueT){
			
			
			
			ebIssueT.setIssueStartDate(CommonUtils.strToDate(ebIssueT.getIssueStartDate1())); //期数开始时间
			ebIssueT.setIssueEndDate(CommonUtils.strToDate(ebIssueT.getIssueEndDate1())); //期数结束
			ebIssueT.setIssueState(0);  //期数业务状态 ：未发布
			ebIssueT.setBillState(0);  //账单业务状态 ：未核对
			ebIssueT.setIssueIsLive(1); //数据状态：1 未删除 0 删除
			String [] issueNameArr = ebIssueT.getIssueEndDate1().split("-");
			ebIssueT.setIssueName(CommonUtils.charToString(issueNameArr)); //期数名称：默认为截止日期
			
			ebIssueT.setIssueOperateDate(new Date());  //期数操作时间
			ebIssueT.setIssueOperater(null); //期数操作人 ： 读取session中用户信息
			
			ebIssueTMapper.insert(ebIssueT);
			
			return ebIssueT;
			
		}else{
			throw new MyException("传参异常，新增失败");
		}
	}
}
