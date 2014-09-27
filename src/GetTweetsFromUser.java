import java.util.Map;

import twitter4j.Category;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;


public class GetTweetsFromUser {



	public static ResponseList<Status> getTweets(Twitter twitter, int count, long user_id, long max_id)
	{
		ResponseList<Status> tweets = null;
		Paging p = new Paging();
		p.setCount(count);
		if (max_id != -1)
			p.setMaxId(max_id);
		try
		{
			tweets = twitter.getUserTimeline(user_id, p);
		}
		catch(TwitterException e)
		{
			e.printStackTrace();
			System.err.println("Twitter Exception: Can't get tweets from user: " + user_id);
			System.exit(-1);
		}
		return tweets;
	}


	public static void main(String[] args) 
	{
		int count = 200;
		int NumTweets = 1600;
		int numGathered = 0;

		TwitterInstance twitter = new TwitterInstance();
		ResponseList<Category> c = twitter.getAllCategories();
		//		System.out.println(c.size());


		for (Category category : c)
		{
			ResponseList<User> u = twitter.getPublicUsersInCategory(category, NumTweets);
			for (User user : u)
			{
				long max_id = -1;
				for (int i = 0; i < NumTweets; i += count)
				{
					ResponseList<Status> s = twitter.getTweets(count, u.get(1).getId(), max_id);
					for (Status stat : s)
					{
						//						System.out.println("Author: " + stat.getUser().getName());
						//						System.out.println("Screen Name: @" + stat.getUser().getScreenName());
						//						System.out.println("Date Created: " + stat.getCreatedAt());
						//						System.out.println("Author Location: " + stat.getUser().getLocation());
						//						System.out.println("Retweet?: " + stat.isRetweet());
						//						System.out.println("Text: " + stat.getText());
						//						System.out.println("Language: " + stat.getIsoLanguageCode());
						//						System.out.println();

						System.out.println(DataObjectFactory.getRawJSON(s));
						max_id = stat.getId();
						numGathered++;

					}

				}

			}
			System.out.println(numGathered);
		}
	}

}
