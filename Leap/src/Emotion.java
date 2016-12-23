
public enum Emotion {
	
	�RGER ("�rger"),
	FREUDE ("Freude"),
	�BERRASCHUNG ("�berraschung"),
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
