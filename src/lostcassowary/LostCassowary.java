
package lostcassowary;

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
/**
 *
 * @author Lawnguy
 */
public class LostCassowary {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
     Scanner input = new Scanner(System.in);  
     Region region = new Region();
     FileHandling files = new FileHandling();
        
    String endProgram = "Cancel";
    String regionCordCalc = "Yes";
     
     // Progam loop will allow to end the program at any time the user wants 
     while (true)
     {
         // user input 
         System.out.println("To stop the program at anytime type: Cancel"); 
         System.out.println("If not enter Continue");
         System.out.println("--------------------------------");
         
         String userInput = input.nextLine();
         
         // if the user enters cencel, the program will end 
         if (userInput.equals(endProgram))
         {
             System.out.println("Program has been added");
             break;
         }
         
         // Ask the user if they want to calculate the region file cord 
         System.out.println("\n------------------------------------");
         System.out.println("Would you like to calculate Region Cords"); 
         System.out.println("Enter Yes for yes, Enter No for No");
         System.out.println("\n------------------------------------");
         String userREgionInput = input.nextLine();
         
         // if they say yes go through this block 
         if (userREgionInput.equals(regionCordCalc))
         {
             System.out.println("\n------------------------------------");
             System.out.println("Enter Your X Cord Then Y Cord");
             int userXInputs = input.nextInt();
             int useryInputs = input.nextInt();

             region.setRegionCords(userXInputs,useryInputs);
             int[] strongMan = region.getRegionCords();
             
             // did not know array class had a tostring method built in
             System.out.println(Arrays.toString(strongMan));
             
             System.out.println("\n\n---------------------------------");
         }
         
         System.out.println("Please copy and Paste your Path to the regionFiles");
         files.setFilePath(input.nextLine());
         
       //  System.out.println("File path: " + files.getFilePath());
         
         List<String> fileNames = files.getfiles(); // Now stores file names in the list
       
        System.out.println(fileNames);
    
         System.out.println(files.getFileNameParseingInfo());
        
        
        
        
     }
    }
}
