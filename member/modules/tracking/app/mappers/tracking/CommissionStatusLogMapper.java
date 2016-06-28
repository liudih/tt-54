package mappers.tracking;

import entity.tracking.CommissionStatusLog;

public interface CommissionStatusLogMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(CommissionStatusLog record);

    int insertSelective(CommissionStatusLog record);

    CommissionStatusLog selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(CommissionStatusLog record);

    int updateByPrimaryKey(CommissionStatusLog record);
}