package com.owitho.www.kafka.common;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * String encoding defaults to UTF8 and can be customized by setting the
 * property key.serializer.encoding, value.serializer.encoding or
 * serializer.encoding. The first two take precedence over the last.
 */
public class DefaultMessageSerializer implements Serializer<GenericMessage> {
	// private String encoding = "UTF8";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// String propertyName = isKey ? "key.serializer.encoding" :
		// "value.serializer.encoding";
		// Object encodingValue = configs.get(propertyName);
		// if (encodingValue == null)
		// encodingValue = configs.get("serializer.encoding");
		// if (encodingValue != null && encodingValue instanceof String)
		// encoding = (String) encodingValue;
	}

	@Override
	public byte[] serialize(String topic, GenericMessage data) {
		try {
			if (data == null)
				return null;
			else
				return GenericMessage.javaSerialize(data);
		} catch (Exception e) {
			throw new SerializationException("Error when serializing GenericMessage");
		}
	}

	@Override
	public void close() {
		// nothing to do
	}
}
