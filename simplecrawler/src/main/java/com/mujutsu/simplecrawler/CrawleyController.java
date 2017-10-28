package com.mujutsu.simplecrawler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawleyController {

	static final double EURO_VALUE = 4.5;

	static final String DECOMANDAT = "decomandat";
	static final String SEMIDECOMANDAT = "semidecomandat";
	static final String CIRCULAR = "circular";

	static final String MAIN_LINK = "http://anuntul.ro/anunturi-imobiliare-vanzari/";
	static final String PAGE_SUFFIX = "?page=";

	static final String GARSONIERE = "garsoniere/";
	static final String DOUA_CAMERE = "apartamente-2-camere/";
	static final String TREI_CAMERE = "apartamente-3-camere/";
	static final String PATRU_CAMERE = "apartamente-4-camere/";
	static final String CASE_VILE = "case-vile/";
	static final String TERENURI = "terenuri/";
	static final String SPATII_COMERCIALE_INDUSTRIALE = "spatii-comerciale-industriale/";
	static final String CUMPARARI_SCHIMBURI = "cumparari-schimburi/";

	static final Map<String, Month> monthEquivalenceMap = new HashMap<>();

	static {
		monthEquivalenceMap.put("ian", Month.JANUARY);
		monthEquivalenceMap.put("feb", Month.FEBRUARY);
		monthEquivalenceMap.put("mar", Month.MARCH);
		monthEquivalenceMap.put("apr", Month.APRIL);
		monthEquivalenceMap.put("mai", Month.MAY);
		monthEquivalenceMap.put("iun", Month.JUNE);
		monthEquivalenceMap.put("iul", Month.JULY);
		monthEquivalenceMap.put("aug", Month.AUGUST);
		monthEquivalenceMap.put("sep", Month.SEPTEMBER);
		monthEquivalenceMap.put("oct", Month.OCTOBER);
		monthEquivalenceMap.put("noi", Month.NOVEMBER);
		monthEquivalenceMap.put("dec", Month.DECEMBER);
	}

	static final List<String> apartmentTypes = new ArrayList<>(Arrays.asList(GARSONIERE, DOUA_CAMERE, TREI_CAMERE,
			PATRU_CAMERE, CASE_VILE, TERENURI, SPATII_COMERCIALE_INDUSTRIALE, CUMPARARI_SCHIMBURI));

	public CrawleyController() {

	}

	public static HashSet<String> getPageLinks(String URL, int minPageNumber, int maxPageNumber) throws InterruptedException {

		HashSet<String> links = new HashSet<String>();

		List<String> tempLinks = new ArrayList<>();

		for (Integer i = minPageNumber; i < maxPageNumber; i++) {

			System.out.println("Processing page: " + i);

			String pageUrl = URL + PAGE_SUFFIX + i.toString();
			System.out.println("Attempting to get links from page: " + pageUrl);

			try {
				Document document = Jsoup.connect(pageUrl).get();
				Elements linksOnPage = document.select("a[href]");

				tempLinks.clear();

				for (Element page : linksOnPage) {
					String tempPage = page.attr("abs:href");
					if (isApartmentSaleLink(tempPage)) {
						tempLinks.add(tempPage);
					}
				}

				if (tempLinks.isEmpty()) {
					System.out.println("Page contains no valid links, assuming it's last page and stopping.");
					break;
				} else {
					System.out.println("Adding " + tempLinks.size() + " records from page " + i + " to records list.");
					for (String apartment : tempLinks) {
						links.add(apartment);
						System.out.println("Added apartment link to list: " + apartment);
					}
				}

			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
		return links;
	}

	public static List<ApartmentEntry> getAllAds(HashSet<String> linksList) {

		List<ApartmentEntry> apartmentEntries = new ArrayList<>();

		int counter = 0;
		int numberOfEntries = linksList.size();

		for (String link : linksList) {

			counter++;

			System.out.println("Processing record " + counter + " out of " + numberOfEntries + ".");

			Document document = null;
			try {
				document = Jsoup.connect(link).get();
			} catch (IOException e) {
				System.err.println("For '" + link + "': " + e.getMessage());
				continue;
			}

			Double price = getPriceFromDocument(document);
			String type = getTypeFromDocument(document);
			ZonedDateTime dateTime = getTimeFromDocument(document);
			Set<String> imagesCollection = getImageLinksFromDocument(document);

			ApartmentEntry apartmentEntry = new ApartmentEntry();
			apartmentEntry.setLink(link);
			apartmentEntry.setPriceInRon(price);
			apartmentEntry.setType(type);
			apartmentEntry.setTimeOfPosting(dateTime);
			apartmentEntry.setImagesCollection(imagesCollection);

			apartmentEntries.add(apartmentEntry);

			System.out.println("Entry added to list: " + apartmentEntry);
		}

		return apartmentEntries;
	}

	private static Boolean isApartmentSaleLink(String URL) throws InterruptedException {
		if (URL == null || "".equals(URL) || !URL.contains(MAIN_LINK))
			return false;
		for (String type : apartmentTypes) {
			if (URL.contains((MAIN_LINK) + type)) {
				String temp = URL.replace((MAIN_LINK + type), "");
				if (!temp.isEmpty() && !temp.startsWith("?")) {
					return true;
				}
			}
		}
		return false;
	}

	private static double getPriceFromDocument(Document document) {
		Double price = 0.0;
		String priceString = "0";
		if (!document.getElementsByClass("price").isEmpty()) {
			priceString = document.getElementsByClass("price").get(0).text();
		}

		if (priceString.contains("€")) {
			try {
				int euroPrice = Integer.parseInt(priceString.replaceAll("[^\\d]", ""));
				price = euroPrice * EURO_VALUE;
			} catch (Exception ex) {
			}
		} else if (priceString.contains("Lei")) {
			try {
				price = Double.parseDouble(priceString.replaceAll("[^\\d]", ""));
			} catch (Exception ex) {
			}
		} else {
			price = 0.0;
		}
		return price;
	}

	private static String getTypeFromDocument(Document document) {
		Elements elements = document.select("div.label-list > ul").select("li");
		for (Element element : elements) {
			String text = element.text().toLowerCase();
			switch (text) {
			case DECOMANDAT:
				return DECOMANDAT;
			case SEMIDECOMANDAT:
				return SEMIDECOMANDAT;
			case CIRCULAR:
				return CIRCULAR;
			}
		}
		return null;
	}

	private static ZonedDateTime getTimeFromDocument(Document document) {

		Element date = document.select("div.loc-data").first();
		String[] dateString = date.text().split(",");

		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;
		LocalTime localTime = LocalTime.parse(dateString[2].trim(), timeFormatter);

		LocalDate localDate = null;

		if ("azi".equals(dateString[1].trim())) {
			localDate = LocalDate.now();
		} else if ("ieri".equals(dateString[1].trim())) {
			localDate = LocalDate.now().minusDays(1);
		} else {
			String[] tempArray = dateString[1].trim().split(" ");
			Integer day = null;
			try {
				day = Integer.parseInt(tempArray[0]);
			} catch (Exception e) {
			}

			int year = LocalDate.now().getYear();
			Month month = monthEquivalenceMap.get(tempArray[1]);

			// current year is unavailable and while it could be calculated it's
			// a bit too much for this timeframe.
			localDate = LocalDate.of(year, month, day);
		}

		ZonedDateTime result = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());

		return result;
	}

	private static Set<String> getImageLinksFromDocument(Document document) {
		Set<String> imagesCollection = new HashSet<>();

		Elements imageGallery = null;
		if (!document.select("ul#image-gallery").isEmpty()) {
			imageGallery = document.select("ul#image-gallery").first().select("li>img");
		}

		if (imageGallery != null && !imageGallery.isEmpty()) {
			for (Element element : imageGallery) {
				String pictureThumbnailLink = element.absUrl("src");
				if (!"".equals(pictureThumbnailLink)) {
					String pictureLink = pictureThumbnailLink.replace("/thumb2/", "/imgs/");
					imagesCollection.add(pictureLink);
				}
			}
		}
		return imagesCollection;
	}
}
