package dao.manager;

import java.util.List;

import entity.manager.ProfessionSkill;

public interface IProfessionSkillDao {
	int getCount();

	List<ProfessionSkill> getPage(int page, int pageSize);

	int updateSkillNameByID(String name, int id);

	int insert(String skillName);

	int deleteByID(int id);

	List<ProfessionSkill> getAll();

	ProfessionSkill getByID(int id);
	
	ProfessionSkill getByName(String name);
	
}
