package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import org.geojson.Feature;
import org.geojson.FeatureCollection;

import java.util.Map;
import java.util.Set;

public class InsertionsFactoryTestUtils {
    public static final Set<String> TAGS = Set.of("giardino", "garage");
    public static final String DESCRIPTION = "Villa in vendita in zona residenziale con stazione metro (che non esiste, infatti questo è un test)";

    public static final String STREET_NAME = "Via Roma";
    public static final String HOUSE_NUMBER = "544";
    public static final String CITY_NAME = "Melito di Napoli";
    public static final String POSTAL_CODE = "80017";
    public static final String COUNTRY_NAME = "Italy";
    public static final double LONGITUDE = 14.195827;
    public static final double LATITUDE = 40.922233;

    public static FeatureCollection getFeatureCollection() {
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.add(getFeature());
        return featureCollection;
    }

    public static Feature getFeature() {
        Map<String, Object> properties = Map.of(
            "street",       STREET_NAME,
            "housenumber",       HOUSE_NUMBER,
            "city",         CITY_NAME,
            "postcode",   POSTAL_CODE,
            "country",      COUNTRY_NAME,
            "lon",          LONGITUDE,
            "lat",          LATITUDE
        );

        Feature feature = new Feature();
        feature.setProperties(properties);
        return feature;
    }

}
