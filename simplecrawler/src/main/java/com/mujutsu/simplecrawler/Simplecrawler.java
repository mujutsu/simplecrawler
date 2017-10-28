package com.mujutsu.simplecrawler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Simplecrawler {

	private static final int NUMBER_OF_DAYS_TO_SHOW_STATISTICS_FOR = 100;
	private static final Double MINIMUM_PRICE = 0.0;
	private static final Double MAXIMUM_PRICE = 1000000000.0;

	public static void main(String[] args) {

		HashSet<String> links = null;
		try {
			links = CrawleyController.getPageLinks("http://anuntul.ro/anunturi-imobiliare-vanzari/");
		} catch (InterruptedException e) {
			System.out.println("Error getting page links.");
			e.printStackTrace();
		}

		List<ApartmentEntry> apartmentEntries = CrawleyController.getAllAds(links);

		for (ApartmentEntry apartmentEntry : apartmentEntries)
			System.out.println(apartmentEntry);

		List<ApartmentEntry> priceFilteredList = new ArrayList<>();

		for (ApartmentEntry apartmentEntry : apartmentEntries) {
			Double price = apartmentEntry.getPriceInRon();
			if (MINIMUM_PRICE <= price && price <= MAXIMUM_PRICE) {
				priceFilteredList.add(apartmentEntry);
			}
		}

		System.out.println("Price filtered list: " + priceFilteredList);

		List<ApartmentEntry> dateFilteredList = new ArrayList<>();

		for (ApartmentEntry apartmentEntry : apartmentEntries) {
			ZonedDateTime time = ZonedDateTime.now().minusDays(NUMBER_OF_DAYS_TO_SHOW_STATISTICS_FOR);
			if (apartmentEntry.getTimeOfPosting().getDayOfMonth() > time.getDayOfMonth()) {
				dateFilteredList.add(apartmentEntry);
			}
		}

		System.out.println("Date filtered list: " + dateFilteredList);

	}

	static class DateComparator implements Comparator<ApartmentEntry> {
		public int compare(ApartmentEntry a1, ApartmentEntry a2) {
			return a1.getTimeOfPosting().compareTo(a2.getTimeOfPosting());
		}
	}

	static class PriceComparator implements Comparator<ApartmentEntry> {
		public int compare(ApartmentEntry a1, ApartmentEntry a2) {
			return a1.getPriceInRon().compareTo(a2.getPriceInRon());
		}
	}

}
