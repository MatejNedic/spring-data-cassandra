/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core.cql.lookup;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.core.cql.session.lookup.MapSessionFactoryLookup;
import org.springframework.data.cassandra.core.cql.session.lookup.SessionFactoryLookupFailureException;

/**
 * Unit tests for {@link MapSessionFactoryLookup}.
 *
 * @author Mark Paluch
 */
@RunWith(MockitoJUnitRunner.class)
public class MapSessionFactoryLookupUnitTests {

	@Mock SessionFactory sessionFactory;

	@Test // DATACASS-330
	public void shouldFailWithUnknownLookup() {

		MapSessionFactoryLookup sessionFactoryLookup = new MapSessionFactoryLookup();

		try {
			sessionFactoryLookup.getSessionFactory("unknown");
			fail("Missing SessionFactoryLookupFailureException");
		} catch (SessionFactoryLookupFailureException e) {
			assertThat(e).hasMessageContaining("No SessionFactory with name [unknown] registered");
		}
	}

	@Test // DATACASS-330
	public void shouldResolveSessionFactoryCorrectly() {

		MapSessionFactoryLookup sessionFactoryLookup = new MapSessionFactoryLookup("factory", sessionFactory);

		assertThat(sessionFactoryLookup.getSessionFactory("factory")).isSameAs(sessionFactory);
	}

	@Test // DATACASS-330
	public void shouldResolveProvidedInConstructorSessionFactoryCorrectly() {

		MapSessionFactoryLookup sessionFactoryLookup = new MapSessionFactoryLookup(
				Collections.singletonMap("factory", sessionFactory));

		assertThat(sessionFactoryLookup.getSessionFactory("factory")).isSameAs(sessionFactory);
	}

	@Test // DATACASS-330
	public void shouldSetSessionFactories() {

		MapSessionFactoryLookup sessionFactoryLookup = new MapSessionFactoryLookup();

		sessionFactoryLookup.setSessionFactories(Collections.singletonMap("factory", sessionFactory));

		assertThat(sessionFactoryLookup.getSessionFactory("factory")).isSameAs(sessionFactory);
	}

	@Test // DATACASS-330
	public void shouldNotOverwriteFactoriesSettingNull() {

		MapSessionFactoryLookup sessionFactoryLookup = new MapSessionFactoryLookup("factory", sessionFactory);

		sessionFactoryLookup.setSessionFactories(null);

		assertThat(sessionFactoryLookup.getSessionFactory("factory")).isSameAs(sessionFactory);
	}
}
