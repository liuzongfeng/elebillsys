package rest.mybatis.dao.eBillDao;

import java.util.List;

import rest.mybatis.model.eBillModel.EbUnrebateT;

public interface EbUnrebateTMapper {
	
	/**
	 * @author 刘宗峰
	 * @TODO 根据期数id 查询应收净额
	 * @param issueId
	 * @return
	 */
	int searchByIssueId(String issueId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insert(EbUnrebateT record);
    /**
     * @author 刘宗峰
     * @TODO 批量保存数据
     * @param list
     * @return
     */
    int insertBatch(List list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insertSelective(EbUnrebateT record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    EbUnrebateT selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKeySelective(EbUnrebateT record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EB_UNREBATE_T
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKey(EbUnrebateT record);
}