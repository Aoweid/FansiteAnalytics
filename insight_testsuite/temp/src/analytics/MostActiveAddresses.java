package analytics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
/**
 * this is the feature 1: get 10 most active addresses.
 * @author alvin
 *
 */
public class MostActiveAddresses {
	private List<String> mostActiveAddresses;
	private String filePath;

	public MostActiveAddresses(String filePath) {
		mostActiveAddresses = new ArrayList<>();
		this.filePath = filePath;
	}
	/**
	 * This method will get 10 most active addresses.
	 * It will first loop through the log file to construct a hashmap to store the address and its requesting 
	 * frequency, then use a priorityqueue to get 10 most active addresses.
	 */

	public void findMostActiveAddresses() {
		Map<String, Long> hostMap =  new HashMap<String, Long>();
		//read file and construct hashmap to store the address and its requesting frequency
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));;
			String line = br.readLine();
			while(line!=null){
				String[] strs1 = line.split("- -");
				String host = strs1[0].trim();
				hostMap.put(host, hostMap.getOrDefault(host, (long)0)+1);
				line = br.readLine();
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//configure heap based on frequency and lexicographical order
		PriorityQueue<Map.Entry<String, Long>> heap = new PriorityQueue<>(10,
				new Comparator<Map.Entry<String, Long>>() {
					public int compare(Map.Entry<String, Long> a, Map.Entry<String, Long> b) {
						if (a.getValue() > b.getValue()) {
							return 1;
						} else if (a.getValue() < b.getValue()) {
							return -1;
						} else {
							return -(a.getKey().compareTo(b.getKey()));
						}
					}
				});
		//get 10 most active addresses
		for (Map.Entry<String, Long> entry : hostMap.entrySet()) {
			heap.offer(entry);
			if (heap.size() > 10) {
				heap.poll();
			}
		}
		while (heap.size() != 0) {
			Map.Entry<String, Long> entry = heap.poll();
			String address = entry.getKey();
			long frequency = entry.getValue();
			mostActiveAddresses.add(address+"," + frequency);
		}
		Collections.reverse(mostActiveAddresses);
	}
	/**
	 * this method is for creating output file
	 */
	public void generateOutputFile(String outputfile){
		try {
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
			for(String output:mostActiveAddresses){
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

	public List<String> getMostActiveAddresses() {
		return mostActiveAddresses;
	}
}
