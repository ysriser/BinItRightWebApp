package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.UserAccessoriesImplementation;

import java.util.List;

@RestController
@RequestMapping("/api/user-accessories")
public class UserAccessoriesRestController {

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<UserAccessories>> getMyAccessories(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());

        // 3. Use the ID to get the accessories
        List<UserAccessories> items = userAccessoriesService.findAllByUser_Id(userId);
        return ResponseEntity.ok(items);
    }

    /**
     * POST to equip a specific accessory
     */
    @PostMapping("/equip/{id}")
    public ResponseEntity<String> equipAccessory(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());

        // Logic to unequip current and equip the new ID for this specific user
        userAccessoriesService.equipItem(userId, id);

        return ResponseEntity.ok("Item equipped successfully.");
    }

    @PostMapping("/unequip/{id}")
    public ResponseEntity<String> unequipAccessory(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());

        userAccessoriesService.unequipItem(userId, id);
        return ResponseEntity.ok("Item unequipped.");
    }
}