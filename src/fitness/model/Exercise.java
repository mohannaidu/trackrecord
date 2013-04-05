package fitness.model;


import android.text.format.Time;

public class Exercise {
	private String exercise = "";
	private String sets = "0";
	private String target = "0";
	private String tempo = "";
	private String rest = "0";
	private String orderingValue = "0";
	private String workoutID = "0";
	private String superSetID = "0";
	private Time dateEntered;
	
	public String getExercise() {
		return exercise;
	}
	public void setExercise(String exercise) {
		this.exercise = exercise;
	}
	
	public String getSets() {
		return sets;
	}
	public void setSets(String sets) {
		this.sets = sets;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTempo() {
		return tempo;
	}
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	public String getRest() {
		return rest;
	}
	public void setRest(String rest) {
		this.rest = rest;
	}
	
	public String getOrderingValue() {
		return orderingValue;
	}
	public void setOrderingValue(String orderingValue) {
		this.orderingValue = orderingValue;
	}
	public String getWorkoutID() {
		return workoutID;
	}
	public void setWorkoutID(String workoutID) {
		this.workoutID = workoutID;
	}
	
	public Time getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(Time dateEntered) {
		this.dateEntered = dateEntered;
	}
	public String getSuperSetID() {
		return superSetID;
	}
	public void setSuperSetID(String superSetID) {
		this.superSetID = superSetID;
	}
	
	
		
	
}
