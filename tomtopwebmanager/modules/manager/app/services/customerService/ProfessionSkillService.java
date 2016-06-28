package services.customerService;

import java.util.List;

import javax.inject.Inject;

import valueobjects.base.Page;
import dao.manager.IProfessionSkillDao;
import entity.manager.ProfessionSkill;

public class ProfessionSkillService {
	@Inject
	IProfessionSkillDao skillDao;

	private int pageSize = 15;

	public Page<ProfessionSkill> getPage(int page) {
		List<ProfessionSkill> list = skillDao.getPage(page, pageSize);
		int total = skillDao.getCount();
		Page<ProfessionSkill> p = new Page<ProfessionSkill>(list, total, page,
				pageSize);
		return p;
	}

	public boolean updateNameByID(String name, int id) {
		int i = skillDao.updateSkillNameByID(name, id);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean deleteByID(int id) {
		skillDao.deleteByID(id);
		return true;
	}

	public boolean insert(String skillName) {
		int i = skillDao.insert(skillName);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public List<ProfessionSkill> getAll() {
		return skillDao.getAll();
	}

	public ProfessionSkill getByID(int id) {
		return skillDao.getByID(id);
	}

}
