package com.dateofrock.jackson.datatype.jts;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiLineStringTest extends BaseJtsModuleTest<MultiLineString> {
    @Override
    protected Class<MultiLineString> getType() {
        return MultiLineString.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        return "{\"type\":\"MultiLineString\",\"coordinates\":[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]]}";
    }

    @Override
    protected MultiLineString createGeometry() {
        return this.gf
                .createMultiLineString(new LineString[] {
						this.gf.createLineString(new Coordinate[] {
                                new Coordinate(100.0, 0.0),
                                new Coordinate(101.0, 1.0) }),
						this.gf.createLineString(new Coordinate[] {
                                new Coordinate(102.0, 2.0),
                                new Coordinate(103.0, 3.0) }) });
    }

}
