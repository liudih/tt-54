package extensions.livechat.score;

import java.util.List;

import valueobjects.livechat.score.LiveChatScoreQuestion;

public interface SessionScoreQuestionProvider {
	public List<LiveChatScoreQuestion> getScoreQuestion(int languageid);
}
