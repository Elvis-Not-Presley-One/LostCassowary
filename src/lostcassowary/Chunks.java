package lostcassowary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

/**
 *
 * @author Lawnguy
 */
public class Chunks extends Region 
{

    private int signCounter = 0;
    private int bannerCounter = 0;
    private int fileBeingUsed = 0;

    public void setFileNames(String initalFilePath) 
    {
        super.setFilePath(initalFilePath);
    }

    public int getNBTData() throws FileNotFoundException, IOException 
    {
        super.getChunkLocations();
        Object[] filename = getFiles().toArray();

        while (fileBeingUsed != filename.length) 
        {

            System.out.println((File) filename[fileBeingUsed]);
            System.out.println("\n===============================================\n");

            MCAFile mcaFile = MCAUtil.read((File) filename[fileBeingUsed]);

            for (int x = 0; x < 32; x++) 
            {
                for (int z = 0; z < 32; z++) 
                {

                    mcaFile.cleanupPalettesAndBlockStates();

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
                    
                    if (handle == null) 
                    {
                        System.out.println("Chunk Data is null at" + x + " -- " + z);
                    }

                    if (handle != null) 
                    {
                        ListTag sections = (ListTag) handle.getListTag("sections");
                        if (sections != null) 
                        {
                            for (int i = 0; i < sections.size(); i++) 
                            {
                                System.out.println("\n\n" + sections.size());

                                CompoundTag section = (CompoundTag) sections.get(i);

                                int sectionY = section.getByte("Y");
                                System.out.println(sectionY);
                                processBlockStates(section, x, z);

                                if (section.containsKey("biomes")) 
                                {
                                    CompoundTag biomes = (CompoundTag) section.getCompoundTag("biomes");
                                    System.out.println("Biomes tag found: at Chunk"
                                            + " " + x + " " + z + biomes);
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
                                            + blockEnity.getInt("z"));

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
                                            + blockEnity.getInt("z"));

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

                                        for (int j = 0; j < patterns.size(); j++) 
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
                    System.out.println(chunkHM);

                    ListTag<CompoundTag> entities = chunk.getTileEntities();
                    if (entities != null) 
                    {
                        System.out.println(entities.asStringTagList());
                    } 
                    else 
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

    public void processBlockStates(CompoundTag section, int chunkX, int chunkZ) 
    {
        if (section.containsKey("block_states")) 
        {
            CompoundTag blockStates = section.getCompoundTag("block_states");
            ListTag<CompoundTag> palette = (ListTag<CompoundTag>) blockStates.getListTag("palette");
            long[] data = blockStates.getLongArray("data");  

            if (data == null || data.length == 0) 
            {
                System.out.println("No block data found in section.");
                return; 
            }

            int bitsPerBlock = data.length * 64 / 4096;  
            int paletteSize = palette.size();

            if (bitsPerBlock == 0) 
            {
                System.out.println("Invalid bitsPerBlock for section: " + bitsPerBlock);
                return;
            }

            if (paletteSize > (1 << bitsPerBlock)) 
            {
                bitsPerBlock++;
            }

            Object[] filename = getFiles().toArray();
            String regionFileName = ((File) filename[fileBeingUsed]).getName();

            String[] splitName = regionFileName.split("\\.");
            int regionX = Integer.parseInt(splitName[1]);
            int regionZ = Integer.parseInt(splitName[2]);

            int baseGlobalX = regionX * 512; 
            int baseGlobalZ = regionZ * 512;

           
            for (int y = 0; y < 16; y++) 
            {
                for (int z = 0; z < 16; z++) 
                {
                    for (int x = 0; x < 16; x++) 
                    {
                        int index = x + (z * 16) + (y * 16 * 16);
                        int paletteIndex = getPaletteIndexFromData(data, index, bitsPerBlock);

                        if (paletteIndex >= 0 && paletteIndex < palette.size()) 
                        {
                            CompoundTag block = palette.get(paletteIndex);

                            String blockName = block.getString("Name");

                            int globalY = (section.getByte("Y") * 16) + y;  
                            int globalX = baseGlobalX + (chunkX * 16) + x;
                            int globalZ = baseGlobalZ + (chunkZ * 16) + z;

                            System.out.println("Block: " + blockName + " at ("
                                    + globalX + ", " + globalY + ", " + globalZ + ")");
                        }
                    }
                }
            }
        } 
        else 
        {
            System.out.println("No block states found in this section.");
        }
    }

    private int getPaletteIndexFromData(long[] data, int index, int bitsPerBlock) 
    {
        if (bitsPerBlock == 0) 
        {
            return -1;  
        }

        int blocksPerLong = 64 / bitsPerBlock;
        int longIndex = index / blocksPerLong;
        int bitIndex = (index % blocksPerLong) * bitsPerBlock;

        if (longIndex >= data.length) 
        {
            return -1;
        }

        long blockData = data[longIndex];
        return (int) ((blockData >> bitIndex) & ((1L << bitsPerBlock) - 1)); 
    }

}
