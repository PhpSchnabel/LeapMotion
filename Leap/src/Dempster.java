import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

public class Dempster {
	
	Map<String, Double> open = new HashMap<String, Double>();
	Map<String, Double> repeat = new HashMap<String, Double>();
	Map<String, Double> closed = new HashMap<String, Double>();
	Map<String, Double> directionNeutral = new HashMap<String, Double>();
	Map<String, Double> slow = new HashMap<String, Double>();
	Map<String, Double> fast = new HashMap<String, Double>();
	Map<String, Double> speedNeutral = new HashMap<String, Double>();
	Map<String, Double> unknown = new HashMap<String, Double>();
	
	
	private final double defaultPOSSIBLE = 0.7;
	private final double defaultNEUTRAL = 0.2;
	private final double defaultUNKNOWN = 0.1;
	private final String EMPTY = "empty";
	
	
	public Dempster() {
		addHashes();
		//Testzwecke
		//System.out.println(getPossibleResults(Direction.OPEN, Speed.FAST));
	}

	//Erstellen der Mengen der einzelnen Möglichkeiten
	private void addHashes()
	{
		open.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		open.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		open.put(Emotion.ÜBERRASCHUNG.toString(),defaultPOSSIBLE);
		
		repeat.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		repeat.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		closed.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		
		directionNeutral.put(Emotion.VERACHTUNG.toString(),defaultNEUTRAL);
		directionNeutral.put(Emotion.EKEL.toString(),defaultNEUTRAL);
		
		
		//Ärger und Freude sollten weniger Plausibel sein: noch nach Lösung suchen
		slow.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		fast.put(Emotion.AERGER.toString(),defaultPOSSIBLE);
		fast.put(Emotion.VERACHTUNG.toString(),defaultPOSSIBLE);
		fast.put(Emotion.EKEL.toString(),defaultPOSSIBLE);
		
		speedNeutral.put(Emotion.ÜBERRASCHUNG.toString(), defaultNEUTRAL);
		
		//Eventuelle Meßfehler ausgleichen
		unknown.put(Emotion.AERGER.toString(), defaultUNKNOWN);
		unknown.put(Emotion.EKEL.toString(), defaultUNKNOWN);
		unknown.put(Emotion.FREUDE.toString(), defaultUNKNOWN);
		unknown.put(Emotion.TRAUER.toString(), defaultUNKNOWN);
		unknown.put(Emotion.VERACHTUNG.toString(), defaultUNKNOWN);
		unknown.put(Emotion.ÜBERRASCHUNG.toString(), defaultUNKNOWN);
		
	}

	public String analyzeGestures(ArrayList<Gesture> gestureList)
	{
		List<Gesture> analyzedList = analyzeForRepeats(gestureList);
		int length = analyzedList.size();
		Map<String,Double> resultList = new HashMap<String,Double>();
		
		for(Gesture g: analyzedList)
		{
			getPossibleResults(g.getDirection(), g.getSpeed()).forEach((k, v) -> resultList.merge(k, v, (v1, v2) -> v1 + v2));
		}
		
		//Berechne die durchschnittliche Plausibilität
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
		
		result.append("Mögliche Emotionen sortiert nach ihrer Plausibilität:\n");
		
		//Ausgabe String formatieren
		for (Map.Entry<String, Double> entry : sortedList.entrySet()) {
		   result.append(" Emotion: "+entry.getKey()+", Plausibilität: "+gerundeteAusgabe(entry.getValue(),2)+"\n");
		}
		
		return result.toString();
	}
	
	private String gerundeteAusgabe(double wert, int stellen) {
	        StringBuilder sb = new StringBuilder(",##0.");
	        for (int i=0; i<stellen; i++)
	            sb.append("0");
	        DecimalFormat df = new DecimalFormat(sb.toString());
	        df.setRoundingMode(RoundingMode.HALF_UP);
	       return df.format(wert).toString();
	    }
	
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

