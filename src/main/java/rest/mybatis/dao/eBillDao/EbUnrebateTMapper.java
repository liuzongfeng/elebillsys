package rest.mybatis.dao.eBillDao;

import rest.mybatis.model.eBillModel.EbUnrebateT;

public interface EbUnrebateTMapper {
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