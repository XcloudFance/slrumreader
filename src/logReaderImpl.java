import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.State;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Date;


public class logReaderImpl implements logReader {
    ArrayList<String> logContent = new ArrayList<String>();
    int line = 0;
    public HashMap<String,Task> logTable = new HashMap<String,Task>();
    HashMap<String,Integer> partitionExecTime = new HashMap<String,Integer>();


    public static String formatTime(String time){
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

        this.load();
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
    public Date stampToData(long timeStamp){
        //SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
        return new Date(Long.parseLong(String.valueOf(timeStamp)) );

    }
    @Override
    public boolean ifInRange(long start, long end, long target){
        if(target <= end && target >= start){
            return true;
        }
        return false;
    }
    
    public int[] detectInTimeRange(long start, long end) throws ParseException{ //taskA
        int created = 0;
        int ended = 0;
        for(int i = 0; i < line; i++){
            String[] analysis = logContent.get(i).split(" ");
            long thisTime = this.dataToStamp(this.formatTime(analysis[0]));
            
            if (this.ifInRange(start, end, thisTime)){
                //System.out.println(analysis[1]);
                if (analysis[1].equals("_job_complete:")){
                    ended ++;
                }
                if (analysis[1].equals("sched:")){
                    created ++;
                }
            }
        }
        return new int[]{created,ended/2};
 
    }
    
    @Override
    public void load() throws ParseException{
        for(int i = 0;i < line; i++){
            String[] analysis = logContent.get(i).split(" ");
            if(analysis[1].equals("sched:")){
                try{
                //System.out.println(analysis[3]);
                String jobid = (analysis[3].split("=")[1]);
                String node = analysis[4].split("=")[1];
                Task task = new Task();
                task.startTime = this.dataToStamp(this.formatTime(analysis[0]));
                task.nodeList = node;
              
                logTable.put(jobid, task);
                //a.put(node, task);
                if (partitionExecTime.containsKey(node)){
                    partitionExecTime.put(node,partitionExecTime.get(node)+1);
                }
                else{
                    partitionExecTime.put(node,1);
                }

            }
                catch(Exception e){
                    continue;
                }
                
                
            }

            if(analysis[1].equals("_job_complete:")){
                try{
                String jobid = (analysis[2].split("=")[1]);
                //String node = analysis[2].split("=")[1];
                if(logTable.containsKey(jobid)){
                    logTable.get(jobid).endTime = this.dataToStamp(logReaderImpl.formatTime(analysis[0]));
                    String node = logTable.get(jobid).nodeList;
                    logTable.get(node).endTime = this.dataToStamp(logReaderImpl.formatTime(analysis[0])); 
                }
                }catch(Exception e){
                    continue;
                }
            }
        }
    }

    public long diffTime(String jobid){
        if(this.logTable.containsKey(jobid)){
            long startTime = this.logTable.get(jobid).startTime;
            long endTime = this.logTable.get(jobid).endTime;
            long diff = endTime - startTime;
            return diff;
        }
        return 0;
    }
    public int getAverageTimeOfJob(){
        long total = 0;
        int num =0;
        for(String id : this.logTable.keySet()){
            if(diffTime(id) < 0 ){
               // System.out.println(id);
                continue;
            }
            //System.out.println(diffTime(id));
            
            total += diffTime(id);
            num ++;
        }
        long avg = (long) Math.ceil(total / num);
        //String res = 
        return (int) (this.stampToData(avg).getTime()/ 1000 / 60);
      
    }

    public HashMap<String,Integer> errorDetection(){
        //error type
        // this association
        int count = 0;
        HashMap<String,Integer> logError = new HashMap<String,Integer>();

        for (int i = 0; i < line ; i++){
           String line = logContent.get(i);
           if(line.indexOf("error")!=-1) count ++;
           if(line.indexOf("This association") != -1){
                int leftBracket = line.indexOf("(");
                int rightBracket = line.indexOf(")");
                String[] metadata = line.substring(leftBracket+1, rightBracket).split(",");

                String account = metadata[0].split("=")[1].substring(1,metadata[0].split("=")[1].length()-1);
            
                String user = metadata[1].split("=")[1].substring(1,metadata[1].split("=")[1].length()-1);
                
                if(!logError.containsKey(user)){
                    logError.put(user, 1);
                }else{
                    logError.put(user,logError.get(user)+1);
                }
           }
           
        }
        return logError;
    }

    public HashMap<String,Integer> numOfJobsByPartitions(){
        HashMap<String,Integer> result = new HashMap<>();
        int epyc = 0;
        int gpuk10 = 0;
        int gpuk40c = 0;
        int gpuTitan = 0;
        //int gpu
        int opteron = 0;
        for(int i = 0 ;i < line; i++){
            if(logContent.get(i).indexOf("Partition=cpu-epyc") != -1) epyc ++;
            if(logContent.get(i).indexOf("Partition=cpu-opteron")!=-1) opteron++;
            if(logContent.get(i).indexOf("Partition=gpu-k10")!=-1) gpuk10++;
            if(logContent.get(i).indexOf("Partition=gpu-k40c")!=-1) gpuk40c++;
            if(logContent.get(i).indexOf("Partition=gpu-Titan")!=-1) gpuTitan++;
        }
        result.put("epyc",epyc);
        result.put("gpuk10",gpuk10);
        result.put("gpuk40c",gpuk40c);
        result.put("gputitan",gpuTitan);
        result.put("opteron",opteron);
        return result;
    }

    
}