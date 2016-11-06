package lifehacker;

import javax.swing.JFrame;

public class scrapeLifehacker 
{

	@SuppressWarnings("unchecked")
	public static void main(String[] args) 
	{
		String url = "http://lifehacker.com/";
		
		JFrame frame = new AppFrame(url);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Lifehacker");
		frame.setVisible(true);

	}
	


}
