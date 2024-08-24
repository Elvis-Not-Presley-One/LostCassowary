
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

     
     /**The getFilePath method gets the path to where the region files are located 
      * 
      * @return the path to the location of the region files 
      */
    public final String getFilePath()
    {
       return regionFilePath;  
    }
    
    /** the setFilePath method takes the users input and sends it off to the getFilePath method 
     * 
     * @param initalFilePath takes in the path from the user 
     */
    public void setFilePath(String initalFilePath)
    {
     regionFilePath = initalFilePath; 
    }
    
    /** the getFiles method obtains all of the file names in that path and stores it in an arrayList
     * 
     * @return all of the elements of the array containing all of the names 
     */
    public List<String> getFiles()
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
    
    /** the getFileNameParseingInfo method removes all unnecessary info from the file name 
     * 
     * @return the array with all of the names of the files without the clunky info 
     */
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


