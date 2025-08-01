package com.dateofrock.jackson.datatype.jts.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;
import java.util.Arrays;

import static com.dateofrock.jackson.datatype.jts.GeoJson.*;

public class GeometrySerializer extends JsonSerializer<Geometry> {

	@Override
	public void serialize(Geometry value, JsonGenerator jgen,
						  SerializerProvider provider) throws IOException {

		this.writeGeometry(jgen, value);
	}

	public void writeGeometry(JsonGenerator jgen, Geometry value)
			throws IOException {
		if (value instanceof Polygon) {
			this.writePolygon(jgen, (Polygon) value);

		} else if(value instanceof Point) {
			this.writePoint(jgen, (Point) value);

		} else if (value instanceof MultiPoint) {
			this.writeMultiPoint(jgen, (MultiPoint) value);

		} else if (value instanceof MultiPolygon) {
			this.writeMultiPolygon(jgen, (MultiPolygon) value);

		} else if (value instanceof LineString) {
			this.writeLineString(jgen, (LineString) value);

		} else if (value instanceof MultiLineString) {
			this.writeMultiLineString(jgen, (MultiLineString) value);

		} else if (value instanceof GeometryCollection) {
			this.writeGeometryCollection(jgen, (GeometryCollection) value);

		} else {
			throw new JsonMappingException("Geometry type " 
					+ value.getClass().getName() + " cannot be serialized as GeoJSON." +
					"Supported types are: " + Arrays.asList(
						Point.class.getName(), 
						LineString.class.getName(), 
						Polygon.class.getName(), 
						MultiPoint.class.getName(), 
						MultiLineString.class.getName(),
						MultiPolygon.class.getName(), 
						GeometryCollection.class.getName()));
		}
	}

	private void writeGeometryCollection(JsonGenerator jgen, GeometryCollection value) throws
			IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, GEOMETRY_COLLECTION);
		jgen.writeArrayFieldStart(GEOMETRIES);

		for (int i = 0; i != value.getNumGeometries(); ++i) {
			this.writeGeometry(jgen, value.getGeometryN(i));
		}

		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	private void writeMultiPoint(JsonGenerator jgen, MultiPoint value)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, MULTI_POINT);
		jgen.writeArrayFieldStart(COORDINATES);

		for (int i = 0; i != value.getNumGeometries(); ++i) {
			this.writePointCoords(jgen, (Point) value.getGeometryN(i));
		}

		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	private void writeMultiLineString(JsonGenerator jgen, MultiLineString value)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, MULTI_LINE_STRING);
		jgen.writeArrayFieldStart(COORDINATES);

		for (int i = 0; i != value.getNumGeometries(); ++i) {
			this.writeLineStringCoords(jgen, (LineString) value.getGeometryN(i));
		}

		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	@Override
	public Class<Geometry> handledType() {
		return Geometry.class;
	}

	private void writeMultiPolygon(JsonGenerator jgen, MultiPolygon value)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, MULTI_POLYGON);
		jgen.writeArrayFieldStart(COORDINATES);

		for (int i = 0; i != value.getNumGeometries(); ++i) {
			this.writePolygonCoordinates(jgen, (Polygon) value.getGeometryN(i));
		}

		jgen.writeEndArray();
		jgen.writeEndObject();
	}

	private void writePolygon(JsonGenerator jgen, Polygon value)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, POLYGON);
		jgen.writeFieldName(COORDINATES);
		this.writePolygonCoordinates(jgen, value);

		jgen.writeEndObject();
	}

	private void writePolygonCoordinates(JsonGenerator jgen, Polygon value)
			throws IOException {
		jgen.writeStartArray();
		this.writeLineStringCoords(jgen, value.getExteriorRing());

		for (int i = 0; i < value.getNumInteriorRing(); ++i) {
			this.writeLineStringCoords(jgen, value.getInteriorRingN(i));
		}
		jgen.writeEndArray();
	}

	private void writeLineStringCoords(JsonGenerator jgen, LineString ring)
			throws IOException {
		jgen.writeStartArray();
		for (int i = 0; i != ring.getNumPoints(); ++i) {
			Point p = ring.getPointN(i);
			this.writePointCoords(jgen, p);
		}
		jgen.writeEndArray();
	}

	private void writeLineString(JsonGenerator jgen, LineString lineString)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, LINE_STRING);
		jgen.writeFieldName(COORDINATES);
		this.writeLineStringCoords(jgen, lineString);
		jgen.writeEndObject();
	}

	private void writePoint(JsonGenerator jgen, Point p)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField(TYPE, POINT);
		jgen.writeFieldName(COORDINATES);
		this.writePointCoords(jgen, p);
		jgen.writeEndObject();
	}

	private void writePointCoords(JsonGenerator jgen, Point p)
			throws IOException {
		jgen.writeStartArray();
                
		jgen.writeNumber(p.getCoordinate().x);
		jgen.writeNumber(p.getCoordinate().y);
                
                if(!Double.isNaN(p.getCoordinate().z))
                {
                    jgen.writeNumber(p.getCoordinate().z);
                }
		jgen.writeEndArray();
	}

}
