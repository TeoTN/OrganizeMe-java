package pl.piotrstaniow.organizeme;

/**
 * Created by slawomir on 20.05.15.
 */
public class Category {
	private String color;
	private long ID;
	private String name;

	public Category(String name, String color, long ID) {
		this.name = name;
		this.color = color;
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
