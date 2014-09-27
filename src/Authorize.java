import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;


public class Authorize 
{

	private static final String CONSUMER_KEY = "EnOhe6L6qb1JhvBvZ6KZSw";
	private static final String CONSUMER_SECRET = "CxhcGWkmhAJSVPyzRGsHnm3I89aIj46q9ECCWmkZ1k";
	private static final String ACCESS_TOKEN = "401758505-b84XMU3weQAZ1Q8GdwypqagaNB0ebziT1xUUdQoa";
	private static final String ACCESS_TOKEN_SECRET = "RrPPXIsnmF4icynoBCVnOcQ7WFYNJQ2cpdLsbyjDeWul6";
	
	/**
	 * Retrieve the "bearer" token from Twitter in order to make application-authenticated calls.
	 *
	 * This is the first step in doing application authentication, as described in Twitter's documentation at
	 * https://dev.twitter.com/docs/auth/application-only-auth
	 *
	 * Note that if there's an error in this process, we just print a message and quit.  That's a pretty
	 * dramatic side effect, and a better implementation would pass an error back up the line...
	 *
	 * @return	The oAuth2 bearer token
	 */
	public static OAuth2Token getOAuth2Token()
	{
		OAuth2Token token = null;
		ConfigurationBuilder cb;

		cb = new ConfigurationBuilder();
		cb.setApplicationOnlyAuthEnabled(true);

		cb.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);

		try
		{
			token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
		}
		catch (Exception e)
		{
			System.out.println("Could not get OAuth2 token");
			e.printStackTrace();
			System.exit(0);
		}

		return token;
	}
	
	/**
	 * Get a fully application-authenticated Twitter object useful for making subsequent calls.
	 *
	 * @return	Twitter4J Twitter object that's ready for API calls
	 */
	public static Twitter getTwitter()
	{
		OAuth2Token token;

		//	First step, get a "bearer" token that can be used for our requests
		token = getOAuth2Token();

		//	Now, configure our new Twitter object to use application authentication and provide it with
		//	our CONSUMER key and secret and the bearer token we got back from Twitter
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setApplicationOnlyAuthEnabled(true);

		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);

		cb.setOAuth2TokenType(token.getTokenType());
		cb.setOAuth2AccessToken(token.getAccessToken());

		cb.setJSONStoreEnabled(true);
		//	And create the Twitter object!
		return new TwitterFactory(cb.build()).getInstance();

	}
	
//	public static void authorize(Twitter twitter)
//	{
//		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
//		AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
//		twitter.setOAuthAccessToken(accessToken);
//	}
}
