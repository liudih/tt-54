package services.member;

import java.util.List;

import dto.member.EmailSuffix;

public interface IEmailSuffixService {

	public List<EmailSuffix> getMaxEmailSuffix(int mid);

	public int getEmailSuffixMaxId();

}
