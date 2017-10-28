package com.mujutsu.simplecrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawleyController {

	static final String MAIN_LINK = "http://anuntul.ro/anunturi-imobiliare-vanzari/";
	static final String PAGE_SUFFIX = "?page=";

	static final Integer MAX_PAGE_NUMBER = 1000;
	static final String GARSONIERE = "garsoniere/";
	static final String DOUA_CAMERE = "apartamente-2-camere/";
	static final String TREI_CAMERE = "apartamente-3-camere/";
	static final String PATRU_CAMERE = "apartamente-4-camere/";
	static final String CASE_VILE = "case-vile/";
	static final String TERENURI = "terenuri/";
	static final String SPATII_COMERCIALE_INDUSTRIALE = "spatii-comerciale-industriale/";
	static final String CUMPARARI_SCHIMBURI = "cumparari-schimburi/";

	static final List<String> apartmentTypes = new ArrayList<>(Arrays.asList(GARSONIERE, DOUA_CAMERE, TREI_CAMERE,
			PATRU_CAMERE, CASE_VILE, TERENURI, SPATII_COMERCIALE_INDUSTRIALE, CUMPARARI_SCHIMBURI));

	private HashSet<String> links;

	public CrawleyController() {
		links = new HashSet<String>();
	}

	public void getPageLinks(String URL) throws InterruptedException {

		List<String> tempLinks = new ArrayList<>();

		for (Integer i = 1; i < MAX_PAGE_NUMBER; i++) {

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
					break;
				} else {
					for (String apartment : tempLinks) {
						links.add(apartment);
						System.out.println("Added apartment link to list: " + apartment);
					}
				}

			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public Boolean isApartmentSaleLink(String URL) throws InterruptedException {
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
}
