package entity.manager;

import org.springframework.util.StringUtils;

public class EnumAffiliateBanner {
	public static String getBannerTypeName(String id) {
		if (!StringUtils.isEmpty(id)) {
			for (BannerType b : BannerType.values()) {
				if (b.getBannerid() == id) {
					return b.getBannername();
				}
			}
		}
		return null;
	}

	public static String getTargetName(String id) {
		if (!StringUtils.isEmpty(id)) {
			for (Target b : Target.values()) {
				if (b.getTargetid() == id) {
					return b.getTargetname();
				}
			}
		}
		return null;
	}

	public enum BannerType {
		IMAGE("image", "image"), FLASH("flash", "flash"), TEXT("text", "text"), HOVER(
				"hover", "Hover"), PAGEPEEL("page peel", "page peel"), ROTATOR(
				"rotator", "rotator");
		private String bannerid;
		private String bannername;

		public String getBannerid() {
			return bannerid;
		}

		public String getBannername() {
			return bannername;
		}

		BannerType(String bannerid, String bannername) {
			this.bannerid = bannerid;
			this.bannername = bannername;
		}

	}

	public enum Target {
		BLANK("blank", "blank"), PARENT("parent", "parent"), SELF("self",
				"self"), TOP("top", "top");
		private String targetid;
		private String targetname;

		public String getTargetid() {
			return targetid;
		}

		public String getTargetname() {
			return targetname;
		}

		Target(String targetid, String targetname) {
			this.targetid = targetid;
			this.targetname = targetname;
		}
	}
}
