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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the class for feature 4
 * @author alvin
 *
 */
public class BlockedLogs {
	private List<String> blockedLogs;
	private String filePath;

	public BlockedLogs(String filePath) {
		blockedLogs = new ArrayList<>();
		this.filePath = filePath;
	}
	/**
	 * this method is for finding block logs
	 * Loop through the file, store failed logins in Map<String, Deque<Date>> failedLogins, 
	 * failedLogins will store the address and its failed logins in 20s, if failure reach 3
	 * set blocktime in Map<String, Long> blocktime, then put the logs during blocktime into
	 * output
	 */
	public void findAllBlockedlogs() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			Map<String, Deque<Date>> failedLogins = new HashMap<String, Deque<Date>>();
			Map<String, Long> blocktime = new HashMap<>();
			while (line != null) {
				String[] strs1 = line.split("- -");
				String host = strs1[0].trim();
				String requestContent = strs1[1].trim();
				Date currentTime = getDate(requestContent);
				if (blocktime.containsKey(host) && currentTime.getTime() <= blocktime.get(host)) {
					blockedLogs.add(line);
				} else {
					String[] strs2 = requestContent.split("\\s+");
					String replyCode = strs2[strs2.length - 2].trim();
					String request = requestContent.substring(requestContent.indexOf('\"'),
							requestContent.lastIndexOf('\"') + 1);
					String[] strs3 = request.replaceAll("\"", "").split("\\s+");
					String method = strs3[0].trim();
					if (method.equals("POST")) {
						if (!failedLogins.containsKey(host)) {
							failedLogins.put(host, new ArrayDeque<Date>());
						}
						if (!replyCode.equals("200")) {
							failedLogins.get(host).addLast(currentTime);
							while ((currentTime.getTime() - failedLogins.get(host).peekFirst().getTime())
									/ 1000 >= 20) {
								failedLogins.get(host).pollFirst();
							}
						} else {
							failedLogins.get(host).clear();
						}
						if (failedLogins.get(host).size() == 3
								&& (currentTime.getTime() - failedLogins.get(host).peekFirst().getTime())
										/ 1000 <= 20) {
							blocktime.put(host, currentTime.getTime() + 300000);
							failedLogins.get(host).clear();
						}
					}
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * this method is for get date from input string
	 * @param s
	 * @return
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

	public void generateOutputFile(String outputfile) {
		try {
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
			for (String output : blockedLogs) {
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

	public List<String> getBlockedLogs() {
		return blockedLogs;
	}
}
