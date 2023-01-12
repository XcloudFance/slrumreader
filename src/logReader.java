import java.text.ParseException;

public interface logReader{
    void initalize_ids();
    long dataToStamp(String s) throws ParseException;
    //String stampToData(String s);
    boolean ifInRange(long start, long end, long target);
}