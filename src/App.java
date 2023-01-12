
public class App {
    public static void main(String[] args) throws Exception {
        logReaderImpl log = new logReaderImpl("./src/logfile");
        //System.out.println("Hello, World!");
        
        System.out.println(log.dataToStamp("2022-06-01 07:15:39.458"));
        
    }
}
