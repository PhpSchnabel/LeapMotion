
public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transformer transformer = new Transformer();

		PointList pointList = transformer.readCSV();
		System.out.println(pointList.getGesture());

	}

}
