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

    public boolean equals(Category other) {
        return other.toString().equals(name);
    }
}
