import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import edu.princeton.cs.introcs.*;

public class GetTextFromJSONInstance {

	// Parses JSON to JSON Array
	public static JSONArray ParseJSONToArray(String allJSON)
	{
		JSONParser parser = new JSONParser();
		JSONArray array = null;
		try
		{
			Object obj = parser.parse(allJSON);
			array = (JSONArray)obj;

		}
		catch(ParseException e){
			System.err.println("Error: Could not parse JSON to JSONArray");
		}
		return array;
	}


	// Retrieves the text fields from the given tweet indices of the JSONArray
	public static String[] getAllTextFields(JSONArray jsonArray, int[] indices, int total)
	{
		int numSaved = 0;
		String[] allText = new String[total];
		for (int i : indices)
		{
			JSONObject obj = (JSONObject)jsonArray.get(i);
			if (obj.get("Retweet?").toString() == "false")
			{
				String tweet = obj.get("Text").toString();
				tweet = stripAtReplies(tweet);
				tweet = stripTwitterURLS(tweet);
				if (tweet.length() > 20);
				{
					allText[numSaved] = tweet;
					numSaved++;
				}
			}
			if (numSaved == total)
				break;	
		}
		return allText;
	}

	// Retrieves the text fields from the given tweet indices of the JSONArray
	public static String[] getAllTextFieldsWithSkip(JSONArray jsonArray, int[] indices, int total, int skip)
	{
		int numSaved = 0;
		int numSkipped = 0;
		String[] allText = new String[total];
		for (int i : indices)
		{
			if (numSkipped == skip)
			{
				JSONObject obj = (JSONObject)jsonArray.get(i);
				if (obj.get("Retweet?").toString() == "false")
				{
					String tweet = obj.get("Text").toString();
					tweet = stripAtReplies(tweet);
					tweet = stripTwitterURLS(tweet);
					if (tweet.length() > 20);
					{
						allText[numSaved] = tweet;
						numSaved++;
					}
				}
				if (numSaved == total)
					break;	
			}
			else
			{
				JSONObject obj = (JSONObject)jsonArray.get(i);
				if (obj.get("Retweet?").toString() == "false")
				{
					String tweet = obj.get("Text").toString();
					tweet = stripAtReplies(tweet);
					tweet = stripTwitterURLS(tweet);
					if (tweet.length() > 20);
					{
						numSkipped++;
					}
				}
			}
		}
		return allText;
	}

	
	
	
	// Returns an integer array containing all of the integers from 0 to end randomly permuted
	public static int[] permute(int end)
	{
		ArrayList<Integer> permute = new ArrayList<Integer>();
		for (int i = 0; i < end; i++)
			permute.add(i);
		Collections.shuffle(permute);
		int[] shuffled = new int[end];
		for (int i = 0; i < end; i++)
			shuffled[i] = permute.get(i).intValue();
		return shuffled;
	}

	
	
	
	// Takes all of the strings in a String[] and coalesces them into one String
	public static String mergeStrings(String[] allText)
	{
		StringBuilder s = new StringBuilder();
		for (String tweet : allText)
		{
			s.append(tweet);
			s.append(" ");
		}
		return s.toString();
	}

	
	
	
	// Strips the @-Replies and mentions
	public static String stripAtReplies(String procText) {
		String procString = new String(procText);
		procString = procString.replaceAll("@(\\w+|_+)+", "*@*");
		return procString;
	}

	// Strips the twitter URLS
	public static String stripTwitterURLS(String procText) {
		String procString = new String(procText);
		procString = procString.replaceAll("http://t.co.*\\s", "*URL* ");
		procString = procString.replaceAll("http://t.co.*$", "*URL*");
		return procString;
	}

	public static void main(String[] args) {
		// Number of tweets in the training and test sets
		int sizeTrain = Integer.parseInt(args[0]);
		int sizeTest  = Integer.parseInt(args[1]);
  
		int numTest = 0;
		String dataFile = null;
		int id = 0;
		while (!edu.princeton.cs.introcs.StdIn.isEmpty()) 
		{
			id++;
			// Read in the first line of the the set of people into a string
			try 
			{
				String file = edu.princeton.cs.introcs.StdIn.readLine();
				dataFile = FileUtils.readFileToString(new File(args[2] + file));
			} 
			catch (IOException e) 
			{
				System.err.println("Was unable to read file to string.");
				e.printStackTrace();
			}
			
			// Parses into JSON array
			JSONArray arr = ParseJSONToArray(dataFile);
			
			// Number of tweets this person has
			int numTweetsPresent = arr.size();
			// Shuffles them up randomly
			int[] shuffledIndices1 = permute(numTweetsPresent);
			
			
			// Gets the text fields sizeTrain/5 tweets
			String[] trainTweets = getAllTextFields(arr, shuffledIndices1, sizeTrain/5);	
			// Merges the strings together
			String concatenatedTrainTweets1 = mergeStrings(trainTweets);
			
			// Gets the text fields sizeTrain/5 tweets
			trainTweets = getAllTextFieldsWithSkip(arr, shuffledIndices1, sizeTrain/5, sizeTrain/5);
			// Merges the strings together
			String concatenatedTrainTweets2 = mergeStrings(trainTweets);
			
			// Gets the text fields sizeTrain/5 tweets
			trainTweets = getAllTextFieldsWithSkip(arr, shuffledIndices1, sizeTrain/5, (sizeTrain/5)*2);	
			// Merges the strings together
			String concatenatedTrainTweets3 = mergeStrings(trainTweets);
						
			// Gets the text fields sizeTrain/5 tweets
			trainTweets = getAllTextFieldsWithSkip(arr, shuffledIndices1, sizeTrain/5, (sizeTrain/5)*3);
			// Merges the strings together
			String concatenatedTrainTweets4 = mergeStrings(trainTweets);
			
			// Gets the text fields sizeTrain/5 tweets
			trainTweets = getAllTextFieldsWithSkip(arr, shuffledIndices1, sizeTrain/5, (sizeTrain/5)*4);
			// Merges the strings together
			String concatenatedTrainTweets5 = mergeStrings(trainTweets);
			
			// Gets the text fields sizeTrain tweets
			String[] testTweets = getAllTextFieldsWithSkip(arr, shuffledIndices1, sizeTest, sizeTrain);
			
			// Merges the strings together
			String concatenatedTestTweets = mergeStrings(testTweets);
			
			try 
			{
				FileUtils.writeStringToFile(new File(id+"a.txt"), concatenatedTrainTweets1);
				FileUtils.writeStringToFile(new File(id+"b.txt"), concatenatedTrainTweets2);
				FileUtils.writeStringToFile(new File(id+"c.txt"), concatenatedTrainTweets3);
				FileUtils.writeStringToFile(new File(id+"d.txt"), concatenatedTrainTweets4);
				FileUtils.writeStringToFile(new File(id+"e.txt"), concatenatedTrainTweets5);
				if (numTest < 5)
				{
					FileUtils.writeStringToFile(new File("u"+id+".txt"), concatenatedTestTweets);
					numTest++;
				}

			} 
			catch (IOException e) 
			{
				System.err.println("Was unable to write merged training and or test strings to file.");
				e.printStackTrace();
			}
		}
		edu.princeton.cs.introcs.StdOut.println("done");

	}

}
