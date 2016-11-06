package lifehacker;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class scrapeLifehacker 
{

	public static void main(String[] args) throws IOException 
	{
		String url = "http://lifehacker.com/";
		Document doc = Jsoup.connect(url).get();
		int size = 0;
		Elements media = doc.select("article.postlist__item");
		Entry en;
		size = media.size();
		ArrayList<Entry> entries = new ArrayList<Entry>(size);
		

//		System.out.println(media.get(0).select(".headline").text());
//		System.out.println(media.get(0).getElementsByTag("source").attr("data-srcset"));
		
		for(int i = 0; i < size; i++)
		{
			en = new Entry();
			en.setHeader(media.get(i).select(".headline").text());
			en.setSummary(media.get(i).getElementsByTag("p").text());
			en.setDatePosted(media.get(i).select(".updated").text());
			en.setLink(media.get(i).select("h1 a[href]").attr("abs:href"));
			en.setImgURL(media.get(i).getElementsByTag("source").attr("data-srcset"));
			entries.add(en);
			
//			System.out.println(media.get(i).getElementsByTag("source").attr("data-srcset"));
//			System.out.println(entries.get(i).toString() + "\n");
		}
		
		SaveImage(entries);
		
		JFrame frame = new AppFrame(entries);
		
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Lifehacker");
		frame.setVisible(true);

	}
	
	public static void SaveImage(ArrayList<Entry> entries) throws IOException 
	{
		String location = "tmpResources";
		File dir = new File(location);
		try
		{
		    if(dir.mkdir()) 
		    { 
//		    	System.out.println("Directory Created");
		    } 
		    else 
		    {
//		       System.out.println("Directory is not created");
		    }
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		} 
		
		for(int i = 0; i < entries.size(); i++)
		{	
			URL url = new URL(entries.get(i).getImgURL());
			
			
			
			String fileName = "";
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1!=(n=in.read(buf)))
			{
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			fileName = "image" + i;
		
		
			FileOutputStream fos = new FileOutputStream(location + "/"+ fileName + ".jpg");
		
			fos.write(response);
			fos.close();
		}
	}

}
