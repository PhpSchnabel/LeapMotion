import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Transformer {
	//Liste aus PointLists (getrennte Bewegungen)
	private ArrayList<PointList> myPointLists = new ArrayList<PointList>();

	public ArrayList<PointList> readCSV() {
		//Einlesen der csv Datei
		String csvFile = "../csv/klopfen.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		PointList pointList = new PointList();

		try {
			//csv in Buffered Reader und jede Zeile einlesen
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {

				// X,Y,Z am Komma trennen und einlesen
				String[] point = line.split(cvsSplitBy);

				if (!(point[0].equals("0.0") && point[1].equals("0.0") && point[2].equals("0.0"))) {
					try {
						//Punkt zu einer (großen) PointList hinzufügen
						pointList.add(new Point(point[0], point[1], point[2]));
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

		//Bewegungen trennen
		sortMeasurements(pointList);
		//mögliche Messfehler entfernen
		removeMeasureErros();
		//Listen nach Entfernung der Messfehler zusammenführen
		appendLists();

		return myPointLists;
	}

	private void appendLists(){
		//Listen zusammenfügen, die aufgrund von Messfehler getrennt wurden
		if(myPointLists.size()-1 > 0){
			PointList buffer1;
			PointList buffer2;

			int i = 0;
			while(myPointLists.size()-1 > i){
				//zwei PointLists aus der ArrayList nehmen
				buffer1 = myPointLists.get(i);
				buffer2 = myPointLists.get(i+1);

				//vergleichen, ob beide Listen in die gleiche x-Richtung zeigen (fallen/steigen)
				if(checkAppendX(buffer1, buffer2)){
					//falls ja, zweite Liste an erste anhängen
					buffer1.append(buffer2);
					//zweite Liste aus ArrayList entfernen
					myPointLists.remove(buffer2);
				}
				else{
					//i nur erhöhen, wenn kein Element entfernt wurde. Andernfalls geht es mit selben Element weiter
					i++;
				}
			}

			i = 0;
			while(myPointLists.size()-1 > i){
				//zwei PointLists aus der ArrayList nehmen
				buffer1 = myPointLists.get(i);
				buffer2 = myPointLists.get(i+1);

				//vergleichen, ob beide Listen in y-Richtung variieren bzw. stark abweichen
				if(checkAppendY(buffer1, buffer2)){
					//falls ja, Liste anhängen
					buffer1.append(buffer2);
					//zweite Liste entfernen
					myPointLists.remove(buffer2);
				}
				else{
					//i nur erhöhen, wenn kein Element entfernt wurde. Andernfalls geht es mit selben Element weiter
					i++;
				}
			}
		}
	}

	//Y-Richtung schauen, ob Liste dazu passt
	private boolean checkAppendY(PointList buffer1, PointList buffer2){

		//erstes Element aus erster PointList entnehmen
		double y1 = buffer1.getFirst().getY();
		//erstes und letztes Element aus zweiter PointList entnehmen
		double y2First = buffer2.getFirst().getY();
		double y2Last = buffer2.getLast().getY();

		//Vergleich ob die Differenz zwischen y1 und y2First oder y2 und y2Last größer als 50 sind
		if(Math.abs(y1-y2First) > 50 || Math.abs(y1-y2Last) > 50){
			return true;
		}

		return false;
	}

	//X-Richtung schauen, ob Liste dazu passt
	private boolean checkAppendX(PointList buffer1, PointList buffer2){
		//herausfinden, ob Listen steigen/fallen
		String direction1 = getDirection(buffer1);
		String direction2 = getDirection(buffer2);

		//falls beide steigen/fallen
		if(direction1.equals(direction2)){
			double x1 = buffer1.getLast().getX();
			double x2 = buffer2.getFirst().getX();

			//überprüfen, ob beide Listen fallen/steigen
			if(direction1.equals("steigt")) {
				//wenn Abstand der Punkte kleiner 1
				if (Math.abs(x1 - x2) < 1) {
					return true;
				//oder wenn der zweite Punkt kleiner als der Erste ist, dann gebe anhängen
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
		//wenn erster Punkt kleiner als letzter Punkt, dann steigt die Punktliste an
		if(list.getFirst().getX() < list.getLast().getX()){
			return "steigt";
		}
		//ansonsten fällt sie
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
			//Pufferliste erstellen und erstes Element hinzufügen
			bufferList = new PointList();
			bufferList.add(buffer);

			//wenn das Puffer X kleiner als das nächste X ist
			if(buffer.getX() < pointList.get(i+1).getX()){
				//und solange das Puffer X kleiner als das nächste X ist
				while(i < pointList.size()-1 && buffer.getX() < pointList.get(i+1).getX()){
					i++;
					//nächstes Element ist neuer Puffer
					buffer = pointList.get(i);
					//und wird zur Pufferliste hinzugefügt
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

			//wenn die PointList nicht zu Ende war..
			if(i < pointList.size()-1){
				i++;
				//dann setzen wir das nächste Element als neuen Puffer und beginnen von vorne
				buffer = pointList.get(i);
			}

			//die PufferListe ist eine vollständige Bewegung in x-Richtung und wird zur ArrayList<PointList> hinzugefügt
			myPointLists.add(bufferList);
		}


	}

	private void removeMeasureErros(){

		int size = myPointLists.size()-1;
		ArrayList<PointList> buffer = new ArrayList<PointList>();

		//mögliche Messfehler entfernen. Gibt es nur einen Wert, dann bleibt er erhalten
		if(size > 0) {
			for (int i = 0; i < myPointLists.size() - 1; i++) {
				//wenn Listengröße kleiner als 4 ist, dann lösche Liste. Kleiner 4 deutet meistens auf Messfehler hin
				if (!(myPointLists.get(i).size() < 4)) {
					buffer.add(myPointLists.get(i));
				}
			}

			myPointLists = buffer;
		}
	}
}

