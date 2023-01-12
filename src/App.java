
public class App {
    public static void main(String[] args) throws Exception {
        logReaderImpl log = new logReaderImpl("./src/logfile");
        //System.out.println("Hello, World!");
        long start = log.dataToStamp("2022-06-01 07:00:00.000");
        long end = log.dataToStamp("2022-06-01 20:00:00.000");
        long target = log.dataToStamp("2022-06-01 07:15:30.684");
        //System.out.println(log.ifInRange(start, end, target));
        log.detectInTimeRange(start,end);
        
    }}

