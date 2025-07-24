package com.dateofrock.jackson.datatype.jts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public abstract class BaseJtsModuleTest<T extends Geometry> {
    protected GeometryFactory gf = new GeometryFactory();
    private ObjectWriter writer;
    private ObjectMapper mapper;
    private T geometry;
    private String geometryAsGeoJson;

    protected BaseJtsModuleTest() {
    }

    @Before
    public void setup() {
		this.mapper = new ObjectMapper();
		this.mapper.registerModule(new JtsModule());
		this.writer = this.mapper.writer();
		this.geometry = this.createGeometry();
		this.geometryAsGeoJson = this.createGeometryAsGeoJson();
    }

    protected abstract Class<T> getType();

    protected abstract String createGeometryAsGeoJson();

    protected abstract T createGeometry();


    @Test
    public void shouldDeserializeConcreteType() throws Exception {
        T concreteGeometry = this.mapper.readValue(this.geometryAsGeoJson, this.getType());
        assertThat(
				this.toJson(concreteGeometry),
                equalTo(this.geometryAsGeoJson));
    }

    @Test
    public void shouldDeserializeAsInterface() throws Exception {
		this.assertRoundTrip(this.geometry);
        assertThat(
				this.toJson(this.geometry),
                equalTo(this.geometryAsGeoJson));
    }

    protected String toJson(Object value) throws IOException {
        return this.writer.writeValueAsString(value);
    }

    protected void assertRoundTrip(T geom) throws IOException {
        String json = this.writer.writeValueAsString(geom);
        System.out.println(json);
        Geometry regeom = this.mapper.reader(Geometry.class).readValue(json);
        assertThat(geom.equalsExact(regeom), is(true));
    }
}
