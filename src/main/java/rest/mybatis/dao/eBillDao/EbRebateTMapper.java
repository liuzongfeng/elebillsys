package rest.mybatis.dao.eBillDao;

import java.util.List;

import rest.mybatis.model.eBillModel.EbRebateT;

public interface EbRebateTMapper {
	
	
	/**
	 * @author 刘宗峰
	 * @TODO 根据期数id 查询应收净额
	 * @param issueId
	 * @return
	 */
	int searchByIssueId(String issueId);
	/**
     * @author 刘宗峰
     * @TODO 批量保存数据
     * @param list
     * @return
     */
    int insertBatch(List list);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insert(EbRebateT record);
    
    

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insertSelective(EbRebateT record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    EbRebateT selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKeySelective(EbRebateT record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_REBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKey(EbRebateT record);
}