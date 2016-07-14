package com.drivingassisstantHouse.library.data.message;


import com.drivingassisstantHouse.library.data.DTB;

/**
 * 消息实体Bean
 * @author sunji
 * @version 1.0
 *
 */
public class Message extends DTB {

	private static final long serialVersionUID = 7491152915368949244L;
	
	/**
	 * 消息ID
	 */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
