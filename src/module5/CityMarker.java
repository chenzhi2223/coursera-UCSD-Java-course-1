package module5;

import java.awt.Image;
import java.awt.Toolkit;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PApplet;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		//pg.ambientLight(102, 102, 102);
		//pg.ambient(51, 26, 0);
		//Image img; 
		//PImage img = new PImage(Toolkit.Image("city.jpg"));
		//Toolkit.createImage("city.jpg");
		//PApplet pa;
		//img = pa.loadImage("city.jpg");
		//pg.image(img, x-TRI_SIZE/2, y-TRI_SIZE/2, TRI_SIZE,TRI_SIZE);
		//img.PImage("city.jpg");
	    //img.loadImage("city.jpg");
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		
		// TODO: Implement this method
		pg.fill(255,255,255);
		pg.textSize(12);
		pg.ambientLight(51, 102, 126);
		pg.rect(x, y, pg.textWidth(getCity()+" "+getCountry()+" "
				+getPopulation())+6, 30);
		pg.fill(0,0,0);
		//pg.strokeWeight(5);
				
		//pg.text(getCity(), x, y+15);
		pg.text(getCity()+" "+getCountry()+" "
		+getPopulation(), x, y+15);
		//System.out.println(getCity());
	}
	/*public void drawMarker(PGraphics pg, float x, float y)
	{
		
		// TODO: Implement this method
	}*/
	
	
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
