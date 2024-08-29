package lostcassowary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lawnguy
 */
public class Region extends FileHandling 
{

    private String fileRegionParsedx;
    private String fileRegionparsedz;
    private int xCord;
    private int zCord;
    private int x;
    private int z;
    private int regionX;
    private int regionZ;
    private List<Byte> chunkByteLocations = new ArrayList<>();


    /**
     * The getRegionCords method finds the appropriate region coordinate from a
     * set of normal Minecraft coordinates
     *
     * @return the x-coordinate and the Z-coordinate of the region
     */
    public int[] getRegionCords() 
    {
        regionX = (int) Math.floor(xCord / 32.0f);
        regionZ = (int) Math.floor(zCord / 32.0f);

        return new int[]{regionX, regionZ};

    }

    /**
     * The setRegionCords takes in the info from the demo file to be used in the
     * getRegionCords method
     *
     * @param startingXCord asks user to enter an x coordinate from the
     * Minecraft world
     * @param startingZCord ask user to enter an z coordinate from the Minecraft
     * world
     */
    public void setRegionCords(int startingXCord, int startingZCord)
    {

        xCord = startingXCord;
        zCord = startingZCord;

    }
    
    
    public void setPath(String initalFilePath)
    {
        super.setFilePath(initalFilePath);
    }

    public List<Byte> getChunkLocations() throws FileNotFoundException, IOException 
    {
        //locations (1024 entries; 4 bytes each)
        Object[] filenames = getFiles().toArray();
        System.out.println(filenames.length);       
        //byte[] a = new byte[1024 * filenames.length];
        
        for (int i = 0; i < filenames.length; i++) 
        {   
         byte[] b = new byte[1024 * 4];

           FileInputStream fileName = new FileInputStream((File) filenames[i]); 
                int cursor = 0;
                for (int j = 0; j < 1024; j++)
                {
                    fileName.read(b, cursor, 4);
                    cursor += 4;
                }
            fileName.close();
            for (int k = 0; k < b.length; k++)
            {
            chunkByteLocations.add(b[k]);
            }
            System.out.println(chunkByteLocations);
        }
        return chunkByteLocations;

    }


    public byte getChunkTimeStamps() {

    }

    public void setChunkTimeStamps() {

    }

    public byte getChunksAndOther() {

    }

    public void setChunksAndOther() {

    }

}
