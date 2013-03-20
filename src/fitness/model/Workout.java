package fitness.model;

import java.util.Date;
import java.util.List;

public class Workout {
	private String name = "";
	private int orderingValue = 0;
	private Date dateCreated;
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
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
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
