import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class Trip{
	List<Integer> certificate;
	int length;
	ArrayList<String> nodes;
	
	
	Trip(List<Integer> c, int l, ArrayList<String> n){
		certificate = new ArrayList<Integer>(c);
		length = l;
		nodes = n;
		
	}
	@Override
	public String toString() {
		return "Trip [distances=" + certificate + ", length=" + length + "]";
	}
	
	public String certificateKey(){
		ArrayList<String> cityOrder = new ArrayList<String>();
		for(int i = 0; i < certificate.size(); i++) {
			
			int q = certificate.get(i);
			String n = nodes.get(q);
			cityOrder.add(n);
			
		}
		cityOrder.add(nodes.get(0));
	    String lengthSting = Integer.toString(length);
		String answer = cityOrder+" "+ lengthSting;
		return answer;
		
	}

}