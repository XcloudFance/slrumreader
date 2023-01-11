import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class logReaderImpl implements logReader{

    logReaderImpl(String filepath){
        File file = new File(filepath);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tmp = null;
            int line = 1;
            while ((tmp = reader.readLine()) != null){
                System.out.println(tmp);
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

    public void finalize(){

    }
}