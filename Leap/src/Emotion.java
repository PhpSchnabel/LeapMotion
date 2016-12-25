//Enum für die möglichen Emotionen
public enum Emotion {
	
	AERGER ("Ärger"),
	FREUDE ("Freude"),
	ÜBERRASCHUNG ("Überraschung"),
	VERACHTUNG ("Verachtung"),
	EKEL("Ekel"),
	TRAUER("Trauer");
	
	 private final String name; 
	 private double believe;

	    private Emotion(String s) {
	        name = s;
	    }

	    public String toString() {
	       return this.name;
	    }

		public double getBelieve() {
			return believe;
		}

		public void setBelieve(double believe) {
			this.believe = believe;
		}
		
		public void resetBelieve() {
			this.believe = 0;
		}
	    
	    
	
}
