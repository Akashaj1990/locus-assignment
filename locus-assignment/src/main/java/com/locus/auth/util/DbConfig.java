package com.locus.auth.util;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.enums.ActionType;
import com.locus.auth.model.Permission;
import com.locus.auth.model.Resource;
import com.locus.auth.model.Role;
import com.locus.auth.model.User;
import com.locus.auth.resource.DbRecord;
import com.locus.auth.resource.SystemFile;

public class DbConfig {

    private volatile static InMemoryDb database;

    public static void initialize() {
        System.out.println("initializing db config");
        database = InMemoryDb.getInstance();
    }

    public static void createDefaultSchema() {
        System.out.println("initializing db default db schema");

        //creating actual resources
        DbRecord record = DbRecord.builder().recordId(IdGenerator.generateId()).build();
        SystemFile file = SystemFile.builder().fileId(IdGenerator.generateId()).build();

        //creating resource wrapper
        Resource<?> dbResource = Resource.builder().resourceId(record.getRecordId()).val(record).name("database record").build();
        Resource<?> fileResource = Resource.builder().resourceId(file.getFileId()).val(record).name("system file descriptor").build();

        /*
         * creating permissions
         * */
        Permission<?> readPermission = Permission.builder()
                .permissionId(IdGenerator.generateId())
                .action(ActionType.READ)
                .permissionName("Read Permission").build()
                .addResourceId(dbResource.getResourceId())
                .addResourceId(fileResource.getResourceId());

        Permission<?> writePermission = Permission.builder()
                .permissionId(IdGenerator.generateId())
                .action(ActionType.WRITE)
                .permissionName("Write Permission").build()
                .addResourceId(dbResource.getResourceId())
                .addResourceId(fileResource.getResourceId());

        Permission<?> deletePermission = Permission.builder()
                .permissionId(IdGenerator.generateId())
                .action(ActionType.DELETE)
                .permissionName("Delete Permission").build()
                .addResourceId(dbResource.getResourceId())
                .addResourceId(fileResource.getResourceId());

        /*
         * creating roles
         * */
        Role readRole = Role.builder()
                .roleId(IdGenerator.generateId())
                .roleName("Read Role")
                .build()
                .addPermission(readPermission);

        Role writeRole = Role.builder()
                .roleId(IdGenerator.generateId())
                .roleName("Write Role")
                .build()
                .addPermission(writePermission);

        Role deleteRole = Role.builder()
                .roleId(IdGenerator.generateId())
                .roleName("Delete Role")
                .build()
                .addPermission(deletePermission);

        Role adminRole = Role.builder()
                .roleId(IdGenerator.generateId())
                .roleName("Admin Role")
                .build()
                .addPermission(readPermission)
                .addPermission(writePermission)
                .addPermission(deletePermission);

        /*
         * creating users
         * */
        User readUser = User.builder().userId(IdGenerator.generateId())
                .userName("Read User")
                .build()
                .addRole(readRole);

        User writeUser = User.builder().userId(IdGenerator.generateId())
                .userName("Write User")
                .build()
                .addRole(writeRole);

        User deleteUser = User.builder().userId(IdGenerator.generateId())
                .userName("Delete User")
                .build()
                .addRole(deleteRole);

        User admin = User.builder().userId(IdGenerator.generateId())
                .userName("Admin User")
                .build()
                .addRole(adminRole);

        database.saveOrUpdateResource(dbResource);
        database.saveOrUpdateResource(fileResource);
        database.addPermission(readPermission);
        database.addPermission(writePermission);
        database.addPermission(deletePermission);
        database.addRole(readRole);
        database.addRole(writeRole);
        database.addRole(deleteRole);
        database.addRole(adminRole);
        database.addUser(readUser);
        database.addUser(writeUser);
        database.addUser(deleteUser);
        database.addUser(admin);

        System.out.println("----current schema-----");
        System.out.println(database);

    }

}
