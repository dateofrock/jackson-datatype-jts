package com.dateofrock.jackson.datatype.jts.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

import static com.dateofrock.jackson.datatype.jts.GeoJson.GEOMETRIES;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GeometryCollectionParser extends BaseParser implements GeometryParser<GeometryCollection> {

    private final GenericGeometryParser genericGeometriesParser;

    public GeometryCollectionParser(GeometryFactory geometryFactory, GenericGeometryParser genericGeometriesParser) {
        super(geometryFactory);
        this.genericGeometriesParser = genericGeometriesParser;
    }

    private Geometry[] geometriesFromJson(JsonNode arrayOfGeoms) throws JsonMappingException {
        Geometry[] items = new Geometry[arrayOfGeoms.size()];
        for(int i=0;i!=arrayOfGeoms.size();++i) {
            items[i] = this.genericGeometriesParser.geometryFromJson(arrayOfGeoms.get(i));
        }
        return items;
    }

    @Override
    public GeometryCollection geometryFromJson(JsonNode node) throws JsonMappingException {
        return this.geometryFactory.createGeometryCollection(
				this.geometriesFromJson(node.get(GEOMETRIES)));
    }
}
