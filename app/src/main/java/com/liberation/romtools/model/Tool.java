package com.liberation.romtools.model;

/**
 * Created by Liberation on 2017/11/9.
 */

public class Tool {
	private String name;

	public Tool(String name) {
		this.name = name;
	}

	private boolean isChecked;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
