/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORM_HHH_10376UnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Foo.class,
				Bar.class
		};
	}

	@Override
	protected String[] getMappings() {
		return new String[] {
		};
	}

	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	@Test
	public void HHH_11278_passing_Test() throws Exception {

		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Bar bar = new Bar();
		bar.setFue("fue");
		s.save(bar);

		Foo foo = new Foo();
		foo.setBar(bar);

		s.save(foo);

		Criteria createCriteria = s.createCriteria(Foo.class, "foo");
		createCriteria.createAlias("bar", "bar");

		createCriteria.addOrder(Order.desc("id"));

		List foos = createCriteria.list();

		assertTrue(true);

		tx.commit();
		s.close();
	}

	@Test
	public void HHH_11278_failing_if_adding_projection_Test() throws Exception {

		Session s = openSession();
		Transaction tx = s.beginTransaction();
	
		Bar bar = new Bar();
		bar.setFue("fue");
		s.save(bar);
		
		Foo foo = new Foo();
		foo.setBar(bar);
		
		s.save(foo);
		
		Criteria createCriteria = s.createCriteria(Foo.class, "foo");
		createCriteria.createAlias("bar", "bar");

		createCriteria.addOrder(Order.desc("id"));

		createCriteria.setProjection(Projections.distinct(Projections.property("bar")));
		List foos = createCriteria.list();
		
		assertTrue(true);
		
		tx.commit();
		s.close();
	}
}
