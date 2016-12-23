import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dempster {
	
	Map<String, Double> open = new HashMap<String, Double>();
	Map<String, Double> repeat = new HashMap<String, Double>();
	Map<String, Double> closed = new HashMap<String, Double>();
	Map<String, Double> directionUnknown = new HashMap<String, Double>();
	Map<String, Double> slow = new HashMap<String, Double>();
	Map<String, Double> fast = new HashMap<String, Double>();
	Map<String, Double> speedUnknown = new HashMap<String, Double>();
	
	private final double defaultPOSSIBLE = 0.7;
	private final double defaultUNKNOWN = 0.3;
	private final String EMPTY = "empty";
	
	
	public Dempster() {
		addHashes();
		//Testzwecke
		getPossibleResults(Direction.OPEN, Speed.FAST);
	}

	//Erstellen der Mengen der einzelnen Möglichkeiten
	private void addHashes()
	{
		open.put(Emotion.ÄRGER.toString(),defaultPOSSIBLE);
		open.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		open.put(Emotion.ÜBERRASCHUNG.toString(),defaultPOSSIBLE);
		
		repeat.put(Emotion.ÄRGER.toString(),defaultPOSSIBLE);
		repeat.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		closed.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		
		directionUnknown.put(Emotion.VERACHTUNG.toString(),defaultUNKNOWN);
		directionUnknown.put(Emotion.EKEL.toString(),defaultUNKNOWN);
		
		
		//Ärger und Freude sollten weniger Plausibel sein: noch nach Lösung suchen
		slow.put(Emotion.TRAUER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.ÄRGER.toString(),defaultPOSSIBLE);
		slow.put(Emotion.FREUDE.toString(),defaultPOSSIBLE);
		
		fast.put(Emotion.ÄRGER.toString(),defaultPOSSIBLE);
		fast.put(Emotion.VERACHTUNG.toString(),defaultPOSSIBLE);
		fast.put(Emotion.EKEL.toString(),defaultPOSSIBLE);
		
		speedUnknown.put(Emotion.ÜBERRASCHUNG.toString(), defaultUNKNOWN);
		
	}
	
	
	public String getPossibleResults(Direction direction,Speed speed)
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
		
		if(possibleDirection != null && possibleSpeed != null)
			result = calculcateEvidence(possibleDirection,possibleSpeed);
		
		return result;
	}

	private String calculcateEvidence(Map<String, Double> possibleDirection, Map<String, Double> possibleSpeed) {
		// TODO Auto-generated method stub
		Map<String,Double> accumulationDirectSpeed = intersectCalculate(possibleDirection, possibleSpeed);
		Map<String,Double> accumulationDirectUnknown = intersectCalculate(possibleDirection, speedUnknown);
		Map<String,Double> accumulationUnknownSpeed = intersectCalculate(directionUnknown, possibleSpeed);
		Map<String,Double> accumulationUnknown = intersectCalculate(directionUnknown, speedUnknown);
		
		double konflikt = 0;
		
		//ToDO Konflikt K
		if(getFirstKeyofHashMap(accumulationDirectSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationDirectSpeed);
			accumulationDirectSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationDirectUnknown).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationDirectUnknown);
			accumulationDirectUnknown.clear();
		}
		if(getFirstKeyofHashMap(accumulationUnknownSpeed).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationUnknownSpeed);
			accumulationUnknownSpeed.clear();
		}
		if(getFirstKeyofHashMap(accumulationUnknown).equals(EMPTY))
		{
			konflikt +=getFirstValueofHashMap(accumulationUnknown);
			accumulationUnknown.clear();
		}
		
		if(Double.compare(konflikt, 0)!= 0)
		{
			konflikt = 1/(1-konflikt);
			
			if(!accumulationDirectSpeed.isEmpty())
				calculateThroughMap(accumulationDirectSpeed,konflikt);
			
			if(!accumulationDirectUnknown.isEmpty())
				calculateThroughMap(accumulationDirectUnknown,konflikt);
			
			if(!accumulationUnknownSpeed.isEmpty())
				calculateThroughMap(accumulationUnknownSpeed,konflikt);
			
			if(!accumulationUnknown.isEmpty())
				calculateThroughMap(accumulationUnknown,konflikt);
			
		}
		
		
		//ToDo Plausibilität
		
		
		return null;
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

