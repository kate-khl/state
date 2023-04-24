package com.example.state.entity.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;


public class JsonFieldType implements UserType{
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public int[] sqlTypes() {
		return new int[]{Types.JAVA_OBJECT};
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return Objects.hashCode(x);
	}

	@Override
	@SneakyThrows
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		String text = rs.getString(names[0]);
		return text == null ? null : mapper.readValue(text, returnedClass());
	}

	@Override
	@SneakyThrows
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if(value == null) {
			st.setNull(index, Types.OTHER);
		} else {
			st.setObject(index, mapper.writeValueAsString(value), Types.OTHER);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return SerializationUtils.clone((Serializable)value);
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public Class<JsonField> returnedClass() {
		return JsonField.class;
	}
}
