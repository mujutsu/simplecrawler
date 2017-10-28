package com.mujutsu.simplecrawler;

public class Simplecrawler {

	public static void main(String[] args) {
		
		try {
			new CrawleyController().getPageLinks("http://anuntul.ro/anunturi-imobiliare-vanzari/");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
