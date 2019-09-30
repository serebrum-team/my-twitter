package uz.serebrum.mytwitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.serebrum.mytwitter.dao.RoleDao;
import uz.serebrum.mytwitter.entity.Role;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public Role saveRole(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.findById(id).get();
    }
}
