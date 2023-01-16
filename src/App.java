import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {
        logReaderImpl log = new logReaderImpl("./src/logfile");
        // logReaderImpl.formatTime("abcd");
        // //System.out.println("Hello, World!");
        long start = log.dataToStamp("2022-06-01 07:00:00.000");
        long end = log.dataToStamp("2022-06-03 20:00:00.000");
        // long target = log.dataToStamp("2022-06-01 07:15:30.684");
        // //System.out.println(log.ifInRange(start, end, target));
        //System.out.println(log.detectInTimeRange(start,end)[1]); //A
        
        // log.load();
      //  System.out.println(log.getAverageTimeOfJob()); // D

        // HashMap<String,Integer> tmp = log.errorDetection();
        // System.out.println(tmp.keySet()); //C
        // System.out.println(tmp.get("han"));

        //  System.out.println(log.numOfJobsByPartitions().get("gpuk10")); //b
        System.out.println(log.partitionExecTime.keySet());
        System.out.println(log.partitionExecTime.get("gpu01")); //E


        // key - value
        
        
        //TableViewSample tvs = new TableViewSample();
        //TableViewSample.main();
        
    }}

