package pl.piotrstaniow.organizeme.Models;

/**
 * OrganizeMe
 * Author: Piotr Staniow, Zuzanna Gniewaszewska, Slawomir Domagala
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public class Category {
	private String color;
	private String name;
	private long id;

	public Category() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

    public String toString(){
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean equals(Object other) {
        return other.toString().equals(name);
    }
}
