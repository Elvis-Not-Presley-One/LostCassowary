package lostcassowary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public byte[] getChunkLocations() throws FileNotFoundException, IOException 
    {
        //locations (1024 entries; 4 bytes each)


        Object[] filenames = fileNameList.toArray();
        
        byte[] b = new byte[1024];

        for (int i = 0; i < filenames.length; i++) 
        {   

            FileInputStream fileName = new FileInputStream((File) filenames[i]);
            for (int j = 0; j < 1024; j++) 
            {
                fileName.read(b, 0, 4);
                System.out.println(Arrays.toString(b));
            }
        }
        return b;

    }

    public void setChunkLocations() {

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
