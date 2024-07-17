package com.dailycodework.lakesidehotel.controller;

import com.dailycodework.lakesidehotel.model.Role;
import com.dailycodework.lakesidehotel.model.User;
import com.dailycodework.lakesidehotel.service.IRoleService;
import com.dailycodework.lakesidehotel.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private IRoleService roleService;

    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<List<Role>>(roleService.getRoles(), FOUND);
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role theRole) throws Exception {
        try{
            roleService.createRole(theRole);
            return ResponseEntity.ok("New role created successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("role already exists");
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long RoleId) throws Exception {
        roleService.deleteRole(RoleId);
    }

    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long RoleId) throws Exception {
        return roleService.removeAllUsersFromRole(RoleId);
    }

    @DeleteMapping("/romove-user-from-role")
    public User removeUserFromRole(@RequestParam Long userId,@RequestParam Long RoleId) throws Exception {
        return roleService.removeUserFromRole(userId,RoleId);
    }

    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(@RequestParam Long userId,@RequestParam Long RoleId) throws Exception {
        return roleService.assignUserToRole(userId,RoleId);
    }

}
