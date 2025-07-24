package com.dateofrock.jackson.datatype.jts;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.IOException;

import static org.junit.Assert.assertThat;

public class JtsModuleTest {
	private final GeometryFactory gf = new GeometryFactory();
	private ObjectMapper mapper;
	@Before
	public void setupMapper() {

		this.mapper = new ObjectMapper();
		this.mapper.registerModule(new JtsModule());
	}

	@Test(expected = JsonMappingException.class)
	public void invalidGeometryType() throws IOException {
		String json = "{\"type\":\"Singularity\",\"coordinates\":[]}";
		this.mapper.readValue(json, Geometry.class);
	}
	
	@Test(expected = JsonMappingException.class)
	public void unsupportedGeometry() throws IOException {
		Geometry unsupportedGeometry = EasyMock.createNiceMock("NonEuclideanGeometry", Geometry.class);
		EasyMock.replay(unsupportedGeometry);

		this.mapper.writeValue(System.out, unsupportedGeometry);
	}

}
