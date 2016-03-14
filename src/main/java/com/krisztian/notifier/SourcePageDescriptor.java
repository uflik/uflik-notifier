package com.krisztian.notifier;

public class SourcePageDescriptor {

    private final String id;
    private final String findingPageUrl;
    private final String selectorForAds;
    private final String selectorForNextButton;

    public SourcePageDescriptor(String id, String findingPageUrl, String selectorForAds, String selectorForNextButton) {
        this.id = id;
        this.findingPageUrl = findingPageUrl;
        this.selectorForAds = selectorForAds;
        this.selectorForNextButton = selectorForNextButton;
    }

    public final String getId() {
        return id;
    }

    public final String getFindingPageUrl() {
        return findingPageUrl;
    }

    public final String getSelectorForNextButton() {
        return selectorForNextButton;
    }

    public final String getSelectorForAds() {
        return selectorForAds;
    }

    @Override
    public final String toString() {
        return "SourcePageDescriptor{" +
                "findingPageUrl='" + findingPageUrl + '\'' +
                ", selectorForAds='" + selectorForAds + '\'' +
                ", selectorForNextButton='" + selectorForNextButton + '\'' +
                '}';
    }

}
