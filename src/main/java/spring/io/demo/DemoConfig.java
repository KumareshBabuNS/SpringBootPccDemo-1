/*
 * Copyright 2017 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package spring.io.demo;


import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.GemfireTemplate;
import spring.io.demo.domain.Employee;




@Configuration
public class DemoConfig {

	@Profile("cloud")
	@Configuration
	static class PccConfiguration extends AbstractCloudConfig {
		@Bean
		public ServiceConnectorConfig gemfireConnectorConfig(PdxSerializer pdxSerializer) {

			GemfireServiceConnectorConfig gemfireConfig = new GemfireServiceConnectorConfig();
			gemfireConfig.setPoolSubscriptionEnabled(true);
			gemfireConfig.setPdxSerializer(pdxSerializer);
			gemfireConfig.setPdxReadSerialized(false);

			return gemfireConfig;
		}

		@Bean(name = "clientCache")
		public ClientCache getGemfireClientCache(ServiceConnectorConfig serviceConnectorConfig) throws Exception {
			Cloud cloud = new CloudFactory().getCloud();
			return cloud.getSingletonServiceConnector(ClientCache.class, serviceConnectorConfig);
		}


	}

	@Profile("!cloud")
	@Configuration
	@ImportResource("cache-config.xml")
	static class StandaloneConfiguration {
	}

	@Bean
	PdxSerializer pdxSerializer() {
		return new ReflectionBasedAutoSerializer(".*");
	}


	@Bean(name = "employee")
	public Region<String, Employee> employeeRegion(ClientCache clientCache) {
		ClientRegionFactory<String, Employee> clientRegionFactory = clientCache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);

		Region<String, Employee> employeeRegion = clientRegionFactory.create("employee");

		return employeeRegion;
	}

	@Bean
	GemfireTemplate gemfireTemplate(Region<String, Employee> region) {
		return new GemfireTemplate(region);
	}

}
