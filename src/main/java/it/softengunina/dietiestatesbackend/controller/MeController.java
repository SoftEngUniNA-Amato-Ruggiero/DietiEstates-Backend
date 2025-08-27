package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/me")
public class MeController {
    private final UserRepository<BaseUser> userRepository;
    private final TokenService tokenService;

    MeController(UserRepository<BaseUser> userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/role")
    public UserDTO getRole() {
        User user = userRepository.findByCognitoSub(tokenService.getCognitoSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserDTO(user);
    }

    @GetMapping("/agency")
    public RealEstateAgencyDTO getAgency() {
        String cognitoSub = tokenService.getCognitoSub();
        BaseUser baseUser = userRepository.findByCognitoSub(cognitoSub)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (baseUser.getAgency() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not associated with any agency");
        }
        return new RealEstateAgencyDTO(baseUser.getAgency());
    }
}
