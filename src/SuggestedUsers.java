import java.util.ArrayList;
import java.util.Collection;

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;


public class SuggestedUsers
{
	public static ResponseList<Category> getCategories(Twitter twitter)
	{
		ResponseList<Category> categories = null;
		try
		{
			categories = twitter.getSuggestedUserCategories();
		}
		catch(TwitterException e)
		{
			e.printStackTrace();
			System.err.println("Twitter Exception: Can't get categories");
			System.exit(-1);
		}
		return categories;
	}

	public static ResponseList<User> getUsersInCategory(Twitter twitter, Category c)
	{
		String category = c.getSlug();
		ResponseList<User> users = null;
		try
		{
			users = twitter.getUserSuggestions(category);
		}
		catch(TwitterException e)
		{
			e.printStackTrace();
			System.err.println("Twitter Exception: Can't get users in category: " + category);
			System.exit(-1);
		}
		return users;
	}
	
	// Returns a list of only the publicly viewable users in the specified category with
	// total number of tweets >= min
	public static ResponseList<User> getPublicUsersInCategory(Twitter twitter, Category c, int min)
	{
		// Converts category object to string form
		String category = c.getSlug();
		
		// This is the response list where initial search of all users gets stored
		ResponseList<User> users = null;
		
		// Response List where only public users with a given number of tweets, preferring English
		// will get stored
		Collection<User> publicUsers = new ArrayList<User>();
		try
		{
			users = twitter.getUserSuggestions(category);
			
			for (User u : users)
			{
				if (!u.isProtected() && u.getStatusesCount() >= min &&u.getLang().equals("en"))
				{
					publicUsers.add(u);
				}
			}
			users.retainAll(publicUsers);
		}
		catch(TwitterException e)
		{
			e.printStackTrace();
			System.err.println("Twitter Exception: Can't get users in category: " + category);
			System.exit(-1);
		}
		return users;
	}

	public static void main(String[] args)
	{
		int min = 1600;
//		TwitterFactory factory = new TwitterFactory();
//		Twitter twitter = factory.getInstance();
//		Authorize.authorize(twitter);
		Twitter twitter = Authorize.getTwitter();
		ResponseList<Category> c = getCategories(twitter);
		assert(c != null);
		ResponseList<User> users = getPublicUsersInCategory(twitter, c.get(0), min);
		int count = 0;
		for (User u : users)
		{
			if (!u.isProtected())
			{
				System.out.println(u.getName());
				count++;
			}
		}
		System.out.println(count);
	}
}
