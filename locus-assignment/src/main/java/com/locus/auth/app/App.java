package com.locus.auth.app;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.exception.AccessValidationException;
import com.locus.auth.model.Resource;
import com.locus.auth.model.User;
import com.locus.auth.processor.AsynchTaskProcessor;
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
        container.writeUserTest();
        container.deleteUserTest();
        AsynchTaskProcessor.shutdown();
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

    private void writeUserTest() {
        User writeUser = InMemoryDb.getInstance().getUser("Write User");
        try {
            service.updateResource(InMemoryDb.getInstance().getResource(10000L)
                    , writeUser);
            System.out.println("Write User Test Passes For Updating Resource");
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("Write User Test Failed");
        }

        try {
            service.deleteResource(1000L
                    , writeUser);
            System.out.println("Write User Test Failed For Deletion");
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("Write User Test Passes For Deletion");
        }
    }

    private void deleteUserTest() {
        User writeUser = InMemoryDb.getInstance().getUser("Delete User");
        try {
            service.deleteResource(10000L
                    , writeUser);

            if (null == InMemoryDb.getInstance().getResource(1000L)) {
                System.out.println("Delete User Test Passes For Deletion");
            }
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("Delete User Test Failed");
        }

        try {
            service.updateResource(InMemoryDb.getInstance().getResource(10001L)
                    , writeUser);
            System.out.println("Delete User Test Failed For Updation");
        } catch (AccessValidationException e) {
            System.out.println(e);
            System.out.println("Delete User Test Passes For Updation");
        }
    }

}
