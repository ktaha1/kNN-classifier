import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList; 
import java.util.Collections;
import java.lang.Comparable;
import java.lang.Float;
import java.util.HashMap;
import java.util.Map;

class DisClass implements Comparable<DisClass>{
	public float distance; 
	public String label;

	DisClass (float dis, String lab) { this.distance=dis;  this.label=lab; }

 
    @Override
    public int compareTo(DisClass o) {
    	float d= ((DisClass) o).distance;
    	if (this.distance >= d) return 1;
    	else return -1; 
    	
    }

    @Override
    public String toString() {
        return "{" +
                "Distance=" + distance + ", Label=" + label +'}';
    }
}


public class kNN {

	static ArrayList<DisClass> Dist_list = null;
	static double[] unabeled=null;
	static Scanner in = null;

	static String csvFile = "dataset/haberman.csv";

    public static void main(String[] args) {
       
        in = new Scanner(System.in);

        // Parameter K
        System.out.print("k = "); 	int k = in.nextInt();
		
		// Number of features
		System.out.print("\nDimensionality = "); 	int dim = in.nextInt();
		
		// Read unlabeled vector
		readVector(dim);

		// Read data and calculate distance		
		readAndCalculate(dim);

        // Sort distances    
        Collections.sort(Dist_list);  

        // Results
        System.out.println("\n\n***********  RESULTS   ***********\n");
        predictClassification(k);

    }


    static void readVector(int dim){

		in = new Scanner(System.in);
		unabeled= new double[dim];
		System.out.println("\nUnlabeled Vector: ");
		for (int i=0;i<dim;i++) {
			System.out.print("Value of attribute("+(i+1)+") = ");
			unabeled[i]= in.nextDouble();
		}
	}

	static void readAndCalculate(int dim){

		String cvsSplitBy = ",";
        BufferedReader br = null;
        String line = "";
        
        Dist_list = new ArrayList<DisClass>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                // Euclidean  Distance calculation
                float sum=0;
                for (int i=0; i<dim; i++) {
                	sum+=Math.pow((unabeled[i]-Float.parseFloat(data[i])),2);
                }

                // add distance to list
                Dist_list.add(new DisClass((float)Math.sqrt(sum), data[dim]));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	static void predictClassification(int k){
		
		System.out.println(k+" nearest neighbor distances :\n");
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
        String label=null;
        for (int i=0; i<k; i++ ) {
        	label=Dist_list.get(i).label;

        	if(labels.containsKey(label))
            {
                //If element is present in labels, incrementing it's count by 1                 
                labels.put(label, labels.get(label)+1);
            }
            else
            {
                //If element is not present in labels, add it to labels with 1 as it's value                 
                labels.put(label, 1);
            }        	
        	System.out.println(Dist_list.get(i));
        }

        System.out.println("\nOccurrences of each label : "+labels);
        String key = Collections.max(labels.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("\nClass predicted: "+key);
	}

}