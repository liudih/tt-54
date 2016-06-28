package valueobjects.loyalty;

import java.util.List;

import entity.loyalty.ThemeGroup;

public class ThemeGroupFragment implements IThemeFragment{
	
	String themeGroupName;
	List<ThemeGroup> tgList;

	public String getThemeGroupName() {
		return themeGroupName;
	}

	public void setThemeGroupName(String themeGroupName) {
		this.themeGroupName = themeGroupName;
	}

	public List<ThemeGroup> getTgList() {
		return tgList;
	}

	public void setTgList(List<ThemeGroup> tgList) {
		this.tgList = tgList;
	}
	

}
