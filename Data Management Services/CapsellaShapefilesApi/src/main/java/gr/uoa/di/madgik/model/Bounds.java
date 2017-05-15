package gr.uoa.di.madgik.model;

import java.io.Serializable;

//import gr.cite.geoanalytics.dataaccess.entities.sysconfig.xml.layer.LayerBounds;

public class Bounds implements Serializable
{
	private static final long serialVersionUID = -7772829081416226435L;
	
	private double minx = 0.0;
	private double maxx = 0.0;

	private double miny = 0.0;
	private double maxy = 0.0;

	private String crs = "";

	public Bounds() { }
	
//	public Bounds(LayerBounds bounds) {
//		this.minx = bounds.getMinX();
//		this.miny = bounds.getMinY();
//		this.maxx = bounds.getMaxX();
//		this.maxy = bounds.getMaxY();
//		this.crs  = "EPSG:4326";
//	}
//	
	public Bounds(double minx, double miny, double maxx, double maxy, String crs)
	{
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		this.crs = crs;
	}

	public double getMinx()
	{
		return minx;
	}

	public void setMinx(double minx)
	{
		this.minx = minx;
	}

	public double getMaxx()
	{
		return maxx;
	}

	public void setMaxx(double maxx)
	{
		this.maxx = maxx;
	}

	public double getMiny()
	{
		return miny;
	}

	public void setMiny(double miny)
	{
		this.miny = miny;
	}

	public double getMaxy()
	{
		return maxy;
	}

	public void setMaxy(double maxy)
	{
		this.maxy = maxy;
	}

	public String getCrs()
	{
		return crs;
	}

	public void setCrs(String crs)
	{
		this.crs = crs;
	}

	@Override
	public String toString() {
		return "Bounds [minx=" + minx + ", maxx=" + maxx + ", miny=" + miny + ", maxy=" + maxy + ", crs=" + crs + "]";
	}
	
	
}
