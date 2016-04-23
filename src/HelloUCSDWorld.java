import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Hello World!
 * 
 * This is the basic stub to start creating interactive maps.
 */
public class HelloUCSDWorld extends PApplet {

	UnfoldingMap map;
	UnfoldingMap map2;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new Google.GoogleTerrainProvider());
		map.zoomAndPanTo(14, new Location(32.881, -117.238)); // UCSD

		MapUtils.createDefaultEventDispatcher(this, map);
		//second map with the default place at U of Waterloo. 
		map2 = new UnfoldingMap(this, new Google.GoogleTerrainProvider());
		map2.zoomAndPanTo(14, new Location(43.4689, -80.5400)); // UCSD

		MapUtils.createDefaultEventDispatcher(this, map2);
	}

	public void draw() {
		background(0);
		map.draw();
		background(GRAY);
		map2.draw();
	}

}
