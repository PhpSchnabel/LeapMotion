//Enum für die möglichen Geschwindigkeiten einer Bewegung
public enum Speed {
	FAST ("Fast"),
    SLOW ("Slow");
    
    private final String name;       

	private Speed(String s) {
		        name = s;
		    }

		    public String toString() {
		       return this.name;
		    }

}
