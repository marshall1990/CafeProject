package com.marshall.cafeproject;

import java.util.ArrayList;

public final class MenuCollections {

	private static ArrayList<MenuCafe> products = new ArrayList<MenuCafe>();
	private static ArrayList<Table> tables = new ArrayList<Table>();

	public ArrayList<MenuCafe> getProducts() {
		return products;
	}

	public void setProduct(String product, String quantity, String price) {
		MenuCafe menuCafe = new MenuCafe();
		menuCafe.setProduct(product);
		menuCafe.setQuantity(quantity);
		menuCafe.setPrice(price);
		MenuCollections.products.add(menuCafe);
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTable(int number, int seats) {
		Table table = new Table();
		table.setTableNumber(number);
		table.setSeats(seats);
		MenuCollections.tables.add(table);
	}
}
