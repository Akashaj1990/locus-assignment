package com.locus.auth.app;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.exception.AccessValidationException;
import com.locus.auth.model.Resource;
import com.locus.auth.model.User;
import com.locus.auth.service.ResourceManagementService;
import com.locus.auth.service.ResourceManagementServiceImpl;
import com.locus.auth.util.DbConfig;
import com.locus.auth.util.IdGenerator;

public class App {

    ResourceManagementService service = ResourceManagementServiceImpl.getInstance();

    public static void main(String[] args) {
        DbConfig.initialize();
        DbConfig.createDefaultSchema();
        App container = new App();

        //Admin User Test
        System.out.println("----Initializing Test----");
        container.AdminUserTest();
        container.readUserTest();
    }


    private void AdminUserTest() {
        Long newResourceId = IdGenerator.generateId();
        User adminUser = InMemoryDb.getInstance().getUser("Admin User");
        System.out.println("Adding Resource");
        adminUser.getRoles().stream().flatMap(role -> role.getPermissions().stream()).forEach((perm) -> perm.addResourceId(newResourceId));
        try {
            service.updateResource(Resource.builder().resourceId(newResourceId).name("New Resource").build()
                    , adminUser);
            System.out.println("AdminUser Test Passed For Adding New Resources");
        } catch (AccessValidationException e) {
            System.out.println("AdminUser Test Failed For Adding New Resources");
            System.out.println(e);
        }

        try {
            System.out.println("Reading Resource");
            System.out.println(service.readResource(newResourceId, adminUser));
            System.out.println("AdminUser Test Passed For Reading New Resources");
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("AdminUser Test Failed For Reading Any Resources");
        }
    }

    private void readUserTest() {
        User readUser = InMemoryDb.getInstance().getUser("Read User");
        try {
            service.updateResource(Resource.builder().resourceId(IdGenerator.generateId()).build()
                    , readUser);
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("Read Test Passed");
        }
    }
}
