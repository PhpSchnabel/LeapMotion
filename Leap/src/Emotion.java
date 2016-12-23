
public enum Emotion {
	
	ÄRGER ("Ärger"),
	FREUDE ("Freude"),
	ÜBERRASCHUNG ("Überraschung"),
	VERACHTUNG ("Verachtung"),
	EKEL("Ekel"),
	TRAUER("Trauer");
	
	 private final String name;       

	    private Emotion(String s) {
	        name = s;
	    }

	    public String toString() {
	       return this.name;
	    }
	
}
