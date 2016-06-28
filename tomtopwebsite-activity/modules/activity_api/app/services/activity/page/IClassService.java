package services.activity.page;

import java.util.List;

import valueobject.activity.page.ClassInfo;

public interface IClassService {

	List<ClassInfo> getQualificationClassList();

	List<ClassInfo> getRuleClassList();

	List<ClassInfo> getPrizeClassList();
}
