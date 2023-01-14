import java.text.ParseException;
import java.util.Date;

public interface logReader{
    void initalize_ids();
    long dataToStamp(String s) throws ParseException;
    Date stampToData(long timeStamp);
    //String stampToData(String s);
    boolean ifInRange(long start, long end, long target);
    void load() throws ParseException;
    Date getAverageTimeOfJob(); //task D
    public int[] detectInTimeRange(long start, long end) throws ParseException; //task A
    
}