package services.member;

import java.util.List;

import javax.inject.Inject;

import dto.member.EmailSuffix;
import mapper.member.EmailSuffixMapper;

public class EmailSuffixService implements IEmailSuffixService{
	@Inject
	EmailSuffixMapper emailSuffixMapper;

	public List<EmailSuffix> getMaxEmailSuffix(int mid) {
		return emailSuffixMapper.getMaxEmailSuffix(mid);
	}

	public int getEmailSuffixMaxId() {
		return emailSuffixMapper.getEmailSuffixMaxId();
	}

}
