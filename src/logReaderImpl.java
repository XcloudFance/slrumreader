import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logReaderImpl implements logReader{

    private String formatTime(String time){
        time = time.substring(1, time.length()-1);
        String res  = time.replace("T", " ");
        //System.out.println(time);
        return res;
    }
    logReaderImpl(String filepath) throws ParseException{
        File file = new File(filepath);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tmp = null;
            int line = 1;
            while ((tmp = reader.readLine()) != null){
                //System.out.println(tmp);
                String[] seq = tmp.split(" ");
                this.dataToStamp(formatTime(seq[0]));

                line ++;
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (reader != null){
                try{
                reader.close();
                } catch (IOException E){

                }
            }
        }

        
    }   

    @Override
    public void initalize_ids(){

    }

    @Override
    public long dataToStamp(String s) throws ParseException{
        String res;
        
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = time.parse(s);
        long ts = date.getTime();
        //res = String.valueOf(ts);
        return ts;
    }


    @Override
    public boolean ifInRange(long start, long end, long target){
        if(target <= end && target >= start){
            return true;
        }
        return false;
    }
    
    public void finalize(){

    }
}