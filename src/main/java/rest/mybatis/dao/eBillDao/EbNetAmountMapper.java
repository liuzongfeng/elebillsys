package rest.mybatis.dao.eBillDao;

import java.util.List;
import java.util.Map;

import rest.mybatis.model.eBillModel.EbNetAmount;
/**
 * 
 * @author 刘宗峰
 *
 */
public interface EbNetAmountMapper extends EleBillMapper{
	@Override
	int deleteBatch(List list);
	/**
	 * @author 刘宗峰
	 * @TODO 分页，传参，查询集合
	 */
	@Override
	List<EbNetAmount> queryListByPage(Map paramMap);
	/**
     * @author 刘宗峰
     * @param list
     * @return
     */
    int updateBatch(List list);
	/**
	 * @author 刘宗峰
	 * @TODO 查询对象集合
	 * @param issueId
	 * @return
	 */
	List<EbNetAmount> searchListByIssueId(String issueId);
	/**
	 * @author 刘宗峰
	 * @TODO 根据期数id 查询应收净额
	 * @param issueId
	 * @return
	 */
	int searchCountByIssueId(String issueId);
	
	 /**
     * @author 刘宗峰
     * @TODO 批量保存数据
     * @param list
     * @return
     */
    int insertBatch(List list);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insert(EbNetAmount record);

   
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insertSelective(EbNetAmount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    EbNetAmount selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKeySelective(EbNetAmount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKey(EbNetAmount record);
}