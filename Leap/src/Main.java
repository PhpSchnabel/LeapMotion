import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transformer transformer = new Transformer();
		ArrayList<PointList> arrayList = transformer.readCSV();
		ArrayList<Gesture> gestureList = new ArrayList<Gesture>();
		for (PointList list:arrayList) {
			gestureList.add(list.getGesture());
		}
		Dempster dempster = new Dempster();
		gestureList.forEach((g)->System.out.println(g.getDirection()+ ", "+g.getSpeed()));
		System.out.println(dempster.analyzeGestures(gestureList));
		
		//pointList.getGesture());

	}

}
