package at.jeff.project.config;

import at.jeff.project.model.external.Role;
import at.jeff.project.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @Value("${roles.check.enabled:true}")
    private boolean rolesCheckEnabled;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void seedRoles() {
        if (!rolesCheckEnabled) {
            return;
        }

        createRoleIfNotExists("admin", "Administrator mit allen Berechtigungen", 0);
        createRoleIfNotExists("registeredUser", "Registrierter Benutzer mit Basisrechten", 1);
        createRoleIfNotExists("user", "Standardbenutzer mit limitierten Rechten", 2);
    }

    private void createRoleIfNotExists(String roleName, String description, int level) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setId(roleName);
            newRole.setName(roleName);
            newRole.setDescription(description);
            newRole.setPermissionsLevel(level);
            roleRepository.save(newRole);
            log.info("Rolle erstellt: " + roleName + " (Level: " + level + ")");
        }
    }
}
