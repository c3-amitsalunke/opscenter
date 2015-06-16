/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package c3.ops.cassandra.utils;

import c3.ops.cassandra.IConfiguration;
import c3.ops.cassandra.scheduler.SimpleTimer;
import c3.ops.cassandra.scheduler.Task;
import c3.ops.cassandra.scheduler.TaskTimer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * This task checks if the Cassandra process is running.
 */
@Singleton
public class CassandraMonitor extends Task {

	public static final String JOBNAME = "CASS_MONITOR_THREAD";
	private static final Logger logger = LoggerFactory.getLogger(CassandraMonitor.class);
	private static final AtomicBoolean isCassandraStarted = new AtomicBoolean(false);

	@Inject
	protected CassandraMonitor(IConfiguration config) {
		super(config);
	}

	public static TaskTimer getTimer() {
		return new SimpleTimer(JOBNAME, 10L * 1000);
	}

	public static Boolean isCassadraStarted() {
		return isCassandraStarted.get();
	}

	//Added for testing only
	public static void setIsCassadraStarted() {
		//Setting cassandra flag to true
		isCassandraStarted.set(true);
	}

	@Override
	public void execute() throws Exception {

		try {
			//This returns pid for the Cassandra process
			Process p = Runtime.getRuntime().exec("pgrep -f " + config.getCassProcessName());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = input.readLine();
			if (line != null && !isCassadraStarted()) {
				//Setting cassandra flag to true
				isCassandraStarted.set(true);
			} else if (line == null && isCassadraStarted()) {
				//Setting cassandra flag to false
				isCassandraStarted.set(false);
			}
		} catch (Exception e) {
			logger.warn("Exception thrown while checking if Cassandra is running or not ", e);
			//Setting Cassandra flag to false
			isCassandraStarted.set(false);
		}

	}

	@Override
	public String getName() {
		return JOBNAME;
	}
}
