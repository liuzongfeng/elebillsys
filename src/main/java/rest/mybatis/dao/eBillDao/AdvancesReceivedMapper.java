package rest.mybatis.dao.eBillDao;

import java.util.List;

import rest.mybatis.model.eBillModel.AdvancesReceived;

public interface AdvancesReceivedMapper {
	
	/**
	 * @author 刘宗峰
	 * @TODO 查询对象集合
	 * @param issueId
	 * @return
	 */
	List<AdvancesReceived> searchListByIssueId(String issueId);

	/**
	 * @author 刘宗峰
	 * @TODO 根据期数id 查询应收净额
	 * @param issueId
	 * @return
	 */
	int searchCountByIssueId(String issueId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insert(AdvancesReceived record);
    /**
     * @author 刘宗峰
     * @param list
     * @return
     */
    int insertBatch(List list);
    /**
     * @author 刘宗峰
     * @param list
     * @return
     */
    int updateBatch(List list);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int insertSelective(AdvancesReceived record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    AdvancesReceived selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKeySelective(AdvancesReceived record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ADVANCES_RECEIVED
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    int updateByPrimaryKey(AdvancesReceived record);
}