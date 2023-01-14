import java.text.ParseException;

public interface logReader{
    void initalize_ids();
    long dataToStamp(String s) throws ParseException;
    String stampToData(long timeStamp);
    //String stampToData(String s);
    boolean ifInRange(long start, long end, long target);
    void load() throws ParseException;
    String getAverageTimeOfJob();
}