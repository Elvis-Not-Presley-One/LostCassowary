
package lostcassowary;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lawnguy
 */
public class FileHandling extends Region
{
      
     private String regionFilePath;
     private final List<String> fileNameList = new ArrayList<>();

     
    public final String getFilePath()
    {
       return regionFilePath;  
    }
    
    public void setFilePath(String initalFilePath)
    {
     regionFilePath = initalFilePath; 
    }
    
    
    public List<String> getfiles()
    {
     File directoryPath = new File(regionFilePath);
     String contents[] = directoryPath.list();
     
        System.out.println("A List of all file names in the path");
        
         for (String content : contents) 
         {
             fileNameList.add(content);
         }
            return fileNameList;
    }
    
    public List<String> getFileNameParseingInfo()
    {
    
        List<String> parseingInfo = new ArrayList<>();
        
        for (String fileName : fileNameList)
        {
            String paresedInfo = fileName.replaceAll("r.,mca", "");
            parseingInfo.add(paresedInfo);
        }
        
        return parseingInfo;
        
        
    }
    
    
    
}


