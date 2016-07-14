package com.drivingassisstantHouse.library.data;

/**
 * 数据传输Bean（继承DTO）
 * 
 * @author  sunji
 * @version 1.0
 * @param <V>
 * @param <K>
 * 
 */
public class DTB<K, V> extends DTO<K, V>{

	private String name;

	private String nameCN;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCN() {
		return nameCN;
	}

	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}
}
