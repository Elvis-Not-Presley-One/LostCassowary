package lostcassowary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;

/**
 *
 * @author Lawnguy
 */
public class Chunks extends Region 
{

    private int signCounter = 0;
    private int bannerCounter = 0;
    private int fileBeingUsed = 0;

    /**
     * the setFileName() method sets the files names, 
     * should not have to use it but I must sadly 
     * 
     * @param initalFilePath the file path
     */
    public void setFileNames(String initalFilePath) 
    {
        super.setFilePath(initalFilePath);
    }

    /**
     * The getNBTData() method gets all nbt data you are looking for 
     * 
     * @return the nbt data 
     * @throws FileNotFoundException
     * @throws IOException 
     */
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
                                    
                                    processBiomes(section, x, z);
                                    CompoundTag biomes = (CompoundTag) section.getCompoundTag("biomes");
                                    System.out.println("Biomes tag found: at Chunk"
                                            + " " + x + " " + z + biomes);
                                                                        
                                    //csvWriter("biomes.csv", biomes.toString(), Integer.toString(x), Integer.toString(z));
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
                                    int xSign = blockEnity.getInt("x");
                                    int ySign = blockEnity.getInt("y");
                                    int zSign = blockEnity.getInt("z");
                                    
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

                                    csvWriter("Signs.csv", Integer.toString(xSign), 
                                            Integer.toString(ySign), 
                                            Integer.toString(zSign), 
                                            frontMsg.toString(), backMsg.toString());
                                }
                            }

                            for (int i = 0; i < blockEntites.size(); i++) 
                            {
                                CompoundTag blockEnity = (CompoundTag) blockEntites.get(i);

                                String id = blockEnity.getString("id");

                                if ("minecraft:banner".equalsIgnoreCase(id)) 
                                {
                                   int bannerX =  blockEnity.getInt("x");
                                   int bannerY = blockEnity.getInt("y");
                                   int bannerZ = blockEnity.getInt("z");
                                   
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
                                        System.out.println("Banner has no name");
                                    }

                                    int baseColor = blockEnity.getInt("Base");

                                    System.out.println("Base Color of banner "
                                            + baseColor);

                                    ListTag patterns = blockEnity.getListTag("Patterns");

                                    bannerCounter++;

                                    if (patterns != null) 
                                    {
                                    String costumeName = blockEnity.getString("CustomName");

                                        System.out.println("Banner pattern");

                                        for (int j = 0; j < patterns.size(); j++) 
                                        {
                                            CompoundTag pattern = (CompoundTag) patterns.get(j);
                                            int color = pattern.getInt("Color");
                                            String patternType = pattern.getString("Pattern");
                                            
                                            
                                            switch (color)
                                            {
                                                case 0:
                                                    System.out.println("Black");
                                                    break;
                                                case 1: 
                                                    System.out.println("Red");
                                                    break;
                                                case 2: 
                                                    System.out.println("Green");
                                                    break;
                                                case 3: 
                                                    System.out.println("Brown");
                                                    break;
                                                case 4: 
                                                    System.out.println("blue");
                                                    break;
                                                case 5: 
                                                    System.out.println("Purple");
                                                    break;
                                                case 6: 
                                                    System.out.println("Cyan");
                                                    break;
                                                case 7: 
                                                    System.out.println("Light Gray");
                                                    break;
                                                case 8: 
                                                    System.out.println("Gray");
                                                    break;
                                                case 9:
                                                    System.out.println("Pink");
                                                    break;
                                                case 10: 
                                                    System.out.println("Lime");
                                                    break;
                                                case 11: 
                                                    System.out.println("Yellow");
                                                    break;
                                                case 12: 
                                                    System.out.println("Light Blue");
                                                    break;
                                                case 13: 
                                                    System.out.println("Magenta");
                                                    break;
                                                case 14: 
                                                    System.out.println("Orange");
                                                    break;
                                                case 15: 
                                                    System.out.println("White");
                                                    break;
                                            }
                                            
                                            
                                            switch (patternType)
                                            {
                                                case "b":
                                                    System.out.println("Fully color Base ");
                                                    break;
                                                case "bs":
                                                    System.out.println("Bottom Stripe ");
                                                    break;
                                                case "ts":
                                                    System.out.println("Top Stripe ");
                                                    break;
                                                case "ls":
                                                    System.out.println("Left Stripe ");
                                                    break;
                                                case "rs":
                                                    System.out.println("Right Stripe ");
                                                    break;
                                                case "cs":
                                                    System.out.println("Center Stripe (Vertical) ");
                                                    break;
                                                case "ms":
                                                    System.out.println("Middle Stripe (Horizontal) ");
                                                    break;
                                                case "drs":
                                                    System.out.println("Down Right Stripe ");
                                                    break;
                                                case "dls":
                                                    System.out.println("Down Left Stripe ");
                                                    break;
                                                case "ss":
                                                    System.out.println("Small (Vertical) Stripes ");
                                                    break;
                                                case "cr":
                                                    System.out.println("Diagonal Cross ");
                                                    break;
                                                case "sc":
                                                    System.out.println("Square Cross ");
                                                    break;
                                                case "ld":
                                                    System.out.println("Left of Diagonal ");
                                                    break;
                                                case "rud":
                                                    System.out.println("Right of upside-down Diagonal ");
                                                    break;
                                                case "lud":
                                                    System.out.println("Left of upside-down Diagonal ");
                                                    break;
                                                case "rd":
                                                    System.out.println("Right of Diagonal ");
                                                    break;
                                                case "vh":
                                                    System.out.println("Vertical Half (left) ");
                                                    break;
                                                case "vhr":
                                                    System.out.println("Vertical Half (right) ");
                                                    break;
                                                case "hh":
                                                    System.out.println("Horizontal Half (top) ");
                                                    break;
                                                case "hhb":
                                                    System.out.println("Horizontal Half (bottom) ");
                                                    break;
                                                case "bl":
                                                    System.out.println("Bottom Left Corner ");
                                                    break;
                                                case "br":
                                                    System.out.println("Bottom Right Corner ");
                                                    break;
                                                case "tl":
                                                    System.out.println("Top Left Corner ");
                                                    break;
                                                case "tr":
                                                    System.out.println("Top Right Corner ");
                                                    break;
                                                case "bt":
                                                    System.out.println("Bottom Triangle ");
                                                    break;
                                                case "tt":
                                                    System.out.println("Top Triangle ");
                                                    break;
                                                case "bts":
                                                    System.out.println("Bottom Triangle Sawtooth ");
                                                    break;
                                                case "tts":
                                                    System.out.println("Top Triangle Sawtooth ");
                                                    break;
                                                case "mc":
                                                    System.out.println("Middle Circle ");
                                                    break;
                                                case "mr":
                                                    System.out.println("Middle Rhombus ");
                                                    break;
                                                case "bo":
                                                    System.out.println("Border ");
                                                    break;
                                                case "cbo":
                                                    System.out.println("Curly Border ");
                                                    break;
                                                case "bir":
                                                    System.out.println("Brick ");
                                                    break;
                                                case "gra":
                                                    System.out.println("Gradient");
                                                    break;
                                                case "gru":
                                                    System.out.println("Gradient upside-down ");
                                                    break;
                                                case "cre":
                                                    System.out.println("Creeper ");
                                                    break;
                                                case "sku":
                                                    System.out.println("Skull");
                                                    break;
                                                case "flo":
                                                    System.out.println("Flower");
                                                    break;
                                                case "moj":
                                                    System.out.println("Mojang");
                                                    break;
                                                case "blb":
                                                    System.out.println("Globe");
                                                    break;
                                                case "pig":
                                                    System.out.println("Piglin");
                                                    break;
                                            }
                                            
                                            System.out.println("Pattern: "
                                                    + patternType + "Color" + color);
                                            
                                            csvWriter("Banners.csv", Integer.toString(bannerX), 
                                                    Integer.toString(bannerY), 
                                                    Integer.toString(bannerZ),
                                                    costumeName,
                                                    Integer.toString(color), patternType);
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
    /**
     * The processBlockStates() method handles all block state stuff
     * 
     * @param section the section data for that chunk
     * @param chunkX where in the chunk you are looking 
     * @param chunkZ where in the chunk you are looking 
     * @throws IOException 
     */
    public void processBlockStates(CompoundTag section, int chunkX, int chunkZ) throws IOException 
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
                            csvWriter("Blocks.csv", blockName, 
                                    Integer.toString(globalX), 
                                    Integer.toString(globalY), 
                                    Integer.toString(globalZ));
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
    
/**
 * The getPaletteIndexFromData() gets all of the palettes from the data 
 * mostly for blocks
 * 
 * @param data Long array of the blocks nbt data 
 * @param index the y index of the section 
 * @param bitsPerBlock amount of bits allowed for the buffer 
 * @return who really knows, idk palette stuff 
 */
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
    
/**
 * the processBiomes() method handles the biomes palette
 * 
 * @param section the section data
 * @param chunkX where in the chunk of x 
 * @param chunkZ where in the chunk of z 
 * @throws IOException 
 */
    public void processBiomes(CompoundTag section, int chunkX, int chunkZ) throws IOException 
    {
    if (section.containsKey("biomes")) 
    {
        CompoundTag biomes = section.getCompoundTag("biomes");

        ListTag<StringTag> palette = (ListTag<StringTag>) biomes.getListTag("palette");
        long[] data = biomes.getLongArray("data");

        if (palette == null || data == null || palette.size() == 0 || data.length == 0) 
        {
            System.out.println("No biome data found in section.");
            return;
        }

        int bitsPerBiome = data.length * 64 / 64; // 64 blocks per section
        int paletteSize = palette.size();

        if (bitsPerBiome == 0) 
        {
            System.out.println("Invalid bitsPerBiome for section: " + bitsPerBiome);
            return;
        }

        if (paletteSize > (1 << bitsPerBiome)) 
        {
            bitsPerBiome++;
        }

        Object[] filename = getFiles().toArray();
        String regionFileName = ((File) filename[fileBeingUsed]).getName();

        String[] splitName = regionFileName.split("\\.");
        int regionX = Integer.parseInt(splitName[1]);
        int regionZ = Integer.parseInt(splitName[2]);

        int baseGlobalX = regionX * 512;
        int baseGlobalZ = regionZ * 512;

        for (int i = 0; i < 64; i++) 
        {
            int biomeIndex = getPaletteIndexFromData(data, i, bitsPerBiome);

            if (biomeIndex >= 0 && biomeIndex < palette.size()) 
            {
                StringTag biome = palette.get(biomeIndex);
                String biomeName = biome.getValue(); 
                
                int globalX = baseGlobalX + (i % 16);
                int globalZ = baseGlobalZ + (i / 16);

                System.out.println("Biome: " + biomeName + " at (" + globalX + 
                        ", " + globalZ + ")");
                csvWriter("Biomes.csv", biomeName, Integer.toString(globalX), 
                        Integer.toString(globalZ));
            } 
            else
            {
                System.out.println("Biome at index " + biomeIndex + " is out of bounds.");
            }
        }
    } 
    else 
    {
        System.out.println("No biomes tag found in this section.");
    }
}

}

