//Enum für die möglichen Richtungen einer Bewegung
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
