package com.zebraprintinglibrary.entities;


public class Printer
{
	private String text;
	private int fontSize;
	private int width;
	private int fontName;
	private int x = 0;
	private int y = 0;
	private int inches;
	private String align = "";
	private String macAddress;
	
	public Printer()
	{
		this.inches = 3;
		this.align = "CENTER";
		this.fontName = 0;
		this.fontSize = 2;
		this.x = 20;
		this.y = 0;
		this.width = 575;
	}

	public final String getText()
	{
		return this.text;
	}
	public final void setText(String value)
	{
		this.text = value;
	}

	public final int getFontName()
	{
		return this.fontName;
	}
	public final void setFontName(int value)
	{
		this.fontName = value;
	}

	public final int getFontSize()
	{
		return this.fontSize;
	}
	public final void setFontSize(int value)
	{
		this.fontSize = value;
	}

	public final int getWidth()
	{
		return this.width;
	}
	public final void setWidth(int value)
	{
		this.width = value;
	}

	public final int getX()
	{
		return this.x;
	}
	public final void setX(int value)
	{
		this.x = value;
	}

	public final int getY()
	{
		return this.y;
	}
	public final void setY(int value)
	{
		this.y = value;
	}

	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}

	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public void setInches(int value) {
		this.inches = value;
	}
	public int getInches() {
		return inches;
	}


}

