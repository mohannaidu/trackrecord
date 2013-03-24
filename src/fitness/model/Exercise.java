package fitness.model;

import java.util.Date;

import android.text.format.Time;

public class Exercise {
	private String exercise = "";
	private String weight = "0.0";
	private String sets = "0";
	private String reps = "0";
	private String target = "0";
	private String tempo = "";
	private String rest = "0";
	private String orderingValue = "0";
	private String workoutID = "0";
	private Time dateEntered;
	
	public String getExercise() {
		return exercise;
	}
	public void setExercise(String exercise) {
		this.exercise = exercise;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSets() {
		return sets;
	}
	public void setSets(String sets) {
		this.sets = sets;
	}
	public String getReps() {
		return reps;
	}
	public void setReps(String reps) {
		this.reps = reps;
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
	
		
	
}
