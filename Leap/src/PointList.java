import java.util.LinkedList;
import java.math.*;

public class PointList extends LinkedList<Point> {


	public String getGesture(){

	    //nur x,y ist relevant, z spielt für die Bewegungsunterscheidung keine Rolle

		Double avgDistanceX = 0.0;
		Double avgDistanceY = 0.0;
		StringBuilder strReturn = new StringBuilder();

		//die Koordinaten des ersten Punktes speichern
        double x = getFirst().getX();
        double y = getFirst().getY();

        for(int i = 0; i < this.size()-1; i++){

            //Abstand vom ersten Punkt zu Punkt i bestimmen
            avgDistanceX += Math.abs(get(i).getX()-x);
            avgDistanceY += Math.abs(get(i).getY()-y);

			System.out.println("x: "+get(i).getX()+" y: "+get(i).getY());
		}

		//Durchschnitt des Abstandes berechnen => wenn avgX sehr groß ist, wird es eine rechts/links Bewegung sein
        //ist avgY sehr groß, wird es ein klopfen sein usw..
		avgDistanceX = avgDistanceX/(this.size()-1);
        avgDistanceY = avgDistanceY/(this.size()-1);

        strReturn.append("Bewegung: ");
        if(avgDistanceX < 20 && avgDistanceY > 20){
            strReturn.append("klopfen ");
        }
        else{
            strReturn.append("rechts/links ");

            //Geschwindigkeit abhängig der Anzahl der erkannten Punkte
            if((this.size()-1) < 75){
                strReturn.append("schnell");
            }
            else{
                strReturn.append("langsam");
            }
        }

		return strReturn.toString();

	}

	public void append(PointList list){
        for (Point point:list) {
            this.add(point);
        }
    }
}
