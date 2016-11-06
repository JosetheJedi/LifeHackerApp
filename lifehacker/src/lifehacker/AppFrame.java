package lifehacker;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class AppFrame extends JFrame
{
	JPanel mainPanel, titlePanel, buttonPanel, nextBPanel, lastBPanel, nextPBPanel,
				imagePanel, linkBPanel, lastPBPanel;
	JLabel titleLabel, titleLabelText;
	JTextArea outputArea;
	JButton nextButton, lastButton, linkButton, nextPageButton, lastPageButton;
	JScrollPane scroll;
	ActionListener blistener;
	ImageComponent imageDisplay;
	int entryLevel = 0;
	ArrayList<Entry> entries;
	Scraper scrape;
	String url;
	Stack<String> lastUrl;
	
	private String header = "", summary = "", date = "", link = "";
	
	
	@SuppressWarnings("unchecked")
	AppFrame(String url)
	{
		this.url = url;
		lastUrl = new Stack<String>();
		
		loadPage(this.url);
		
		blistener = new ButtonListener();
		imageDisplay = new ImageComponent();
		
		
		createPanel();
		
		createLabels();

		createTextArea();
		getInfo();
		createScrollPane();
		createButton();
		
		add(mainPanel);
	}
	
	@SuppressWarnings("unchecked")
	private void loadPage(String url)
	{
		this.url = url;
		scrape = new Scraper(this.url);
		
		this.entries = (ArrayList<Entry>) scrape.getEntry().clone();
	}
	
	private void getInfo()
	{
		header = entries.get(entryLevel).getHeader();
		summary = entries.get(entryLevel).getSummary();
		date = entries.get(entryLevel).getDatePosted();
		link = entries.get(entryLevel).getLink();
		
		titleLabelText.setText(header);
		outputArea.setText("Article Summary: \n" + 
				summary + "\n\nLink to Article: \n" + link);
		
		imageDisplay.changeImage("image" + entryLevel + ".jpg");
	}
	
	private void createPanel()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		titlePanel = new JPanel();
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5,1));
		nextBPanel = new JPanel();
		lastBPanel = new JPanel();
		linkBPanel = new JPanel();
		nextPBPanel = new JPanel();
		lastPBPanel = new JPanel();
		
		imagePanel = new JPanel(new GridLayout(1,1));
		imagePanel.setBorder(new TitledBorder(new EtchedBorder(), "Image"));
		imagePanel.add(imageDisplay);
		
		mainPanel.add(imagePanel, BorderLayout.CENTER);
	}
	
	private void createLabels()
	{
		titleLabel = new JLabel();
		titleLabelText = new JLabel();
		
		titleLabel.setText("Article Title:");
		titleLabelText.setText(header);
		
		titlePanel.add(titleLabel);
		titlePanel.add(titleLabelText);
		
		mainPanel.add(titlePanel, BorderLayout.NORTH);
	}
	
	private void createTextArea()
	{
		outputArea = new JTextArea(20, 40);
		outputArea.setEditable(false);
		outputArea.setText("Article Summary: \n" + 
					summary + "\n\nLink to Article: \n" + link);
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		
	}
	
	private void createScrollPane()
	{
		scroll = new JScrollPane(outputArea);
		
		mainPanel.add(scroll, BorderLayout.SOUTH);
	}
	
	private void createButton()
	{
		nextButton = new JButton("Next");
		nextButton.addActionListener(blistener);
		
		lastButton = new JButton("Last");
		lastButton.addActionListener(blistener);
		
		linkButton = new JButton("Open");
		linkButton.addActionListener(blistener);
		
		nextPageButton = new JButton("Next Page");
		nextPageButton.addActionListener(blistener);
		
		lastPageButton = new JButton("Last Page");
		lastPageButton.addActionListener(blistener);
		
		nextBPanel.add(nextButton);
		lastBPanel.add(lastButton);
		linkBPanel.add(linkButton);
		nextPBPanel.add(nextPageButton);
		lastPBPanel.add(lastPageButton);
		
		buttonPanel.add(nextBPanel);
		buttonPanel.add(lastBPanel);
		buttonPanel.add(linkBPanel);
		buttonPanel.add(nextPBPanel);
		buttonPanel.add(lastPBPanel);
		
		mainPanel.add(buttonPanel, BorderLayout.EAST);
	}
	
	class ButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getActionCommand().equals("Next"))
			{
				
				if(entryLevel < entries.size()-1)
				{
					entryLevel++;
					
					getInfo();
				}
				else if(entryLevel == entries.size()-1)
				{
					lastUrl.add(url);
					url = scrape.getNextLink();
					scrape.getNextPost(url);
					updateEntries();
					
					entryLevel = 0;
					
					getInfo();
				}
			}
			else if(e.getActionCommand().equals("Last"))
			{
				if(entryLevel > 0)
				{
					entryLevel--;
					
					getInfo();
				}
				else if(entryLevel == 0)
				{
					if(!lastUrl.isEmpty())
					{
						url = lastUrl.pop();
						scrape.getLastPosts(url);
						updateEntries();
						
						entryLevel = 19;
						
						getInfo();
					}
				}
			}
			else if(e.getActionCommand().equals("Open"))
			{
				String url = link;
				
				if(Desktop.isDesktopSupported())
				{
		            Desktop desktop = Desktop.getDesktop();
		            try 
		            {
		                desktop.browse(new URI(url));
		            } 
		            catch (IOException | URISyntaxException ex) 
		            {
		                ex.printStackTrace();
		            }
		        }
				else
				{
		            Runtime runtime = Runtime.getRuntime();
		            try 
		            {
		                runtime.exec("xdg-open " + url);
		            } 
		            catch (IOException ex) 
		            {
		                ex.printStackTrace();
		            }
		        }
			}
			else if(e.getActionCommand().equals("Next Page"))
			{
				lastUrl.add(url);
				url = scrape.getNextLink();
				scrape.getNextPost(url);
				updateEntries();
				
				entryLevel = 0;
				
				getInfo();
			}
			else if(e.getActionCommand().equals("Last Page"))
			{
				if(!lastUrl.isEmpty())
				{
					url = lastUrl.pop();
					scrape.getLastPosts(url);
					updateEntries();
					
					entryLevel = 0;
					
					getInfo();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void updateEntries() 
	{
		this.entries = (ArrayList<Entry>) scrape.getEntry().clone();
	}
}
