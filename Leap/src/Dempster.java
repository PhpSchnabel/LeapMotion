import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dempster {
	
//	Einzelne Mengen f�r die verschiedenen F�lle
	Map<String, Double> open = new HashMap<String, Double>();
	Map<String, Double> repeat = new HashMap<String, Double>();
	Map<String, Double> closed = new HashMap<String, Double>();
	Map<String, Double> directionNeutral = new HashMap<String, Double>();
	Map<String, Double> slow = new HashMap<String, Double>();
	Map<String, Double> fast = new HashMap<String, Double>();
	Map<String, Double> speedNeutral = new HashMap<String, Double>();
	Map<String, Double> unknown = new HashMap<String, Double>();
	
	/*Festlegung der Evidenzen*/
	private final double defaultPOSSIBLE = 0.7;
	private final double defaultNEUTRAL = 0.2;
	private final double defaultUNKNOWN = 0.1;
	private final String EMPTY = "empty";
	
	
	
	/*Default Konstruktor. F�hrt die Definition der Mengen durch*/
	public Dempster() {
		addHashes();
	}

	//Bef�llen der Mengen f�r die einzelnen M�glichkeiten
	private void addHashes()
	{
		open.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		open.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		open.put(Emotion.�BERRASCHUNG.toString(),defaultPOSSIBLE);
		
		repeat.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		repeat.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		closed.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		
		directionNeutral.put(Emotion.VERACHTUNG.toString(),defaultNEUTRAL);
		directionNeutral.put(Emotion.EKEL.toString(),defaultNEUTRAL);
		
		slow.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		fast.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		fast.put(Emotion.VERACHTUNG.toString(),defaultPOSSIBLE);
		fast.put(Emotion.EKEL.toString(),defaultPOSSIBLE);
		
		speedNeutral.put(Emotion.�BERRASCHUNG.toString(), defaultNEUTRAL);
		
		//Eventuelle Me�fehler ausgleichen
		unknown.put(Emotion.AERGER.toString(), defaultUNKNOWN);
		unknown.put(Emotion.EKEL.toString(), defaultUNKNOWN);
		unknown.put(Emotion.FREUDE.toString(), defaultUNKNOWN);
		unknown.put(Emotion.TRAUER.toString(), defaultUNKNOWN);
		unknown.put(Emotion.VERACHTUNG.toString(), defaultUNKNOWN);
		unknown.put(Emotion.�BERRASCHUNG.toString(), defaultUNKNOWN);
		
	}

	
//	F�hrt die Berechnung der Plausibilit�t durch und gibt einen Ergebnis String zur Ausgabe zur�ck
	public String analyzeGestures(ArrayList<Gesture> gestureList)
	{
		Emotion.AERGER.resetBelieve();
		Emotion.EKEL.resetBelieve();
		Emotion.FREUDE.resetBelieve();
		Emotion.TRAUER.resetBelieve();
		Emotion.VERACHTUNG.resetBelieve();
		Emotion.�BERRASCHUNG.resetBelieve();
		
		//�berpr�fe die �bergebene List auf Repeatabfolgen
		List<Gesture> analyzedList = analyzeForRepeats(gestureList);
		int length = analyzedList.size();
		Map<String,Double> resultList = new HashMap<String,Double>();
		
		for(Gesture g: analyzedList)
		{
			getPossibleResults(g.getDirection(), g.getSpeed()).forEach((k, v) -> resultList.merge(k, v, (v1, v2) -> v1 + v2));
		}
		
		//Berechne die durchschnittliche Plausibilit�t
		for (Map.Entry<String, Double> entry : resultList.entrySet()) {
		    entry.setValue(entry.getValue()/length);
		}
		
		//Sortieren der Resultatsliste
		Map<String,Double> sortedList =	resultList.entrySet()
													.stream()
											        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
											        .collect(Collectors.toMap(
											          Map.Entry::getKey, 
											          Map.Entry::getValue, 
											          (e1, e2) -> e1, 
											          LinkedHashMap::new));
		
		StringBuilder result = new StringBuilder();
		
		result.append("M�gliche Emotionen sortiert nach ihrer Plausibilit�t:\n");
		
		//Ausgabe String formatieren
		for (Map.Entry<String, Double> entry : sortedList.entrySet()) {
		   result.append(" Emotion: "+entry.getKey()+", Plausibilit�t: "+roundedPrint(entry.getValue(),2) + ", Zweifel: " +roundedPrint(1-entry.getValue(),2)
		   +", Believe: "+roundedPrint(getBelieveValue(entry.getKey(),length),2)+ "\n");
		}
		
		return result.toString();
	}
	
	//Runden des Wertes f�r eine sch�ne Ausgabe
	private String roundedPrint(double wert, int stellen) {
	        StringBuilder sb = new StringBuilder(",##0.");
	        for (int i=0; i<stellen; i++)
	            sb.append("0");
	        DecimalFormat df = new DecimalFormat(sb.toString());
	        df.setRoundingMode(RoundingMode.HALF_UP);
	       return df.format(wert).toString();
	    }
	
	//�berpr�fung ob Liste mit Gesten Abfolgen enth�lt die auf ein Repeat schlie�en lassen
	private List<Gesture> analyzeForRepeats(ArrayList<Gesture> gestureList) {
		
		
		List<Gesture> analyzedList = new ArrayList<Gesture>();
		
	for(int i = 0; i < gestureList.size(); i++ )
	{
			Gesture actual = gestureList.get(i);
			
			
		if((i+1)<gestureList.size())
		{
			Gesture next = gestureList.get(i+1);
			
			if((actual.getDirection() == Direction.OPEN && next.getDirection() == Direction.CLOSE)||
					(actual.getDirection() == Direction.CLOSE && next.getDirection() == Direction.OPEN))
			{
				actual.setDirection(Direction.REPEAT);
				analyzedList.add(actual);
				++i;
			}else
			{
				analyzedList.add(actual);
			}
		}
		else
		{
			analyzedList.add(actual);
		}
	}
		return analyzedList;
		
	}

	//Startet die Evidenzberechnung, bestimmt welche Mengen betrachtet werden m�ssen
	private Map<String,Double> getPossibleResults(Direction direction,Speed speed)
	{
		Map<String, Double> possibleDirection = null;
		Map<String, Double> possibleSpeed = null;
		
		switch(direction)
		{
		case CLOSE:
			possibleDirection = closed;
			break;
		case OPEN:
			possibleDirection = open;
			break;
		case REPEAT:
			possibleDirection = repeat;
			break;
		default:
			break;
		}
		
		switch(speed)
		{
		case FAST:
			possibleSpeed = fast;
			break;
		case SLOW:
			possibleSpeed = slow;
			break;
		default:
			break;
		}
		
		return calculcateEvidence(possibleDirection,possibleSpeed);
	}

	//Durchf�hrung der Akkumulation und der Berechnung der Plausibilit�t
	private Map<String,Double> calculcateEvidence(Map<String, Double> possibleDirection, Map<String, Double> possibleSpeed) {
		
		//Durchf�hrung der Akkumulation
		Map<String,Double> accumulationDirectSpeed = intersectCalculate(possibleDirection, possibleSpeed);
		Map<String,Double> accumulationDirectNeutralSpeed = intersectCalculate(possibleDirection, speedNeutral);
		Map<String,Double> accumulationDirectUnknow = intersectCalculate(possibleDirection, unknown);
		Map<String,Double> accumulationNeutralDirectionSpeed = intersectCalculate(directionNeutral, possibleSpeed);
		Map<String,Double> accumulationUnknownSpeed = intersectCalculate(unknown, possibleSpeed);
		Map<String,Double> accumulationNeutralDirectionNeutralSpeed = intersectCalculate(directionNeutral, speedNeutral);
		Map<String,Double> accumulationUnknownNeutralSpeed = intersectCalculate(unknown, speedNeutral);
		Map<String,Double> accumulationNeutralDirectionUnknown = intersectCalculate(directionNeutral, unknown);
		Map<String,Double> accumulationUnknown = intersectCalculate(unknown, unknown);
		
		Map<String, Double> plausibilit�t = new HashMap<String, Double>();
		
		double konflikt = 0;
		
		//Ist die Berechnung eines Konflikts K von N�ten?
		if(getFirstKeyofHashMap(accumulationDirectSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationDirectSpeed);
			accumulationDirectSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationDirectNeutralSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationDirectNeutralSpeed);
			accumulationDirectNeutralSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationDirectUnknow).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationDirectUnknow);
			accumulationDirectUnknow.clear();
		}
		if(getFirstKeyofHashMap(accumulationNeutralDirectionSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationNeutralDirectionSpeed);
			accumulationNeutralDirectionSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationUnknown).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationUnknown);
			accumulationUnknown.clear();
		}
		if(getFirstKeyofHashMap(accumulationUnknownSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationUnknownSpeed);
			accumulationUnknownSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationNeutralDirectionNeutralSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationNeutralDirectionNeutralSpeed);
			accumulationNeutralDirectionNeutralSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationUnknownNeutralSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationUnknownNeutralSpeed);
			accumulationUnknownNeutralSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationNeutralDirectionUnknown).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationNeutralDirectionUnknown);
			accumulationNeutralDirectionUnknown.clear();
		}
		
		
		
		//Berechnung des Konflikts K falls leere Mengen vorhanden waren
		if(Double.compare(konflikt, 0)!= 0)
		{
			konflikt = 1/(1-konflikt);
			
			if(!accumulationDirectSpeed.isEmpty())
			{
				calculateThroughMap(accumulationDirectSpeed,konflikt);
				calculateBelieve(accumulationDirectSpeed);
			}
			
			if(!accumulationDirectNeutralSpeed.isEmpty())
			{
				calculateThroughMap(accumulationDirectNeutralSpeed,konflikt);
				calculateBelieve(accumulationDirectSpeed);
			}
			if(!accumulationDirectUnknow.isEmpty())
			{
				calculateThroughMap(accumulationDirectUnknow,konflikt);
				calculateBelieve(accumulationDirectUnknow);
			}
			if(!accumulationNeutralDirectionSpeed.isEmpty())
			{
				calculateThroughMap(accumulationNeutralDirectionSpeed,konflikt);
				calculateBelieve(accumulationNeutralDirectionSpeed);
			}
			if(!accumulationUnknown.isEmpty())
			{
				calculateThroughMap(accumulationUnknown,konflikt);
				calculateBelieve(accumulationUnknown);
			}
			if(!accumulationUnknownSpeed.isEmpty())
			{
				calculateThroughMap(accumulationUnknownSpeed,konflikt);
				calculateBelieve(accumulationUnknownSpeed);
			}
			if(!accumulationNeutralDirectionNeutralSpeed.isEmpty())
			{
				calculateThroughMap(accumulationNeutralDirectionNeutralSpeed,konflikt);
				calculateBelieve(accumulationNeutralDirectionNeutralSpeed);
			}
			if(!accumulationUnknownNeutralSpeed.isEmpty())
			{	
				calculateThroughMap(accumulationUnknownNeutralSpeed,konflikt);
				calculateBelieve(accumulationUnknownNeutralSpeed);
			}
			if(!accumulationNeutralDirectionUnknown.isEmpty())
			{
				calculateThroughMap(accumulationNeutralDirectionUnknown,konflikt);
				calculateBelieve(accumulationNeutralDirectionUnknown);
			}
		

		}
		
		
		//Berechnung der Plausibilit�t
		accumulationDirectSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationDirectNeutralSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationDirectUnknow.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknown.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknownSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionNeutralSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknownNeutralSpeed.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionUnknown.forEach((k, v) -> plausibilit�t.merge(k, v, (v1, v2) -> v1 + v2));

		return plausibilit�t;
	}
	
	//Berechnen der Believewerte
	private void calculateBelieve(Map<String, Double> accumulation) 
	{
		if(accumulation.size()==1)
		{
			String emotion = getFirstKeyofHashMap(accumulation);
			double value = getFirstValueofHashMap(accumulation);
			
			if (Emotion.AERGER.toString().equals(emotion)) {
				Emotion.AERGER.setBelieve(Emotion.AERGER.getBelieve()+value);
			}
			else if (Emotion.EKEL.toString().equals(emotion))
			{
				Emotion.EKEL.setBelieve(Emotion.EKEL.getBelieve()+value);
			}
			else if (Emotion.FREUDE.toString().equals(emotion))
			{
				Emotion.FREUDE.setBelieve(Emotion.FREUDE.getBelieve()+value);
			}
			else if (Emotion.TRAUER.toString().equals(emotion))
			{
				Emotion.TRAUER.setBelieve(Emotion.TRAUER.getBelieve()+value);
			}
			else if (Emotion.VERACHTUNG.toString().equals(emotion))
			{
				Emotion.VERACHTUNG.setBelieve(Emotion.VERACHTUNG.getBelieve()+value);
			}
			else if (Emotion.�BERRASCHUNG.toString().equals(emotion))
			{
				Emotion.�BERRASCHUNG.setBelieve(Emotion.�BERRASCHUNG.getBelieve()+value);
			}	
		}
		
	}

	//Zur�ckgeben des Believewerts, teile ihn vor der R�ckgabe durch die Anzahl der vorhandenen Auswertungen
	private double getBelieveValue(String emotion, int length)
	{
		double result = 0;
		if (Emotion.AERGER.toString().equals(emotion)) {
			result = Emotion.AERGER.getBelieve();
		}
		else if (Emotion.EKEL.toString().equals(emotion))
		{
			result = Emotion.EKEL.getBelieve();
		}
		else if (Emotion.FREUDE.toString().equals(emotion))
		{
			result = Emotion.FREUDE.getBelieve();
		}
		else if (Emotion.TRAUER.toString().equals(emotion))
		{
			result = Emotion.TRAUER.getBelieve();
		}
		else if (Emotion.VERACHTUNG.toString().equals(emotion))
		{
			result = Emotion.VERACHTUNG.getBelieve();
		}
		else if (Emotion.�BERRASCHUNG.toString().equals(emotion))
		{
			result = Emotion.�BERRASCHUNG.getBelieve();
		}	
		return result/length;
	}
	
	//Bestimme den Durchschnitt zweier Mengen
	private Map<String,Double> intersectCalculate(Map<String, Double> mapOne, Map<String, Double> mapTwo)
	{
		Map<String,Double> result = new HashMap<String, Double>(mapOne);
		result.keySet().retainAll(mapTwo.keySet());
		
		for (Map.Entry<String, Double> entry : result.entrySet()) {
		    String key = entry.getKey();
		    entry.setValue(mapOne.get(key)*mapTwo.get(key));
		    
		}
		
		if(result.isEmpty())
		{
			 Double valueOne = getFirstValueofHashMap(mapOne);
			 Double valueTwo = getFirstValueofHashMap(mapTwo);
			 
			result.put(EMPTY, valueOne*valueTwo);
		}
		
		return result;
	}
	
	//F�hre eine Berechnung auf allen Eintr�gen einer Map durch
	private void calculateThroughMap(Map<String, Double> map, double k)
	{
		for (Map.Entry<String, Double> entry : map.entrySet()) {
		    entry.setValue(entry.getValue()*k);
		}
	}
	
	//Erhalte den Wert des ersten Eintrags einer HashMap
	private double getFirstValueofHashMap(Map<String, Double> map)
	{
		 Map.Entry<String,Double> entry = map.entrySet().iterator().next();
		 String key= entry.getKey();
		 return map.get(key);
	}
	
	//Erhalte den Key des ersten Eintrags einer HashMap
	private String getFirstKeyofHashMap(Map<String, Double> map)
	{
		 Map.Entry<String,Double> entry = map.entrySet().iterator().next();
		 return  entry.getKey();
		 
	}
	
}

