package com.dateofrock.jackson.datatype.jts.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import static com.dateofrock.jackson.datatype.jts.GeoJson.COORDINATES;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class PolygonParser extends BaseParser implements GeometryParser<Polygon> {

    public PolygonParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public Polygon polygonFromJson(JsonNode node) {
        JsonNode arrayOfRings = node.get(COORDINATES);
        return this.polygonFromJsonArrayOfRings(arrayOfRings);
    }

    public Polygon polygonFromJsonArrayOfRings(JsonNode arrayOfRings) {
        LinearRing shell = this.linearRingsFromJson(arrayOfRings.get(0));
        int size = arrayOfRings.size();
        LinearRing[] holes = new LinearRing[size - 1];
        for (int i = 1; i < size; i++) {
            holes[i - 1] = this.linearRingsFromJson(arrayOfRings.get(i));
        }
        return this.geometryFactory.createPolygon(shell, holes);
    }

    private LinearRing linearRingsFromJson(JsonNode coordinates) {
        assert coordinates.isArray() : "expected coordinates array";
        return this.geometryFactory.createLinearRing(PointParser.coordinatesFromJson(coordinates));
    }


    @Override
    public Polygon geometryFromJson(JsonNode node) throws JsonMappingException {
        return this.polygonFromJson(node);
    }
}
