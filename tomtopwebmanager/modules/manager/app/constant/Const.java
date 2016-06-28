package constant;
/**
 * 常量类
 * @author Administrator
 *
 */
public class Const {
	
	//返回状态
	public static final String STATE="state";//返回状态 key
	public static final String MSG="msg";	//返回错误信息
	
	public static final String STATE_OK="Y";
	public static final String STATE_FAIL="N";
	public static final String STATE_ERROR="E";
	
	public static final String STORAGE_TYPE_PARENT="1";//type为查询的类型 1：为仓库层级关系 2：为地区默认仓库 3：仓库可送达地区 4：前面三种集合
	public static final String STORAGE_TYPE_ARRIVAL="2";//type为查询的类型 1：为仓库层级关系 2：为地区默认仓库 3：仓库可送达地区 4：前面三种集合
	public static final String STORAGE_TYPE_DEFAULT="3";//type为查询的类型 1：为仓库层级关系 2：为地区默认仓库 3：仓库可送达地区 4：前面三种集合
	public static final String STORAGE_TYPE_SUB="4";//type为查询的类型 1：为仓库层级关系 2：为地区默认仓库 3：仓库可送达地区 4：前面三种集合
	public static final String STORAGE_TYPE_ALL="5";//type为查询的类型 1：为仓库层级关系 2：为地区默认仓库 3：仓库可送达地区 4：前面三种集合
}
