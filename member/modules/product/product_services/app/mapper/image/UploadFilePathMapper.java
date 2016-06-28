package mapper.image;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.image.UploadFilePath;

public interface UploadFilePathMapper {
    @Insert("INSERT INTO t_uploadfile_path (cpath, ccreateuser) "
    	  + "VALUES (#{cpath}, #{ccreateuser})")
    @Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int createPath(UploadFilePath filePath);
    
    @Select("SELECT * FROM t_uploadfile_path ORDER BY iid")
    List<UploadFilePath> getAllFilePath();
    
    @Update("UPDATE t_uploadfile_path SET cpath = #{cpath} WHERE iid = #{iid}")
    int updatePath(UploadFilePath filePath);
    
    @Delete("DELETE FROM t_uploadfile_path WHERE iid = #{0}")
    int deletePathById(Integer iid);
    
    @Select("SELECT * FROM t_uploadfile_path WHERE cpath = #{0}")
    UploadFilePath getUploadFilePathByPath(String path);
}