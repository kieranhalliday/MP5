import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * This is the main class for the Tour de France program. If has the main( )
 * method that reads biker information from a file and then performs some
 * computations with the data.
 * 
 * @author Sathish Gopalakrishnan
 * 
 */
public class TdFMain {

    /**
     * This is the main( ) method.
     * 
     * @param args
     *            We do not use this parameter at this point.
     */
    public static void main(String[] args) {

        // The FileInputStream to open and read from the file that
        // has the Tour de France data.
        FileInputStream tdfStream;

        // This map maintains the relationship between the
        // name of the biker and the object that holds the
        // biker's data.
        Map<String, Biker> allBikers = new TreeMap<String, Biker>();

        // Let us try to open the data file.
        // The file name is hardcoded, which is not elegant.
        // Suffices for now.
        try {
            tdfStream = new FileInputStream("tdf.txt");
        } catch (FileNotFoundException e) {
            // If, for some reason, the file was not found,
            // then throw an exception.
            // The file is however included in the git repo
            // so this should not happen.
            throw new RuntimeException(e);
        }

        // We have opened the file.
        // Let us try to read data.
        try {
            // We will use a BufferedReader to read the data from the file.
            BufferedReader tdfReader = new BufferedReader(
                    new InputStreamReader(tdfStream));

            // We will read one line at a time and then split it.
            // The format for tdf.txt is as follows:
            // - Column 1: Year
            // - Column 2: Average Speed
            // - Column 3: Biker's last name
            // - Column 4: Biker's first name
            // tdf.txt contains real data. It is also noisy like real data.
            // Some of the names have formatting issues but we have left
            // things as is.
            String line;

            // Read each line of the file until there is nothing left to read.
            while ((line = tdfReader.readLine()) != null) {

                // Split the line into columns using the split( )
                // method for Strings.
                String[] columns = line.split(",");

                // After the split, we should have the following (as Strings):
                // - columns[0] contains the year,
                // - columns[1] contains the average speed,
                // - columns[2] contains the last name,
                // - columns[3] contains the first name.

                // Is this biker already in our list?
                // If so then we do not have to create a new biker.
                // We only have to add an entry to the existing biker.
                // We will use the full name to search allBikers.
                String key = columns[3] + columns[2]; // this is the full name

                // If search is successful then add stats
                if (allBikers.containsKey(key)) {
                    allBikers.get(key).addPerformanceStats(
                            Integer.parseInt(columns[0]),
                            Double.parseDouble(columns[1]));

                    // System.out.println("Added data to biker "+allBikers.get(
                    // key ).getName( ));
                } else {
                    // Let us now create a new Biker
                    Biker newBiker = new Biker(columns[2], columns[3],
                            Integer.parseInt(columns[0]),
                            Double.parseDouble(columns[1]));

                    // Now we add this biker to allBikers.
                    allBikers.put(key, newBiker);

                    // System.out.println("Created new biker "+newBiker.getName()
                    // );
                }

            }
            tdfReader.close();
            tdfStream.close();
        } catch (Exception e) {
            // If, for any reason, we had some problems reading data...
            throw new RuntimeException(e);
        }

        // for each entry in allBikers:
        // print the best gain for the biker.
        // The best gain is defined as the maximum improvement in
        // speed between successive entries at the Tour de France.
        // This does not have to be between consecutive years;
        // entries with a gap (no racing) between the years is okay.
        for (Map.Entry<String, Biker> currentEntry : allBikers.entrySet()) {
            Biker currentBiker = currentEntry.getValue();

            // How to print formatted strings
            // Note the use of String.format( )
            System.out.println(String.format("%-30s: %s",
                    currentBiker.getName(), currentBiker.getBestGain()));
        }

        // TODO: Compute the median speed across all the entries.
        double medianSpeed = 0;
        // Your code for this should go here and should set the correct value in
        // medianSpeed.
        double temp=0;
        ArrayList <Double> allSpeeds = new ArrayList();
        
        for(Entry<String, Biker> bikers : allBikers.entrySet()){
        	for(int j=2005;j<2013;j++){
        		if(bikers.getValue().getSpeedForYear(j)!=0){
        			allSpeeds.add(bikers.getValue().getSpeedForYear(j));
        		}
        	}
        	
        }
        
        for (int i=1;i<allBikers.size();i++){
    		if(allSpeeds.get(i)<allSpeeds.get(i-1)){
    			temp=allSpeeds.get(i);
    			allSpeeds.add(i, allSpeeds.get(i-1));
    			allSpeeds.set(i-1, temp);
    			i=1;
    		}
    	}
        
        if(allSpeeds.size()%2==0){
        	medianSpeed=allSpeeds.get(((allSpeeds.size()/2)+((allSpeeds.size()/2)+1))/2);
        }
        else{
        	medianSpeed=allSpeeds.get(allSpeeds.size()/2);
        }

        System.out.println("\nThe median speed at the Tour de France is "
                + medianSpeed);

        // TODO: Compute the median of medians.
        double medianOfMedians = 0;
        // For each biker, compute the median speed. This will result in a list
        // of
        // median speeds. Now determine the median of this list.
        // Store the result in medianOfMedians.
        // Your code should go here.
        
        ArrayList <Double> OnlyMiddleSpeeds = new ArrayList();
        
        for(Entry<String, Biker> speed : allBikers.entrySet()){
        	for(int k=2005;k<2013;k++){
        		if(speed.getValue().getMedianSpeed()!=0){
        			OnlyMiddleSpeeds.add(speed.getValue().getMedianSpeed());
        		}
        	}
        }

        for (int i=1;i<allBikers.size();i++){
    		if(OnlyMiddleSpeeds.get(i)<OnlyMiddleSpeeds.get(i-1)){
    			temp=OnlyMiddleSpeeds.get(i);
    			OnlyMiddleSpeeds.add(i, OnlyMiddleSpeeds.get(i-1));
    			OnlyMiddleSpeeds.set(i-1, temp);
    			i=1;
    		}
    	}
        
        if(OnlyMiddleSpeeds.size()%2==0){
        	medianOfMedians=OnlyMiddleSpeeds.get(((OnlyMiddleSpeeds.size()/2)+((OnlyMiddleSpeeds.size()/2)+1))/2);
        }
        else{
 
        	medianOfMedians=OnlyMiddleSpeeds.get(OnlyMiddleSpeeds.size()/2);
        }

        System.out.println("\nThe median of medians at the Tour de France is "
                + medianOfMedians);
    }
}