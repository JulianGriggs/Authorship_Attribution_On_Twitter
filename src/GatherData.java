import java.io.File;
import java.io.PrintWriter;

import twitter4j.Category;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONObject;


public class GatherData {

	// When about to go over the rate limit, this causes the program to sleep
	public static void limit(RateLimitStatus limit)
	{
		try
		{
			System.err.printf("!!! Sleeping for %d seconds due to rate limits\n", limit.getSecondsUntilReset());
			Thread.sleep(((long)limit.getSecondsUntilReset()+3) * 1000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Number of Tweets to grab in each batch
		int count = 200;
		// Total Number of tweets to grab.  If user has less than this number
		// ignore them
		int NumTweets = 1600;

		// Creates a new twitter instance
		TwitterInstance twitter = new TwitterInstance();

		// Gets all the categories
		ResponseList<Category> c = twitter.getAllCategories();
		//		System.out.println(c.size());

		

		try
		{
			
			// File for List of Categories
			File listOfCategories = new File("./ListOfCategories.txt");
			listOfCategories.createNewFile();
			PrintWriter categoryWriter = new PrintWriter(listOfCategories, "UTF-8");

			// Prints the number of categories
			categoryWriter.println(c.size());

			// Prints the name of each category on a new line
			for (Category cat : c)
				categoryWriter.println(cat.getName());

			categoryWriter.close();
		}
		catch(Exception e)
		{
			// Will be put in log.txt
			System.out.println("Couldn't create file: ListOfCategories.txt");
		}
		try
		{
			for (Category category : c)
			{
				// Will be put in log.txt
				System.out.println("Current Category: " + category.getName());
				
				// Make a directory for the category name
				File folder = new File("./" + category.getName());
				folder.mkdir();
				String location = folder.getCanonicalPath();

				// Make a file that contains all the users in the category
				File people = new File(location + "/All_Users.txt");
				people.createNewFile();
				PrintWriter p = new PrintWriter(people, "UTF-8");

				ResponseList<User> u = twitter.getPublicUsersInCategory(category, NumTweets);

				// Check if requesting users is about to hit rate limit
				// If so, wait
				try
				{
				RateLimitStatus uStat = u.getRateLimitStatus();
				if(uStat.getRemaining() == 1) limit(uStat);
				}
				catch(Exception e)
				{
					System.out.println("Error getting rate limit status");
				}

				//Print the number of users in the category
				p.println(u.size());
				// Print the screenName of every user in the category
				for (User user : u)
				{
					p.println(user.getScreenName());
				}
				p.close();

				// Every User gets their own file which will have all of their tweet data put inside of it
				for (User user : u)
				{
					// Total number of tweets gathered from this user
					int numGathered = 0;
					
					// Creates the file for this user
					File person = new File(location + "/" +user.getScreenName() + ".txt");
					person.createNewFile();
					PrintWriter pw = new PrintWriter(person, "UTF-8");

					
					
					// Outer JSON Object, all tweets in one JSON object
					JSONArray arr = new JSONArray();
					
					long max_id = -1;
					for (int i = 0; i < NumTweets; i += count)
					{
						ResponseList<Status> s = twitter.getTweets(count, user.getId(), max_id);

						// Check if requesting statuses is about to hit rate limit
						// If so, wait
						RateLimitStatus sStat = s.getRateLimitStatus();
						if (sStat.getRemaining() == 1) limit(sStat);
						
						for (Status stat : s)
						{
							// Build JSON object with data
							JSONObject obj = new JSONObject();
							obj.put("Author", stat.getUser().getName());
							obj.put("Screen Name", stat.getUser().getScreenName());
							obj.put("Date", stat.getCreatedAt());
							obj.put("Retweet?", stat.isRetweet());
							obj.put("Language", stat.getIsoLanguageCode());
							obj.put("Text", stat.getText());
							
							// Add the tweet to the list
							arr.put(obj);
							
//							pw.println(DataObjectFactory.getRawJSON(s));
							max_id = stat.getId();
							numGathered++;
						}
					}
					pw.println(arr);
					pw.close();
					// Will be put in log.txt
					System.out.println("Number of Tweets Gathered for " + user.getName() + ": " + numGathered);
				}
				
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			// Will be put in log.txt
			System.out.println("Probably an error creating or writing to file");
//			System.exit(-1);
		}
	}

}
