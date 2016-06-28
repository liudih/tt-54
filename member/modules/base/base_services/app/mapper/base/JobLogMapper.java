package mapper.base;

import org.apache.ibatis.annotations.Insert;

import dto.JobLog;

public interface JobLogMapper {
	@Insert("insert into t_job_log(ctype,iresult,cremaeks) values(#{type},#{result},#{remark})")
	public int Insert(JobLog joblog);
}
