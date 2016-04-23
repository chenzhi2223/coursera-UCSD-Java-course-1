package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;
/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		//pg.ambientLight(102, 102, 102);
		//pg.ambient(51, 26, 0);
		/*if (getClicked()) {
			/*for (Marker marker: ) {
			     pg.getScreenPosition(getLocation())	
			}
			float threatRadius = (float) threatCircle();
			pg.ellipse(x,y,threatRadius/1000,threatRadius/1000);
			
		}*/
	}
	

	

}
