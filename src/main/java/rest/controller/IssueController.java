package rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.eBillDao.EbIssueTMapper;
import rest.mybatis.model.eBillModel.EbIssueT;
import rest.utils.CommonUtils;

@Controller
public class IssueController {

	@Autowired
	private EbIssueTMapper ebIssueTMapper;
	
	@RequestMapping(value = "/saveIssue2", method = RequestMethod.GET)
	@ResponseBody
	public String saveIssue(String issueStartDate,String issueEndDate ){
		if(null !=issueStartDate && null != issueEndDate){
			System.out.println(issueStartDate);
			System.out.println(issueEndDate);
		}else{
			System.out.println("===============null==============");
		}
		
		return "";
	}
	
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
	 * TODO 分页查询
	 * @return
	 */
	@RequestMapping(value = "/queryListByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryListByPage(@RequestBody Map<String,T> map){
		/*Map<String,T> map = (Map<String,T>)r.getAttribute("map");
		Map<String,String[]> map2 = r.getParameterMap();*/
		//Map<String,T> map = new HashMap<String,T>();
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, ebIssueTMapper);
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
			System.out.println(ebIssueT.getIssueStartDate1());
			System.out.println(ebIssueT.getIssueEndDate1());
			
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
			System.out.println("===============null==============");
		}
		
		return ebIssueT;
	}
}
