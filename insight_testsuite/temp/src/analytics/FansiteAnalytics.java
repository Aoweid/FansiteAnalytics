package analytics;
/**
 * 
 * This is the main class, it will initiate four classes for four features, respectively, and run codes to output 
 * results from 4 features.
 * @author alvin
 *
 */

public class FansiteAnalytics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFile = args[0];
		String feature1ouptut = args[1];
		String feature2ouptut = args[2];
		String feature3ouptut = args[3];
		String feature4ouptut = args[4];
		//feature 1, get 10 most active addresses
		long fearture1starttime = System.currentTimeMillis();
		MostActiveAddresses mostActiveAddresses = new MostActiveAddresses(inputFile);
		mostActiveAddresses.findMostActiveAddresses();
		mostActiveAddresses.generateOutputFile(feature1ouptut);
		long fearture1endtime = System.currentTimeMillis();
		System.out.println("feature1 time:"+(fearture1endtime-fearture1starttime)/1000+"s");
		//feature 2, get 10 resources with biggest bandwidth
		long fearture2starttime = System.currentTimeMillis();
		ResourcesWithMostBandwidth resourcesWithMostBandwidth = new ResourcesWithMostBandwidth(inputFile);
		resourcesWithMostBandwidth.findResourcesWithMostBandwidth();
		resourcesWithMostBandwidth.generateOutputFile(feature2ouptut);
		long fearture2endtime = System.currentTimeMillis();
		System.out.println("feature2 time:"+(fearture2endtime-fearture2starttime)/1000+"s");
		//feature 3, get 10 most busiest hour
		long fearture3starttime = System.currentTimeMillis();
		BusiestHours busiestHours = new BusiestHours(inputFile);
		busiestHours.findBusiestHours();
		busiestHours.generateOutputFile(feature3ouptut);
		long fearture3endtime = System.currentTimeMillis();
		System.out.println("feature3 time:"+(fearture3endtime-fearture3starttime)/1000+"s");
		//feature 4, get blocked logs
		long fearture4starttime = System.currentTimeMillis();
		BlockedLogs blockedLogs = new BlockedLogs(inputFile);
		blockedLogs.findAllBlockedlogs();
		blockedLogs.generateOutputFile(feature4ouptut);
		long fearture4endtime = System.currentTimeMillis();
		System.out.println("feature4 time:"+(fearture4endtime-fearture4starttime)/1000+"s");
	}
}
