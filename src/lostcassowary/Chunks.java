
package lostcassowary;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lawnguy
 */
public class Chunks extends Region
{
    
    public void setFileNames(String initalFilePath) 
    {
        super.setFilePath(initalFilePath);
    }
    
    public Object[] getNBTData()
    {
        Object[] chunkOffsets = getChunkLocationOffset().toArray();
        Object[] filenames = getFiles().toArray();
        //System.out.println(filenames.length);
        
        //First bring the info into the first file in here then bring it down 
        // when it reaches to 8192
        
        for (int i = 0; i < chunkOffsets.length; i++ )
        {
            if (chunkOffsets[i].equals(8192))
            {
              //change to the next file   
            }
            //Skip chunkOffsets[i] bytes into the file to get to the chunks
            // sub bytes of [i+1] to find the stoping point then move into a 
            //text file
        }
        
        return filenames;
    }
    
    
    
    
    
}
