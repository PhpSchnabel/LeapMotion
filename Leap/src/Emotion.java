//Enum f�r die m�glichen Emotionen
public enum Emotion {
	
	AERGER ("�rger"),
	FREUDE ("Freude"),
	�BERRASCHUNG ("�berraschung"),
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
