package com.info.dao;

import com.info.model.Activity;
import com.info.model.ActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int countByExample(ActivityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int deleteByExample(ActivityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int deleteByPrimaryKey(Integer aId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    List<Activity> selectByExample(ActivityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    Activity selectByPrimaryKey(Integer aId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int updateByExampleSelective(@Param("record") Activity record, @Param("example") ActivityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int updateByExample(@Param("record") Activity record, @Param("example") ActivityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table a_activity
     *
     * @mbggenerated Thu Jul 09 10:07:06 CST 2015
     */
    int updateByPrimaryKey(Activity record);
}