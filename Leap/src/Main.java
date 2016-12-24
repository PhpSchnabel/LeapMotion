import java.util.ArrayList;

public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transformer transformer = new Transformer();
		ArrayList<PointList> arrayList = transformer.readCSV();

		for (PointList list:arrayList) {
			System.out.println(list.getGesture());
		}
		//System.out.println(pointList.getGesture());

	}

}
