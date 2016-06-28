package mapper.interaction;

import dto.interaction.ReportError;

public interface ReportErrorMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(ReportError record);

    int insertSelective(ReportError record);

    ReportError selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(ReportError record);

    int updateByPrimaryKey(ReportError record);
}