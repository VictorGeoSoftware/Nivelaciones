package com.victor.nuevo.nivelaciones;

public class FoundedPointsModel {
	private String project;
	private String pointName;
	private String x;
	private String y;
	private String z;
	
	public FoundedPointsModel(String project, String pointName, String x, String y, String z){
		this.project = project;
		this.pointName = pointName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}
}
