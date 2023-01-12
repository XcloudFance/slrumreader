import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class logReaderImpl implements logReader{
    ArrayList<String> logContent = new ArrayList<String>();
    int line = 0;
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
            
            while ((tmp = reader.readLine()) != null){
                //System.out.println(tmp);
                logContent.add(tmp);
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

    public int[] detectInTimeRange(long start, long end) throws ParseException{
        int created = 0;
        int ended = 0;
        for(int i = 0; i < line; i++){
            String[] analysis = logContent.get(i).split(" ");
            long thisTime = this.dataToStamp(this.formatTime(analysis[0]));
            
            if (this.ifInRange(start, end, thisTime)){
                System.out.println(analysis[1]);
                if (analysis[1].equals("_job_complete:")){
                    ended ++;
                }
                if (analysis[1].equals("sched:")){
                    created ++;
                }
            }
        }
        return new int[]{created,ended};
 
    }
}