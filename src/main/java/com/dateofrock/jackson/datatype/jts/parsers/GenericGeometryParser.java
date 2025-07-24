package com.dateofrock.jackson.datatype.jts.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.HashMap;
import java.util.Map;

import static com.dateofrock.jackson.datatype.jts.GeoJson.*;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GenericGeometryParser extends BaseParser implements GeometryParser<Geometry> {

    private final Map<String, GeometryParser> parsers;

    public GenericGeometryParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
		this.parsers = new HashMap<String, GeometryParser>();
		this.parsers.put(POINT, new PointParser(geometryFactory));
		this.parsers.put(MULTI_POINT, new MultiPointParser(geometryFactory));
		this.parsers.put(LINE_STRING, new LineStringParser(geometryFactory));
		this.parsers.put(MULTI_LINE_STRING, new MultiLineStringParser(geometryFactory));
		this.parsers.put(POLYGON, new PolygonParser(geometryFactory));
		this.parsers.put(MULTI_POLYGON, new MultiPolygonParser(geometryFactory));
		this.parsers.put(GEOMETRY_COLLECTION, new GeometryCollectionParser(geometryFactory, this));
    }

    @Override
    public Geometry geometryFromJson(JsonNode node) throws JsonMappingException {
        String typeName = node.get(TYPE).asText();
        GeometryParser parser = this.parsers.get(typeName);
        if (parser != null) {
            return parser.geometryFromJson(node);
        }
        else {
            throw new JsonMappingException("Invalid geometry type: " + typeName);
        }
    }
}
