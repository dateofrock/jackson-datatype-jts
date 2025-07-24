package com.dateofrock.jackson.datatype.jts.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import static com.dateofrock.jackson.datatype.jts.GeoJson.COORDINATES;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiPolygonParser extends BaseParser implements GeometryParser<MultiPolygon> {

    private final PolygonParser helperParser;
    public MultiPolygonParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
		this.helperParser = new PolygonParser(geometryFactory);
    }

    public MultiPolygon multiPolygonFromJson(JsonNode root) {
        JsonNode arrayOfPolygons = root.get(COORDINATES);
        return this.geometryFactory.createMultiPolygon(this.polygonsFromJson(arrayOfPolygons));
    }

    private Polygon[] polygonsFromJson(JsonNode arrayOfPolygons) {
        Polygon[] polygons = new Polygon[arrayOfPolygons.size()];
        for (int i = 0; i != arrayOfPolygons.size(); ++i) {
            polygons[i] = this.helperParser.polygonFromJsonArrayOfRings(arrayOfPolygons.get(i));
        }
        return polygons;
    }

    @Override
    public MultiPolygon geometryFromJson(JsonNode node) throws JsonMappingException {
        return this.multiPolygonFromJson(node);
    }
}
