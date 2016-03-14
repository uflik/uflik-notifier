package com.krisztian.notifier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jsoup.select.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uflik on 3/12/16.
 */
public final class Collector {

    public static final String ABS_HREF = "abs:href";
    private static Logger LOGGER = LoggerFactory.getLogger(Collector.class);

    public static List<String> collect() throws IOException {
        List<String> collectedLinks = new ArrayList<>();
        for (SourcePageDescriptor sPD : CsvHandler.getSourcePageDescriptorListFromCsv()) {
            LOGGER.info("------------------------{}------------------------", sPD.getId());
            collectedLinks.addAll(collectLinksBySourceDescriptor(sPD));
        }
        return collectedLinks;
    }

    public static List<String> collectLinksBySourceDescriptor(SourcePageDescriptor sPD) throws IOException {
        List<String> linkCollection = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(sPD.getFindingPageUrl()).get();
            linkCollection.addAll(collectLinksFromCurrentPage(doc, sPD));
            linkCollection.addAll(collectLinksFromRemainPages(doc, sPD));
        } catch (Selector.SelectorParseException | IllegalArgumentException e) {
            LOGGER.warn("Selector parsing failure: {} \n Name: {} \n Advertisement selector: {} \n Next button selector: {}",
                    e.getMessage(), sPD.getId(), sPD.getSelectorForAds(), sPD.getSelectorForNextButton());
        }
        return linkCollection;
    }

    private static List<String> collectLinksFromCurrentPage(Document doc, SourcePageDescriptor sPD) throws IOException {
        List<String> linkCollection = new ArrayList<>();
        Elements links = doc.select(sPD.getSelectorForAds());
        for (Element link : links) {
            linkCollection.add(link.attr(ABS_HREF));
        }
        LOGGER.info("Links from page: {} collected", sPD.getFindingPageUrl());

        return linkCollection;
    }

    private static List<String> collectLinksFromRemainPages(Document doc, SourcePageDescriptor sPD) throws IOException {
        List<String> linkCollection = new ArrayList<>();
        Elements nextPageButton = doc.select(sPD.getSelectorForNextButton());

        if (!nextPageButton.isEmpty()) {
            SourcePageDescriptor nextPage = new SourcePageDescriptor(
                    sPD.getId(), nextPageButton.first().attr(ABS_HREF),
                    sPD.getSelectorForAds(), sPD.getSelectorForNextButton());
            linkCollection.addAll(collectLinksBySourceDescriptor(nextPage));
        }

        return linkCollection;
    }

    private Collector() {
    }
}
