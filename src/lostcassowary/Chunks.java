package lostcassowary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.ListTag;
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
        
       // System.out.println(chunkOffsets.length);
       // System.out.println(filename.length);
       
        int counter = 0; 
        int fileBeingUsed = 0;
        while (fileBeingUsed != filename.length)
        {
            if (chunkOffsets[counter].equals(8192)) 
            {
               fileBeingUsed++; 
            }
           System.out.println((File) filename[fileBeingUsed]);

           //NamedTag namedTag = NBTUtil.read((File) filename[fileBeingUsed]);
           MCAFile mcaFile = MCAUtil.read((File) filename[fileBeingUsed]);
           
          for (int x = 0; x < 32; x++)
           {
               for (int z = 0; z < 32; z++)
               {
                  
               Chunk chunk = mcaFile.getChunk(x, z);
               CompoundTag chunkHM = chunk.getHeightMaps();
                   System.out.println(chunkHM);
               
               if (chunk == null)
               {
                   System.out.println("chunk is null at" + x + z);
                   chunk.cleanupPalettesAndBlockStates();
                   continue;
               }
               
               for (int y = 0; y < 256; y++)
               {
                   
                   int chunkNBT = chunk.getBiomeAt(x, y, z);
                   System.out.println(chunkNBT);
               }
               }
           }
          
           counter++;
        }

       return fileBeingUsed;
    }

}
