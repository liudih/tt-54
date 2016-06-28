package valueobjects.base.activity;

/**
 * 组件参数
 * 
 * @author fcl
 *
 */
public class ActivityComponentParam {

	/**
	 * id 主键
	 */
	int id;
	/**
	 * 类全名
	 */
	String className;
	/**
	 * 类参数 josn
	 */
	String param;
	/**
	 * 类额外参数 json
	 */
	String extendsParam;
	/**
	 * 优先级
	 */
	int priority;
	
	/**
	 * 名称
	 */
	String name;

	public ActivityComponentParam(int id, String className, String param,
			String extendsParam, int priority,String name) {
		super();
		this.id = id;
		this.className = className;
		this.param = param;
		this.extendsParam = extendsParam;
		this.priority = priority;
		this.name = name;
	}

	public ActivityComponentParam() {
		
	}

	public int getId() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	public String getParam() {
		return param;
	}

	public String getExtendsParam() {
		return extendsParam;
	}

	public int getPriority() {
		return priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
