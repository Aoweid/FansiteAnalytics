package analytics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
/**
 * 
 * this class is for feature 3: get 10 most busiest hours.
 * @author alvin
 *
 */

public class BusiestHours {
	private List<String> busiestHours;
	private String filePath;
	// private class Hour to store information of start time of a time period, and its related frequency
	private class Hour {
		String time;
		int frequency;
		public Hour(String time, int frequency) {
			this.time = time;
			this.frequency = frequency;
		}
	}

	public BusiestHours(String filePath) {
		this.filePath = filePath;
		busiestHours = new ArrayList<>();
	}
	/**
	 * this method is for getting 10 most busiest hours.
	 * It will first loop through the file to construct a hashmap of time where this are events and the number of 
	 * events SO FAR at this time, here we call one time with events as a record.
	 * 
	 * Then set four pointers:
	 * left: the start of a 60-min window
	 * right: the tail of a 60-min window
	 * leftRecord: the nearest record to the start of the 60-min window, but outside of the window
	 * rightRecord: the nearest record to the tail of the 60-min window, but inside of the window
	 * 
	 * I move the window by adding left and right by 1 each time. And for each move, I will check if 
	 * left or right reached one record, if so, update leftRecord and rightRecord. Use hashmap to get
	 * the value under key leftRecord and rightRecord, use rightRecord value minus leftRecord value 
	 * to get current window's frequency. Then use heap to store 10 busiest hours and keep updating it
	 * through the whole process.
	 */
	public void findBusiestHours() {
		try {
			//loop through file to set up record hashmap of time where this are events and the number of events SO FAR at this time
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			List<Date> records = new ArrayList<>();
			Map<Date, Integer> recordMap = new HashMap<>();
			String line = br.readLine();
			int count = 0;
			while (line != null) {
				Date time = getDate(line);
				if (!recordMap.containsKey(time)) {
					records.add(time);
				}
				recordMap.put(time, ++count);
				line = br.readLine();
			}
			//initiate left and right pointer
			Date left = new Date();
			left.setTime(records.get(0).getTime());;
			Date right = new Date();
			right.setTime(left.getTime() + 3600000);
			Date lastRecord = records.get(records.size() - 1);
			//initiate leftRecord and rightRecord pointer
			Date leftRecord = new Date();
			leftRecord.setTime(left.getTime());
			Date rightRecord = new Date();
			rightRecord.setTime(right.getTime());
			while (!recordMap.containsKey(rightRecord)) {
				rightRecord.setTime(rightRecord.getTime() - 1000);
			}
			PriorityQueue<Hour> heap = new PriorityQueue<>(10, new Comparator<Hour>() {
				public int compare(Hour a, Hour b){
					if(a.frequency>b.frequency){
						return 1;
					}
					else if(a.frequency<b.frequency){
						return -1;
					}
					else{
						return -(a.time.compareTo(b.time));
					}
				}
			});
			//while left not reaching lastRecord, loop to update heap
			while (left.getTime() <= lastRecord.getTime()) {
				String time = revertDate(left);
				int frequency = 0;
				if (left.getTime() == records.get(0).getTime()) {
					frequency = recordMap.get(rightRecord);
				} else {
					frequency = recordMap.get(rightRecord) - recordMap.get(leftRecord);
				}
				if (recordMap.containsKey(left)) {
					leftRecord.setTime(left.getTime());
				}
				if (recordMap.containsKey(right)) {
					rightRecord.setTime(right.getTime());
				}
				Hour hour = new Hour(time, frequency);
				heap.offer(hour);
				if (heap.size() > 10) {
					heap.poll();
				}
				left.setTime(left.getTime() + 1000);
				right.setTime(right.getTime() + 1000);
			}
			while (heap.size() != 0) {
				Hour hour = heap.poll();
				String time = hour.time;
				int frequency = hour.frequency;
				busiestHours.add(time + "," + frequency);
			}
			Collections.reverse(busiestHours);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * this method is for get date from input string
	 * @param s
	 * @return date
	 */
	public Date getDate(String s) {
		String time = s.substring(s.indexOf('[') + 1, s.indexOf(']'));
		DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		Date date = new Date();
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * this method is for revert date to string
	 * @param date
	 * @return time string
	 */
	public String revertDate(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		String time = df.format(date);
		return time;
	}
	/**
	 * this method is for creating output file
	 * @param outputfile
	 */

	public void generateOutputFile(String outputfile) {
		try {
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
			for (String output : busiestHours) {
				writer.println(output);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getBuiestHours() {
		return busiestHours;
	}

}
