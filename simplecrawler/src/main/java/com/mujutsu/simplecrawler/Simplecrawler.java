package com.mujutsu.simplecrawler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Simplecrawler {

	private static final int NUMBER_OF_DAYS_TO_SHOW_STATISTICS_FOR = 3;
	private static final Double MINIMUM_PRICE = 50000.0;
	private static final Double MAXIMUM_PRICE = 200000.0;

	private static final int PRINT_PAUSE_MILISECONDS = 750;

	// Note: STARTING_PAGE_NUMBER has to be larger than 0, MAX_PAGE_NUMBER has
	// to be larger than STARTING_PAGE_NUMBER.
	// MAX_PAGE_NUMBER is just a failsafe in case the program doesn't stop when
	// detecting that the last page has no entries, but it can be set with a small value
	// for tests as processing 100+ pages can be quite time-consuming.
	private static final int STARTING_PAGE_NUMBER = 1;
	private static final int MAX_PAGE_NUMBER = 1000;

	public static void main(String[] args) {

		HashSet<String> links = null;
		try {
			links = CrawleyController.getPageLinks("http://anuntul.ro/anunturi-imobiliare-vanzari/",
					STARTING_PAGE_NUMBER, MAX_PAGE_NUMBER);
		} catch (InterruptedException e) {
			System.out.println("Error getting page links.");
			e.printStackTrace();
		}

		List<ApartmentEntry> apartmentEntries = CrawleyController.getAllAds(links);

		System.out.println("Printing lists with a small delay for readability...");

		System.out.println("Printing all entries:");
		for (ApartmentEntry apartmentEntry : apartmentEntries) {
			System.out.println(apartmentEntry);
			try {
				TimeUnit.MILLISECONDS.sleep(PRINT_PAUSE_MILISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// price filtering
		List<ApartmentEntry> priceFilteredList = new ArrayList<>();
		for (ApartmentEntry apartmentEntry : apartmentEntries) {
			Double price = apartmentEntry.getPriceInRon();
			if (MINIMUM_PRICE <= price && price <= MAXIMUM_PRICE) {
				priceFilteredList.add(apartmentEntry);
			}
		}

		System.out.println("Printing price filtered entries:");
		for (ApartmentEntry apartmentEntry : priceFilteredList) {
			System.out.println("Price filtered list: " + apartmentEntry);
			try {
				TimeUnit.MILLISECONDS.sleep(PRINT_PAUSE_MILISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// date filtering
		List<ApartmentEntry> dateFilteredList = new ArrayList<>();
		for (ApartmentEntry apartmentEntry : apartmentEntries) {
			ZonedDateTime time = ZonedDateTime.now().minusDays(NUMBER_OF_DAYS_TO_SHOW_STATISTICS_FOR);
			if (apartmentEntry.getTimeOfPosting().getDayOfMonth() > time.getDayOfMonth()) {
				dateFilteredList.add(apartmentEntry);
			}
		}

		System.out.println("Printing date filtered entries:");
		for (ApartmentEntry apartmentEntry : dateFilteredList) {
			System.out.println("Date filtered list: " + apartmentEntry);
			try {
				TimeUnit.MILLISECONDS.sleep(PRINT_PAUSE_MILISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
