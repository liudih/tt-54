package services.activity.page.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import services.activity.page.IClassService;
import valueobject.activity.page.ClassFieldInfo;
import valueobject.activity.page.ClassInfo;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import extensions.activity.ActivityComponent;
import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityQualificationProvider;
import extensions.activity.IActivityRuleProvider;
import extensions.activity.annotation.ParamInfo;
import extensions.activity.annotation.ParamType;

public class ClassServiceImp implements IClassService {

	@Inject
	ActivityComponent activityComponent;

	/*
	 * (non-Javadoc) <p>Title: getQualificationClassList</p> <p>Description:
	 * 获取筛选类</p>
	 * 
	 * @return
	 * 
	 * @see services.activity.page.IClassService#getQualificationClassList()
	 */
	@Override
	public List<ClassInfo> getQualificationClassList() {
		Set<IActivityQualificationProvider> qualset = activityComponent
				.getQualificationSet();
		List<ClassInfo> qualificationClassInfoList = FluentIterable
				.from(qualset).transform(q -> {
					ClassInfo classInfo = new ClassInfo();
					classInfo.setName(q.getClass().getName());
					classInfo.setDesc(q.getName());
					classInfo.setSelfField(getClassAllField(q.getParam()));
					classInfo.setExtraField(new ArrayList<ClassFieldInfo>());
					return classInfo;
				}).toList();
		return qualificationClassInfoList;
	}

	/*
	 * (non-Javadoc) <p>Title: getRuleClassList</p> <p>Description: 获取规则类</p>
	 * 
	 * @return
	 * 
	 * @see services.activity.page.IClassService#getRuleClassList()
	 */
	@Override
	public List<ClassInfo> getRuleClassList() {
		Set<IActivityRuleProvider> ruleSet = activityComponent.getRuleSet();
		List<ClassInfo> ruleClassInfoList = FluentIterable
				.from(ruleSet)
				.transform(
						r -> {
							ClassInfo classInfo = new ClassInfo();
							classInfo.setName(r.getClass().getName());
							classInfo.setDesc(r.getName());
							classInfo.setSelfField(getClassAllField(r
									.getParam()));
							classInfo.setExtraField(getClassAllField(r
									.getPrizeParam()));
							return classInfo;
						}).toList();
		return ruleClassInfoList;
	}

	/*
	 * (non-Javadoc) <p>Title: getPrizeClassList</p> <p>Description: 获取奖品类</p>
	 * 
	 * @return
	 * 
	 * @see services.activity.page.IClassService#getPrizeClassList()
	 */
	@Override
	public List<ClassInfo> getPrizeClassList() {
		Set<IActivityPrizeProvider> prizeSet = activityComponent.getPrizeSet();
		List<ClassInfo> priceClassInfoList = FluentIterable.from(prizeSet)
				.transform(r -> {
					ClassInfo classInfo = new ClassInfo();
					classInfo.setName(r.getClass().getName());
					classInfo.setDesc(r.getName());
					classInfo.setSelfField(getClassAllField(r.getParam()));
					classInfo.setExtraField(new ArrayList<ClassFieldInfo>());
					return classInfo;
				}).toList();
		return priceClassInfoList;
	}

	/**
	 * 
	 * @Title: getFieldList
	 * @Description: TODO(获取字段信息)
	 * @param @param fields
	 * @param @return
	 * @return List<ClassFieldInfo>
	 * @throws
	 * @author yinfei
	 */
	private List<ClassFieldInfo> getFieldList(Field[] fields,
			List<ClassFieldInfo> FieldList) {
		for (int i = 0; i < fields.length; i++) {
			ClassFieldInfo cfi = new ClassFieldInfo();
			cfi.setType(fields[i].getType().getName());
			cfi.setName(fields[i].getName());
			cfi.setDesc(fields[i].getName());
			cfi.setPriority(0);
			ParamInfo pinfo = fields[i].getDeclaredAnnotation(ParamInfo.class);
			if (pinfo != null) {
				cfi.setDesc(pinfo.desc());
				cfi.setPriority(pinfo.priority());
				if (null != pinfo.type() && pinfo.type() != ParamType.none) {
					cfi.setType(ParamType.couponrule.toString());
				}
			}
			FieldList.add(cfi);
		}
		return FieldList;
	}

	/**
	 * 获取类和超类的所有field
	 * 
	 * @param clas
	 * @return
	 */
	public List<ClassFieldInfo> getClassAllField(Class<?> classs) {
		List<ClassFieldInfo> fieldList = Lists.newArrayList();
		getFieldList(classs.getDeclaredFields(), fieldList);
		for (Class<?> clas : ClassUtils.getAllSuperclasses(classs)) {
			getFieldList(clas.getDeclaredFields(), fieldList);
		}
		return fieldList;
	}
}
