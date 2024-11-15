package com.animewebsite;

import com.animewebsite.system.enums.RoleType;
import com.animewebsite.system.model.Role;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.RoleRepository;
import com.animewebsite.system.repository.UserRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class AnimewebsiteApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AnimewebsiteApplication.class, args);
	}

	@Override
//	@Transactional
	public void run(java.lang.String... args) {
//		for (RoleType roleType : RoleType.values()) {
//			roleRepository.save(new Role(roleType.getName(), roleType.getDescription()));
//		}
//
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		User user = User.builder()
//				.enabled(true)
//				.username("admin")
//				.email("m1503@gmail.com")
//				.roles(new ArrayList<>())
//				.password(bCryptPasswordEncoder.encode("12345"))
//				.build();
//		Role role = roleRepository.findByName(RoleType.ADMIN.getName()).get();
//		user.getRoles().add(role);
//		userRepository.save(user);
	}
}
