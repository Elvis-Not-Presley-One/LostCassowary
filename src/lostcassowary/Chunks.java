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
public class Chunks extends Region 
{
    private int signCounter = 0;
    private int bannerCounter = 0;
    
    public void setFileNames(String initalFilePath) 
    {
        super.setFilePath(initalFilePath);
    }

    //Chunk offset length set to 0 so nothing enters the loop 
    public int getNBTData() throws FileNotFoundException, IOException 
    {
        super.getChunkLocations();
        //Object[] chunkOffsets = super.getChunkLocationOffset().toArray();
        Object[] filename = getFiles().toArray();

        // System.out.println(chunkOffsets.length);
        // System.out.println(filename.length);
        //int counter = 0;
        int fileBeingUsed = 0;
        while (fileBeingUsed != filename.length) 
        {

            System.out.println((File) filename[fileBeingUsed]);
            System.out.println("\n===============================================\n");


            //NamedTag namedTag = NBTUtil.read((File) filename[fileBeingUsed]);
            MCAFile mcaFile = MCAUtil.read((File) filename[fileBeingUsed]);

            for (int x = 0; x < 32; x++) 
            {
                for (int z = 0; z < 32; z++) 
                {
                    
                  //  mcaFile.cleanupPalettesAndBlockStates();

                    Chunk chunk = mcaFile.getChunk(x, z);
                    
                    if (chunk == null)
                    {
                        System.out.println("Chunk data is not there");  
                        continue;
                    }
                    
                    if (chunk.getHandle() == null)
                    {
                        System.out.println("\nChunk data is not there\n");
                        continue;
                    }
               

                    CompoundTag handle = chunk.getHandle();
                    //  System.out.println(ahhh.toString());
                    if (handle == null)
                    {
                        System.out.println("Chunk Data is null at"  + x + " -- " + z);
                    }
                                        
                    
                    if (handle != null) 
                    {
                        ListTag sections = (ListTag) handle.getListTag("sections");
                        if (sections != null) 
                        {
                            for (int i = 0; i < sections.size(); i++) 
                            {
                                CompoundTag section = (CompoundTag) sections.get(i);
                                if (section.containsKey("biomes")) 
                                {
                                    CompoundTag biomes = (CompoundTag) section.getCompoundTag("biomes");
                                    System.out.println("Biomes tag found: at Chunk" + 
                                            " " + x + " " + z + biomes);
                                }
                                if (section.containsKey("block_states"))
                                {
                                    CompoundTag block = (CompoundTag) section.getCompoundTag("block_states");
                                    
                                    System.out.println("Blocks: " + block);
                                }
                              
                            }
                        }
                       
                        ListTag blockEntites = (ListTag) handle.getListTag("block_entities");
                        
                        if (blockEntites != null)
                        {
                            for (int k = 0; k < blockEntites.size(); k++) 
                            {
                                CompoundTag blockEnity = (CompoundTag) blockEntites.get(k);
                                
                                String id = blockEnity.getString("id");
                                
                                if ("minecraft:sign".equalsIgnoreCase(id))
                                {
                                    System.out.println("Sign found at: (X,Y,Z) " 
                                            + blockEnity.getInt("x") + " , " 
                                            + blockEnity.getInt("y") + " , " 
                                            + blockEnity.getInt("z") );
                                    
                                    signCounter++;
                                            
                                    CompoundTag frontText = blockEnity.getCompoundTag("front_text");
                                    CompoundTag backText = blockEnity.getCompoundTag("back_text");
                                    
                                    ListTag frontMsg = frontText.getListTag("messages");
                                    ListTag backMsg = backText.getListTag("messages");
                                    
                                    System.out.println("Front of Sign: ");
                                    
                                    System.out.println(frontMsg.asStringTagList());
                                    
                                    
                                    System.out.println("Back of sign: ");
                                    
                                    System.out.println(backMsg.asStringTagList());
                                    
                                 
                                }
                            }
                            
                            for (int i = 0; i < blockEntites.size(); i++)
                            {
                              CompoundTag blockEnity = (CompoundTag) blockEntites.get(i);
                              
                              String id = blockEnity.getString("id");
                              
                              if ("minecraft:banner".equalsIgnoreCase(id))
                              {
                                  System.out.println("Banner found at: (X,Y,Z) " 
                                            + blockEnity.getInt("x") + " , " 
                                            + blockEnity.getInt("y") + " , " 
                                            + blockEnity.getInt("z") );
                                  
                                  if (blockEnity.containsKey("CustomName"))
                                  {
                                      String costumeName = blockEnity.getString("CustomName");
                                      System.out.println("Banner Name" + costumeName);
                                  }
                                  else
                                  {
                                      System.out.println("Banner has not name");
                                  }
                                  
                                  
                                  int baseColor = blockEnity.getInt("Base");
                                  
                                  System.out.println("Base Color of banner " 
                                          + baseColor);
                                  
                                  ListTag patterns = blockEnity.getListTag("Patterns");
                                  
                                  bannerCounter++;
                                  
                                  if (patterns != null)
                                  {
                                      System.out.println("Banner pattern");
                                      
                                      for (int j = 0; j <patterns.size(); j++)
                                      {
                                          CompoundTag pattern = (CompoundTag) patterns.get(j);
                                          int color = pattern.getInt("Color");
                                          String patternType = pattern.getString("Pattern");
                                          System.out.println("Pattern: " 
                                                  + patternType + "Color" + color);
                                        
                                      }
                                      
                                  }
                                  else
                                  {
                                      System.out.println("No patterns exsit");
                                  }
                                  
                              }

                            }
                        }
   
                    }
                    else
                    {
                        System.out.println("Chunk is null at" + x + " -- " + z);
                    }

                    CompoundTag chunkHM = chunk.getHeightMaps();
                    //System.out.println(chunkHM);

                    ListTag<CompoundTag> entities = chunk.getTileEntities();
                    if (entities != null) 
                    {
                        System.out.println(entities.asStringTagList());
                    } else 
                    {
                        System.out.println("No entites found in" + x + " -- " + z);
                    }
                   
                    
                    
                    
                }
            }
            
            System.out.println("\n===============================================\n");
            fileBeingUsed++;
        }
        System.out.println("\n\n\nsignCounter=======" + signCounter 
                + "\n\n\n=========");
        System.out.println("\n\n\nsignCounter=======" + bannerCounter 
                + "\n\n\n=========");
        return fileBeingUsed;
    }

}

/*
Blocks: {"type":"CompoundTag","value":{"palette":{"type":"ListTag","value":{"type":"CompoundTag","list":[{"Name":{"type":"StringTag","value":"minecraft:cobblestone"}},{"Name":{"type":"StringTag","value":"minecraft:air"}}]}},"data":{"type":"LongArrayTag","value":[1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768,72340172838076673,1157442765409226768]}}}
*/