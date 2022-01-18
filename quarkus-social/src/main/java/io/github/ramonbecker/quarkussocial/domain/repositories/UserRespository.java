package io.github.ramonbecker.quarkussocial.domain.repositories;

import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRespository implements PanacheRepository<User> {

}
