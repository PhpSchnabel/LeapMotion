import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Transformer {
	ArrayList<PointList> myPointLists = new ArrayList<PointList>();

	public PointList readCSV() {
		String csvFile = "../csv/klopfen.csv";
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

				if (!(point[0].equals("0.0") && point[1].equals("0.0") && point[2].equals("0.0"))) {
					try {
						pointList.add(new Point(point[0], point[1], point[2], point[3], point[4], point[5]));
					} catch (IndexOutOfBoundsException e) {
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

		sortMeasurements(pointList);
		removeMeasureErros();
		appendLists();


		System.out.println("pointlists: "+ myPointLists.size());
		for (PointList list:myPointLists) {
			System.out.println("size: "+ list.size());

			for (Point pkt:list) {
				System.out.println("punkt y: "+ pkt.getY());
			}
		}
		return pointList;
	}

	private void appendLists(){
		//Listen zusammenfügen, die aufgrund von Messfehler getrennt wurden
		if(myPointLists.size()-1 > 0){
			PointList buffer1;
			PointList buffer2;

			int i = 0;
			while(myPointLists.size()-1 > i){
				buffer1 = myPointLists.get(i);
				buffer2 = myPointLists.get(i+1);

				if(checkAppendX(buffer1, buffer2)){
					buffer1.append(buffer2);
					myPointLists.remove(buffer2);
				}
				else{
					i++;
				}
			}

			i = 0;
			while(myPointLists.size()-1 > i){
				buffer1 = myPointLists.get(i);
				buffer2 = myPointLists.get(i+1);

				if(checkAppendY(buffer1, buffer2)){
					buffer1.append(buffer2);
					myPointLists.remove(buffer2);
				}
				else{
					i++;
				}
			}
		}
	}

	//Y-Richtung schauen, ob Liste dazu passt
	private boolean checkAppendY(PointList buffer1, PointList buffer2){

		double y1 = buffer1.getFirst().getY();
		double y2First = buffer2.getFirst().getY();
		double y2Last = buffer2.getLast().getY();

		if(Math.abs(y1-y2First) > 50 || Math.abs(y1-y2Last) > 50){
			return true;
		}

		return false;
	}

	//X-Richtung schauen, ob Liste dazu passt
	private boolean checkAppendX(PointList buffer1, PointList buffer2){
		String direction1 = getDirection(buffer1);
		String direction2 = getDirection(buffer2);

		if(direction1.equals(direction2)){
			double x1 = buffer1.getLast().getX();
			double x2 = buffer2.getFirst().getX();

			if(direction1.equals("steigt")) {
				if (Math.abs(x1 - x2) < 1) {
					return true;
				} else if (x1 < x2) {
					return true;
				}
			}
			else{
				if (Math.abs(x1 - x2) < 1) {
					return true;
				} else if (x1 > x2) {
					return true;
				}
			}
		}

		return false;
	}

	private String getDirection(PointList list){
		if(list.getFirst().getX() < list.getLast().getX()){
			return "steigt";
		}
		else{
			return "fällt";
		}
	}

	private void sortMeasurements(PointList pointList){
		//aufteilen der Messergebnisse in entsprechende Bewegungen in x-Richtung
		PointList bufferList;

		int i = 0;
		Point buffer = pointList.getFirst();
		while(i < pointList.size()-1){

			bufferList = new PointList();
			bufferList.add(buffer);

			if(buffer.getX() < pointList.get(i+1).getX()){
				while(i < pointList.size()-1 && buffer.getX() < pointList.get(i+1).getX()){
					i++;
					buffer = pointList.get(i);
					bufferList.add(buffer);
				}

			}
			else{
				while(i < pointList.size()-1 && buffer.getX() > pointList.get(i+1).getX()){
					i++;
					buffer = pointList.get(i);
					bufferList.add(buffer);
				}
			}

			if(i < pointList.size()-1){
				i++;
				buffer = pointList.get(i);
			}

			myPointLists.add(bufferList);
		}


	}

	private void removeMeasureErros(){

		int size = myPointLists.size()-1;
		ArrayList<PointList> buffer = new ArrayList<PointList>();

		//mögliche Messfehler entfernen. Gibt es nur einen Wert, dann bleibt er erhalten
		if(size > 0) {
			for (int i = 0; i < myPointLists.size() - 1; i++) {

				if (!(myPointLists.get(i).size() < 4)) {
					buffer.add(myPointLists.get(i));
				}
			}

			myPointLists = buffer;
		}
	}
}

