package com.marshall.cafeproject;

public class Table {

	private int tableNumber;
	private int seats;
	private int cafeId;
	private boolean isReservedForOtherUser;
	private boolean isYourReservation;

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int number) {
		this.tableNumber = number;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getCafeId() {
		return cafeId;
	}

	public void setCafeId(int cafeId) {
		this.cafeId = cafeId;
	}

	public boolean isReservedForOtherUser() {
		return isReservedForOtherUser;
	}

	public void setReservedForOtherUser(boolean isReservedForOtherUser) {
		this.isReservedForOtherUser = isReservedForOtherUser;
	}

	public boolean isYourReservation() {
		return isYourReservation;
	}

	public void setYourReservation(boolean isYourReservation) {
		this.isYourReservation = isYourReservation;
	}

}
