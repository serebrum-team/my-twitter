package uz.serebrum.mytwitter.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.serebrum.mytwitter.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role,Long> {

    Role findByRoleName(String roleName);
}
