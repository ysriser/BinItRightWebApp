package tech3.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.UserAccessoriesImplementation;
import tech3.binitright.service.UserImplementation;

@RestController
@RequestMapping("/api/user-accessories")
public final class UserAccessoriesRestController {

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(final UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(final UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<UserAccessories>> getMyAccessories(final Authentication authentication) {
        final Long userId = Long.valueOf(authentication.getName());
        final List<UserAccessories> items = userAccessoriesService.findAllByUserUId(userId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/equip/{id}")
    public ResponseEntity<String> equipAccessory(@PathVariable final Long id, final Authentication authentication) {
        final Long userId = Long.valueOf(authentication.getName());
        userAccessoriesService.equipItem(userId, id);
        return ResponseEntity.ok("Item equipped successfully.");
    }

    @PostMapping("/unequip/{id}")
    public ResponseEntity<String> unequipAccessory(@PathVariable final Long id, final Authentication authentication) {
        final Long userId = Long.valueOf(authentication.getName());
        userAccessoriesService.unequipItem(userId, id);
        return ResponseEntity.ok("Item unequipped.");
    }
}