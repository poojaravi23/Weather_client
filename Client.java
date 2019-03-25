/*URL taken from:
 https://graphical.weather.gov/xml/rest.php
*http connection: 
 http://www.baeldung.com/java-http-request
*xml parsing:
 https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
*weather icon:
 https://www.mkyong.com/java/how-to-read-an-image-from-file-or-url/
*/

package weather;
/**
 * @author Pooja Ravi(1001578517)
 * @description  Implementation of a client application that will connect to the National Weather Service website using HTTP and XML 
 *               and display current weather conditions.The client process will connect to the server over a socket connection and 
 *               request weather information for a certain location. The National Weather Service specifies the location using latitude 
 *               and longitude instead of zip code. The coordinates are entered into the client program and current update is obtained 
                 for that location.The following variables are displayed based on the latitude and longitude values entered: Maximum 
                 Temperature,Minimum Temperature,Dew point Temperature,Apparent Temperature,12 Hour Probability of Precipitation,
                 Wind Speed,Wind Direction, Wave Height and Weather Icon.
*/
import java.awt.Button;
import java.awt.TextArea;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.net.*; 
import java.io.*; 
import java.util.*;
import java.net.InetAddress;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Client extends JFrame
implements WindowListener {                                          //an interface for receiving window events
	


	private JTextField longitude;
	private JTextField latitude;
	/**Constructor of the class for creating the UI of the Weather Report*/
	 Client()
	 {
		 setFont(new Font("Bodoni MT Black", Font.BOLD, 15));        //Creating UI for weather report
		setTitle("WEATHER CLIENT");
		 this.addWindowListener(this);
		 this.setSize(566,442);
		 this.setResizable(true);
		 getContentPane().setLayout(null);
		 
		 JLabel lblNewLabel = new JLabel("Longitude");               //label to enter Longitude value
		 lblNewLabel.setBounds(22, 0, 72, 24);
		 getContentPane().add(lblNewLabel);
		 
		 longitude = new JTextField();                               
		 longitude.setBounds(22, 24, 80, 37);
		 getContentPane().add(longitude);
		 longitude.setColumns(50);
		 
		 JLabel lblNewLabel_1 = new JLabel("Latitude");              //label to enter Latitude value
		 lblNewLabel_1.setBounds(175, 0, 72, 24);
		 getContentPane().add(lblNewLabel_1);
		 
		 latitude = new JTextField();
		 latitude.setBounds(175, 24, 80, 37);
		 getContentPane().add(latitude);
		 latitude.setColumns(50);
		 
		 JButton btnNewButton = new JButton("Search");                //search button to submit the entered values
		 btnNewButton.setBounds(315, 26, 80, 32);
		 getContentPane().add(btnNewButton);
		 JLabel label=new JLabel();
		 label.setLocation(22, 332);
		 label.setSize(50, 50);
		 getContentPane().add(label);
		 
		 JTextArea Report = new JTextArea();
		 Report.setForeground(Color.BLACK);
		 Report.setFont(new Font("Sitka Small", Font.BOLD, 15));
		 Report.setBounds(0, 112, 544, 205);
		 getContentPane().add(Report);
		 
		 JLabel lblWeatherReport = new JLabel("  WEATHER REPORT");   //area where the output variables are displayed
		 lblWeatherReport.setBounds(206, 79, 140, 24);
		 getContentPane().add(lblWeatherReport);
		 this.setVisible(true);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 btnNewButton.addActionListener(new ActionListener()         //The actionlistner is used to call the user defined method for sending messages to the server		                                                 
				 {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 try {
					 Report.setText("");                        //clear previous output when entering new values of inputs
				    URL url = new URL("https://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php?whichClient=GmlLatLonList&gmlListLatLon="+latitude.getText()+"%2C"+longitude.getText()+"&featureType=Forecast_Gml2Point&product=time-series&begin=2004-01-01T00%3A00%3A00&end=2021-12-02T00%3A00%3A00&Unit=e&Submit=Submit");
				    HttpURLConnection con = (HttpURLConnection) url.openConnection();         //using http connection
				    con.setRequestMethod("GET");
					
				    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //reading the document instanced from XML file
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(url.openStream());
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("gml:featureMember");         //Parsing the data from XML file and this is the root name of the file
					
					for (int temp = 0; temp < nList.getLength(); temp++) {

						Node nNode = nList.item(temp);
						
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {                    //accessing the data from url and displaying it using the instances of XML file

							Element eElement = (Element) nNode;
							Report.append("Maximum Temperature:"+eElement.getElementsByTagName("app:maximumTemperature").item(0).getTextContent()+"\n"); //parsing maximum temperature from XML file
							Report.append("Minimum Temperature:"+eElement.getElementsByTagName("app:minimumTemperature").item(0).getTextContent()+"\n"); //parsing minimum temperature from XML file
							Report.append("Dewpoint Temperature:"+eElement.getElementsByTagName("app:dewpointTemperature").item(0).getTextContent()+"\n"); //parsing Dew point temperature from XML file
							Report.append("12 Hour Probability of Precipitation:"+eElement.getElementsByTagName("app:probOfPrecip12hourly").item(0).getTextContent()+"\n"); //parsing 12 Hour Probability of Precipitation from XML file
							Report.append("Apparent Temperature:"+eElement.getElementsByTagName("app:apparentTemperature").item(0).getTextContent()+"\n"); //parsing Apparent temperature from XML file
							Report.append("Wind Speed:"+eElement.getElementsByTagName("app:windSpeed").item(0).getTextContent()+"\n");             //parsing Wind Speed from XML file
							Report.append("Wind Direction:"+eElement.getElementsByTagName("app:windDirection").item(0).getTextContent()+"\n");     //parsing Wind Direction from XML file
							Report.append("Wave Height:"+eElement.getElementsByTagName("app:waveHeight").item(0).getTextContent()+"\n");           //parsing Wave height from XML file
							Report.append("Weather Icon:"+eElement.getElementsByTagName("app:weatherIcon").item(0).getTextContent()+"\n");         //parsing Weather Icon from XML file
							Image image = null;
							URL iurl = new URL(eElement.getElementsByTagName("app:weatherIcon").item(0).getTextContent()); //url parsing to display weather icon
				            image = ImageIO.read(iurl);                                                                    //read image from the url
				            label.setIcon(new ImageIcon(image));
					    }
						
						con.disconnect();        // end the session after fetching the data
					}
					}
				    
				 catch (Exception ae) 
				 {
					ae.printStackTrace();
				 }
				    /**
					 * When called, close all the I/O and Sockets opened. Prevent creation of orphans and hence prevent
					 * memory leak.
					 */	
			}
		 });

	 }
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		}

  public static void main(String[] args) {
   new Client();
  }
}