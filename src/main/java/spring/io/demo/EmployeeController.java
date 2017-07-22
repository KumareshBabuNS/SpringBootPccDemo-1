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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.io.demo.domain.Employee;
import spring.io.demo.repo.EmployeeRepository;

import java.util.Collection;

/**
 * @author David Turanski
 **/
@RestController
public class EmployeeController {
	private static Log log = LogFactory.getLog(EmployeeController.class);

	@Autowired
	private GemfireTemplate template;

	@Autowired
	private EmployeeRepository repository;

	@PostMapping("/employees")
	public void addEmployee(@RequestBody Employee employee) {
		repository.save(employee);
	}

	@GetMapping("/employees/{id}")
	public Employee getEmployee(@PathVariable int id) {
		return repository.findOne(id);
	}

	@GetMapping("/employees")
	public Collection<Employee> getEmployees(@RequestParam(value = "q", required = false) String query) {
		query = StringUtils.hasText(query) ? query : "";
		log.info("Executing Query " + query);
		return template.query(query);
	}
}
