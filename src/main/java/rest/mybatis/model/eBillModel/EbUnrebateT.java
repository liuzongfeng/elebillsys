package rest.mybatis.model.eBillModel;

import java.util.Date;

public class EbUnrebateT {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.ID
     *@author 刘宗峰
     *@see 未核销留返利表主键uuid
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NO
     * @author 刘宗峰
     *@see 对账对象编号
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private String reconciliationObjNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NAME
     *@author 刘宗峰
     *@see 对账对象名称
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private String reconciliationObjName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.REBATE_NET_AMOUNT
     *@author 刘宗峰
     *@see 留返利净额
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private Double rebateNetAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.UNREBATE_OPERATOR
     *@author 刘宗峰
     *@see 未核销留返利表操作人
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private Integer unrebateOperator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.UNREBATE_OPERATE_DATE
     *@author 刘宗峰
     *@see 操作人操作时间
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private Date unrebateOperateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.UNREBATE_IS_LIVE
     *@author 刘宗峰
     *@see 未核销留返利是否作废
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private Integer unrebateIsLive;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EB_UNREBATE_T.FR_ISSUE_ID
     *@author 刘宗峰
     *@see 账单期数id
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    private String frIssueId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.ID
     *
     * @return the value of EB_UNREBATE_T.ID
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.ID
     *
     * @param id the value for EB_UNREBATE_T.ID
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NO
     *
     * @return the value of EB_UNREBATE_T.RECONCILIATION_OBJ_NO
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public String getReconciliationObjNo() {
        return reconciliationObjNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NO
     *
     * @param reconciliationObjNo the value for EB_UNREBATE_T.RECONCILIATION_OBJ_NO
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setReconciliationObjNo(String reconciliationObjNo) {
        this.reconciliationObjNo = reconciliationObjNo == null ? null : reconciliationObjNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NAME
     *
     * @return the value of EB_UNREBATE_T.RECONCILIATION_OBJ_NAME
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public String getReconciliationObjName() {
        return reconciliationObjName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.RECONCILIATION_OBJ_NAME
     *
     * @param reconciliationObjName the value for EB_UNREBATE_T.RECONCILIATION_OBJ_NAME
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setReconciliationObjName(String reconciliationObjName) {
        this.reconciliationObjName = reconciliationObjName == null ? null : reconciliationObjName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.REBATE_NET_AMOUNT
     *
     * @return the value of EB_UNREBATE_T.REBATE_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public Double getRebateNetAmount() {
        return rebateNetAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.REBATE_NET_AMOUNT
     *
     * @param rebateNetAmount the value for EB_UNREBATE_T.REBATE_NET_AMOUNT
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setRebateNetAmount(Double rebateNetAmount) {
        this.rebateNetAmount = rebateNetAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.UNREBATE_OPERATOR
     *
     * @return the value of EB_UNREBATE_T.UNREBATE_OPERATOR
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public Integer getUnrebateOperator() {
        return unrebateOperator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.UNREBATE_OPERATOR
     *
     * @param unrebateOperator the value for EB_UNREBATE_T.UNREBATE_OPERATOR
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setUnrebateOperator(Integer unrebateOperator) {
        this.unrebateOperator = unrebateOperator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.UNREBATE_OPERATE_DATE
     *
     * @return the value of EB_UNREBATE_T.UNREBATE_OPERATE_DATE
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public Date getUnrebateOperateDate() {
        return unrebateOperateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.UNREBATE_OPERATE_DATE
     *
     * @param unrebateOperateDate the value for EB_UNREBATE_T.UNREBATE_OPERATE_DATE
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setUnrebateOperateDate(Date unrebateOperateDate) {
        this.unrebateOperateDate = unrebateOperateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.UNREBATE_IS_LIVE
     *
     * @return the value of EB_UNREBATE_T.UNREBATE_IS_LIVE
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public Integer getUnrebateIsLive() {
        return unrebateIsLive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.UNREBATE_IS_LIVE
     *
     * @param unrebateIsLive the value for EB_UNREBATE_T.UNREBATE_IS_LIVE
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setUnrebateIsLive(Integer unrebateIsLive) {
        this.unrebateIsLive = unrebateIsLive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EB_UNREBATE_T.FR_ISSUE_ID
     *
     * @return the value of EB_UNREBATE_T.FR_ISSUE_ID
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public String getFrIssueId() {
        return frIssueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EB_UNREBATE_T.FR_ISSUE_ID
     *
     * @param frIssueId the value for EB_UNREBATE_T.FR_ISSUE_ID
     *
     * @mbggenerated Fri Sep 22 14:09:36 CST 2017
     */
    public void setFrIssueId(String frIssueId) {
        this.frIssueId = frIssueId == null ? null : frIssueId.trim();
    }
}