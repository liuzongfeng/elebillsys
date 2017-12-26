package rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.eBillDao.AdvancesReceivedMapper;
import rest.mybatis.dao.eBillDao.EbFoundsUseAmountMapper;
import rest.mybatis.dao.eBillDao.EbNetAmountMapper;
import rest.mybatis.dao.eBillDao.EbRebateTMapper;
import rest.service.HandleTransactionalService;
import rest.utils.CommonUtils;

@Controller
public class eleBillSearchController {

	@Autowired
	private HandleTransactionalService handleTransactionalService;
	@Autowired
	private EbNetAmountMapper ebNetAmountMapper;
	@Autowired
	private EbRebateTMapper ebRebateTMapper;
	@Autowired
	private EbFoundsUseAmountMapper ebFoundsUseAmountMapper;
	@Autowired
	private AdvancesReceivedMapper advancesReceivedMapper;
	
	
	@RequestMapping("/searchDZObj")
	@ResponseBody
	public List<Map> searchDZObj(String param_s){
		return handleTransactionalService.searchDZObj(param_s);
		
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 查询应收信息
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryEbNetAmountsByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryEbNetAmountsByPage(@RequestBody Map<String,T> map){
		
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, ebNetAmountMapper);
	}
	/**
	 * @author 刘宗峰
	 * @TODO 查询预收款
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryAdvancesReceivedsByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryAdvancesReceivedsByPage(@RequestBody Map<String,T> map){
		
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, advancesReceivedMapper);
	}
	/**
	 * @author 刘宗峰
	 * @TODO 查询留返利
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryEbRebatesByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryEbRebatesByPage(@RequestBody Map<String,T> map){
		
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, ebRebateTMapper);
	}
	
	/**
	 * @author 刘宗峰
	 * @TODO 查询资金占用费
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryEbFoundsUseAmountsByPage", method = RequestMethod.POST)
	@ResponseBody
	public <T> PageInfo queryEbFoundsUseAmountsByPage(@RequestBody Map<String,T> map){
		
		CommonUtils commonUtils = new CommonUtils();
		
		return commonUtils.queryListByPage(map, ebFoundsUseAmountMapper);
	}
}
