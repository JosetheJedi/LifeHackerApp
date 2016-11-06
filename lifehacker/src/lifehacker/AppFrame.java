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
	JPanel mainPanel, titlePanel, buttonPanel, nextBPanel, lastBPanel,
				imagePanel, linkBPanel;
	JLabel titleLabel, titleLabelText;
	JTextArea outputArea;
	JButton nextButton, lastButton, linkButton;
	JScrollPane scroll;
	ActionListener blistener;
	ImageComponent imageDisplay;
	int entryLevel = 0;
	ArrayList<Entry> entries;
	
	private String header = "", summary = "", date = "", link = "";
	
	AppFrame()
	{
		blistener = new ButtonListener();
		createPanel();
		createLabels();
		createTextArea();
		createButton();
		
		add(mainPanel);
	}
	
	AppFrame(ArrayList<Entry> entries)
	{
		this.entries = (ArrayList<Entry>) entries.clone();
		blistener = new ButtonListener();
		imageDisplay = new ImageComponent();
		
		header = entries.get(0).getHeader();
		summary = entries.get(0).getSummary();
		date = entries.get(0).getDatePosted();
		link = entries.get(0).getLink();
		
		createPanel();
		
		createLabels();
		createTextArea();
		createScrollPane();
		createButton();
		
		add(mainPanel);
	}
	
	private void createPanel()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		titlePanel = new JPanel();
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,1));
		nextBPanel = new JPanel();
		lastBPanel = new JPanel();
		linkBPanel = new JPanel();
		
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
		
		nextBPanel.add(nextButton);
		lastBPanel.add(lastButton);
		linkBPanel.add(linkButton);
		
		buttonPanel.add(nextBPanel);
		buttonPanel.add(lastBPanel);
		buttonPanel.add(linkBPanel);
		
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
					header = entries.get(entryLevel).getHeader();
					summary = entries.get(entryLevel).getSummary();
					date = entries.get(entryLevel).getDatePosted();
					link = entries.get(entryLevel).getLink();

					titleLabelText.setText(header);
					outputArea.setText("Article Summary: \n" + 
							summary + "\n\nLink to Article: \n" + link);
					
					imageDisplay.changeImage("image" + entryLevel + ".jpg");
				}
			}
			else if(e.getActionCommand().equals("Last"))
			{
				if(entryLevel > 0)
				{
					entryLevel--;
					header = entries.get(entryLevel).getHeader();
					summary = entries.get(entryLevel).getSummary();
					date = entries.get(entryLevel).getDatePosted();
					link = entries.get(entryLevel).getLink();

					titleLabelText.setText(header);
					outputArea.setText("Article Summary: \n" + 
							summary + "\n\nLink to Article: \n" + link);
					
					imageDisplay.changeImage("image" + entryLevel + ".jpg");
				}
			}
			else if(e.getActionCommand().equals("Open"))
			{
				String url = link;
				
				//System.out.println(Desktop.isDesktopSupported());
				
				if(Desktop.isDesktopSupported())
				{
		            Desktop desktop = Desktop.getDesktop();
		            try 
		            {
		                desktop.browse(new URI(url));
		            } 
		            catch (IOException | URISyntaxException ex) 
		            {
		                // TODO Auto-generated catch block
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
		                // TODO Auto-generated catch block
		                ex.printStackTrace();
		            }
		        }
			}
			
		}
		
	}
}