	private Map<String,Double> getPossibleResults(Direction direction,Speed speed)
	{
		String result = "";
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

	private Map<String,Double> calculcateEvidence(Map<String, Double> possibleDirection, Map<String, Double> possibleSpeed) {
		
		//Akumulation der Werte
		//da
		Map<String,Double> accumulationDirectSpeed = intersectCalculate(possibleDirection, possibleSpeed);
		//da
		Map<String,Double> accumulationDirectNeutralSpeed = intersectCalculate(possibleDirection, speedNeutral);
		
		Map<String,Double> accumulationDirectUnknow = intersectCalculate(possibleDirection, unknown);
		//da
		Map<String,Double> accumulationNeutralDirectionSpeed = intersectCalculate(directionNeutral, possibleSpeed);
		Map<String,Double> accumulationUnknownSpeed = intersectCalculate(unknown, possibleSpeed);
		Map<String,Double> accumulationNeutralDirectionNeutralSpeed = intersectCalculate(directionNeutral, speedNeutral);
		Map<String,Double> accumulationUnknownNeutralSpeed = intersectCalculate(unknown, speedNeutral);
		Map<String,Double> accumulationNeutralDirectionUnknown = intersectCalculate(directionNeutral, unknown);
		//da
		Map<String,Double> accumulationUnknown = intersectCalculate(unknown, unknown);
		
		Map<String, Double> plausibilität = new HashMap<String, Double>();
		
		double konflikt = 0;
		
		//ToDO Konflikt K
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
		
		
		
		
		if(Double.compare(konflikt, 0)!= 0)
		{
			konflikt = 1/(1-konflikt);
			
			if(!accumulationDirectSpeed.isEmpty())
				calculateThroughMap(accumulationDirectSpeed,konflikt);
			
			if(!accumulationDirectNeutralSpeed.isEmpty())
				calculateThroughMap(accumulationDirectNeutralSpeed,konflikt);
			
			if(!accumulationDirectUnknow.isEmpty())
				calculateThroughMap(accumulationDirectUnknow,konflikt);
			
			if(!accumulationNeutralDirectionSpeed.isEmpty())
				calculateThroughMap(accumulationNeutralDirectionSpeed,konflikt);
			
			if(!accumulationUnknown.isEmpty())
				calculateThroughMap(accumulationUnknown,konflikt);
			
			if(!accumulationUnknownSpeed.isEmpty())
				calculateThroughMap(accumulationUnknownSpeed,konflikt);
			
			if(!accumulationNeutralDirectionNeutralSpeed.isEmpty())
				calculateThroughMap(accumulationNeutralDirectionNeutralSpeed,konflikt);
			
			if(!accumulationUnknownNeutralSpeed.isEmpty())
				calculateThroughMap(accumulationUnknownNeutralSpeed,konflikt);
			
			if(!accumulationNeutralDirectionUnknown.isEmpty())
				calculateThroughMap(accumulationNeutralDirectionUnknown,konflikt);

		}
		
		
		//Plausibilität
		
		accumulationDirectSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationDirectNeutralSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationDirectUnknow.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknown.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknownSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionNeutralSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationUnknownNeutralSpeed.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));
		accumulationNeutralDirectionUnknown.forEach((k, v) -> plausibilität.merge(k, v, (v1, v2) -> v1 + v2));

		return plausibilität;
	}
	
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
	
	private void calculateThroughMap(Map<String, Double> map, double k)
	{
		for (Map.Entry<String, Double> entry : map.entrySet()) {
		    entry.setValue(entry.getValue()*k);
		}
	}
	
	
	double getFirstValueofHashMap(Map<String, Double> map)
	{
		 Map.Entry<String,Double> entry = map.entrySet().iterator().next();
		 String key= entry.getKey();
		 return map.get(key);
	}
	
	String getFirstKeyofHashMap(Map<String, Double> map)
	{
		 Map.Entry<String,Double> entry = map.entrySet().iterator().next();
		 return  entry.getKey();
		 
	}
	
}

