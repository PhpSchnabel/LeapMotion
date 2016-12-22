import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Transformer {

	public static PointList readCSV(){
		String csvFile = "C:/Users/Philipp/Desktop/Studium/LeapMotion/csv/open_fast.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		PointList pointList = new PointList();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] point = line.split(cvsSplitBy);

				//System.out.println(point[0] + " " + point[1] + " " + point[2]);

				if(!(point[0].equals("0.0") && point[1].equals("0.0") && point[2].equals("0.0"))){
					try {
						pointList.add(new Point(point[0], point[1], point[2], point[3], point[4], point[5]));
					}
					catch (IndexOutOfBoundsException e){
						e.printStackTrace();
					}
					
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pointList;

	}
}

