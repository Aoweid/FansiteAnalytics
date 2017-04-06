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
 * this class is for feature 2
 * @author alvin
 *
 */
public class ResourcesWithMostBandwidth {
	private List<String> resourcesWithMostBandwidth;
	private String filePath;

	public ResourcesWithMostBandwidth(String filePath) {
		resourcesWithMostBandwidth = new ArrayList<>();
		this.filePath = filePath;
	}
	/**
	 * this method is for getting resources with most bandwidth
	 * It will first loop through the file to constrcut a hashmap storing resources and related
	 * bandwidth, then use a heap to find the top 10.
	 */

	public void findResourcesWithMostBandwidth() {
		//construct hashmap to store resource and bandwidth
		Map<String, Long> resourceMap = new HashMap<String, Long>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			while (line != null) {
				String request = line.substring(line.indexOf('\"'), line.lastIndexOf('\"') + 1);
				String[] strs1 = request.replaceAll("\"", "").split("\\s+");
				String[] strs2 = line.split("\\s+");
				String bytes = "";
				String resource = "";
				if (strs1 != null && strs1.length > 1) {
					resource = request.replaceAll("\"", "").split("\\s+")[1].trim();
				}
				bytes = strs2[strs2.length - 1];
				long byteSize = 0;
				if (bytes.equals("-")) {
					byteSize = 0;
				} else {
					byteSize = Long.parseLong(bytes);
				}
				resourceMap.put(resource, resourceMap.getOrDefault(resource, (long) 0) + byteSize);
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//set heap based on bandwidth and lexicographical order
		PriorityQueue<Map.Entry<String, Long>> heap = new PriorityQueue<>(10,
				new Comparator<Map.Entry<String, Long>>() {
					public int compare(Map.Entry<String, Long> a, Map.Entry<String, Long> b) {
						if (a.getValue() > b.getValue()) {
							return 1;
						} else if (a.getValue() < b.getValue()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
		for (Map.Entry<String, Long> entry : resourceMap.entrySet()) {
			heap.offer(entry);
			if (heap.size() > 10) {
				heap.poll();
			}
		}
		while (heap.size() != 0) {
			Map.Entry<String, Long> entry = heap.poll();
			String resource = entry.getKey();
			long bandwidth = entry.getValue();
			resourcesWithMostBandwidth.add(resource);
		}
		Collections.reverse(resourcesWithMostBandwidth);
	}

	public void generateOutputFile(String outputfile) {
		try {
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
			for (String output : resourcesWithMostBandwidth) {
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

	public List<String> getResourcesWithMostBandwidth() {
		return resourcesWithMostBandwidth;
	}
}
