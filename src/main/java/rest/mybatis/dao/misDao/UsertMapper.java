package rest.mybatis.dao.misDao;

import rest.mybatis.model.misModel.Usert;

public interface UsertMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int insert(Usert record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int insertSelective(Usert record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    Usert selectByPrimaryKey(Integer userId);

    Usert selectByComNum(String comNum);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int updateByPrimaryKeySelective(Usert record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(Usert record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table UserT
     *
     * @mbggenerated Fri Dec 08 15:15:59 CST 2017
     */
    int updateByPrimaryKey(Usert record);
}