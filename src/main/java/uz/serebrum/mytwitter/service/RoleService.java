package uz.serebrum.mytwitter.service;


import uz.serebrum.mytwitter.entity.Role;

public interface RoleService {

    Role saveRole(Role role);

    Role getRoleById(Long id);
}
