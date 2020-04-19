package com.ssja.lms.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ssja.lms.dao.RoleUrlMappingRepository;
import com.ssja.lms.model.RoleUrlMapping;
import com.ssja.lms.util.ConfigurationConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	DataSource dataSource;

	@Autowired
	RoleUrlMappingRepository roleUrlMappingRepository;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource).authoritiesByUsernameQuery(
				"select u.username,r.name role from user u join user_role_mapping urm on (u.id=urm.user_id) join role r on (urm.role_id=r.id)where u.status=1 and urm.status=1 and u.username=?")
				.usersByUsernameQuery("select username,password,status from user where username=?");
	}

	/**
	 *
	 * @param httpSecurity
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		List<RoleUrlMapping> roleUrlMappings = roleUrlMappingRepository
				.findByStatus(ConfigurationConstants.ACTIVE.getId());

		Map<String, List<String>> roleMap = new HashMap<>();

		for (RoleUrlMapping roleUrlMapping : roleUrlMappings) {
			if (roleMap.containsKey(roleUrlMapping.getUrlPattern())) {
				List<String> roles = roleMap.get(roleUrlMapping.getUrlPattern());
				roles.add(roleUrlMapping.getRoleId().getPermission());
				roleMap.put(roleUrlMapping.getUrlPattern(), roles);
			} else {
				List<String> roles = new ArrayList<>();
				roles.add(roleUrlMapping.getRoleId().getPermission());
				roleMap.put(roleUrlMapping.getUrlPattern(), roles);
			}
		}

		for (Entry<String, List<String>> role : roleMap.entrySet()) {
			logger.info("Key : {} \t Role : {}", role.getKey(), role.getValue());
			String[] roles = role.getValue().toArray(new String[0]);
			httpSecurity.authorizeRequests().antMatchers(role.getKey()).hasAnyRole(roles).and();
		}

		httpSecurity.httpBasic().and().formLogin().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();

	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
