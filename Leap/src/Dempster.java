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
		
		//ToDO Konflikt K
		
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
			 Map.Entry<String,Double> entry = mapOne.entrySet().iterator().next();
			 String key= entry.getKey();
			 Double valueOne = mapOne.get(key);
			 
			 Map.Entry<String,Double> entryTwo =mapTwo.entrySet().iterator().next();
			 String keyTwo = entryTwo.getKey();
			 Double valueTwo = mapTwo.get(keyTwo);
			 
			result.put(EMPTY, valueOne*valueTwo);
		}
		
		return result;
	}
	
}

