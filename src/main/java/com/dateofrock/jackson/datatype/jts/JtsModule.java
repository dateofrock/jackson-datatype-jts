package com.dateofrock.jackson.datatype.jts;

import com.dateofrock.jackson.datatype.jts.parsers.GenericGeometryParser;
import com.dateofrock.jackson.datatype.jts.parsers.GeometryCollectionParser;
import com.dateofrock.jackson.datatype.jts.parsers.LineStringParser;
import com.dateofrock.jackson.datatype.jts.parsers.MultiLineStringParser;
import com.dateofrock.jackson.datatype.jts.parsers.MultiPointParser;
import com.dateofrock.jackson.datatype.jts.parsers.MultiPolygonParser;
import com.dateofrock.jackson.datatype.jts.parsers.PointParser;
import com.dateofrock.jackson.datatype.jts.parsers.PolygonParser;
import com.dateofrock.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.dateofrock.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class JtsModule extends SimpleModule {

	public JtsModule() {
		this(new GeometryFactory());
	}

	public JtsModule(GeometryFactory geometryFactory) {
		super("JtsModule", new Version(3, 0, 1, null, "com.dateofrock", "jackson-datatype-jts"));

		this.addSerializer(Geometry.class, new GeometrySerializer());
		GenericGeometryParser genericGeometryParser = new GenericGeometryParser(geometryFactory);
		this.addDeserializer(Geometry.class, new GeometryDeserializer<Geometry>(genericGeometryParser));
		this.addDeserializer(Point.class, new GeometryDeserializer<Point>(new PointParser(geometryFactory)));
		this.addDeserializer(MultiPoint.class, new GeometryDeserializer<MultiPoint>(new MultiPointParser(geometryFactory)));
		this.addDeserializer(LineString.class, new GeometryDeserializer<LineString>(new LineStringParser(geometryFactory)));
		this.addDeserializer(MultiLineString.class, new GeometryDeserializer<MultiLineString>(new MultiLineStringParser(geometryFactory)));
		this.addDeserializer(Polygon.class, new GeometryDeserializer<Polygon>(new PolygonParser(geometryFactory)));
		this.addDeserializer(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>(new MultiPolygonParser(geometryFactory)));
		this.addDeserializer(GeometryCollection.class, new GeometryDeserializer<GeometryCollection>(new GeometryCollectionParser(geometryFactory, genericGeometryParser)));
	}

	@Override
	public void setupModule(SetupContext context) {
		super.setupModule(context);
	}
}
