import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Transformer {

	ArrayList<PointList> myPointLists;


	public PointList readCSV(){
		String csvFile = "C:/Users/Philipp/Desktop/Studium/LeapMotion/csv/klopfen.csv";
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

		Point first;
		PointList newList;
		int i = 1;
		myPointLists = new ArrayList<PointList>();

		while(i < pointList.size()-1){
			newList = new PointList();

			if(pointList.get(i).getX() < pointList.get(i-1).getX()) {
				while (i < (pointList.size() - 1) && pointList.get(i).getX() < pointList.get(i-1).getX()) {
					newList.add(pointList.get(i));
					pointList.remove(i);
					i++;
				}
			}

			else{
				while (i < (pointList.size() - 1) && pointList.get(i).getX() >= pointList.get(i-1).getX()) {
					newList.add(pointList.get(i));
					pointList.remove(i);
					i++;
				}
			}

			myPointLists.add(newList);
		}

		i = 0;
		PointList buffer;
		PointList bufferNext;

		while(i < myPointLists.size()-1){
			buffer = myPointLists.get(i);

			if(buffer.size() <= 2){
				myPointLists.remove(buffer);
			}
			i++;
		}
		/*
		while(i < myPointLists.size()-1){
			 buffer = myPointLists.get(i);

			 if(buffer.size() < 5){
			 	bufferNext = myPointLists.get(i+1);

			 	while(i < myPointLists.size()-1 && bufferNext.size() < 5){
			 		buffer.append(bufferNext);
					myPointLists.remove(bufferNext);
					i++;
					bufferNext = myPointLists.get(i);
				}
			 }
			 else{
			 	i++;
			 }
		}
		*/


		for (PointList list:myPointLists){
			System.out.println("size: "+list.size());
		}

		return pointList;

	}
}

