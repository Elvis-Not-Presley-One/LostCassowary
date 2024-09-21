package lostcassowary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
/**
 *
 * @author Lawnguy
 */
public class Chunks extends Region {

    public void setFileNames(String initalFilePath) {
        super.setFilePath(initalFilePath);
    }
    
    //Chunk offset length set to 0 so nothing enters the loop 
    public int getNBTData() throws FileNotFoundException, IOException 
    {
        super.getChunkLocations();
        Object[] chunkOffsets = super.getChunkLocationOffset().toArray();
        Object[] filename = getFiles().toArray();
        
        System.out.println(chunkOffsets.length);
        System.out.println(filename.length);

        int fileBeingUsed = 0;
        
        for (int i = 0; i < chunkOffsets.length; i++) 
        {
            if (chunkOffsets[i].equals(8192)) 
            {
               fileBeingUsed++; 
            }
           
           // NamedTag namedTag = NBTUtil.read((File) filename[fileBeingUsed]);
           // MCAFile mcaFile = MCAUtil.read((File) filename[fileBeingUsed]);
            
           // System.out.println(namedTag.getTag().toString());
           // System.out.println(namedTag.getName());
           // System.out.println(namedTag.toString());
          
           
        }

       return fileBeingUsed;
    }

}
