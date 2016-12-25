import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transformer transformer = new Transformer();
		//CSV Datei einlesen
		ArrayList<PointList> arrayList = transformer.readCSV();
		//Liste aus Gestures erstellen
		ArrayList<Gesture> gestureList = new ArrayList<Gesture>();
		//jede Geste der PointListen herausfinden und in gestureList hinzufügen
		for (PointList list:arrayList) {
			gestureList.add(list.getGesture());
		}
		Dempster dempster = new Dempster();
		//jede Geste ausgeben
		gestureList.forEach((g)->System.out.println(g.getDirection()+ ", "+g.getSpeed()));
		
		//Gebe das Ergebnis der möglichen Emotionen aus
		System.out.println(dempster.analyzeGestures(gestureList));
		
		//pointList.getGesture());

	}

}
