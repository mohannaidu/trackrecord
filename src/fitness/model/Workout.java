package fitness.model;


import java.util.List;

import android.text.format.Time;

public class Workout {
	private String name = "";
	private int orderingValue = 0;
	private Time dateCreated;
	private List<Exercise> exercise;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderingValue() {
		return orderingValue;
	}
	public void setOrderingValue(int orderingValue) {
		this.orderingValue = orderingValue;
	}
	public Time getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Time dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public List<Exercise> getExercise() {
		return exercise;
	}
	
	public void setExercise(Exercise exercise) {
		this.exercise.add(exercise);
	}
	
	public void removeExercise(Exercise exercise) {
		this.exercise.remove(exercise);
	}
	
	public void removeAllExercise() {
		this.exercise.clear();
	}

}
