package lifehacker;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Scraper 
{
	private Document doc;
	private int size;
	private Elements media;
	private ArrayList<Entry> entries;
	private String nextPage = "";
	private String baseURL = "";
	private String url = "";
	private Elements links;
	
	Scraper(String url)
	{
		this.baseURL = url;
		this.url = baseURL;
		getPosts(baseURL);
	}
	
	public ArrayList<Entry> getEntry()
	{
		return entries;
	}
	
	public String getNextLink()
	{
		url = baseURL + nextPage;
		//System.out.println(url);
		return url;
	}
	
	public void getNextPost(String url)
	{
		getPosts(url);
	}
	
	public void getLastPosts(String url)
	{
		this.url = url;
		getPosts(url);
	}
	
	public String getUrl()
	{
		return url;
	}
	
	private void getPosts(String url)
	{
		try
		{	
			doc = Jsoup.connect(url).get();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		media = doc.select("article.postlist__item");
		Entry en;
		size = media.size();
		entries = new ArrayList<Entry>(size);
		
		
		// to get the next lifehacker page
		links = doc.select("div.load-more__button");
		
		nextPage = links.select("a").attr("href");
		//System.out.println(nextPage);
		
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
	}
	
	
	
	public void SaveImage(ArrayList<Entry> entries) 
	{
		try
		{
			String location = "tmpResources";
			File dir = new File(location);
			try
			{
			    if(dir.mkdir()) 
			    { 
//			    	System.out.println("Directory Created");
			    } 
			    else 
			    {
//			       System.out.println("Directory is not created");
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
		catch(IOException e)
		{
			
		}
	}
}
