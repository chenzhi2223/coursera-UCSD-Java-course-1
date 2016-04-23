package module5;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
//import controlP5.*;
/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	private PGraphics buffer;
	private String inputData;
	private boolean controlSignal;
	int controlCount;
	float depth;
	//private 
	//private ControlP5 P5;
	//private String input="";
	//private Textfield tf;
	//private boolean cityClicked; 
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		//buffer = new PGraphics();
		//buffer.init();
		//buffer = createGraphics(900,700,OPENGL);
		//buffer.size(900, 700, OPENGL);
		buffer = createGraphics(900,700,OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		//MapUtils.createDefaultEventDispatcher(buffer, map);
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    buffer = this.g;
	    inputData = "";
	    controlSignal = false;
	    controlCount = 0;
	    depth = 0;
	    //P5 = new ControlP5(this);
	    //P5.addTextfield("test");
	    // 
	    //tf = new Textfield(P5,"test");
	}  // End setup
	
	
	public void draw() {
		background(0);
		//map.draw();
		//
		buffer.beginDraw();
		//map.
		
		map.draw();
		addKey();
		/*test of ControlP5;
		 * 
		 */		
		addMarker();
		control();
		//control of canvas.
		
		//buffer.
		buffer.endDraw();
		image(buffer,0,0);
		buffer.beginDraw();
		plotCityThreated();
		showTitle(cityMarkers,quakeMarkers);
		fill(color(0,0,0));
		translate(width/4, height/4);
		rotate((float)(PI/3.0));
		textSize(30);
		text("Jack@Cathy",200,100);		
		//showTitle(quakeMarkers);
		textSize(10);
		//control();
		buffer.endDraw();
		image(buffer,0,0);		
	}
	
	
	private void control() {
		//simple GUI control implemented.
		fill(255,0,0);
		/* for ControlP5
		 * tf.setPosition(20,620);
		tf.setSize(50,20);
		tf.setFocus(true);
		tf.setVisible(true);
		text(tf.getText(),50,620);
		*/
		//textSize(20);
		text("Depth",10,520);
		strokeWeight(3);
		rect(50,515,100,20,10);
		fill(255,255,0);
		strokeWeight(2);
		ellipse(100,550,20,20);
		//text(getText(),55,620);
		getText();
		//if (inputData!="") 
		textfieldEvent();
	}
	private void getText() {
		
		//int count = 0;
	    //if (mouseX)	
		  if (keyPressed) {
		  //while (inputData.length()<100 ) {
		    	//if (keyPressed) {
		       if(key!=ENTER && key!=DELETE && key!=BACKSPACE)	{   
		    	 inputData += Character.toString(key);
		     	 keyPressed = false;
			   }
		       if (key==DELETE || key == BACKSPACE) {
		    	   int size = inputData.length(); 
		    	   if (size>0) 
		    		   inputData = inputData.substring(0,size-1);
		    	   keyPressed = false;
		       }
		       keyPressed=false;
		  }
			//count++;
	    //System.out.println(inputData);
		fill(255,255,0);
		text(inputData,55,520);
		    //}
     }
	private void textfieldEvent() {	
		//int count=0;
		//float depth = 0;
		if (key==ENTER || ( mousePressed==true && 90 < mouseX && mouseX<110 && 540<mouseY && mouseY<560) ) {
			controlSignal = true;
			//mousePressed = false;
			//keyPressed = false;
		}
		else controlSignal = false;
		//String mousePressedIndicator="false";
		if (inputData.length()>0 && !inputData.equals("-")) {
		   depth = Float.parseFloat(inputData);		   
		   if (controlSignal==true) {
			   controlCount = 0;
			  for (Marker marker: quakeMarkers) {
			    EarthquakeMarker quake = (EarthquakeMarker) marker;
			    if (quake.getDepth()<depth) {
			    	quake.setHidden(true);
			    	controlCount++;
			    }
			    else quake.setHidden(false);
		 	  }
			  //controlSignal=true;
		   }
		}
		//else controlSignal = false;
		//if (mousePressed==true) mousePressedIndicator="true";
		if (depth>=0) {
			  fill(255,100,100);
			  text("number of quakes with depth deeper than : " + depth +"miles: " + (quakeMarkers.size()-controlCount),30,600);
		}
		else {
			fill(255,100,100);
			text("depth cannot be negative! Please enter a valid number!",30,600);
		}
		//return inputData;
		//System.out.println("number of quakes: " + quakeMarkers.size());
		//System.out.println("number of visible quakes: " + (quakeMarkers.size()-count));
		fill(0,255,255);
		text("number of quakes: " + quakeMarkers.size(),30,580);
		
		//if (mousePressed==true) mousePressedIndicator="true";
		//else mousePressedIndicator="false";
		/*if (depth>0) {
			  fill(255,100,100);
			  text("number of quakes with depth deeper than : " + depth +"miles: ",30,600);
		}*/
	}
	
	
	//use of showTitle method of each marker class
	private void showTitle(List<Marker> markers1, List<Marker> markers2) {
		for (Marker marker: markers1) {
			CityMarker city = (CityMarker) marker;
			if (city.isSelected()) {
				ScreenPosition pos = map.getScreenPosition(marker.getLocation());  
			      city.showTitle(this.g, pos.x, pos.y);  // You will implement this in the subclasses
		    }
		}
		for (Marker marker: markers2) {
			EarthquakeMarker quake = (EarthquakeMarker) marker;
			if (quake.isSelected()) {
				ScreenPosition pos = map.getScreenPosition(marker.getLocation());  
			      quake.showTitle(this.g, pos.x, pos.y);  // You will implement this in the subclasses
		    }
		}
	}
	
	
	@Override
	public void keyPressed() {
		//filter based on keyboard interactions.
		if (key=='h') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float magnitude = earthquake.getMagnitude();
				if (magnitude<5) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='m') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float magnitude = earthquake.getMagnitude();
				if (magnitude>5 || magnitude<4) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='l') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float magnitude = earthquake.getMagnitude();
				if (magnitude>4) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='d') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float depth = earthquake.getDepth();
				if (depth<300) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='i') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float depth = earthquake.getDepth();
				if (depth>300 || depth<70) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='s') {
			for (Marker marker: quakeMarkers) {
				EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				float depth = earthquake.getDepth();
				if (depth>70) marker.setHidden(true);
				else marker.setHidden(false);
			}
		}
		if (key=='a') {
			for (Marker marker: quakeMarkers) {
				//EarthquakeMarker earthquake = (EarthquakeMarker) marker;
				//float depth = earthquake.getDepth();
				//if (depth>70) marker.setHidden(true);
				//else 
					marker.setHidden(false);
			}
		}
	}
	
	/**load images for each kind of marker selected.
	 * 
	 */
	private void addMarker() {
		//if () {
		for (Marker marker: cityMarkers) {
			if (marker.isInside(map,mouseX,mouseY)) {
			  PImage img = loadImage("city.jpg");
			  ScreenPosition pos = map.getScreenPosition(marker.getLocation());
			//marker.get
			  image(img,pos.x-30,pos.y-30,60,60);
			}
		}
		for (Marker marker: quakeMarkers) {
			if (marker.isInside(map,mouseX,mouseY)) {
			    EarthquakeMarker earthMarker = (EarthquakeMarker) marker;
			    PImage img;
			    if (earthMarker.isOnLand()) {
			       img = loadImage("land.jpg");
			    }
			    else {
				    img = loadImage("ocean.jpg");
			    }
		    	ScreenPosition pos = map.getScreenPosition(marker.getLocation());
			    //marker.get
		    	image(img,pos.x-30,pos.y-30,60,60);
			}
		}
	}
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
		
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// TODO: Implement this method
		if (lastSelected==null){
		  for (Marker marker: markers) {
			if(marker.isInside(map, mouseX, mouseY)==true) {
				
				lastSelected = (CommonMarker) marker;
				if(lastSelected.isSelected()==false) 
				{   
				    lastSelected.setSelected(true);				    
				}
				break;	
			}
		  }
		}
	}
	private void selectMarkerIfClicked(List<Marker> markers)
	{
		// TODO: Implement this method
		if (lastClicked==null){
		  for (Marker marker: markers) {
			if(marker.isInside(map, mouseX, mouseY)==true) {
				
				lastClicked = (CommonMarker) marker;
				if(lastClicked.isSelected()==false) 
				{   
				    lastClicked.setSelected(true);
				    lastClicked.setClicked(true);				    
				}
				
				break;	
			}
		  }
		}
	}
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		// TODO: Implement this method
		// Hint: You probably want a helper method or two to keep this code
		// from getting too long/disorganized
		if (lastClicked != null) {
			lastClicked.setSelected(false);
			lastClicked.setClicked(false);
			lastClicked = null;
			unhideMarkers();		
		}
		else {
			selectMarkerIfClicked(quakeMarkers);
			selectMarkerIfClicked(cityMarkers);
			if (lastClicked !=null) {
				lastClicked.setSelected(true);
				lastClicked.setClicked(true);
				hideUnThreatMarkers();				
			}
		}
	}
	private void hideUnThreatMarkers() {
		//EarthquakeMarker selected = new EarthquakeMarker(lastClicked.getLocation());
		//double radius = selected.threatCircle();
		boolean cityClicked=false;
		for (Marker marker: cityMarkers) {
			if (lastClicked.getDistanceTo(marker.getLocation())<=0.0001) 
				{ 
				    cityClicked=true;
				    break;
				}
		}
		if (cityClicked==true) {
		   hideMarkers(cityMarkers);
		   for (Marker marker: quakeMarkers) {
			   EarthquakeMarker earthmarker = (EarthquakeMarker) marker;
			   double radius = earthmarker.threatCircle();
		       if (lastClicked.getDistanceTo(marker.getLocation())>radius) {
		    	  marker.setHidden(true);		    	                
		       }
		       else marker.setHidden(false);
		   }		   
		}
		else {
			hideMarkers(quakeMarkers);
			EarthquakeMarker earthmarker = (EarthquakeMarker) lastClicked;
			double radius = earthmarker.threatCircle();
			System.out.println("threat radius is: " +radius);
			for (Marker marker: cityMarkers) {
				 double disc = lastClicked.getDistanceTo(marker.getLocation());
				 //System.out.println("distance is: " + disc);
				 if (disc>radius) {
			    	  marker.setHidden(true);		    	                
			     }
			     else marker.setHidden(false);
			}
			if (earthmarker.isOnLand()==false) {
				//ocean marker is clicked on and lines 
				//between it and all cities within threat 
				//range will be shown. 
				 System.out.println("threat radius is: " + radius);
				
				for (Marker marker: cityMarkers) {
					 double disc = lastClicked.getDistanceTo(marker.getLocation());
					 //System.out.println("distance is: " + disc);
					 if (disc<radius) {
						// ScreenPosition ocean = map.getScreenPosition(lastClicked.getLocation());
						// ScreenPosition city = map.getScreenPosition(marker.getLocation());
						 //double[] codinate = 
				    	 //line(ocean.x,ocean.y, city.x,city.y);		    	                
						 System.out.println("distance is: " + disc);
							
					 }
				     //else marker.setHidden(false);
				}
			}
		}
		lastClicked.setHidden(false);
	}
	private void plotCityThreated() {
		boolean cityClicked=false;
		if (lastClicked!=null) {
		   for (Marker marker: cityMarkers) {
		    	if ( lastClicked.getDistanceTo(marker.getLocation())<=0.0001) 
			  	{ 
				    cityClicked=true;
				    break;
				}
		    }
		    if (!cityClicked ) {
			EarthquakeMarker earthmarker = (EarthquakeMarker) lastClicked;
		    if (earthmarker.isOnLand()==false) {
			//ocean marker is clicked on and lines 
			//between it and all cities within threat 
			//range will be shown. 
		      
		      double radius = earthmarker.threatCircle();
		      double exactDisc = lastClicked.getDistanceTo(cityMarkers.get(0).getLocation());
		      ScreenPosition pos1 = map.getScreenPosition(lastClicked.getLocation());
		      ScreenPosition pos2 = map.getScreenPosition(cityMarkers.get(0).getLocation());
		      double mapDisc = Math.sqrt(Math.pow(pos1.x-pos2.x,2)+Math.pow(pos1.y-pos2.y,2));
		      double proRadius = radius * mapDisc/exactDisc;
		      ellipse(pos1.x,pos1.y,(float)proRadius,(float)proRadius);
		      //System.out.println("threat radius is: " + radius);
		      for (Marker marker: cityMarkers) {
				 double disc = lastClicked.getDistanceTo(marker.getLocation());
				 //System.out.println("distance is: " + disc);
				 if (disc<radius) {
					 //System.out.println("distance is: " + disc);
					 ScreenPosition ocean = map.getScreenPosition(lastClicked.getLocation());
					 ScreenPosition city = map.getScreenPosition(marker.getLocation());
					 //double[] codinate = 
			    	 line(ocean.x,ocean.y, city.x,city.y);		    	                
			     }
				 //else System.out.println("distance is: " + disc);
			     //else marker.setHidden(false);
			 }
		    }
		  }
		}
	}
	private void hideMarkers(List<Marker> markers) {
		for(Marker marker : markers) {
			marker.setHidden(true);
		}
	}
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
			
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
