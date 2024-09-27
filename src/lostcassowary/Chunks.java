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
    public int getNBTData() throws FileNotFoundException, IOException {
        super.getChunkLocations();
        //Object[] chunkOffsets = super.getChunkLocationOffset().toArray();
        Object[] filename = getFiles().toArray();

        // System.out.println(chunkOffsets.length);
        // System.out.println(filename.length);
        //int counter = 0;
        int fileBeingUsed = 0;
        while (fileBeingUsed != filename.length) {

            System.out.println((File) filename[fileBeingUsed]);

            //NamedTag namedTag = NBTUtil.read((File) filename[fileBeingUsed]);
            MCAFile mcaFile = MCAUtil.read((File) filename[fileBeingUsed]);

            for (int x = 0; x < 32; x++) {
                for (int z = 0; z < 32; z++) {
                    mcaFile.cleanupPalettesAndBlockStates();

                    Chunk chunk = mcaFile.getChunk(x, z);

                    CompoundTag ahhh = chunk.getHandle();
                    //  System.out.println(ahhh.toString());

                    if (ahhh != null) {
                        ListTag sections = (ListTag) ahhh.getListTag("sections");
                        if (sections != null) {
                            for (int i = 0; i < sections.size(); i++) {
                                CompoundTag section = (CompoundTag) sections.get(i);
                                if (section.containsKey("biomes")) {
                                    CompoundTag biomes = (CompoundTag) section.getCompoundTag("biomes");
                                    System.out.println("Biomes tag found: " + biomes);
                                }
                            }
                        }
                    }

                    CompoundTag chunkHM = chunk.getHeightMaps();
                    System.out.println(chunkHM);

                    ListTag<CompoundTag> entities = chunk.getTileEntities();
                    if (entities != null) {
                        System.out.println(entities.asStringTagList());
                    } else {
                        System.out.println("No entites found in" + x + " " + z);
                    }

                    for (int y = -60; y < 256; y++) {
                        //System.out.println(mcaFile.getBlockStateAt(x, y, z));
                       // int biomes = mcaFile.getBiomeAt(x, y, z);
                        //System.out.println(biomes);
                    }
                }
            }
            fileBeingUsed++;
        }

        //counter++;
        return fileBeingUsed;
    }

}
