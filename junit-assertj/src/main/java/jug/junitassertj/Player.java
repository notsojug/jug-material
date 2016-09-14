package jug.junitassertj;

import java.util.List;

public class Player {
	// no getter needed to generate assertion for public fields
	// private fields getters and setters omitted for brevity

	public String name; // Object assertion generated
	private int age; // whole number assertion generated
	private double height; // real number assertion generated
	private boolean retired; // boolean assertion generated
	private List<String> teamMates; // Iterable assertion generated
	public int getAge() {
		return age;
	}
	public double getHeight() {
		return height;
	}
	public boolean isRetired() {
		return retired;
	}
	public List<String> getTeamMates() {
		return teamMates;
	}
	
	
}