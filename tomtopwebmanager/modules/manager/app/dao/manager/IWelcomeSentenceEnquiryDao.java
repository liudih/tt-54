package dao.manager;

import java.util.List;

import dao.IManagerEnquiryDao;
import entity.manager.WelcomeSentence;

public interface IWelcomeSentenceEnquiryDao extends IManagerEnquiryDao {

	List<WelcomeSentence> getWelcomeSentenceByLanguage(int languageID);

}
