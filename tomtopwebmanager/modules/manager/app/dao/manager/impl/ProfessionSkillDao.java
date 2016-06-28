package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.ProfessionSkillMapper;
import dao.manager.IProfessionSkillDao;
import entity.manager.ProfessionSkill;

public class ProfessionSkillDao implements IProfessionSkillDao {
	@Inject
	ProfessionSkillMapper mapper;

	@Override
	public int getCount() {
		return mapper.getCount();
	}

	@Override
	public List<ProfessionSkill> getPage(int page, int pageSize) {
		return mapper.getPage(page, pageSize);
	}

	@Override
	public int updateSkillNameByID(String name, int id) {
		return mapper.updateSkillNameByID(name, id);
	}

	@Override
	public int insert(String skillName) {
		return mapper.insert(skillName);
	}

	@Override
	public int deleteByID(int id) {
		return mapper.deleteByID(id);
	}

	@Override
	public List<ProfessionSkill> getAll() {
		return mapper.getAll();
	}

	@Override
	public ProfessionSkill getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public ProfessionSkill getByName(String name) {
		return mapper.getByName(name);
	}

}
