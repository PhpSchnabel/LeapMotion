//Enum f�r die m�glichen Richtungen einer Bewegung
public enum Direction {
	OPEN ("Open"),
    CLOSE ("Close"),
    REPEAT ("Repeat");
		
    private final String name;       

	private Direction(String s) {
		        name = s;
		    }

		    public String toString() {
		       return this.name;
		    }

}
