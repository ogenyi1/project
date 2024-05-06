package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.RegisterDeviceToken;
import ng.optisoft.rosabon.dto.response.DeviceTokenDto;
import ng.optisoft.rosabon.event.InAppEvent;
import ng.optisoft.rosabon.mapper.DeviceTokenMapper;
import ng.optisoft.rosabon.model.DeviceToken;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.model.InApp.Status;
import ng.optisoft.rosabon.service.DeviceTokenMngr;
import ng.optisoft.rosabon.service.NotificationMngr;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private DeviceTokenMngr deviceTokenMngr;
    private ModelMapper mapper;
    private UseraccountDao useraccountDao;
    private NotificationMngr notificationMngr;

    @PostMapping("/send-notification")
    public ResponseEntity<?> sendNotification(@RequestBody @Valid InAppEvent request) {
        notificationMngr.createInApp(request);

        return ResponseEntity.ok("notification will be sent to your device");
    }

    @GetMapping()
    public ResponseEntity<?> getAllUserNotificationByPlatform(Authentication auth, @RequestParam("platform") GenericModuleBaseEntity.Platform platform, @RequestParam(value = "initiator", required = false) Long initiator) {
        if (initiator != null) {
            return ResponseEntity.ok(notificationMngr.getAllNotificationsByInitiator(initiator, platform));
        }
        return ResponseEntity.ok(notificationMngr.getAllNotificationsByUser(useraccountDao.findByEmail(auth.getName()), platform));
    }

    @PutMapping()
    public ResponseEntity<?> markUserNotificationAsRead(Authentication auth, @RequestParam("platform") GenericModuleBaseEntity.Platform platform, @RequestParam(value = "initiator", required = false) Long initiator) {
        if (initiator != null) {
            notificationMngr.markInitiatorNotificationAsRead(platform, initiator);
            return ResponseEntity.ok().build();
        }
        notificationMngr.markUserNotificationAsRead(platform, useraccountDao.findByEmail(auth.getName()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(notificationMngr.getNotificationById(id));
    }

    @GetMapping("filter-by-status")
    public ResponseEntity<?> getAllUserNotificationByReadStatusAndPlatform(Authentication auth, @RequestParam("platform") GenericModuleBaseEntity.Platform platform, @RequestParam Status readStatus) {

        return ResponseEntity.ok(notificationMngr.getAllNotificationsByUser(useraccountDao.findByEmail(auth.getName()), platform, readStatus));
    }

    @PostMapping("/register-token")
    public ResponseEntity<?> registerDeviceToken(@RequestBody RegisterDeviceToken request) {
        DeviceToken deviceToken = deviceTokenMngr.createDeviceToken(request);

        DeviceTokenDto dto = DeviceTokenMapper.mapToDto(deviceToken, mapper);

        return new ResponseEntity<>(new ResponseHelper(true, "new device registered", dto), HttpStatus.CREATED);

    }
}
