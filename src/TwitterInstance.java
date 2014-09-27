/***********************************************************
 * This class is simply a wrapper of the Twitter object, so
 * that methods requiring the inner Twitter object can be 
 * called without passing around the Twitter object as a 
 * parameter.
 ***********************************************************/

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterInstance 
{
	private Twitter twitter;
	
	public TwitterInstance()
	{
//		TwitterFactory factory = new TwitterFactory();
//		this.twitter = factory.getInstance();
//		
//		// Sets authorization for this Twitter instance
//		Authorize.authorize(twitter);
		this.twitter = Authorize.getTwitter();
	}
	
	// Returns the inner twitter object
	public Twitter getTwitter()
	{
		return this.twitter;
	}
	
	// Returns a list of all the Twitter Categories
	public ResponseList<Category> getAllCategories()
	{
		return SuggestedUsers.getCategories(twitter);
	}
	
	// Returns a list of all the users in a given Category
	public ResponseList<User> getUsersInCategory(Category c)
	{
		return SuggestedUsers.getUsersInCategory(twitter, c);
	}
	
	// Returns a list of only the publicly viewable users in the specified category
	public ResponseList<User> getPublicUsersInCategory(Category c, int min)
	{
		return SuggestedUsers.getPublicUsersInCategory(twitter, c, min);
	}
	
	
	// Returns subsequent pages(of size count) of tweets from a user
	public ResponseList<Status> getTweets(int count, long user_id, long max_id)
	{
		return GetTweetsFromUser.getTweets(twitter, count, user_id, max_id);
	}
	
}
