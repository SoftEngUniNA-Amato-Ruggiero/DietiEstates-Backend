package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BusinessUserRepository;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserPromotionService {
    private final BusinessUserRepository businessUserRepository;

    public UserPromotionService(BusinessUserRepository businessUserRepository) {
        this.businessUserRepository = businessUserRepository;
    }

    public BusinessUser promoteToBusinessUser(@NonNull BaseUser user, @NonNull RealEstateAgency agency) {
        BusinessUser businessUser;
        Optional<BusinessUser> businessUserOpt = businessUserRepository.findById(user.getId());
        if (businessUserOpt.isEmpty()) {
            businessUser = businessUserRepository.save(new BusinessUser(user, agency));
        } else {
            businessUser = businessUserOpt.get();
            if (!businessUser.getAgency().equals(agency)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User works for a different agency");
            }
        }
        return businessUser;
    }
}
