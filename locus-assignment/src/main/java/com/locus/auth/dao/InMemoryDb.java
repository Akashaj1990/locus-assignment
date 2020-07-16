package com.locus.auth.dao;

import com.locus.auth.model.Permission;
import com.locus.auth.model.Resource;
import com.locus.auth.model.Role;
import com.locus.auth.model.User;
import lombok.ToString;

import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public final class InMemoryDb {

    private InMemoryDb() {

    }

    private final ConcurrentMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Role> roles = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Permission<?>> permissions = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Resource<?>> resources = new ConcurrentHashMap<>();

    public void addUser(User role) {
        if (null == role || null == role.getUserId() || null == role.getUserName()) {
            throw new NullPointerException();
        }
        //Adding User into inMemoryDb
        users.put(role.getUserName(), role);
    }

    public User getUser(String userName) {
        return users.get(userName);
    }

    public void saveOrUpdateResource(Resource<?> resource) {
        if (null == resource || null == resource.getResourceId()) {
            throw new NullPointerException();
        }
        //Adding resource into inMemoryDb
        resources.put(resource.getResourceId(), resource);
        //Adding lock if not Exists
        if (!lockMap.containsKey(resource.getResourceId())) {
            lockMap.put(resource.getResourceId(), new ReentrantLock());
        }
    }

    public void deleteResource(Long resourceId) {
        if (null == resourceId) {
            throw new NullPointerException();
        }
        //removing resource from inMemoryDb
        resources.remove(resourceId);
    }

    public Resource<?> getResource(Long id) {
        return resources.get(id);
    }

    public void addRole(Role role) {
        if (null == role || null == role.getRoleId()) {
            throw new NullPointerException();
        }
        //Adding Role into inMemoryDb
        roles.put(role.getRoleId(), role);
    }

    public Role getRole(Long id) {
        return roles.get(id);
    }

    public void addPermission(Permission<?> perm) {
        if (null == perm || null == perm.getPermissionId()) {
            throw new NullPointerException();
        }
        //Adding Permission into inMemoryDb
        permissions.put(perm.getPermissionId(), perm);
    }

    public Permission<?> getPermission(Long id) {
        return permissions.get(id);
    }

    /**
     * creating singleton instance at runtime
     */
    private static class InMemoryDbHolder {
        private static final InMemoryDb INSTANCE = new InMemoryDb();
    }

    public static InMemoryDb getInstance() {
        return InMemoryDbHolder.INSTANCE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("----users----");
        users.entrySet().forEach((e) -> sb.append("\n").append(e));
        sb.append("\n");
        sb.append("----roles----");
        roles.entrySet().forEach((e) -> sb.append("\n").append(e));
        sb.append("\n");
        sb.append("----permissions----");
        permissions.entrySet().forEach((e) -> sb.append("\n").append(e));
        sb.append("\n");
        sb.append("----resources----");
        resources.entrySet().forEach((e) -> sb.append("\n").append(e));
        return sb.toString();
    }


}
