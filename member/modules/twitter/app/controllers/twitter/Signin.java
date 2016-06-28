package controllers.twitter;

import play.Logger;
import play.api.mvc.Call;
import play.mvc.Result;
import session.ISessionService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.twitter.Token;
import valueobjects.twitter.TwitterConstant;

import com.google.inject.Inject;

import controllers.member.LoginProviderControllerBase;
import extensions.twitter.login.TwitterLoginProvider;

/**
 * twitter 登录 授权
 * 
 * @author lijun
 *
 */
public class Signin extends LoginProviderControllerBase {

	@Inject
	ISessionService sessionService;
	@Inject
	TwitterLoginProvider configProvider;

	/**
	 * @author lijun
	 * @return
	 */
	public Result signin() {
		if (Logger.isDebugEnabled()) {
			Logger.debug("---------twitter signin coming-----------");
		}

		String consumerKey = configProvider.getConsumerKey();
		String consumerSecret = configProvider.getConsumerSecret();
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
		Configuration configuration = builder.build();

		Twitter twitter = new TwitterFactory(configuration).getInstance();

		Call callback = controllers.twitter.routes.Signin.callback();
		// 回调url，twitter会调用该回调url把oauth_verifier传回来
		String callbackUrl = callback.absoluteURL(request());

		if (Logger.isDebugEnabled()) {
			Logger.debug(callbackUrl);
		}

		try {
			RequestToken requestToken = twitter
					.getOAuthRequestToken(callbackUrl);

			/*
			 * 要把request token 存到session 等到Twitter callback 后用request token
			 * 去获取access token
			 */
			String rToken = requestToken.getToken();
			String rSecret = requestToken.getTokenSecret();
			Token token = new Token();
			token.setRequestToken(rToken);
			token.setRequestTokenSecret(rSecret);

			sessionService.set(TwitterConstant.REQUEST_TOKEN, token);

			String authenticationUrl = requestToken.getAuthenticationURL();
			if (Logger.isDebugEnabled()) {
				Logger.debug(authenticationUrl);
			}

			return redirect(authenticationUrl);

		} catch (TwitterException e) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("twitter signin failed", e);
			}
			sessionService.remove(TwitterConstant.REQUEST_TOKEN);

			return redirect(controllers.member.routes.Login.loginForm("/"));
		}
	}

	/**
	 * twitter 回调入口
	 * 
	 * @author lijun
	 * @return
	 */
	public Result callback() {
		if (Logger.isDebugEnabled()) {
			Logger.debug("-----------twitter signin callback-----------");
		}

		String consumerKey = configProvider.getConsumerKey();
		String consumerSecret = configProvider.getConsumerSecret();

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
		Configuration configuration = builder.build();
		Twitter twitter = new TwitterFactory(configuration).getInstance();

		Token token = (Token) sessionService.get(TwitterConstant.REQUEST_TOKEN);
		String rToken = token.getRequestToken();
		String rSecret = token.getRequestTokenSecret();

		RequestToken requestToken = new RequestToken(rToken, rSecret);
		String verifier = request().getQueryString("oauth_verifier");

		try {
			if (Logger.isDebugEnabled()) {
				Logger.debug("-----------twitter signin getOAuthAccessToken start-----------");
			}
			AccessToken aToken = twitter.getOAuthAccessToken(requestToken,
					verifier);

			if (Logger.isDebugEnabled()) {
				Logger.debug("-----------twitter signin getOAuthAccessToken end--------------");
			}

			String screenname = twitter.getScreenName();
			long id = twitter.getId();

			if (Logger.isDebugEnabled()) {
				Logger.debug(id + ":" + screenname);
			}

			MemberOtherIdentity otherId = new MemberOtherIdentity(
					TwitterConstant.TWITTER, id + "", screenname);

			sessionService.remove(TwitterConstant.REQUEST_TOKEN);
			return loginOrRegistrationCompletion(otherId);

		} catch (TwitterException e) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("twitter signin getOAuthAccessToken failed", e);
			}
			sessionService.remove(TwitterConstant.REQUEST_TOKEN);
			return redirect(controllers.member.routes.Login.loginForm("/"));
		}
	}
}
